package com.senja.core;

import java.sql.Connection;
import java.sql.PreparedStatement;

/** Key-value store di tabel sync_meta (device_id, api_key, server_url, last_pull). */
public final class SyncMeta {
    private SyncMeta() {}

    public static String get(String kunci) {
        Db.init();
        var r = Db.query("SELECT nilai FROM sync_meta WHERE kunci=?", kunci);
        return r.isEmpty() ? null : (String) r.get(0).get("nilai");
    }

    public static String get(String kunci, String def) {
        Db.init();
        String v = get(kunci);
        return v == null ? def : v;
    }

    public static long getLong(String kunci, long def) {
        try { return Long.parseLong(get(kunci, String.valueOf(def))); }
        catch (NumberFormatException e) { return def; }
    }

    public static void set(String kunci, String nilai) {
        Db.init();
        Db.exec("INSERT INTO sync_meta(kunci,nilai) VALUES(?,?) " +
                "ON CONFLICT(kunci) DO UPDATE SET nilai=excluded.nilai", kunci, nilai);
    }

    /** Versi yang memakai koneksi transaksi yang sama (dipakai SyncEngine). */
    public static void set(Connection c, String kunci, String nilai) throws Exception {
        String sql = "INSERT INTO sync_meta(kunci,nilai) VALUES(?,?) " +
                     "ON CONFLICT(kunci) DO UPDATE SET nilai=excluded.nilai";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, kunci); ps.setString(2, nilai); ps.executeUpdate();
        }
    }
}