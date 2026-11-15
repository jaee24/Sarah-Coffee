-- ================================================================
--  SENJA COFFEE — schema.sql (portabel: SQLite & MySQL 8+)
--  SQLite : jalankan sekali saat app pertama dibuka.
--           Wajib: PRAGMA foreign_keys = ON di tiap koneksi.
--  MySQL  : CREATE DATABASE senja CHARACTER SET utf8mb4
--           COLLATE utf8mb4_unicode_ci; lalu jalankan skrip ini.
-- ================================================================

-- ---------- 1. PENGGUNA / KARYAWAN ----------
CREATE TABLE users (
  id            CHAR(36)     NOT NULL PRIMARY KEY,
  kode          VARCHAR(10)  NOT NULL,             -- ADM01 / KSR01 (tidak bisa diubah)
  nama          VARCHAR(100) NOT NULL,
  level         VARCHAR(10)  NOT NULL CHECK (level IN ('admin','kasir')),
  password_hash VARCHAR(100) NOT NULL,             -- BCrypt, BUKAN plaintext
  no_hp         VARCHAR(20)  NULL,
  alamat        VARCHAR(200) NULL,
  created_at    BIGINT NOT NULL,
  updated_at    BIGINT NOT NULL,
  is_deleted    INTEGER NOT NULL DEFAULT 0,
  dirty         INTEGER NOT NULL DEFAULT 1,
  UNIQUE (kode)
);

-- ---------- 2. KATEGORI ----------
CREATE TABLE kategori (
  id         CHAR(36)    NOT NULL PRIMARY KEY,
  nama       VARCHAR(50) NOT NULL,
  jenis      VARCHAR(10) NOT NULL CHECK (jenis IN ('minuman','makanan')),
  penyajian  VARCHAR(50) NULL,                     -- JSON: ["hot","cold"]
  created_at BIGINT NOT NULL,
  updated_at BIGINT NOT NULL,
  is_deleted INTEGER NOT NULL DEFAULT 0,
  dirty      INTEGER NOT NULL DEFAULT 1,
  UNIQUE (nama)
);

-- ---------- 3. PRODUK / MENU ----------
CREATE TABLE produk (
  id          CHAR(36)     NOT NULL PRIMARY KEY,
  kode        VARCHAR(10)  NOT NULL,               -- PR-01
  nama        VARCHAR(100) NOT NULL,
  kategori_id CHAR(36)     NOT NULL,
  penyajian   VARCHAR(50)  NULL,                   -- JSON: ["hot","cold"]
  opsi        VARCHAR(150) NULL,                   -- JSON: ["regular","large","less_ice",...]
  harga       BIGINT       NOT NULL,               -- rupiah, tanpa sen
  status      VARCHAR(10)  NOT NULL CHECK (status IN ('aktif','nonaktif')),
  gambar      VARCHAR(300) NULL,                   -- path/URL
  created_at  BIGINT NOT NULL,
  updated_at  BIGINT NOT NULL,
  is_deleted  INTEGER NOT NULL DEFAULT 0,
  dirty      INTEGER NOT NULL DEFAULT 1,
  UNIQUE (kode),
  FOREIGN KEY (kategori_id) REFERENCES kategori(id)
);

-- ---------- 4. BAHAN BAKU ----------
-- stok = CACHE tampilan. Sumber kebenaran = stok_mutasi.
CREATE TABLE bahan (
  id         CHAR(36)     NOT NULL PRIMARY KEY,
  kode       VARCHAR(10)  NOT NULL,                -- BB-01
  nama       VARCHAR(100) NOT NULL,
  satuan     VARCHAR(10)  NOT NULL,                -- gram / ml / pcs
  stok       REAL         NOT NULL DEFAULT 0,
  stok_min   REAL         NOT NULL DEFAULT 0,
  harga      BIGINT       NOT NULL,                -- modal per satuan
  created_at BIGINT NOT NULL,
  updated_at BIGINT NOT NULL,
  is_deleted INTEGER NOT NULL DEFAULT 0,
  dirty      INTEGER NOT NULL DEFAULT 1,
  UNIQUE (kode),
  UNIQUE (nama)
);

-- ---------- 5. RESEP ----------
CREATE TABLE resep (
  id         CHAR(36) NOT NULL PRIMARY KEY,
  produk_id  CHAR(36) NOT NULL,
  bahan_id   CHAR(36) NOT NULL,
  jumlah     REAL     NOT NULL,                    -- 18, 150, 0.25 …
  created_at BIGINT NOT NULL,
  updated_at BIGINT NOT NULL,
  is_deleted INTEGER NOT NULL DEFAULT 0,
  dirty      INTEGER NOT NULL DEFAULT 1,
  UNIQUE (produk_id, bahan_id),
  FOREIGN KEY (produk_id) REFERENCES produk(id),
  FOREIGN KEY (bahan_id)  REFERENCES bahan(id)
);

-- ---------- 6. SUPPLIER ----------
CREATE TABLE supplier (
  id         CHAR(36)     NOT NULL PRIMARY KEY,
  kode       VARCHAR(10)  NOT NULL,
  nama       VARCHAR(100) NOT NULL,
  no_hp      VARCHAR(20)  NULL,
  alamat     VARCHAR(200) NULL,
  created_at BIGINT NOT NULL,
  updated_at BIGINT NOT NULL,
  is_deleted INTEGER NOT NULL DEFAULT 0,
  dirty      INTEGER NOT NULL DEFAULT 1,
  UNIQUE (kode)
);

-- ---------- 7. PEMBELIAN ----------
CREATE TABLE pembelian (
  id          CHAR(36)    NOT NULL PRIMARY KEY,
  kode        VARCHAR(12) NOT NULL,                -- PO-001
  tanggal     DATE        NOT NULL,
  supplier_id CHAR(36)    NOT NULL,
  user_id     CHAR(36)    NULL,                    -- admin pencatat
  total       BIGINT      NOT NULL,
  created_at  BIGINT NOT NULL,
  updated_at  BIGINT NOT NULL,
  is_deleted  INTEGER NOT NULL DEFAULT 0,
  dirty       INTEGER NOT NULL DEFAULT 1,
  UNIQUE (kode),
  FOREIGN KEY (supplier_id) REFERENCES supplier(id),
  FOREIGN KEY (user_id)     REFERENCES users(id)
);

CREATE TABLE pembelian_detail (
  id           CHAR(36) NOT NULL PRIMARY KEY,
  pembelian_id CHAR(36) NOT NULL,
  bahan_id     CHAR(36) NOT NULL,
  jumlah       REAL     NOT NULL,
  harga        BIGINT   NOT NULL,
  subtotal     BIGINT   NOT NULL,
  created_at   BIGINT NOT NULL,
  updated_at   BIGINT NOT NULL,
  is_deleted   INTEGER NOT NULL DEFAULT 0,
  dirty        INTEGER NOT NULL DEFAULT 1,
  FOREIGN KEY (pembelian_id) REFERENCES pembelian(id),
  FOREIGN KEY (bahan_id)     REFERENCES bahan(id)
);

-- ---------- 8. PENGELUARAN ----------
CREATE TABLE pengeluaran (
  id         CHAR(36)     NOT NULL PRIMARY KEY,
  kode       VARCHAR(12)  NOT NULL,                -- EXP-001
  tanggal    DATE         NOT NULL,
  keterangan VARCHAR(200) NOT NULL,
  kategori   VARCHAR(20)  NOT NULL,                -- Operasional/Peralatan/Perawatan/Transportasi/Lainnya
  jenis      VARCHAR(15)  NOT NULL CHECK (jenis IN ('rutin','tak_terduga')),
  jumlah     BIGINT       NOT NULL,
  bukti      MEDIUMBLOB   NULL,                    -- foto nota terkompresi (SQLite juga terima)
  created_at  BIGINT NOT NULL,
  updated_at  BIGINT NOT NULL,
  is_deleted  INTEGER NOT NULL DEFAULT 0,
  dirty      INTEGER NOT NULL DEFAULT 1,
  UNIQUE (kode)
);

-- ---------- 9. DISKON ----------
CREATE TABLE diskon (
  id          CHAR(36)    NOT NULL PRIMARY KEY,
  kode        VARCHAR(20) NOT NULL,                -- SENJA10
  tipe        VARCHAR(10) NOT NULL CHECK (tipe IN ('persen','nominal')),
  nilai       BIGINT      NOT NULL,                -- 10 (=10%) atau 5000 (=Rp)
  min_belanja BIGINT      NOT NULL DEFAULT 0,
  mulai       DATE        NOT NULL,
  berakhir    DATE        NOT NULL,
  status      VARCHAR(10) NOT NULL CHECK (status IN ('aktif','nonaktif')),
  created_at  BIGINT NOT NULL,
  updated_at  BIGINT NOT NULL,
  is_deleted  INTEGER NOT NULL DEFAULT 0,
  dirty      INTEGER NOT NULL DEFAULT 1,
  UNIQUE (kode)
);

-- ---------- 10. SHIFT ----------
-- P = Pagi 10:00–16:30, S = Sore 16:30–23:00
CREATE TABLE shift (
  id         CHAR(36)   NOT NULL PRIMARY KEY,
  user_id    CHAR(36)   NOT NULL,
  tanggal    DATE       NOT NULL,
  jenis      VARCHAR(1) NOT NULL CHECK (jenis IN ('P','S')),
  created_at BIGINT NOT NULL,
  updated_at BIGINT NOT NULL,
  is_deleted INTEGER NOT NULL DEFAULT 0,
  dirty      INTEGER NOT NULL DEFAULT 1,
  UNIQUE (user_id, tanggal),                       -- 1 pegawai 1 shift/hari
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ---------- 11. ABSENSI ----------
CREATE TABLE absensi (
  id          CHAR(36)    NOT NULL PRIMARY KEY,
  user_id     CHAR(36)    NOT NULL,
  tanggal     DATE        NOT NULL,
  shift_jenis VARCHAR(1)  NOT NULL,
  masuk       VARCHAR(5)  NULL,                    -- 'HH:MM', NULL = belum check-in
  pulang      VARCHAR(5)  NULL,
  status      VARCHAR(10) NOT NULL CHECK (status IN ('hadir','terlambat','alpa')),
  created_at  BIGINT NOT NULL,
  updated_at  BIGINT NOT NULL,
  is_deleted  INTEGER NOT NULL DEFAULT 0,
  dirty       INTEGER NOT NULL DEFAULT 1,
  UNIQUE (user_id, tanggal, shift_jenis),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ---------- 12. TRANSAKSI ----------
-- Insert-only → bebas konflik.
-- kode = TRX-YYYYMMDD-<kodeDevice>-NN (cth TRX-20250421-K1-003) agar unik antar device.
CREATE TABLE transaksi (
  id           CHAR(36)    NOT NULL PRIMARY KEY,
  kode         VARCHAR(30) NOT NULL,
  waktu        BIGINT      NOT NULL,               -- epoch-millis
  user_id      CHAR(36)    NOT NULL,               -- kasir
  subtotal     BIGINT      NOT NULL,
  diskon       BIGINT      NOT NULL DEFAULT 0,
  diskon_kode  VARCHAR(20) NULL,
  total        BIGINT      NOT NULL,
  metode       VARCHAR(5)  NOT NULL CHECK (metode IN ('cash','qris','debit')),
  dibayar      BIGINT      NOT NULL,
  kembalian    BIGINT      NOT NULL,
  ref          VARCHAR(50) NULL,                   -- no. kartu debit dsb
  status       VARCHAR(10) NOT NULL DEFAULT 'selesai',
  device_id    CHAR(36)    NOT NULL,
  created_at   BIGINT NOT NULL,
  updated_at   BIGINT NOT NULL,
  is_deleted   INTEGER NOT NULL DEFAULT 0,
  dirty        INTEGER NOT NULL DEFAULT 1,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE transaksi_detail (
  id           CHAR(36)     NOT NULL PRIMARY KEY,
  transaksi_id CHAR(36)     NOT NULL,
  produk_id    CHAR(36)     NULL,                  -- boleh NULL jika produk dihapus
  nama         VARCHAR(100) NOT NULL,              -- snapshot saat jual
  opsi         VARCHAR(100) NULL,                  -- snapshot: 'Cold · Large · Less Sugar'
  qty          INTEGER      NOT NULL,
  harga        BIGINT       NOT NULL,              -- termasuk +Large
  subtotal     BIGINT       NOT NULL,
  created_at   BIGINT NOT NULL,
  updated_at   BIGINT NOT NULL,
  is_deleted   INTEGER NOT NULL DEFAULT 0,
  dirty        INTEGER NOT NULL DEFAULT 1,
  FOREIGN KEY (transaksi_id) REFERENCES transaksi(id)
);

-- ---------- 13. MUTASI STOK (buku besar) ----------
-- KUNCI SYNC: stok tidak pernah dikirim sebagai angka mentah.
CREATE TABLE stok_mutasi (
  id         CHAR(36)     NOT NULL PRIMARY KEY,
  bahan_id   CHAR(36)     NOT NULL,
  arah        VARCHAR(6)   NOT NULL CHECK (arah IN ('masuk','keluar')),
  jumlah     REAL         NOT NULL,
  sumber      VARCHAR(12)  NOT NULL CHECK (sumber IN ('transaksi','pembelian','manual','awal')),
  ref_id      CHAR(36)     NULL,                   -- id transaksi / pembelian
  keterangan  VARCHAR(200) NULL,
  waktu       BIGINT       NOT NULL,
  device_id   CHAR(36)     NOT NULL,
  created_at  BIGINT NOT NULL,
  updated_at  BIGINT NOT NULL,
  is_deleted  INTEGER NOT NULL DEFAULT 0,
  dirty       INTEGER NOT NULL DEFAULT 1,
  FOREIGN KEY (bahan_id) REFERENCES bahan(id)
);

-- ---------- 14. LAPORAN BUG ----------
CREATE TABLE laporan_bug (
  id         CHAR(36)      NOT NULL PRIMARY KEY,
  app        VARCHAR(10)   NOT NULL CHECK (app IN ('admin','kasir')),
  user_id    CHAR(36)      NULL,
  judul      VARCHAR(150)  NOT NULL,
  deskripsi  VARCHAR(1000) NULL,
  waktu      BIGINT        NOT NULL,
  created_at BIGINT NOT NULL,
  updated_at BIGINT NOT NULL,
  is_deleted INTEGER NOT NULL DEFAULT 0,
  dirty      INTEGER NOT NULL DEFAULT 1
);

-- ---------- 15. PENGATURAN ----------
CREATE TABLE pengaturan (
  id         CHAR(36)     NOT NULL PRIMARY KEY,
  kunci      VARCHAR(50)  NOT NULL,                -- theme, lang, toko_nama, struk_footer …
  nilai      VARCHAR(300) NOT NULL,
  created_at BIGINT NOT NULL,
  updated_at BIGINT NOT NULL,
  is_deleted INTEGER NOT NULL DEFAULT 0,
  dirty      INTEGER NOT NULL DEFAULT 1,
  UNIQUE (kunci)
);

-- ---------- 16. SYNC META (khusus SQLite, TIDAK ikut sync) ----------
CREATE TABLE sync_meta (
  kunci VARCHAR(50) PRIMARY KEY,                   -- device_id, device_nama, last_push, last_pull
  nilai VARCHAR(100) NOT NULL
);

-- ---------- INDEX ----------
CREATE INDEX idx_users_d       ON users(dirty);
CREATE INDEX idx_kategori_d    ON kategori(dirty);
CREATE INDEX idx_produk_d      ON produk(dirty);
CREATE INDEX idx_bahan_d       ON bahan(dirty);
CREATE INDEX idx_resep_d       ON resep(dirty);
CREATE INDEX idx_supplier_d    ON supplier(dirty);
CREATE INDEX idx_pembelian_d   ON pembelian(dirty);
CREATE INDEX idx_pgd_d         ON pembelian_detail(dirty);
CREATE INDEX idx_pengeluaran_d ON pengeluaran(dirty);
CREATE INDEX idx_diskon_d      ON diskon(dirty);
CREATE INDEX idx_shift_d       ON shift(dirty);
CREATE INDEX idx_absensi_d     ON absensi(dirty);
CREATE INDEX idx_transaksi_d   ON transaksi(dirty);
CREATE INDEX idx_txd_d         ON transaksi_detail(dirty);
CREATE INDEX idx_mutasi_d      ON stok_mutasi(dirty);
CREATE INDEX idx_bug_d         ON laporan_bug(dirty);
CREATE INDEX idx_set_d         ON pengaturan(dirty);
CREATE INDEX idx_trx_waktu     ON transaksi(waktu);
CREATE INDEX idx_trx_user      ON transaksi(user_id);
CREATE INDEX idx_txd_trx       ON transaksi_detail(transaksi_id);
CREATE INDEX idx_mutasi_bahan  ON stok_mutasi(bahan_id);
CREATE INDEX idx_absensi_tgl   ON absensi(tanggal);
CREATE INDEX idx_shift_tgl     ON shift(tanggal);

-- ---------- KHUSUS SERVER (jalankan hanya di MySQL) ----------
-- CREATE TABLE devices (
--   id         CHAR(36)    NOT NULL PRIMARY KEY,
--   nama       VARCHAR(50) NOT NULL,
--   tipe       VARCHAR(10) NOT NULL,
--   last_seen  BIGINT NOT NULL,
--   created_at BIGINT NOT NULL
-- );