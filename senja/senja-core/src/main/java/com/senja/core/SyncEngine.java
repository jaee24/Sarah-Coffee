package com.senja.core;

import java.net.InetAddress;
import java.sql.*;
import java.util.*;

/**
 * PUSH baris dirty → server, PULL perubahan device lain (LWW by updated_at),
 * lalu hitung ulang cache stok dari buku besar stok_mutasi.
 * Panggil dari thread background di UI.
 */
public final class SyncEngine {

    private SyncEngine() {}
    private static final Object LOCK = new Object();

    public static SyncResult sync(String appType) {
        synchronized (LOCK) {
            try { return doSync(appType); }
            catch (Exception e) {
                return SyncResult.offline(e.getMessage() == null ? e.toString() : e.getMessage());
            }
        }
    }
    private static SyncResult doSync(String appType) throws Exception {
        Db.init();
        String server = SyncMeta.get("server_url", "http://localhost:8080");

        // 1. Registrasi device sekali seumur hidup DB
        String deviceId = SyncMeta.get("device_id");
        String apiKey = SyncMeta.get("api_key");
        if (deviceId == null || apiKey == null) {
            String nama = SyncMeta.get("device_nama",
                    InetAddress.getLocalHost().getHostName() + "-" + appType);
            Map<String, Object> reg = SyncClient.register(server, nama, appType);
            deviceId = String.valueOf(reg.get("device_id"));
            apiKey = String.valueOf(reg.get("api_key"));
            SyncMeta.set("device_id", deviceId);
            SyncMeta.set("api_key", apiKey);
        }

        long lastPull = SyncMeta.getLong("last_pull", 0L);

        // 2. Kumpulkan baris dirty
        Map<String, Object> changes = collectDirty();

        // 3. Kirim + terima dalam satu request
        Map<String, Object> resp = SyncClient.sync(server, deviceId, apiKey, lastPull, changes);

        @SuppressWarnings("unchecked")
        Map<String, Object> pushedRaw =
                resp.get("pushed") instanceof Map ? (Map<String, Object>) resp.get("pushed") : Map.of();
        Map<String, Integer> pushed = new LinkedHashMap<>();
        pushedRaw.forEach((k, v) -> pushed.put(k, ((Number) v).intValue()));

        long serverTime = resp.get("server_time") instanceof Number n
                ? n.longValue() : System.currentTimeMillis();

        // 4. Terapkan hasil dalam SATU transaksi SQLite
        Map<String, Integer> pulled = new LinkedHashMap<>();
        Db.tx(c -> {
            markClean(c, changes);                        // terkirim → dirty=0
            applyPull(c, resp.get("changes"), pulled);    // terapkan data server (LWW)
            recalcStok(c);                                // stok = SUM(masuk)-SUM(keluar)
            SyncMeta.set(c, "last_pull", String.valueOf(serverTime));
            SyncMeta.set(c, "last_sync", String.valueOf(System.currentTimeMillis()));
        });

        return new SyncResult(true, pushed, pulled, serverTime, null);
    }

    /* ---------- PUSH: kumpulkan baris dirty ---------- */
    private static Map<String, Object> collectDirty() {
        Map<String, Object> changes = new LinkedHashMap<>();
        for (Tables.T t : Tables.ORDER) {
            List<Map<String, Object>> rows = Db.query(
                    "SELECT " + String.join(",", t.cols()) + " FROM " + t.name() + " WHERE dirty=1");
            if (rows.isEmpty()) continue;
            for (Map<String, Object> r : rows)
                for (String b : t.blobs())                     // BLOB → Base64 utk JSON
                    if (r.get(b) instanceof byte[] bytes)
                        r.put(b, Base64.getEncoder().encodeToString(bytes));
            changes.put(t.name(), rows);
        }
        return changes;
    }

    /* ---------- tandai baris yang sudah terkirim ---------- */
    private static void markClean(Connection c, Map<String, Object> changes) throws SQLException {
        for (Map.Entry<String, Object> e : changes.entrySet()) {
            if (!(e.getValue() instanceof List<?> rows) || rows.isEmpty()) continue;
            List<String> ids = new ArrayList<>();
            for (Object o : rows)
                if (o instanceof Map<?, ?> m && m.get("id") != null)
                    ids.add(String.valueOf(m.get("id")));
            for (int i = 0; i < ids.size(); i += 400) {        // chunk agar aman
                List<String> chunk = ids.subList(i, Math.min(ids.size(), i + 400));
                String in = String.join(",", Collections.nCopies(chunk.size(), "?"));
                try (PreparedStatement ps = c.prepareStatement(
                        "UPDATE " + e.getKey() + " SET dirty=0 WHERE id IN (" + in + ")")) {
                    for (int j = 0; j < chunk.size(); j++) ps.setString(j + 1, chunk.get(j));
                    ps.executeUpdate();
                }
            }
        }
    }

    /* ---------- PULL: terapkan baris dari server (LWW) ---------- */
    @SuppressWarnings("unchecked")
    private static void applyPull(Connection c, Object changesObj, Map<String, Integer> pulled) throws Exception {
        if (!(changesObj instanceof Map<?, ?> map)) return;
        for (Map.Entry<?, ?> e : map.entrySet()) {
            Tables.T t = Tables.byName(String.valueOf(e.getKey()));
            if (!(e.getValue() instanceof List<?> rows)) continue;
            int n = 0;
            for (Object o : rows) {
                if (!(o instanceof Map)) continue;
                Map<String, Object> row = (Map<String, Object>) o;
                String id = String.valueOf(row.get("id"));
                long upd = row.get("updated_at") instanceof Number num ? num.longValue() : 0;

                Long local = localUpdated(c, t.name(), id);   // LWW: lokal sama/baru → skip
                if (local != null && local >= upd) continue;

                // Upsert by id (bukan INSERT OR REPLACE — aman terhadap FK)
                String cols = String.join(",", t.cols());
                String marks = String.join(",", Collections.nCopies(t.cols().size() + 1, "?"));
                StringBuilder set = new StringBuilder();
                for (String col : t.cols())
                    if (!col.equals("id")) set.append(col).append("=excluded.").append(col).append(',');
                set.append("dirty=0");
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO " + t.name() + " (" + cols + ",dirty) VALUES (" + marks + ")" +
                        " ON CONFLICT(id) DO UPDATE SET " + set)) {
                    for (int i = 0; i < t.cols().size(); i++) {
                        String col = t.cols().get(i);
                        Object v = row.get(col);
                        if (v != null && t.blobs().contains(col) && v instanceof String s)
                            v = Base64.getDecoder().decode(s);   // Base64 → byte[]
                        ps.setObject(i + 1, v);
                    }
                    ps.setObject(t.cols().size() + 1, 0);         // dirty=0
                    ps.executeUpdate();
                    n++;
                } catch (SQLException ex) {
                    System.err.println("[sync] Gagal terapkan " + t.name() + "/" + id + ": " + ex.getMessage());
                }
            }
            if (n > 0) pulled.put(t.name(), n);
        }
    }

    private static Long localUpdated(Connection c, String table, String id) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "SELECT updated_at FROM " + table + " WHERE id=?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getLong(1) : null; }
        }
    }

    /* ---------- stok = buku besar, bukan angka yang dikirim-kirim ---------- */
    public static void recalcStok(Connection c) throws SQLException {
        try (Statement st = c.createStatement()) {
            st.executeUpdate("""
                UPDATE bahan SET stok = COALESCE((
                    SELECT SUM(CASE WHEN m.arah='masuk' THEN m.jumlah ELSE -m.jumlah END)
                    FROM stok_mutasi m
                    WHERE m.bahan_id = bahan.id AND m.is_deleted = 0
                ), 0)""");
        }
    }
}