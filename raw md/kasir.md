☕ COFFEE SHOP POS / KASIR

├── 🏠 Home
│
│   ├── Produk Paling Laris — Semua Kategori
│   │   └── Menampilkan 5 produk dengan jumlah penjualan
│   │       terbanyak dari seluruh kategori
│   │
│   ├── Produk Paling Laris — Kategori Cemilan
│   │   └── Menampilkan 5 produk cemilan dengan jumlah
│   │       penjualan terbanyak
│   │
│   ├── Produk Paling Laris — Kategori Minuman
│   │   └── Menampilkan 7 produk minuman dengan jumlah
│   │       penjualan terbanyak
│   │
│   └── Produk Terjual Hari Ini
│       └── Menampilkan total jumlah produk yang terjual
│           pada hari berjalan
│   │
│   └── Informasi Kasir
│       ├── Nama Kasir
│       └── Shift Aktif
│
├── 🛒 Transaksi
│
│   ├── Daftar Kategori
│   │   ├── All Items
│   │   ├── Coffee
│   │   ├── Non-Coffee
│   │   └── Food
│   │
│   ├── Search Menu
│   │
│   ├── Daftar Menu
│   │   └── Menampilkan menu yang Aktif
│   │
│   └── Keranjang / Pesanan
│       ├── Nama Produk
│       ├── Opsi Produk
│       │   ├── Ukuran
│       │   │   ├── Regular
│       │   │   └── Large
│       │   ├── Ice
│       │   │   ├── Less Ice
│       │   │   └── Normal Ice
│       │   └── Sugar
│       │       ├── Less Sugar
│       │       └── Normal Sugar
│       │
│       ├── Jumlah
│       ├── Harga
│       ├── Subtotal
│       ├── Hapus Item
│       └── Ubah Item
│
├── 💳 Pembayaran
│
│   ├── Ringkasan Pesanan
│   │   ├── Subtotal
│   │   ├── Diskon
│   │       ├── Input Discount Code 
│   │       ├── Validasi Code ke Database 
│   │       └── Jika valid: 
│   │                └── Menampilkan nominal discount
│   │       └── Jika tidak valid: Discount = 0 "discount tidak tersedia/kadaluarsa"
│   │   └── Total
│   │
│   ├── Metode Pembayaran
│   │   ├── Cash
│   │   ├── QRIS
│   │   └── Debit
│   │   
│   │
│   ├── Jumlah Pembayaran
│   │
│   ├── Kembalian
│   │
│   └── Tombol
│       ├── Bayar > cetak struk
│       └── Batal
│
├── 🧾 Riwayat Transaksi
│
│   ├── Search
│   │   └── ID Transaksi
│   │
│   ├── Filter
│   │   ├── Hari Ini
│   │   └── Tanggal
│   │
│   └── Daftar Transaksi
│       ├── ID Transaksi
│       ├── Waktu
│       ├── Total
│       ├── Metode Pembayaran
│       └── Status
│
│       └── Detail Transaksi
│           ├── ID Transaksi
│           ├── Tanggal
│           ├── Jam
│           ├── Nama Kasir
│           ├── Daftar Produk
│           │   ├── Produk
│           │   ├── Opsi
│           │   ├── Jumlah
│           │   ├── Harga
│           │   └── Subtotal
│           ├── Subtotal
│           ├── Diskon
│           ├── Total
│           ├── Metode Pembayaran
│           ├── Dibayar
│           ├── Kembalian
│           │
│           └── Selesai Transaksi
│                    ↓
│              "Apakah pesanan sudah benar?"
│                    ↓
│                Transaksi Berhasil
│                    ↓
│                  Struk
│                    ↓
│                 Cetak Struk
│
│
├── 🕐 Shift
│   ├── Shift Saat Ini
│   │   ├── Nama Kasir
│   │   ├── User ID
│   │   ├── Jenis Shift
│   │   │   ├── P = pagi
│   │   │   └── S = Sore
│   │   ├── Tanggal Shift
│   │   ├── Jam Mulai
│   │   ├── Jam Selesai
│   │   └── Status Shift
│   │       ├── Belum Dimulai
│   │       ├── Berlangsung
│   │       └── Selesai
│   │
│   ├── Absensi Shift
│   │   ├── Daftar Pegawai dalam Shift
│   │   │   ├── User ID
│   │   │   ├── Nama Pegawai
│   │   │   ├── Jenis Shift
│   │   │   └── Status Absensi
│   │   │       ├── Belum Check-in
│   │   │       ├── Sudah Check-in
│   │   │       └── Sudah Check-out
│   │   │
│   │   ├── Check-in Bersama
│   │   │   ├── Pegawai memilih nama atau User ID miliknya
│   │   │   ├── Memasukkan User ID dan Password pribadi
│   │   │   ├── Sistem memverifikasi identitas
│   │   │   ├── Tombol [Absen Masuk]
│   │   │   └── Jam masuk tercatat otomatis
│   │   │
│   │   └── Check-out Bersama
│   │       ├── Pegawai memilih nama atau User ID miliknya
│   │       ├── Memasukkan User ID dan Password pribadi
│   │       ├── Sistem memverifikasi identitas
│   │       ├── Tombol [Absen Pulang]
│   │       └── Jam pulang tercatat otomatis
│   │
│   note : Ketentuan Absensi
│     Satu perangkat dapat digunakan bersama oleh anggota shift
│     Setiap pegawai wajib melakukan verifikasi dengan akun sendiri
│     Absen masuk hanya dapat dilakukan satu kali per shift
│     Setelah berhasil, tombol Absen Masuk berubah menjadi jam masuk
│     Klik berulang tidak mengubah jam masuk yang sudah tercatat
│     Absen pulang hanya dapat dilakukan setelah check-in
│     Tombol Absen Pulang aktif sesuai aturan waktu pulang shift
│     Setelah berhasil, tombol berubah menjadi jam pulang
│     Klik berulang tidak mengubah jam pulang yang sudah tercatat
│
└── ⚙️ Pengaturan
│ ├── 🎨 Theme 
│ ├── Light 
│ └── Dark 
│ ├── 🌐 Language 
│ └── Mengubah bahasa aplikasi 
│ ├── 🐞 Report 
│ └── Melaporkan bug/error aplikasi 
│ └── 👤 Account 
    ├── Nomor HP 
    ├── Password 
        │ ├── Password Lama 
        │ ├── Password Baru 
        │ └── Konfirmasi Password 
        └── [Simpan Perubahan]