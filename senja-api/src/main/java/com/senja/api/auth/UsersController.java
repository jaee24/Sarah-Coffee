package com.senja.api.auth;

import java.util.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final JdbcTemplate jdbc;
    private final TokenService tokens;

    public UsersController(JdbcTemplate jdbc, TokenService tokens) {
        this.jdbc = jdbc;
        this.tokens = tokens;
    }

    private void requireAdmin(HttpServletRequest req) {
        if (!"admin".equals(String.valueOf(req.getAttribute("user_level"))))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Hanya admin yang boleh mengakses endpoint ini");
    }

    @GetMapping
    public List<Map<String, Object>> list(HttpServletRequest req,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status) {
        requireAdmin(req);
        StringBuilder where = new StringBuilder("is_deleted=0");
        List<Object> args = new ArrayList<>();
        if (q != null && !q.isBlank()) {
            String like = "%" + q.toLowerCase() + "%";
            where.append(" AND (lower(nama) LIKE ? OR lower(kode) LIKE ?)");
            args.add(like); args.add(like);
        }
        if (status != null && !status.isBlank()) { where.append(" AND status=?"); args.add(status); }
        return jdbc.queryForList("SELECT id,kode,nama,level,no_hp,alamat,status,updated_at FROM users WHERE "
            + where + " ORDER BY kode", args.toArray());
    }

    @GetMapping("/{id}")
    public Map<String, Object> get(HttpServletRequest req, @PathVariable String id) {
        requireAdmin(req);
        List<Map<String, Object>> r = jdbc.queryForList(
            "SELECT id,kode,nama,level,no_hp,alamat,status,updated_at FROM users WHERE id=? AND is_deleted=0", id);
        if (r.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan");
        return r.get(0);
    }

    @PostMapping
    public Map<String, Object> create(HttpServletRequest req, @RequestBody Map<String, Object> b) {
        requireAdmin(req);
        String nama = str(b.get("nama")), level = str(b.get("level")), password = str(b.get("password"));
        if (nama.isBlank()) throw bad("Nama wajib diisi");
        if (!level.equals("admin") && !level.equals("kasir")) throw bad("Level harus admin atau kasir");
        if (password.length() < 4) throw bad("Password minimal 4 karakter");
        String id = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();
        jdbc.update("""
            INSERT INTO users(id,kode,nama,level,password_hash,no_hp,alamat,status,created_at,updated_at,is_deleted,dirty)
            VALUES (?,?,?,?,?,?,?,?,?,?,0,0)""",
            id, nextKode(level), nama, level, BCrypt.hashpw(password, BCrypt.gensalt()),
            str(b.get("no_hp")), str(b.get("alamat")), "aktif", now, now);
        return get(req, id);
    }

    @PutMapping("/{id}")
    public Map<String, Object> update(HttpServletRequest req, @PathVariable String id, @RequestBody Map<String, Object> b) {
        requireAdmin(req);
        if (jdbc.queryForList("SELECT id FROM users WHERE id=? AND is_deleted=0", id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan");
        List<String> sets = new ArrayList<>();
        List<Object> args = new ArrayList<>();
        if (b.containsKey("nama") && !str(b.get("nama")).isBlank()) { sets.add("nama=?"); args.add(str(b.get("nama"))); }
        if (b.containsKey("level") && (str(b.get("level")).equals("admin") || str(b.get("level")).equals("kasir"))) {
            sets.add("level=?"); args.add(str(b.get("level")));
        }
        if (b.containsKey("no_hp")) { sets.add("no_hp=?"); args.add(str(b.get("no_hp"))); }
        if (b.containsKey("alamat")) { sets.add("alamat=?"); args.add(str(b.get("alamat"))); }
        if (b.containsKey("password")) {
            if (str(b.get("password")).length() < 4) throw bad("Password minimal 4 karakter");
            sets.add("password_hash=?"); args.add(BCrypt.hashpw(str(b.get("password")), BCrypt.gensalt()));
        }
        if (sets.isEmpty()) throw bad("Tidak ada perubahan");
        sets.add("updated_at=?"); args.add(System.currentTimeMillis());
        args.add(id);
        jdbc.update("UPDATE users SET " + String.join(",", sets) + " WHERE id=?", args.toArray());
        return get(req, id);
    }

    @PatchMapping("/{id}/status")
    public Map<String, Object> status(HttpServletRequest req, @PathVariable String id, @RequestBody Map<String, Object> b) {
        requireAdmin(req);
        String st = str(b.get("status"));
        if (!st.equals("aktif") && !st.equals("nonaktif")) throw bad("status harus 'aktif' atau 'nonaktif'");
        if (id.equals(String.valueOf(req.getAttribute("user_id")))) throw bad("Tidak bisa mengubah status akun sendiri");
        if (jdbc.queryForList("SELECT id FROM users WHERE id=? AND is_deleted=0", id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan");
        jdbc.update("UPDATE users SET status=?, updated_at=? WHERE id=?", st, System.currentTimeMillis(), id);
        if (st.equals("nonaktif")) tokens.revokeUser(id); // sesinya langsung dicabut
        return get(req, id);
    }

    private String nextKode(String level) {
        String pre = level.equals("admin") ? "ADM" : "KSR";
        Integer max = jdbc.queryForObject(
            "SELECT MAX(CAST(SUBSTRING(kode,4) AS UNSIGNED)) FROM users WHERE kode LIKE ?", Integer.class, pre + "%");
        return pre + String.format("%02d", (max == null ? 0 : max) + 1);
    }

    private static String str(Object o) { return o == null ? "" : String.valueOf(o).trim(); }
    private static ResponseStatusException bad(String m) { return new ResponseStatusException(HttpStatus.BAD_REQUEST, m); }
}