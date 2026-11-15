package com.senja.core;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Seed data. Master data pakai UUID deterministik → device mana pun yang seed
 * menghasilkan baris sama (tanpa duplikasi saat sync).
 * Demo (transaksi dkk.) cukup di-seed SATU device; device lain dapat via pull.
 */
public final class Seed {

    private Seed() {}

    private static final String OM = "[\"regular\",\"large\",\"less_ice\",\"normal_ice\",\"less_sugar\",\"normal_sugar\"]";
    private static final String OM2 = "[\"regular\",\"less_sugar\",\"normal_sugar\"]";
    private static final String HC = "[\"hot\",\"cold\"]", H = "[\"hot\"]", C = "[\"cold\"]";
    private static final String KOSONG = "[]";

    private static final Object[][] BAHAN = {
        {"BB-01","Kopi Arabica","gram",1800,500,130},{"BB-02","Espresso Blend","gram",2200,500,120},
        {"BB-03","Susu Segar","ml",15000,4000,18},{"BB-04","Gula Pasir","gram",6000,1500,15},
        {"BB-05","Sirup Caramel","ml",2600,600,90},{"BB-06","Bubuk Matcha","gram",900,250,340},
        {"BB-07","Cokelat Bubuk","gram",1100,300,180},{"BB-08","Air Mineral","ml",60000,15000,1},
        {"BB-09","Susu Kental Manis","gram",4200,1000,40},{"BB-10","Telur","pcs",80,24,2500},
        {"BB-11","Tepung Terigu","gram",9000,2000,12},{"BB-12","Mentega","gram",2400,600,45},
        {"BB-13","Keju Cheddar","gram",1800,500,110},{"BB-14","Bun Burger","pcs",45,15,3500},
        {"BB-15","Patty Ayam","pcs",55,20,6500},{"BB-16","Es Batu","gram",25000,6000,1},
        {"BB-17","Bubuk Teh Hitam","gram",700,180,90},{"BB-18","Pisang Raja","pcs",140,40,2000},
        {"BB-19","Gula Aren","gram",2200,500,35},{"BB-20","Minyak Goreng","ml",8000,2000,3},
        {"BB-21","Kentang Beku","gram",5000,1500,28}
    };

    private static final Object[][] PRODUK = {
        {"PR-01","Kopi Susu Gula Aren","Coffee",HC,OM,22000,new String[][]{{"BB-02","18"},{"BB-03","150"},{"BB-19","25"}}},
        {"PR-02","Caramel Latte","Coffee",HC,OM,28000,new String[][]{{"BB-02","18"},{"BB-03","180"},{"BB-05","20"},{"BB-08","60"}}},
        {"PR-03","Americano","Coffee",HC,OM,18000,new String[][]{{"BB-02","18"},{"BB-08","220"}}},
        {"PR-04","Cappuccino","Coffee",HC,OM,26000,new String[][]{{"BB-02","18"},{"BB-03","140"}}},
        {"PR-05","Kopi Tubruk","Coffee",H,OM2,12000,new String[][]{{"BB-01","12"},{"BB-08","200"}}},
        {"PR-06","Matcha Latte","Non-Coffee",HC,OM,26000,new String[][]{{"BB-06","12"},{"BB-03","150"},{"BB-04","10"}}},
        {"PR-07","Cokelat Hangat","Non-Coffee",HC,OM,24000,new String[][]{{"BB-07","20"},{"BB-03","150"},{"BB-09","20"}}},
        {"PR-08","Teh Tarik","Non-Coffee",HC,OM,18000,new String[][]{{"BB-17","8"},{"BB-03","120"},{"BB-09","15"}}},
        {"PR-09","Lemon Tea","Non-Coffee",C,OM,16000,new String[][]{{"BB-17","10"},{"BB-08","250"},{"BB-04","15"}}},
        {"PR-10","Chicken Burger","Food",KOSONG,KOSONG,32000,new String[][]{{"BB-14","1"},{"BB-15","1"},{"BB-11","50"},{"BB-12","10"},{"BB-13","15"}}},
        {"PR-11","Kentang Goreng","Food",KOSONG,KOSONG,20000,new String[][]{{"BB-21","200"},{"BB-20","30"}}},
        {"PR-12","Roti Bakar Cokelat Keju","Cemilan",KOSONG,KOSONG,24000,new String[][]{{"BB-11","80"},{"BB-07","15"},{"BB-13","20"},{"BB-12","12"}}},
        {"PR-13","Pisang Goreng Keju","Cemilan",KOSONG,KOSONG,22000,new String[][]{{"BB-18","2"},{"BB-11","60"},{"BB-13","20"},{"BB-20","50"}}},
        {"PR-14","Donat Gula","Cemilan",KOSONG,KOSONG,12000,new String[][]{{"BB-11","100"},{"BB-04","15"},{"BB-12","10"},{"BB-10","0.25"}}},
        {"PR-15","Cireng Bumbu Rujak","Cemilan",KOSONG,KOSONG,15000,new String[][]{{"BB-11","80"},{"BB-04","10"},{"BB-20","40"}}},
        {"PR-16","Croissant Butter","Cemilan",KOSONG,KOSONG,25000,new String[][]{{"BB-11","100"},{"BB-12","40"}}}
    };

    /* ================= PUBLIC ================= */

    public static boolean isMasterSeeded() { return Db.count("users") > 0; }
    public static boolean isDemoSeeded()   { return Db.count("transaksi") > 0; }

    public static void seedMasterIfEmpty() {
        if (isMasterSeeded()) return;
        Db.tx(Seed::seedMaster);
    }

    public static void seedDemoIfEmpty() {
        if (isDemoSeeded() || !isMasterSeeded()) return;
        Db.tx(Seed::seedDemo);
    }

    /* ================= helpers ================= */

    private static String uid(String k) { return Uuid.s("users:" + k); }
    private static String kid(String n) { return Uuid.s("kategori:" + n); }
    private static String bid(String k) { return Uuid.s("bahan:" + k); }
    private static String pid(String k) { return Uuid.s("produk:" + k); }
    private static String sid(String k) { return Uuid.s("supplier:" + k); }

    private static String dOff(int n) { return LocalDate.now().plusDays(n).toString(); }
    private static long epoch(String tanggal, String jam) {
        return LocalDateTime.of(LocalDate.parse(tanggal), LocalTime.parse(jam))
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private static Map<String, Object> row(Object... kv) {
        Map<String, Object> m = new LinkedHashMap<>();
        for (int i = 0; i < kv.length; i += 2) m.put((String) kv[i], kv[i + 1]);
        return m;
    }

    private static void ins(Connection c, String table, Map<String, Object> data) throws Exception {
        long now = System.currentTimeMillis();
        data.putIfAbsent("created_at", now);
        data.putIfAbsent("updated_at", now);
        data.putIfAbsent("is_deleted", 0);
        data.putIfAbsent("dirty", 1);   // seed ikut ter-push saat sync pertama
        String cols = String.join(",", data.keySet());
        String marks = String.join(",", Collections.nCopies(data.size(), "?"));
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO " + table + " (" + cols + ") VALUES (" + marks + ")")) {
            int i = 1;
            for (Object v : data.values()) ps.setObject(i++, v);
            ps.executeUpdate();
        }
    }

    /* ================= MASTER ================= */

    private static void seedMaster(Connection c) throws Exception {
        String hashAdmin = BCrypt.hashpw("admin123", BCrypt.gensalt());
        String hashKasir = BCrypt.hashpw("kasir123", BCrypt.gensalt());

        ins(c, "users", row("id", uid("ADM01"), "kode", "ADM01", "nama", "Nadia Prameswari",
                "level", "admin", "password_hash", hashAdmin, "no_hp", "081234567001",
                "alamat", "Jl. Melati No. 3, Yogyakarta"));
        ins(c, "users", row("id", uid("KSR01"), "kode", "KSR01", "nama", "Rani Safitri",
                "level", "kasir", "password_hash", hashKasir, "no_hp", "081234567002",
                "alamat", "Jl. Kenanga No. 7, Yogyakarta"));
        ins(c, "users", row("id", uid("KSR02"), "kode", "KSR02", "nama", "Bagas Pratama",
                "level", "kasir", "password_hash", hashKasir, "no_hp", "081234567003",
                "alamat", "Jl. Anggrek No. 21, Sleman"));

        ins(c, "kategori", row("id", kid("Coffee"), "nama", "Coffee", "jenis", "minuman", "penyajian", HC));
        ins(c, "kategori", row("id", kid("Non-Coffee"), "nama", "Non-Coffee", "jenis", "minuman", "penyajian", HC));
        ins(c, "kategori", row("id", kid("Food"), "nama", "Food", "jenis", "makanan", "penyajian", KOSONG));
        ins(c, "kategori", row("id", kid("Cemilan"), "nama", "Cemilan", "jenis", "makanan", "penyajian", KOSONG));

        for (Object[] b : BAHAN) {
            ins(c, "bahan", row("id", bid((String) b[0]), "kode", b[0], "nama", b[1], "satuan", b[2],
                    "stok", b[3], "stok_min", b[4], "harga", b[5]));
            ins(c, "stok_mutasi", row("id", Uuid.s("mut:awal:" + b[0]), "bahan_id", bid((String) b[0]),
                    "arah", "masuk", "jumlah", (double) (int) b[3], "sumber", "awal",
                    "ref_id", null, "keterangan", "Stok awal", "waktu", System.currentTimeMillis(),
                    "device_id", "seed"));
        }

        for (Object[] p : PRODUK) {
            String kode = (String) p[0];
            ins(c, "produk", row("id", pid(kode), "kode", kode, "nama", p[1],
                    "kategori_id", kid((String) p[2]), "penyajian", p[3], "opsi", p[4],
                    "harga", p[5], "status", "aktif",
                    "gambar", "https://picsum.photos/seed/senja-" + kode.substring(3) + "/600/450.jpg"));
            for (String[] r : (String[][]) p[6])
                ins(c, "resep", row("id", Uuid.s("resep:" + kode + ":" + r[0]),
                        "produk_id", pid(kode), "bahan_id", bid(r[0]),
                        "jumlah", Double.parseDouble(r[1])));
        }

        ins(c, "diskon", row("id", Uuid.s("diskon:SENJA10"), "kode", "SENJA10", "tipe", "persen",
                "nilai", 10, "min_belanja", 30000, "mulai", dOff(-30), "berakhir", dOff(60), "status", "aktif"));
        ins(c, "diskon", row("id", Uuid.s("diskon:HEMAT5K"), "kode", "HEMAT5K", "tipe", "nominal",
                "nilai", 5000, "min_belanja", 25000, "mulai", dOff(-10), "berakhir", dOff(30), "status", "aktif"));
        ins(c, "diskon", row("id", Uuid.s("diskon:PROMO20"), "kode", "PROMO20", "tipe", "persen",
                "nilai", 20, "min_belanja", 50000, "mulai", dOff(-90), "berakhir", dOff(-5), "status", "aktif"));

        ins(c, "supplier", row("id", sid("SUP-01"), "kode", "SUP-01", "nama", "Toko Kopi Makmur",
                "no_hp", "081299887766", "alamat", "Jl. Pasar Gede No. 12, Yogyakarta"));
        ins(c, "supplier", row("id", sid("SUP-02"), "kode", "SUP-02", "nama", "Susu Murni Nusantara",
                "no_hp", "081377665544", "alamat", "Jl. Kaliurang KM 5, Sleman"));
        ins(c, "supplier", row("id", sid("SUP-03"), "kode", "SUP-03", "nama", "Segar Jaya Bakery",
                "no_hp", "081555444333", "alamat", "Jl. Solo KM 8, Yogyakarta"));

        setting(c, "toko_nama", "SENJA COFFEE");
        setting(c, "toko_alamat", "Jl. Kenanga No. 12, Yogyakarta");
        setting(c, "toko_telp", "0812-3456-7890");
        setting(c, "struk_footer", "Terima kasih sudah mampir!");
        setting(c, "theme", "light");
        setting(c, "lang", "id");
    }

    private static void setting(Connection c, String kunci, String nilai) throws Exception {
        ins(c, "pengaturan", row("id", Uuid.s("set:" + kunci), "kunci", kunci, "nilai", nilai));
    }

    /* ================= DEMO (cukup 1 device) ================= */

    private static void seedDemo(Connection c) throws Exception {
        long now = System.currentTimeMillis();

        demoPembelian(c, "PO-001", dOff(-9), "SUP-01",
                new String[][]{{"BB-01","1000","125"},{"BB-02","1200","118"}}, now);
        demoPembelian(c, "PO-002", dOff(-5), "SUP-02",
                new String[][]{{"BB-03","10000","17"},{"BB-09","2000","38"}}, now);

        Object[][] exp = {
            {"EXP-001", -6, "Pembayaran token listrik & air", "Operasional", "rutin", 450000},
            {"EXP-002", -4, "Isi ulang LPG 2 tabung", "Operasional", "rutin", 340000},
            {"EXP-003", -3, "Bonus penjualan tim", "Lainnya", "rutin", 300000},
            {"EXP-004", -1, "Servis mesin espresso & ganti seal", "Perawatan", "tak_terduga", 275000},
            {"EXP-005", 0, "Sample biji kopi baru", "Operasional", "rutin", 150000}
        };
        for (Object[] e : exp)
            ins(c, "pengeluaran", row("id", Uuid.s("exp:" + e[0]), "kode", e[0],
                    "tanggal", dOff((int) e[1]), "keterangan", e[2],
                    "kategori", e[3], "jenis", e[4], "jumlah", e[5]));

        for (int i = -6; i <= 13; i++) {
            String ds = dOff(i);
            ins(c, "shift", row("id", Uuid.s("shift:KSR01:" + ds), "user_id", uid("KSR01"), "tanggal", ds, "jenis", "P"));
            ins(c, "shift", row("id", Uuid.s("shift:KSR02:" + ds), "user_id", uid("KSR02"), "tanggal", ds, "jenis", "S"));
        }
        for (int i = 1; i <= 6; i++) {
            String ds = dOff(-i);
            ins(c, "absensi", row("id", Uuid.s("abs:KSR01:" + ds), "user_id", uid("KSR01"), "tanggal", ds,
                    "shift_jenis", "P", "masuk", "09:5" + i, "pulang", "16:3" + (i % 6), "status", "hadir"));
            ins(c, "absensi", row("id", Uuid.s("abs:KSR02:" + ds), "user_id", uid("KSR02"), "tanggal", ds,
                    "shift_jenis", "S", "masuk", "16:2" + i, "pulang", "23:0" + (i % 6), "status", "hadir"));
        }

        Random rnd = new Random(20240521);       // seed tetap → data reproducible
        String[] kasir = {"KSR01", "KSR02"}, metode = {"cash", "qris", "debit"};
        Map<String, Integer> seq = new HashMap<>();

        for (int day = 13; day >= 0; day--) {
            String ds = dOff(-day);
            int n = 3 + rnd.nextInt(5);
            for (int k = 0; k < n; k++) {
                int jam = 10 + rnd.nextInt(12);
                if (day == 0 && jam > LocalTime.now().getHour()) continue;
                int no = seq.merge(ds, 1, Integer::sum);
                String kode = "TRX-" + ds.replace("-", "") + "-" + String.format("%02d", no);
                String trxId = Uuid.s("trx:" + kode);

                int nItem = 1 + rnd.nextInt(3);
                List<Integer> dipakai = new ArrayList<>();
                List<Object[]> items = new ArrayList<>();   // {produk, qty, harga, opsi}
                long subtotal = 0;
                while (items.size() < nItem) {
                    int ix = rnd.nextInt(PRODUK.length);
                    if (dipakai.contains(ix)) continue;
                    dipakai.add(ix);
                    Object[] p = PRODUK[ix];
                    boolean minuman = p[2].equals("Coffee") || p[2].equals("Non-Coffee");
                    int qty = 1 + rnd.nextInt(2);
                    long harga = (int) p[5];
                    StringBuilder opsi = new StringBuilder();
                    if (minuman) {
                        opsi.append(rnd.nextBoolean() ? "Hot" : "Cold");
                        if (rnd.nextInt(10) < 3) { opsi.append(" · Large"); harga += Const.HARGA_LARGE; }
                        else opsi.append(" · Regular");
                        if (opsi.charAt(0) == 'C') opsi.append(rnd.nextBoolean() ? " · Normal Ice" : " · Less Ice");
                        opsi.append(rnd.nextBoolean() ? " · Normal Sugar" : " · Less Sugar");
                    }
                    items.add(new Object[]{p, qty, harga, opsi.toString()});
                    subtotal += harga * qty;
                }

                long diskon = 0; String dkode = null;
                if (subtotal >= 30000 && rnd.nextInt(10) < 3) { diskon = subtotal / 10; dkode = "SENJA10"; }
                long total = subtotal - diskon;
                String met = metode[rnd.nextInt(3)];
                long dibayar = met.equals("cash") ? ((total + 4999) / 5000) * 5000 : total;

                ins(c, "transaksi", row("id", trxId, "kode", kode,
                        "waktu", epoch(ds, String.format("%02d:00", jam)),
                        "user_id", uid(kasir[rnd.nextInt(2)]), "subtotal", subtotal,
                        "diskon", diskon, "diskon_kode", dkode, "total", total,
                        "metode", met, "dibayar", dibayar, "kembalian", dibayar - total,
                        "ref", null, "status", "selesai", "device_id", "seed"));

                for (Object[] it : items) {
                    Object[] p = (Object[]) it[0];
                    int qty = (int) it[1]; long harga = (long) it[2]; String opsi = (String) it[3];
                    ins(c, "transaksi_detail", row("id", Uuid.s("txd:" + kode + ":" + p[0]),
                            "transaksi_id", trxId, "produk_id", pid((String) p[0]),
                            "nama", p[1], "opsi", opsi.isEmpty() ? null : opsi,
                            "qty", qty, "harga", harga, "subtotal", harga * qty));
                    for (String[] r : (String[][]) p[6])       // mutasi keluar sesuai resep
                        ins(c, "stok_mutasi", row("id", Uuid.s("mut:trx:" + kode + ":" + p[0] + ":" + r[0]),
                                "bahan_id", bid(r[0]), "arah", "keluar",
                                "jumlah", Double.parseDouble(r[1]) * qty,
                                "sumber", "transaksi", "ref_id", trxId,
                                "keterangan", kode, "waktu", now, "device_id", "seed"));
                }
            }
        }
        SyncEngine.recalcStok(c);
    }

    private static void demoPembelian(Connection c, String kode, String tanggal, String supKode,
                                      String[][] rows, long now) throws Exception {
        long total = 0;
        for (String[] r : rows) total += Long.parseLong(r[1]) * Long.parseLong(r[2]);
        String poId = Uuid.s("po:" + kode);
        ins(c, "pembelian", row("id", poId, "kode", kode, "tanggal", tanggal,
                "supplier_id", sid(supKode), "user_id", uid("ADM01"), "total", total));
        for (String[] r : rows) {
            long j = Long.parseLong(r[1]), h = Long.parseLong(r[2]);
            ins(c, "pembelian_detail", row("id", Uuid.s("pod:" + kode + ":" + r[0]),
                    "pembelian_id", poId, "bahan_id", bid(r[0]),
                    "jumlah", (double) j, "harga", h, "subtotal", j * h));
            ins(c, "stok_mutasi", row("id", Uuid.s("mut:po:" + kode + ":" + r[0]),
                    "bahan_id", bid(r[0]), "arah", "masuk", "jumlah", (double) j,
                    "sumber", "pembelian", "ref_id", poId, "keterangan", kode,
                    "waktu", now, "device_id", "seed"));
        }
    }
}