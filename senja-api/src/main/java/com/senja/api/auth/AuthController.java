package com.senja.api.auth;

import java.util.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final TokenService tokens;
    private final JdbcTemplate jdbc;

    public AuthController(TokenService tokens, JdbcTemplate jdbc) {
        this.tokens = tokens;
        this.jdbc = jdbc;
    }
    
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, Object> b) {
        return tokens.login(str(b.get("kode")), str(b.get("password")));
    }
    @PostMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest req) {
        tokens.logout(bearer(req));
        return Map.of("ok", true);
    }

    @GetMapping("/me")
    public Map<String, Object> me(HttpServletRequest req) {
        return jdbc.queryForMap(
            "SELECT id,kode,nama,level,no_hp,alamat,status FROM users WHERE id=?",
            req.getAttribute("user_id"));
    }

    @PutMapping("/password")
    public Map<String, Object> password(HttpServletRequest req, @RequestBody Map<String, Object> b) {
        String uid = String.valueOf(req.getAttribute("user_id"));
        Map<String, Object> u = jdbc.queryForMap("SELECT password_hash FROM users WHERE id=?", uid);
        if (!BCrypt.checkpw(str(b.get("password_lama")), String.valueOf(u.get("password_hash"))))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password lama salah");
        String baru = str(b.get("password_baru"));
        if (baru.length() < 4) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password baru minimal 4 karakter");
        jdbc.update("UPDATE users SET password_hash=?, updated_at=?, dirty=0 WHERE id=?",
            BCrypt.hashpw(baru, BCrypt.gensalt()), System.currentTimeMillis(), uid);
        tokens.revokeUser(uid); // paksa semua sesi login ulang
        return Map.of("ok", true, "pesan", "Password diubah — login ulang diperlukan");
    }

    @PutMapping("/profile")
    public Map<String, Object> profile(HttpServletRequest req, @RequestBody Map<String, Object> b) {
        String uid = String.valueOf(req.getAttribute("user_id"));
        List<String> sets = new ArrayList<>();
        List<Object> args = new ArrayList<>();
        if (b.containsKey("nama") && !str(b.get("nama")).isBlank()) { sets.add("nama=?"); args.add(str(b.get("nama"))); }
        if (b.containsKey("no_hp")) { sets.add("no_hp=?"); args.add(str(b.get("no_hp"))); }
        if (b.containsKey("alamat")) { sets.add("alamat=?"); args.add(str(b.get("alamat"))); }
        if (sets.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tidak ada perubahan");
        sets.add("updated_at=?"); args.add(System.currentTimeMillis());
        args.add(uid);
        jdbc.update("UPDATE users SET " + String.join(",", sets) + " WHERE id=?", args.toArray());
        return me(req);
    }

    private static String bearer(HttpServletRequest req) {
        String a = req.getHeader("Authorization");
        return a != null && a.startsWith("Bearer ") ? a.substring(7).trim() : "";
    }
    private static String str(Object o) { return o == null ? "" : String.valueOf(o).trim(); }
}