package com.senja.api.pos;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TransactionService {

    public static final long HARGA_LARGE = 5000;
    private static final ZoneId ZONA = ZoneId.of("Asia/Jakarta");

    private final JdbcTemplate jdbc;
    public TransactionService(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    /** Generator placeholder — mencegah salah hitung '?' secara manual selamanya. */
    private static String ph(int n) { return String.join(",", Collections.nCopies(n, "?")); }

    /* ================= CREATE (idempoten by id) ================= */
    @Transactional
    public Map<String, Object> create(String userId, Map<String, Object> b) {
        String id = str(b.get("id"));
        if (id.isBlank()) throw bad("Field 'id' wajib diisi (UUID buatan aplikasi)");
        List<Map<String, Object>> ex = jdbc.queryForList("SELECT * FROM transaksi WHERE id=?", id);
        if (!ex.isEmpty()) return response(id, true, List.of());   // replay → balas yang ada

        if (!(b.get("items") instanceof List<?> items) || items.isEmpty()) throw bad("items tidak boleh kosong");

        long subtotal = 0;
        List<Object[]> detailPlan = new ArrayList<>();   // {detailId, produkId, nama, opsi, qty, harga, subtotal, hpp}
        List<Object[]> mutasiPlan = new ArrayList<>();   // {mutasiId, bahanId, jumlah}
        Set<String> bahanIds = new LinkedHashSet<>();

        for (int i = 0; i < items.size(); i++) {
            if (!(items.get(i) instanceof Map<?, ?> it)) throw bad("Item ke-" + (i + 1) + " tidak valid");
            String pid = str(it.get("produk_id"));
            long qty = lng(it.get("qty"));
            String opsi = str(it.get("opsi"));
            if (qty < 1) throw bad("Item ke-" + (i + 1) + ": qty minimal 1");

            List<Map<String, Object>> p = jdbc.queryForList(
                "SELECT nama,harga FROM produk WHERE id=? AND is_deleted=0 AND status='aktif'", pid);
            if (p.isEmpty()) throw bad("Menu tidak tersedia / tidak aktif (item ke-" + (i + 1) + ")");

            long hargaServer = lng(p.get(0).get("harga")) + (opsi.contains("Large") ? HARGA_LARGE : 0);
            if (lng(it.get("harga")) != hargaServer)
                throw bad("Harga \"" + p.get(0).get("nama") + "\" berubah menjadi " + hargaServer + " — muat ulang menu");

            long st = hargaServer * qty;
            subtotal += st;

            Long hppSatuan = jdbc.queryForObject("""
                SELECT COALESCE(SUM(b.harga*r.jumlah),0) FROM resep r
                JOIN bahan b ON b.id=r.bahan_id
                WHERE r.produk_id=? AND r.is_deleted=0 AND b.is_deleted=0""", Long.class, pid);
            long hppRow = (hppSatuan == null ? 0 : hppSatuan) * qty;

            detailPlan.add(new Object[]{ PosIds.detailId(id, i), pid, p.get(0).get("nama"),
                opsi.isBlank() ? null : opsi, qty, hargaServer, st, hppRow });

            for (Map<String, Object> r : jdbc.queryForList(
                    "SELECT bahan_id,jumlah FROM resep WHERE produk_id=? AND is_deleted=0", pid)) {
                String bid = String.valueOf(r.get("bahan_id"));
                double jml = ((Number) r.get("jumlah")).doubleValue() * qty;
                mutasiPlan.add(new Object[]{ PosIds.mutasiId(id, i, bid), bid, jml });
                bahanIds.add(bid);
            }
        }

        // diskon: divalidasi ulang saat simpan
        long potongan = 0; String kodeDiskon = null;
        String kodeInput = str(b.get("diskon_kode"));
        if (!kodeInput.isBlank()) {
            DiscountLogic.Result d = DiscountLogic.compute(jdbc, kodeInput, subtotal, LocalDate.now(ZONA));
            if (!d.valid()) throw bad("Diskon " + kodeInput + ": " + d.alasan());
            potongan = d.potongan(); kodeDiskon = kodeInput.toUpperCase();
        }
        long total = subtotal - potongan;

        String metode = str(b.get("metode")).toLowerCase();
        if (!List.of("cash", "qris", "debit").contains(metode)) throw bad("metode harus cash/qris/debit");
        long dibayar = b.get("dibayar") instanceof Number ? lng(b.get("dibayar")) : total;
        if (dibayar < total) throw bad("Jumlah bayar kurang dari total");
        if (!metode.equals("cash")) dibayar = total;
        long kembalian = dibayar - total;

        long now = System.currentTimeMillis();
        String kode = nextKode();
        String ref = str(b.get("ref"));

        /* 18 kolom = ph(16) + literal 0,0 (is_deleted, dirty) — 16 bind, dihitung otomatis */
        jdbc.update("INSERT INTO transaksi(id,kode,waktu,user_id,subtotal,diskon,diskon_kode,total,metode,dibayar,kembalian,ref,status,device_id,created_at,updated_at,is_deleted,dirty) VALUES ("
                + ph(16) + ",0,0)",
            id, kode, now, userId, subtotal, potongan, kodeDiskon, total, metode, dibayar, kembalian,
            ref.isBlank() ? null : ref, "selesai", userId, now, now);

        /* 13 kolom = ph(11) + 0,0 — 11 bind */
        for (Object[] d : detailPlan)
            jdbc.update("INSERT INTO transaksi_detail(id,transaksi_id,produk_id,nama,opsi,qty,harga,subtotal,hpp,created_at,updated_at,is_deleted,dirty) VALUES ("
                    + ph(11) + ",0,0)",
                d[0], id, d[1], d[2], d[3], d[4], d[5], d[6], d[7], now, now);

        /* 13 kolom = ph(11) + 0,0 — 11 bind */
        for (Object[] m : mutasiPlan)
            jdbc.update("INSERT INTO stok_mutasi(id,bahan_id,arah,jumlah,sumber,ref_id,keterangan,waktu,device_id,created_at,updated_at,is_deleted,dirty) VALUES ("
                    + ph(11) + ",0,0)",
                m[0], m[1], "keluar", m[2], "transaksi", id, kode, now, userId, now, now);

        return response(id, false, lowStockWarnings(bahanIds));
    }

    /* ================= CANCEL (admin, stok dikembalikan) ================= */
    @Transactional
    public Map<String, Object> cancel(String trxId, String olehUserId) {
        List<Map<String, Object>> ex = jdbc.queryForList("SELECT * FROM transaksi WHERE id=? AND is_deleted=0", trxId);
        if (ex.isEmpty()) throw notFound();
        Map<String, Object> t = ex.get(0);
        if (!"selesai".equals(String.valueOf(t.get("status"))))
            throw bad("Transaksi sudah berstatus " + t.get("status"));

        long now = System.currentTimeMillis();
        jdbc.update("UPDATE transaksi SET status='dibatalkan', updated_at=? WHERE id=?", now, trxId);

        var details = jdbc.queryForList("SELECT * FROM transaksi_detail WHERE transaksi_id=? AND is_deleted=0", trxId);
        List<String> mutIds = new ArrayList<>();
        for (int i = 0; i < details.size(); i++) {
            var d = details.get(i);
            if (d.get("produk_id") == null) continue;
            for (Map<String, Object> r : jdbc.queryForList(
                    "SELECT bahan_id,jumlah FROM resep WHERE produk_id=? AND is_deleted=0", d.get("produk_id"))) {
                String bid = String.valueOf(r.get("bahan_id"));
                String mid = PosIds.mutasiBatalId(trxId, i, bid);
                jdbc.update("INSERT INTO stok_mutasi(id,bahan_id,arah,jumlah,sumber,ref_id,keterangan,waktu,device_id,created_at,updated_at,is_deleted,dirty) VALUES ("
                        + ph(11) + ",0,0)",
                    mid, bid, "masuk", ((Number) r.get("jumlah")).doubleValue() * lng(d.get("qty")),
                    "manual", trxId, "Pembatalan " + t.get("kode"), now, olehUserId, now, now);
                mutIds.add(mid);
            }
        }
        Map<String, Object> out = response(trxId, false, List.of());
        if (!mutIds.isEmpty()) {
            String in = String.join(",", Collections.nCopies(mutIds.size(), "?"));
            out.put("mutasi", jdbc.queryForList("SELECT * FROM stok_mutasi WHERE id IN (" + in + ")", mutIds.toArray()));
        }
        return out;
    }

    /* ================= helpers ================= */
    private String nextKode() {
        String ds = LocalDate.now(ZONA).toString().replace("-", "");
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM transaksi WHERE kode LIKE ?",
            Integer.class, "TRX-" + ds + "-S-%");
        return "TRX-" + ds + "-S-" + String.format("%03d", (n == null ? 0 : n) + 1);
    }

    private List<String> lowStockWarnings(Set<String> bahanIds) {
        List<String> w = new ArrayList<>();
        for (String bid : bahanIds) {
            Map<String, Object> b = jdbc.queryForMap("""
                SELECT nama, stok_min,
                  COALESCE((SELECT SUM(CASE WHEN m.arah='masuk' THEN m.jumlah ELSE -m.jumlah END)
                            FROM stok_mutasi m WHERE m.bahan_id=b.id AND m.is_deleted=0),0) AS sisa
                FROM bahan b WHERE b.id=?""", bid);
            double sisa = ((Number) b.get("sisa")).doubleValue(), min = ((Number) b.get("stok_min")).doubleValue();
            if (sisa <= min)
                w.add("Stok " + b.get("nama") + " menipis — sisa " + (sisa == Math.floor(sisa) ? (long) sisa : sisa));
        }
        return w;
    }

    private Map<String, Object> response(String trxId, boolean idempotent, List<String> warnings) {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("idempotent", idempotent);
        out.put("transaksi", jdbc.queryForMap("SELECT * FROM transaksi WHERE id=?", trxId));
        out.put("detail", jdbc.queryForList("SELECT * FROM transaksi_detail WHERE transaksi_id=? AND is_deleted=0", trxId));
        if (!idempotent) out.put("mutasi", jdbc.queryForList(
            "SELECT * FROM stok_mutasi WHERE ref_id=? AND sumber='transaksi'", trxId));
        if (!warnings.isEmpty()) out.put("warnings", warnings);
        return out;
    }

    static String str(Object o) { return o == null ? "" : String.valueOf(o).trim(); }
    static long lng(Object o) { return o instanceof Number n ? n.longValue() : -1; }
    static ResponseStatusException bad(String m) { return new ResponseStatusException(HttpStatus.BAD_REQUEST, m); }
    static ResponseStatusException notFound() { return new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaksi tidak ditemukan"); }
}