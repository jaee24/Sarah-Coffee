│
├── 🏠 Home
│
│   ├── Produk Terlaris Semua Kategori
│   │   └── Menampilkan 5 produk terlaris dari seluruh kategori
│   │
│   ├── Produk Terlaris Kategori Cemilan
│   │   └── Menampilkan 5 produk cemilan terlaris
│   │
│   ├── Produk Terlaris Kategori Minuman
│   │   └── Menampilkan 5 produk minuman terlaris
│   │
│   └── Produk Terjual Hari Ini
│       └── Menampilkan jumlah produk yang terjual pada hari ini
│
├── 👥 Karyawan
│
├── Data Karyawan
│ ├── Search Bar
│ │ └── Pencarian berdasarkan User ID, Nama, atau Posisi
│ ├── [+ User]
│ │ └── Menambahkan user/karyawan baru
│ └── Tabel Data Karyawan
│ ├── User ID
│ ├── Nama User
│ ├── Level Akses
│ │ ├── Kasir
│ │ └── Admin
│ ├── Nomor HP
│ ├── Alamat
│ └── Aksi
│ ├── Edit User
│ └── Hapus User
│
├── Absensi
│ ├── Search Bar
│ │ └── Mencari berdasarkan ID atau Nama Pegawai
│ ├── Filter by Date
│ │ ├── Tanggal Mulai
│ │ └── Tanggal Akhir
│ └── Data Absensi
│ ├── ID Pegawai
│ ├── Nama Pegawai
│ ├── Posisi
│ ├── Jenis Shift
│ ├── Tanggal
│ ├── Jadwal Jam Masuk
│ ├── Jam Masuk Aktual
│ ├── Jadwal Jam Pulang
│ ├── Jam Pulang Aktual
│ └── Status
│ ├── Hadir
│ ├── Terlambat
│ ├── Izin
│ ├── Sakit
│ └── Tidak Hadir
│
└── Shift
│
├── Search Bar
│ └── Mencari berdasarkan User ID atau Nama Pegawai
│
├── Filter Shift
│ ├── Tanggal Mulai
│ ├── Tanggal Akhir
│ ├── Jenis Shift
│ └── Posisi Pegawai
│
├── [+ Shift]
│ │
│ ├── Data Shift
│ │ ├── Nama Shift
│ │ ├── Jenis Shift
│ │ │ ├── P = pagi
│ │ │ └── S = Sore
│ │ ├── Jam Mulai
│ │ └── Jam Selesai
│ │
│ ├── Pilih Pegawai
│ │ ├── Search Pegawai
│ │ │ └── Berdasarkan User ID atau Nama
│ │ ├── Daftar Pegawai
│ │ └── Pegawai Terpilih
│ │ ├── User ID
│ │ ├── Nama Pegawai
│ │ ├── Posisi
│ │ └── Dapat memilih lebih dari 1 pegawai
│ │
│ ├── Tanggal Shift
│ │ ├── Pilih satu tanggal
│ │ └── Dapat menambahkan beberapa tanggal
│ │
│ └── Tombol
│ ├── Simpan Shift
│ └── Cancel
│
├── List Shift
│ ├── Ditampilkan per 7 hari
│ ├── Navigasi Minggu Sebelumnya / Berikutnya
│ ├── Nama Pegawai
│ ├── User ID
│ ├── Posisi
│ ├── Tanggal
│ ├── Jenis Shift
│ ├── Jam Mulai
│ ├── Jam Selesai
│ ├── Status Jadwal
│ │ ├── Terjadwal
│ │ ├── Berlangsung
│ │ └── Selesai
│ └── Aksi
│ ├── Edit Shift
│ └── Hapus Shift
│
note : Ketentuan Shift
Satu shift dapat memiliki beberapa pegawai
Setiap pegawai memiliki jadwal shift masing-masing
Pegawai tidak boleh memiliki jadwal yang bertabrakan
Jam mulai dan selesai ditentukan oleh Admin
Jadwal yang disimpan muncul di aplikasi Kasir
Perubahan jadwal tidak mengubah riwayat absensi
Penghapusan jadwal tidak menghapus riwayat absensi
│
├── ☕ Produk
│
│   ├── Menu
│   │
│   │   ├── Search Bar
│   │   │
│   │   ├── Filter Kategori
│   │   │   ├── All Items
│   │   │   ├── Coffee
│   │   │   ├── Non-Coffee
│   │   │   └── Food
│   │   │
│   │   ├── [+ Items]
│   │   │
│   │   ├── List Menu
│   │   │   └── Setiap menu dapat diklik untuk melihat detail
│   │   │
│   │   └── Detail Menu
│   │       ├── Gambar Menu
│   │       ├── Nama Menu
│   │       ├── Kategori
│   │       │   └── Dropdown kategori yang tersedia
│   │       │
│   │       ├── Kategori Lain
│   │       │   └── Jika dipilih:
│   │       │       └── Input untuk menambahkan kategori baru
│   │       │
│   │       ├── Jenis Produk
│   │       │   ├── Minuman
│   │       │   │   └── Penyajian:
│   │       │   │       ├── Hot
│   │       │   │       └── Cold
│   │       │   │
│   │       │   └── Makanan
│   │       │       └── Tidak memiliki pilihan Hot/Cold
│   │       │
│   │       ├── Opsi Minuman
│   │       │   ├── ☐ Less Ice >if cold
│   │       │   ├── ☐ Normal Ice >if cold
│   │       │   ├── ☐ Less Sugar
│   │       │   ├── ☐ Normal Sugar
│   │       │   ├── ☐ Large
│   │       │   └── ☐ Regular
│   │       │
│   │       ├── 💡 Komposisi Bahan Baku / Resep
│   │       │
│   │       │   ├── [+ Bahan Baku]
│   │       │   │
│   │       │   └── Daftar Bahan yang Digunakan
│   │       │       ├── Bahan Baku
│   │       │       ├── Jumlah
│   │       │       ├── Satuan
│   │       │       └── Aksi
│   │       │           └── Hapus
│   │       │
│   │       │   Contoh:
│   │       │
│   │       │   ┌─────────────────────────────────────┐
│   │       │   │ Kopi Arabica     18    gram   [X] │
│   │       │   │ Susu             150   ml     [X] │
│   │       │   │ Gula              10   gram   [X] │
│   │       │   │ Caramel           20   ml     [X] │
│   │       │   └─────────────────────────────────────┘
│   │       │
│   │       │   [+ Bahan Baku]
│   │       │
│   │       ├── Harga
│   │       │  ├── Harga Jual
│   │       │  ├── HPP
│   │       │  └── Margin
│   │       │
│   │       └── Tombol
│   │           ├── Simpan / Edit Items
│   │           ├── Hapus Items
│   │           └── Cancel Edit
│   │
│   │   └── Kategori
│   │
│   │       ├── Daftar Kategori
│   │       ├── [+ Kategori]
│   │       ├── Nama Kategori
│   │       ├── Jenis
│   │       │   ├── Minuman
│   │       │   └── Makanan
│   │       │
│   │       └── Penyajian
│   │           └── Hanya tersedia untuk kategori Minuman
│   │               ├── Hot
│   │               └── Cold
│
├── 📦 Persediaan
│
│   ├── Stok
│   │
│   │   └── Bahan Baku
│   │       ├── Search Bar
│   │       ├── [+ Bahan Baku]
│   │       │
│   │       └── Tabel Bahan Baku
│   │           ├── ID Bahan Baku
│   │           ├── Nama Bahan Baku
│   │           ├── Satuan
│   │           ├── Stok
│   │           ├── Minimum Stok
│   │           ├── Harga Modal
│   │           └── Aksi
│   │               └── Edit
│   │
│   │           Saat Edit:
│   │           ├── Edit Data
│   │           ├── Simpan
│   │           ├── Hapus
│   │           └── Cancel
│   │
│   │           Ketentuan:
│   │           ├── Setiap bahan memiliki ID Bahan Baku
│   │           └── Nama bahan baku tidak boleh sama
│   │
│   ├── Supplier
│   │
│   │   ├── Search Bar
│   │   ├── [+ Supplier]
│   │   └── Data Supplier
│   │
│   └── Pembelian
│       ├── Data Pengadaan Bahan Baku
│       ├── Supplier
│       ├── Tanggal Pembelian
│       ├── Bahan Baku
│       ├── Jumlah
│       ├── Harga
│       └── Total Pembelian
│
├── 🛒 Penjualan
│
│   └── Transaksi
│
│       ├── Search Bar
│       ├── Filter Transaksi
│       └── List Transaksi
│           ├── ID Transaksi
│           ├── Tanggal
│           ├── Kasir
│           └── Total Transaksi
│   Note: gw mau ketika transaksi di klik dia menampilkan detail transaksi, dan ada button cetak struk
│   └── Discount 
│        ├── Search Bar 
│        ├── [+ Discount] 
│        │ 
│        ├── Daftar Discount 
│        │   ├── Kode Discount 
│        │   ├── Jenis Discount 
│        │   │   ├── Nominal 
│        │   │   └── Persentase 
│        │   ├── Nilai Discount 
│        │   ├── Maksimal Discount 
│        │   ├── Minimal Pembelian 
│        │   ├── Tanggal Mulai 
│        │   ├── Tanggal Berakhir 
│        │   ├── Status 
│        │   └── Aksi 
│        │       ├── Edit 
│        │       └── Hapus 
│        │          
│        └── Tambah Discount 
│            ├── Kode Discount 
│            ├── Jenis Discount
│            │   ├── Nominal 
│            │   │   └── Nominal Discount 
│            │   │   
│            │   └── Persentase 
│            │       ├── Persentase Discount 
│            │       └── Maksimal Discount 
│            │       
│            ├── Minimal Pembelian 
│            ├── Tanggal Mulai 
│            ├── Tanggal Berakhir 
│            ├── Status 
│            ├── [Simpan] 
│            └── [Cancel]
│           
│
├── 💸 Pengeluaran
│
│   ├── Ringkasan Pengeluaran
│   │   ├── Total Pengeluaran Hari Ini
│   │   ├── Pengeluaran Rutin
│   │   └── Pengeluaran Tak Terduga
│   │
│   ├── Search / Filter
│   │   ├── Tanggal Mulai
│   │   ├── Tanggal Akhir
│   │   ├── Kategori Pengeluaran
│   │   └── Jenis Pengeluaran
│   │       ├── Rutin
│   │       └── Tak Terduga
│   │
│   ├── [+ Pengeluaran]
│   │   ├── Tanggal
│   │   ├── Keterangan
│   │   ├── Kategori
│   │   │   ├── Operasional
│   │   │   ├── Peralatan
│   │   │   ├── Perawatan
│   │   │   ├── Transportasi
│   │   │   └── Lainnya
│   │   ├── Jenis Pengeluaran
│   │   │   ├── Rutin
│   │   │   └── Tak Terduga
│   │   ├── Nominal
│   │   ├── Bukti Transaksi (Opsional)
│   │   ├── [Simpan]
│   │   └── [Cancel]
│   │
│   └── Data Pengeluaran
│       ├── ID Pengeluaran
│       ├── Tanggal
│       ├── Keterangan
│       ├── Kategori
│       ├── Jenis Pengeluaran
│       ├── Nominal
│       └── Aksi
│           ├── Detail
│           └── Edit
│
├── 📊 Laporan
│
│   ├── Penjualan
│   │
│   │   ├── Filter by Date
│   │   ├── Data Penjualan
│   │   └── Convert / Cetak Excel
│   │       └── Berdasarkan data yang sudah difilter
│   │
│   ├── Stok
│   │
│   │   ├── Search / Filter
│   │   ├── Data Stok
│   │   └── Convert / Cetak Excel
│   │   note: ini adalah laporan pembelian stock
│   │
│   ├── Absensi
│   │
│   │   ├── Search Bar
│   │   │   └── Mencari ID / Nama Pegawai
│   │   │
│   │   ├── Filter by Date
│   │   │   ├── Tanggal Mulai
│   │   │   └── Tanggal Akhir
│   │   │
│   │   └── Cetak Absensi ke Excel
│   │       └── Berdasarkan data yang sudah difilter
│   │
│   └── Keuangan
│
│       ├── Search / Filter by Date
│       │   ├── Tanggal Mulai
│       │   └── Tanggal Akhir
│       │
│       ├── Ringkasan Keuangan
│       │   ├── Total Pendapatan
│       │   ├── HPP Penjualan
│       │   ├── Laba Kotor
│       │   ├── Total Pengeluaran
│       │   ├── Laba Bersih
│       │   └── Rugi
│       │
│       ├── Detail Keuangan
│       │   ├── Pendapatan Penjualan
│       │   ├── HPP Produk Terjual
│       │   └── Pengeluaran
│       │
│       └── Convert / Cetak Excel
│           └── Berdasarkan data yang sudah difilter
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