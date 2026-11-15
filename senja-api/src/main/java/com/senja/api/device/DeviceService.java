package com.senja.api.device;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DeviceService {

    private final JdbcTemplate jdbc;

    public DeviceService(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public Map<String, Object> register(String nama, String tipe) {
        if (nama == null || nama.isBlank() || tipe == null || tipe.isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nama & tipe wajib diisi");
        String id = UUID.randomUUID().toString();
        String key = UUID.randomUUID().toString().replace("-", "");
        long now = System.currentTimeMillis();
        jdbc.update("INSERT INTO devices(id,nama,tipe,api_key_hash,last_seen,created_at) VALUES (?,?,?,?,?,?)",
            id, nama.trim(), tipe.trim(), sha256(key), now, now);
        return Map.of("device_id", id, "api_key", key); // api_key cuma ditampilkan sekali
    }

    public void verify(String deviceId, String apiKey) {
        if (deviceId == null || apiKey == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Header X-Device-Id & X-Api-Key wajib diisi");
        Integer n = jdbc.queryForObject(
            "SELECT COUNT(*) FROM devices WHERE id=? AND api_key_hash=?", Integer.class, deviceId, sha256(apiKey));
        if (n == null || n == 0)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Device tidak terdaftar / api key salah");
    }

    private String sha256(String s) {
        try {
            return HexFormat.of().formatHex(
                MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) { throw new IllegalStateException(e); }
    }
}