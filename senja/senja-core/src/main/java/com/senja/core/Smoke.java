package com.senja.core;

/** Uji: init DB → seed → sync ke API. Jalankan: mvn -pl senja-core clean compile exec:java */
public final class Smoke {
    public static void main(String[] args) {
        Db.init();
        System.out.println("SQLite  : " + Db.DB_FILE);
        System.out.println("Server  : " + SyncMeta.get("server_url", "http://localhost:8080") + "\n");

        boolean kosong = !Seed.isMasterSeeded();
        Seed.seedMasterIfEmpty();
        Seed.seedDemoIfEmpty();
        System.out.println(kosong ? "Seed master + demo selesai\n" : "DB sudah berisi data (seed dilewati)\n");
        counts();

        System.out.println("\nSinkronisasi ke API...");
        SyncResult r = SyncEngine.sync("admin");
        if (r.online()) {
            System.out.println("ONLINE  : pushed=" + r.totalPushed() + "  pulled=" + r.totalPulled());
            if (!r.pushed().isEmpty()) System.out.println("  push : " + r.pushed());
            if (!r.pulled().isEmpty()) System.out.println("  pull : " + r.pulled());
        } else {
            System.out.println("OFFLINE : " + r.error());
        }

        System.out.println("\nStok (hasil recalc dari buku besar):");
        Db.query("SELECT kode,nama,stok FROM bahan ORDER BY kode LIMIT 5")
          .forEach(m -> System.out.printf("  %s  %-22s %s%n", m.get("kode"), m.get("nama"), m.get("stok")));

        System.out.println("\nTop 5 terlaris:");
        Db.query("""
                SELECT d.nama AS nama, SUM(d.qty) AS qty
                FROM transaksi_detail d JOIN transaksi t ON t.id = d.transaksi_id
                WHERE t.is_deleted = 0
                GROUP BY d.produk_id, d.nama ORDER BY qty DESC LIMIT 5
                """)
          .forEach(m -> System.out.printf("  %-28s %s terjual%n", m.get("nama"), m.get("qty")));
    }

    private static void counts() {
        for (String t : new String[]{"users","kategori","bahan","produk","resep","diskon","supplier",
                "pembelian","pengeluaran","shift","absensi","transaksi","transaksi_detail","stok_mutasi"})
            System.out.printf("  %-16s %d%n", t, Db.count(t));
    }
}