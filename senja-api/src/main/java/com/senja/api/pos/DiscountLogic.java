package com.senja.api.pos;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;

/** Aturan diskon — dipakai bersama oleh /validate dan penyimpanan transaksi. */
public final class DiscountLogic {
    private DiscountLogic() {}

    public record Result(boolean valid, long potongan, String alasan) {}

    public static Result compute(JdbcTemplate jdbc, String kode, long subtotal, LocalDate today) {
        if (kode == null || kode.isBlank()) return new Result(false, 0, "Kode kosong");
        List<Map<String, Object>> rows = jdbc.queryForList(
            "SELECT * FROM diskon WHERE is_deleted=0 AND upper(kode)=?", kode.trim().toUpperCase());
        if (rows.isEmpty()) return new Result(false, 0, "Kode tidak ditemukan");
        Map<String, Object> d = rows.get(0);

        if (!"aktif".equals(String.valueOf(d.get("status")))) return new Result(false, 0, "Kode tidak aktif");

        String mulai = str(d.get("mulai")), berakhir = str(d.get("berakhir")), td = today.toString();
        if (td.compareTo(mulai) < 0) return new Result(false, 0, "Belum berlaku (mulai " + mulai + ")");
        if (td.compareTo(berakhir) > 0) return new Result(false, 0, "Kedaluwarsa (" + berakhir + ")");

        long minB = lng(d.get("min_belanja"));
        if (subtotal < minB) return new Result(false, 0, "Minimum belanja Rp " + minB);

        long pot;
        if ("persen".equals(String.valueOf(d.get("tipe")))) {
            pot = Math.round(subtotal * lng(d.get("nilai")) / 100.0);
            Object maks = d.get("maks_potongan");
            if (maks instanceof Number m && pot > m.longValue()) pot = m.longValue();
        } else {
            pot = Math.min(lng(d.get("nilai")), subtotal);
        }
        return new Result(true, pot, null);
    }

    static String str(Object o) { return o == null ? "" : String.valueOf(o).trim(); }
    static long lng(Object o) { return o instanceof Number n ? n.longValue() : 0; }
}