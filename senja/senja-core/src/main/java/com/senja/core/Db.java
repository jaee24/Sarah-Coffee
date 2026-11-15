package com.senja.core;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

/** Akses SQLite. Lokasi file: env SENJA_DB, default ~/.senja/senja.db */
public final class Db {

    public static final Path DB_FILE = System.getenv("SENJA_DB") != null
            ? Paths.get(System.getenv("SENJA_DB"))
            : Paths.get(System.getProperty("user.home"), ".senja", "senja.db");

    private static volatile boolean inited = false;

    private Db() {}

    public static Connection open() throws SQLException {
        try { Files.createDirectories(DB_FILE.getParent()); }
        catch (IOException e) { throw new SQLException("Gagal membuat folder DB", e); }
        Connection c = DriverManager.getConnection("jdbc:sqlite:" + DB_FILE);
        try (Statement st = c.createStatement()) {
            st.execute("PRAGMA foreign_keys=ON");
            st.execute("PRAGMA journal_mode=WAL");     // 2 app boleh buka DB yang sama
            st.execute("PRAGMA busy_timeout=5000");
        }
        return c;
    }

    /** Buat semua tabel dari resources/schema.sql + jalankan migrasi kolom. Idempoten. */
    public static synchronized void init() {
        if (inited) return;
        try (Connection c = open()) {
            if (!c.getMetaData().getTables(null, null, "users", null).next()) {
                String raw = new String(readResource("/schema.sql"), StandardCharsets.UTF_8);
                StringBuilder sb = new StringBuilder();
                for (String line : raw.split("\n"))
                    if (!line.strip().startsWith("--")) sb.append(line).append('\n');
                for (String stmt : sb.toString().split(";"))
                    if (!stmt.strip().isEmpty())
                        try (Statement st = c.createStatement()) { st.execute(stmt); }
            }
            migrate(c);
            inited = true;
        } catch (Exception e) {
            throw new RuntimeException("Inisialisasi DB gagal: " + e.getMessage(), e);
        }
    }

    /**
     * Upgrade kolom untuk DB yang dibuat sebelum kolom itu ada.
     * DB baru pun tetap aman: cek dulu, kolom sudah ada → tidak di-ALTER.
     * Tambahkan ensureColumn baru di sini setiap kali skema berkembang.
     */
    private static void migrate(Connection c) throws SQLException {
        ensureColumn(c, "users", "status",
            "ALTER TABLE users ADD COLUMN status VARCHAR(10) NOT NULL DEFAULT 'aktif'");
        ensureColumn(c, "diskon", "maks_potongan",
            "ALTER TABLE diskon ADD COLUMN maks_potongan BIGINT");
        ensureColumn(c, "transaksi_detail", "hpp",
            "ALTER TABLE transaksi_detail ADD COLUMN hpp BIGINT");
    }

    private static void ensureColumn(Connection c, String table, String col, String alterSql) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT " + col + " FROM " + table + " LIMIT 1")) {
            ps.executeQuery(); // kolom ada → selesai
        } catch (SQLException e) {
            try (Statement st = c.createStatement()) { st.execute(alterSql); }
        }
    }

    public interface SqlBody { void run(Connection c) throws Exception; }

    /** Jalankan beberapa operasi dalam SATU transaksi. */
    public static void tx(SqlBody body) {
        try (Connection c = open()) {
            c.setAutoCommit(false);
            try { body.run(c); c.commit(); }
            catch (Exception e) {
                try { c.rollback(); } catch (SQLException ignore) {}
                throw new RuntimeException(e);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static int exec(String sql, Object... args) {
        try (Connection c = open(); PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, args);
            return ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static List<Map<String, Object>> query(String sql, Object... args) {
        try (Connection c = open(); PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, args);
            try (ResultSet rs = ps.executeQuery()) {
                List<Map<String, Object>> out = new ArrayList<>();
                ResultSetMetaData md = rs.getMetaData();
                while (rs.next()) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    for (int i = 1; i <= md.getColumnCount(); i++)
                        m.put(md.getColumnLabel(i), rs.getObject(i));
                    out.add(m);
                }
                return out;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static long count(String table) {
        var r = query("SELECT COUNT(*) AS n FROM " + table);
        return r.isEmpty() ? 0 : ((Number) r.get(0).get("n")).longValue();
    }

    private static void bind(PreparedStatement ps, Object... args) throws SQLException {
        for (int i = 0; i < args.length; i++) ps.setObject(i + 1, args[i]);
    }

    private static byte[] readResource(String path) throws IOException {
        try (InputStream in = Db.class.getResourceAsStream(path)) {
            if (in == null) throw new IOException("Resource tidak ditemukan: " + path);
            return in.readAllBytes();
        }
    }
}