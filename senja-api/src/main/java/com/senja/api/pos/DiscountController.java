package com.senja.api.pos;

import java.time.LocalDate;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    private final JdbcTemplate jdbc;
    public DiscountController(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    private void requireAdmin(HttpServletRequest req) {
        if (!"admin".equals(String.valueOf(req.getAttribute("user_level"))))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Hanya admin");
    }

    @GetMapping
    public List<Map<String, Object>> list(HttpServletRequest req,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status) {
        requireAdmin(req);
        StringBuilder w = new StringBuilder("is_deleted=0");
        List<Object> args = new ArrayList<>();
        if (q != null && !q.isBlank()) { w.append(" AND upper(kode) LIKE ?"); args.add("%" + q.toUpperCase() + "%"); }
        if (status != null && !status.isBlank()) { w.append(" AND status=?"); args.add(status); }
        return jdbc.queryForList("SELECT * FROM diskon WHERE " + w + " ORDER BY kode", args.toArray());
    }

    @GetMapping("/{id}")
    public Map<String, Object> get(HttpServletRequest req, @PathVariable String id) {
        requireAdmin(req);
        List<Map<String, Object>> r = jdbc.queryForList("SELECT * FROM diskon WHERE id=? AND is_deleted=0", id);
        if (r.isEmpty()) throw notFound();
        return r.get(0);
    }

    @PostMapping
    public Map<String, Object> create(HttpServletRequest req, @RequestBody Map<String, Object> b) {
        requireAdmin(req);
        String kode = DiscountLogic.str(b.get("kode")).toUpperCase();
        String tipe = DiscountLogic.str(b.get("tipe"));
        long nilai = DiscountLogic.lng(b.get("nilai"));
        if (kode.isBlank()) throw bad("kode wajib diisi");
        if (!List.of("persen", "nominal").contains(tipe)) throw bad("tipe harus persen/nominal");
        if (nilai <= 0) throw bad("nilai harus > 0");
        if (tipe.equals("persen") && nilai > 100) throw bad("persen maksimal 100");
        String mulai = DiscountLogic.str(b.get("mulai")), berakhir = DiscountLogic.str(b.get("berakhir"));
        if (mulai.isBlank() || berakhir.isBlank() || mulai.compareTo(berakhir) > 0) throw bad("periode mulai/berakhir tidak valid");
        if (!jdbc.queryForList("SELECT id FROM diskon WHERE is_deleted=0 AND upper(kode)=?", kode).isEmpty())
            throw bad("Kode sudah dipakai");

        String id = b.get("id") != null ? DiscountLogic.str(b.get("id")) : UUID.randomUUID().toString();
        long now = System.currentTimeMillis();
        jdbc.update("""
            INSERT INTO diskon(id,kode,tipe,nilai,maks_potongan,min_belanja,mulai,berakhir,status,created_at,updated_at,is_deleted,dirty)
            VALUES (?,?,?,?,?,?,?,?,?,?,?,?,0)""",
            id, kode, tipe, nilai,
            b.get("maks_potongan") == null ? null : DiscountLogic.lng(b.get("maks_potongan")),
            b.containsKey("min_belanja") ? DiscountLogic.lng(b.get("min_belanja")) : 0,
            mulai, berakhir,
            b.containsKey("status") ? DiscountLogic.str(b.get("status")) : "aktif",
            now, now, 0);
        return get(req, id);
    }

    @PutMapping("/{id}")
    public Map<String, Object> update(HttpServletRequest req, @PathVariable String id, @RequestBody Map<String, Object> b) {
        requireAdmin(req);
        if (jdbc.queryForList("SELECT id FROM diskon WHERE id=? AND is_deleted=0", id).isEmpty()) throw notFound();
        List<String> sets = new ArrayList<>();
        List<Object> args = new ArrayList<>();
        if (b.containsKey("kode")) {
            String kode = DiscountLogic.str(b.get("kode")).toUpperCase();
            if (kode.isBlank()) throw bad("kode tidak boleh kosong");
            if (!jdbc.queryForList("SELECT id FROM diskon WHERE is_deleted=0 AND upper(kode)=? AND id<>?", kode, id).isEmpty())
                throw bad("Kode sudah dipakai");
            sets.add("kode=?"); args.add(kode);
        }
        if (b.containsKey("tipe")) {
            String tipe = DiscountLogic.str(b.get("tipe"));
            if (!List.of("persen", "nominal").contains(tipe)) throw bad("tipe harus persen/nominal");
            sets.add("tipe=?"); args.add(tipe);
        }
        if (b.containsKey("nilai")) {
            long nilai = DiscountLogic.lng(b.get("nilai"));
            if (nilai <= 0) throw bad("nilai harus > 0");
            sets.add("nilai=?"); args.add(nilai);
        }
        if (b.containsKey("maks_potongan")) {
            sets.add("maks_potongan=?");
            args.add(b.get("maks_potongan") == null ? null : DiscountLogic.lng(b.get("maks_potongan")));
        }
        if (b.containsKey("min_belanja")) { sets.add("min_belanja=?"); args.add(DiscountLogic.lng(b.get("min_belanja"))); }
        if (b.containsKey("mulai")) { sets.add("mulai=?"); args.add(DiscountLogic.str(b.get("mulai"))); }
        if (b.containsKey("berakhir")) { sets.add("berakhir=?"); args.add(DiscountLogic.str(b.get("berakhir"))); }
        if (b.containsKey("status")) { sets.add("status=?"); args.add(DiscountLogic.str(b.get("status"))); }
        if (sets.isEmpty()) throw bad("Tidak ada perubahan");
        sets.add("updated_at=?"); args.add(System.currentTimeMillis());
        args.add(id);
        jdbc.update("UPDATE diskon SET " + String.join(",", sets) + " WHERE id=?", args.toArray());
        return get(req, id);
    }

    @PatchMapping("/{id}/status")
    public Map<String, Object> status(HttpServletRequest req, @PathVariable String id, @RequestBody Map<String, Object> b) {
        requireAdmin(req);
        String st = DiscountLogic.str(b.get("status"));
        if (!List.of("aktif", "nonaktif").contains(st)) throw bad("status harus aktif/nonaktif");
        if (jdbc.queryForList("SELECT id FROM diskon WHERE id=? AND is_deleted=0", id).isEmpty()) throw notFound();
        jdbc.update("UPDATE diskon SET status=?, updated_at=? WHERE id=?", st, System.currentTimeMillis(), id);
        return get(req, id);
    }

    /** Validasi kode saat pembayaran — semua user login boleh (advisory; final dihitung ulang saat simpan). */
    @PostMapping("/validate")
    public Map<String, Object> validate(HttpServletRequest req, @RequestBody Map<String, Object> b) {
        String kode = DiscountLogic.str(b.get("kode"));
        long subtotal = DiscountLogic.lng(b.get("subtotal"));
        DiscountLogic.Result r = DiscountLogic.compute(jdbc, kode, subtotal, LocalDate.now());
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("valid", r.valid());
        out.put("kode", kode.toUpperCase());
        out.put("potongan", r.valid() ? r.potongan() : 0);
        out.put("alasan", r.alasan());
        out.put("subtotal", subtotal);
        if (r.valid()) out.put("total_setelah_diskon", subtotal - r.potongan());
        return out;
    }

    private static ResponseStatusException bad(String m) { return new ResponseStatusException(HttpStatus.BAD_REQUEST, m); }
    private static ResponseStatusException notFound() { return new ResponseStatusException(HttpStatus.NOT_FOUND, "Diskon tidak ditemukan"); }
}