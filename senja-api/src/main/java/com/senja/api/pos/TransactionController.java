package com.senja.api.pos;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private static final ZoneId ZONA = ZoneId.of("Asia/Jakarta");
    private final TransactionService service;
    private final JdbcTemplate jdbc;

    public TransactionController(TransactionService service, JdbcTemplate jdbc) {
        this.service = service;
        this.jdbc = jdbc;
    }

    @PostMapping
    public Map<String, Object> create(HttpServletRequest req, @RequestBody Map<String, Object> b) {
        return service.create(String.valueOf(req.getAttribute("user_id")), b);
    }

    @GetMapping
    public Map<String, Object> list(HttpServletRequest req,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String user_id,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        // kasir hanya melihat transaksinya sendiri
        if ("kasir".equals(String.valueOf(req.getAttribute("user_level"))))
            user_id = String.valueOf(req.getAttribute("user_id"));

        StringBuilder w = new StringBuilder("t.is_deleted=0");
        List<Object> args = new ArrayList<>();
        try {
            if (from != null && !from.isBlank()) { w.append(" AND t.waktu>=?"); args.add(ms(from, true)); }
            if (to != null && !to.isBlank()) { w.append(" AND t.waktu<?"); args.add(ms(to, false)); }
        } catch (Exception e) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Format from/to: yyyy-MM-dd"); }
        if (user_id != null && !user_id.isBlank()) { w.append(" AND t.user_id=?"); args.add(user_id); }
        if (status != null && !status.isBlank()) { w.append(" AND t.status=?"); args.add(status); }
        if (q != null && !q.isBlank()) { w.append(" AND t.kode LIKE ?"); args.add("%" + q + "%"); }

        int lim = Math.max(1, Math.min(200, limit));
        Integer total = jdbc.queryForObject(
            "SELECT COUNT(*) FROM transaksi t WHERE " + w, Integer.class, args.toArray());

        List<Object> qArgs = new ArrayList<>(args);
        qArgs.add(lim); qArgs.add(Math.max(0, offset));
        List<Map<String, Object>> items = jdbc.queryForList(
            "SELECT t.*, u.nama kasir_nama, "
            + "(SELECT COALESCE(SUM(qty),0) FROM transaksi_detail WHERE transaksi_id=t.id AND is_deleted=0) nitem "
            + "FROM transaksi t LEFT JOIN users u ON u.id=t.user_id "
            + "WHERE " + w + " ORDER BY t.waktu DESC LIMIT ? OFFSET ?", qArgs.toArray());

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("total", total == null ? 0 : total);
        out.put("limit", lim);
        out.put("offset", offset);
        out.put("items", items);
        return out;
    }

    @GetMapping("/{id}")
    public Map<String, Object> detail(@PathVariable String id) {
        List<Map<String, Object>> t = jdbc.queryForList("SELECT * FROM transaksi WHERE id=? AND is_deleted=0", id);
        if (t.isEmpty()) throw TransactionService.notFound();
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("transaksi", t.get(0));
        out.put("items", jdbc.queryForList("SELECT * FROM transaksi_detail WHERE transaksi_id=? AND is_deleted=0", id));
        return out;
    }

    @GetMapping("/{id}/receipt")
    public Map<String, Object> receipt(@PathVariable String id) {
        List<Map<String, Object>> t = jdbc.queryForList("SELECT * FROM transaksi WHERE id=? AND is_deleted=0", id);
        if (t.isEmpty()) throw TransactionService.notFound();
        Map<String, Object> toko = new LinkedHashMap<>();
        for (String k : List.of("toko_nama", "toko_alamat", "toko_telp", "struk_footer"))
            toko.put(k.replace("toko_", "").replace("struk_", ""), pengaturan(k));
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("toko", toko);
        out.put("transaksi", t.get(0));
        out.put("kasir", jdbc.queryForObject("SELECT nama FROM users WHERE id=?", String.class, t.get(0).get("user_id")));
        out.put("items", jdbc.queryForList("SELECT * FROM transaksi_detail WHERE transaksi_id=? AND is_deleted=0", id));
        return out;
    }

    @PostMapping("/{id}/cancel")
    public Map<String, Object> cancel(HttpServletRequest req, @PathVariable String id) {
        if (!"admin".equals(String.valueOf(req.getAttribute("user_level"))))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Hanya admin yang dapat membatalkan transaksi");
        return service.cancel(id, String.valueOf(req.getAttribute("user_id")));
    }

    private String pengaturan(String kunci) {
        List<Map<String, Object>> r = jdbc.queryForList("SELECT nilai FROM pengaturan WHERE kunci=? AND is_deleted=0", kunci);
        return r.isEmpty() ? "" : String.valueOf(r.get(0).get("nilai"));
    }

    private static long ms(String tanggal, boolean awalHari) {
        LocalDate d = LocalDate.parse(tanggal);
        var t = awalHari ? d.atStartOfDay(ZONA) : d.plusDays(1).atStartOfDay(ZONA);
        return t.toInstant().toEpochMilli();
    }
}