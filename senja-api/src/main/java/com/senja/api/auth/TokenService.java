package com.senja.api.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TokenService {

    public static final long TTL_MS = 12L * 3600 * 1000; // sesi 12 jam
    private final JdbcTemplate jdbc;
    private final SecureRandom rnd = new SecureRandom();

    public TokenService(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    /** Login → Map berisi token, expires_at, user (tanpa password). Tidak pakai record bersarang. */
    public Map<String, Object> login(String kode, String password) {
        List<Map<String, Object>> rows = jdbc.queryForList(
            "SELECT id,kode,nama,level,password_hash,status FROM users WHERE is_deleted=0 AND lower(kode)=?",
            kode == null ? "" : kode.toLowerCase());
        if (rows.isEmpty()) throw unauthorized("User ID tidak ditemukan");
        Map<String, Object> u = rows.get(0);
        String hash = String.valueOf(u.get("password_hash"));
        if (hash == null || !hash.startsWith("$2")) throw unauthorized("Kredensial salah");
        if (!BCrypt.checkpw(password == null ? "" : password, hash)) throw unauthorized("Password salah");
        if (!"aktif".equals(u.get("status"))) throw forbidden("Akun dinonaktifkan");
        byte[] raw = new byte[32];
        rnd.nextBytes(raw);
        String token = HexFormat.of().formatHex(raw);
        long now = System.currentTimeMillis();
        jdbc.update("INSERT INTO auth_tokens(token_hash,user_id,created_at,expires_at) VALUES (?,?,?,?)",
            sha256(token), u.get("id"), now, now + TTL_MS);
        u.remove("password_hash"); // password tidak pernah keluar dari server
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("token", token);
        out.put("expires_at", now + TTL_MS);
        out.put("user", u);
        return out;
    }

    public Map<String, Object> verify(String token) {
        List<Map<String, Object>> rows = jdbc.queryForList("""
            SELECT t.user_id, t.expires_at, u.level, u.status
            FROM auth_tokens t JOIN users u ON u.id=t.user_id
            WHERE t.token_hash=?""", sha256(token));
        if (rows.isEmpty()) throw unauthorized("Token tidak valid");
        Map<String, Object> r = rows.get(0);
        if (((Number) r.get("expires_at")).longValue() < System.currentTimeMillis()) {
            jdbc.update("DELETE FROM auth_tokens WHERE token_hash=?", sha256(token));
            throw unauthorized("Token kedaluwarsa — login ulang");
        }
        if (!"aktif".equals(r.get("status"))) throw forbidden("Akun dinonaktifkan");
        return r;
    }

    public void logout(String token) { jdbc.update("DELETE FROM auth_tokens WHERE token_hash=?", sha256(token)); }
    public void revokeUser(String userId) { jdbc.update("DELETE FROM auth_tokens WHERE user_id=?", userId); }

    static String sha256(String s) {
        try { return HexFormat.of().formatHex(
            MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8))); }
        catch (NoSuchAlgorithmException e) { throw new IllegalStateException(e); }
    }
    static ResponseStatusException unauthorized(String m) { return new ResponseStatusException(HttpStatus.UNAUTHORIZED, m); }
    static ResponseStatusException forbidden(String m) { return new ResponseStatusException(HttpStatus.FORBIDDEN, m); }
}