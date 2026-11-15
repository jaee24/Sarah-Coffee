package com.senja.api.sync;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SyncService {

    private final JdbcTemplate jdbc;

    public SyncService(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @Transactional
    public Map<String, Object> sync(String deviceId, Map<String, Object> body) {
        long lastPull = body.get("last_pull") instanceof Number n ? n.longValue() : 0L;
        Object raw = body.get("changes");
        @SuppressWarnings("unchecked")
        Map<String, Object> changes = raw instanceof Map ? (Map<String, Object>) raw : Map.of();

        for (String k : changes.keySet()) SyncTables.byName(k); // validasi nama tabel

        Map<String, Integer> pushed = new LinkedHashMap<>();
        Map<String, Integer> skipped = new LinkedHashMap<>();
        Map<String, List<String>> accepted = new LinkedHashMap<>();
        long maxUpd = lastPull;

        /* ================= PUSH ================= */
        for (SyncTables.T t : SyncTables.ORDER) {
            Object rowsObj = changes.get(t.name());
            if (!(rowsObj instanceof List<?> rows) || rows.isEmpty()) continue;
            List<String> acc = new ArrayList<>();
            int p = 0, s = 0;
            for (Object o : rows) {
                if (!(o instanceof Map)) continue;
                @SuppressWarnings("unchecked")
                Map<String, Object> row = (Map<String, Object>) o;
                Object idv = row.get("id");
                Object upv = row.get("updated_at");
                if (idv == null || !(upv instanceof Number))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        t.name() + ": setiap baris wajib punya 'id' dan 'updated_at'");
                long incUp = ((Number) upv).longValue();

                List<Long> srv = jdbc.query("SELECT updated_at FROM " + t.name() + " WHERE id=?",
                    (rs, i) -> rs.getLong(1), idv);
                boolean exists = !srv.isEmpty();
                if (exists && srv.get(0) >= incUp) { s++; continue; } // server lebih baru → kalah (LWW)

                upsert(t, row, exists);
                acc.add(String.valueOf(idv));
                maxUpd = Math.max(maxUpd, incUp);
                p++;
            }
            if (p > 0) pushed.put(t.name(), p);
            if (s > 0) skipped.put(t.name(), s);
            if (!acc.isEmpty()) accepted.put(t.name(), acc);
        }

        long now = System.currentTimeMillis();
        jdbc.update("UPDATE devices SET last_seen=? WHERE id=?", now, deviceId);
        long serverTime = Math.max(now, maxUpd); // anti-echo jika jam device lebih cepat

        /* ================= PULL ================= */
        Map<String, Object> outChanges = new LinkedHashMap<>();
        for (SyncTables.T t : SyncTables.ORDER) {
            List<String> acc = accepted.getOrDefault(t.name(), List.of());
            String notIn = acc.isEmpty() ? ""
                : " AND id NOT IN (" + String.join(",", Collections.nCopies(acc.size(), "?")) + ")";
            Object[] args = new Object[acc.size() + 1];
            args[0] = lastPull;
            for (int i = 0; i < acc.size(); i++) args[i + 1] = acc.get(i);
            List<Map<String, Object>> rows = jdbc.queryForList(
                "SELECT " + String.join(",", t.cols()) + " FROM " + t.name()
                + " WHERE updated_at > ?" + notIn + " ORDER BY updated_at ASC", args);
            if (!rows.isEmpty()) outChanges.put(t.name(), rows);
        }

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("server_time", serverTime);
        resp.put("pushed", pushed);
        resp.put("skipped", skipped);
        resp.put("changes", outChanges);
        return resp;
    }

    private void upsert(SyncTables.T t, Map<String, Object> row, boolean exists) {
        if (!exists) {
            List<String> cols = new ArrayList<>(t.cols());
            cols.add("dirty"); // server selalu 0
            String marks = String.join(",", Collections.nCopies(cols.size(), "?"));
            Object[] args = new Object[cols.size()];
            for (int i = 0; i < cols.size(); i++) {
                String c = cols.get(i);
                args[i] = "dirty".equals(c) ? 0 : conv(t, c, row.get(c));
            }
            jdbc.update("INSERT INTO " + t.name() + " (" + String.join(",", cols) + ") VALUES (" + marks + ")", args);
        } else {
            List<String> setCols = new ArrayList<>();
            for (String c : t.cols()) if (!c.equals("id") && !c.equals("created_at")) setCols.add(c);
            setCols.add("dirty");
            String sql = "UPDATE " + t.name() + " SET "
                + setCols.stream().map(c -> c + "=?").collect(Collectors.joining(",")) + " WHERE id=?";
            Object[] args = new Object[setCols.size() + 1];
            for (int i = 0; i < setCols.size(); i++) {
                String c = setCols.get(i);
                args[i] = "dirty".equals(c) ? 0 : conv(t, c, row.get(c));
            }
            args[setCols.size()] = row.get("id");
            jdbc.update(sql, args);
        }
    }

    /** Base64 (JSON) <-> byte[] (BLOB) untuk kolom bukti foto. */
    private Object conv(SyncTables.T t, String col, Object v) {
        if (v == null) return null;
        if (t.blobs().contains(col)) {
            if (v instanceof String s) return Base64.getDecoder().decode(s);
            if (v instanceof byte[] b) return b;
        }
        return v;
    }

    public Map<String, Object> stats() {
        Map<String, Object> m = new LinkedHashMap<>();
        for (SyncTables.T t : SyncTables.ORDER)
            m.put(t.name(), jdbc.queryForObject("SELECT COUNT(*) FROM " + t.name(), Integer.class));
        return m;
    }
}