Bro, setelah gw cek keseluruhan rancangan Senja POS dan Admin lu, kebutuhan API-nya ternyata cukup luas. Aplikasi lu bukan cuma aplikasi kasir, tetapi sistem operasional coffee shop yang mencakup transaksi, resep dan HPP, persediaan, shift, absensi, diskon, pengeluaran, laporan, dan sinkronisasi antarkomputer.

Saran utama gw: jangan membuat satu endpoint untuk setiap tombol. Buat API berdasarkan modul bisnis, dengan aturan yang konsisten agar aplikasi JavaFX yang sudah jadi bisa tetap dipakai.

Di bawah ini rancangan yang gw sarankan sebagai acuan pengembangan API Senja.

# 1. Daftar modul API yang harus tersedia

01 — Autentikasi, akun, dan hak akses

Wajib

* Login dan logout pengguna.

* Informasi akun yang sedang login.

* Ganti password dan nomor HP.

* Pengelolaan akun aktif/nonaktif.

* Hak akses Admin dan Kasir.

* Verifikasi password pribadi untuk absensi.

* Autentikasi perangkat untuk sinkronisasi.

* Pencatatan aktivitas sensitif.

02 — Menu dan transaksi POS

Wajib

* Daftar menu aktif dan kategori.

* Pencarian menu.

* Ukuran, suhu penyajian, opsi es dan gula.

* Keranjang di sisi aplikasi.

* Pembuatan transaksi dan detail item.

* Pembayaran tunai, QRIS, dan debit.

* Perhitungan subtotal, diskon, total, pembayaran, dan kembalian.

* Detail transaksi, riwayat, dan data struk.

* Pencegahan transaksi ganda.

03 — Diskon

* Membuat, mengubah, menonaktifkan, dan melihat kode diskon.

* Diskon nominal atau persentase.

* Batas maksimum potongan.

* Minimum pembelian.

* Tanggal mulai dan berakhir.

* Validasi kode pada saat pembayaran.

* Aturan penggunaan dan pencatatan diskon pada transaksi.

04 — Karyawan, shift, dan absensi

* CRUD karyawan.

* Membuat jadwal shift untuk beberapa pegawai.

* Mencegah jadwal pegawai yang bertabrakan.

* Melihat jadwal mingguan di aplikasi Kasir.

* Check-in dan check-out.

* Validasi waktu absensi di server.

* Koreksi absensi oleh pihak berwenang.

* Riwayat absensi tidak hilang saat jadwal diubah atau dihapus.

05 — Produk, resep, dan persediaan

* CRUD produk dan kategori.

* Gambar produk dan status aktif.

* Komposisi resep per ukuran atau varian bila dibutuhkan.

* Data bahan baku, satuan, stok minimum, dan harga modal.

* Supplier dan pembelian beserta detailnya.

* Mutasi stok masuk, keluar, dan koreksi.

* Perhitungan HPP.

* Peringatan stok menipis.

06 — Pengeluaran dan keuangan

* Mencatat pengeluaran rutin dan tak terduga.

* Kategori, nominal, tanggal, keterangan, dan bukti.

* Detail, perubahan, dan riwayat pengeluaran.

* Ringkasan pendapatan, HPP, laba kotor, pengeluaran, laba bersih, dan rugi.

* Definisi perhitungan laporan yang konsisten.

07 — Dashboard dan laporan

* Produk terlaris semua kategori, cemilan, dan minuman.

* Jumlah produk terjual hari ini.

* Laporan penjualan, pembelian stok, absensi, dan keuangan.

* Filter tanggal, pencarian, pagination.

* Ekspor laporan sesuai filter.

* Data ringkasan yang konsisten dengan transaksi sumber.

08 — Sinkronisasi dan status perangkat

* Registrasi dan autentikasi perangkat.

* Push dan pull perubahan.

* Sinkronisasi bertahap.

* Penanganan konflik dan penghapusan data.

* Retry ketika jaringan kembali.

* Pencegahan perubahan terkirim dua kali.

* Status sinkronisasi dan waktu terakhir berhasil.

# 2. Rancangan endpoint API yang dibutuhkan

Berikut endpoint yang sebaiknya menjadi target. Ini rancangan yang perlu dicocokkan dengan endpoint yang sudah ada, bukan klaim bahwa semuanya belum tersedia di `senja-api`.

Format umum: `GET` untuk membaca, `POST` untuk membuat atau menjalankan aksi, `PUT` untuk memperbarui, dan `DELETE` untuk menghapus bila penghapusan memang diizinkan.

## A. Login dan akun

| Method | Endpoint                 | Fungsi                   |
| ------ | ------------------------ | ------------------------ |
| POST   | `/api/auth/login`        | Login                    |
| POST   | `/api/auth/logout`       | Logout                   |
| GET    | `/api/auth/me`           | Informasi akun aktif     |
| PUT    | `/api/auth/password`     | Ganti password           |
| PUT    | `/api/auth/profile`      | Ubah profil dan nomor HP |
| GET    | `/api/users`             | Daftar karyawan          |
| POST   | `/api/users`             | Tambah karyawan          |
| GET    | `/api/users/{id}`        | Detail karyawan          |
| PUT    | `/api/users/{id}`        | Ubah data karyawan       |
| PATCH  | `/api/users/{id}/status` | Aktif/nonaktifkan akun   |

Password harus di-hash. Jangan mengirim password kembali ke aplikasi dalam respons API. Hak akses perlu diperiksa di server pada setiap endpoint yang dilindungi.

## B. Menu, kategori, dan resep

| Method | Endpoint                    | Fungsi                                  |
| ------ | --------------------------- | --------------------------------------- |
| GET    | `/api/categories`           | Daftar kategori                         |
| POST   | `/api/categories`           | Tambah kategori                         |
| PUT    | `/api/categories/{id}`      | Ubah kategori                           |
| GET    | `/api/products`             | Daftar menu dengan pencarian dan filter |
| GET    | `/api/products/{id}`        | Detail menu                             |
| POST   | `/api/products`             | Tambah menu                             |
| PUT    | `/api/products/{id}`        | Ubah menu                               |
| PATCH  | `/api/products/{id}/status` | Aktif/nonaktifkan menu                  |
| GET    | `/api/products/{id}/recipe` | Ambil komposisi resep                   |
| PUT    | `/api/products/{id}/recipe` | Simpan atau ubah resep                  |
| GET    | `/api/ingredients`          | Daftar bahan baku                       |

Catatan penting: opsi Regular/Large, Hot/Cold, Less Ice, Normal Ice, Less Sugar, dan Normal Sugar harus mempunyai aturan yang jelas. Resep dan harga bisa berbeda per ukuran jika bisnis memang menerapkannya. Opsi es dan gula umumnya merupakan instruksi pembuatan, bukan produk stok terpisah.

## C. Transaksi dan pembayaran

| Method | Endpoint                          | Fungsi                         |
| ------ | --------------------------------- | ------------------------------ |
| POST   | `/api/transactions`               | Membuat transaksi              |
| GET    | `/api/transactions`               | Daftar transaksi dengan filter |
| GET    | `/api/transactions/{id}`          | Detail transaksi               |
| POST   | `/api/transactions/{id}/payments` | Catat pembayaran               |
| POST   | `/api/transactions/{id}/cancel`   | Batalkan transaksi sesuai izin |
| GET    | `/api/transactions/{id}/receipt`  | Ambil data struk               |

Alur yang disarankan:

1. Kasir memilih menu dan opsi produk.

2. Aplikasi menghitung perkiraan subtotal untuk ditampilkan.

3. API memvalidasi menu, harga, diskon, dan total final.

4. Pembayaran diproses atau dicatat sesuai metode pembayaran.

5. API menyimpan transaksi, detail item, dan dampak stok secara konsisten.

6. Aplikasi menerima nomor transaksi dan data struk.

7. Kasir mencetak struk atau mencetaknya kembali dari riwayat.

Untuk QRIS dan debit, API perlu membedakan pembayaran yang sekadar dicatat manual dengan pembayaran yang benar-benar dikonfirmasi oleh penyedia pembayaran. Jangan menandai pembayaran elektronik sukses hanya karena kasir menekan tombol Bayar.

Untuk tombol “Selesai Transaksi” dan konfirmasi pesanan: gunakan status transaksi yang jelas, misalnya `PENDING`, `PAID`, atau `CANCELLED`, sesuai alur yang dipilih. Jangan menyimpan satu transaksi sebagai berhasil dua kali jika tombol ditekan berulang.

## D. Diskon

| Method | Endpoint                     | Fungsi               |
| ------ | ---------------------------- | -------------------- |
| GET    | `/api/discounts`             | Daftar diskon        |
| POST   | `/api/discounts`             | Tambah kode diskon   |
| GET    | `/api/discounts/{id}`        | Detail diskon        |
| PUT    | `/api/discounts/{id}`        | Ubah aturan diskon   |
| PATCH  | `/api/discounts/{id}/status` | Aktif/nonaktifkan    |
| POST   | `/api/discounts/validate`    | Validasi kode diskon |

Aturan perhitungan perlu disepakati, contohnya:

* Diskon nominal: potongan tetap.

* Diskon persentase: persentase dari dasar perhitungan yang ditentukan.

* Maksimal diskon: batas tertinggi untuk potongan persentase.

* Minimum pembelian: nilai minimum yang wajib dipenuhi.

* Tanggal berlaku: kode hanya bisa digunakan pada periode yang ditetapkan.

* Kode tidak valid atau kedaluwarsa: tidak diterapkan, dan aplikasi menampilkan alasan yang sesuai.

API harus menghitung ulang diskon ketika transaksi disimpan. Hasil validasi dari langkah sebelumnya tidak boleh dianggap berlaku selamanya, karena status dan periode diskon bisa berubah.

## E. Shift dan absensi

| Method | Endpoint                          | Fungsi                                |
| ------ | --------------------------------- | ------------------------------------- |
| GET    | `/api/shifts`                     | Daftar jadwal shift                   |
| POST   | `/api/shifts`                     | Membuat shift beserta penugasan       |
| GET    | `/api/shifts/{id}`                | Detail shift                          |
| PUT    | `/api/shifts/{id}`                | Mengubah jadwal                       |
| DELETE | `/api/shifts/{id}`                | Membatalkan jadwal jika diperbolehkan |
| GET    | `/api/shifts/current`             | Shift aktif pengguna                  |
| GET    | `/api/attendance`                 | Daftar absensi dengan filter          |
| POST   | `/api/attendance/check-in`        | Absen masuk                           |
| POST   | `/api/attendance/check-out`       | Absen pulang                          |
| POST   | `/api/attendance/{id}/correction` | Koreksi absensi dengan otorisasi      |

Untuk absensi bersama, aplikasi boleh menyediakan pilihan nama atau User ID. Namun, API harus memverifikasi identitas pegawai menggunakan kredensialnya sendiri, lalu menentukan jadwal yang berlaku.

Validasi server harus memastikan:

* Tidak ada check-in kedua untuk jadwal yang sama.

* Check-out hanya dilakukan setelah check-in.

* Pengulangan request tidak mengubah jam yang sudah tercatat.

* Waktu absensi berasal dari waktu server yang tepercaya.

* Aturan keterlambatan dan jam pulang mengikuti jadwal yang berlaku.

* Perubahan atau penghapusan jadwal tidak menghilangkan riwayat absensi.

Jangan menyimpan password pribadi pegawai di tabel absensi. Verifikasi melalui mekanisme autentikasi yang aman.

## F. Persediaan, pembelian, dan pengeluaran

| Method | Endpoint                 | Fungsi                         |
| ------ | ------------------------ | ------------------------------ |
| POST   | `/api/ingredients`       | Tambah bahan baku              |
| PUT    | `/api/ingredients/{id}`  | Ubah data bahan                |
| GET    | `/api/stock/movements`   | Riwayat mutasi stok            |
| POST   | `/api/stock/adjustments` | Koreksi stok dengan alasan     |
| GET    | `/api/suppliers`         | Daftar supplier                |
| POST   | `/api/suppliers`         | Tambah supplier                |
| PUT    | `/api/suppliers/{id}`    | Ubah supplier                  |
| POST   | `/api/purchases`         | Catat pembelian beserta detail |
| GET    | `/api/purchases`         | Riwayat pembelian              |
| GET    | `/api/purchases/{id}`    | Detail pembelian               |
| GET    | `/api/expenses`          | Daftar pengeluaran             |
| POST   | `/api/expenses`          | Tambah pengeluaran             |
| PUT    | `/api/expenses/{id}`     | Ubah pengeluaran               |
| GET    | `/api/expenses/{id}`     | Detail pengeluaran             |

Ada dua hal yang perlu diperjelas di desain lu:

1. Laporan Stok yang lu maksud adalah laporan pembelian stok. Sebaiknya sediakan laporan pembelian terpisah dari laporan posisi stok saat ini dan laporan mutasi stok.

2. HPP harus memiliki definisi perhitungan. Untuk tahap awal, HPP bahan dapat dihitung dari resep dan biaya bahan. Jika ingin laporan laba lebih akurat, tentukan juga metode penilaian persediaan dan bagaimana perubahan harga bahan memengaruhi HPP historis transaksi.

## G. Dashboard dan laporan

| Method | Endpoint                      | Fungsi                                   |
| ------ | ----------------------------- | ---------------------------------------- |
| GET    | `/api/dashboard/summary`      | Ringkasan dashboard                      |
| GET    | `/api/dashboard/top-products` | Produk terlaris                          |
| GET    | `/api/dashboard/sales-today`  | Jumlah produk terjual hari ini           |
| GET    | `/api/reports/sales`          | Laporan penjualan                        |
| GET    | `/api/reports/purchases`      | Laporan pembelian bahan baku             |
| GET    | `/api/reports/stock`          | Posisi dan kondisi stok                  |
| GET    | `/api/reports/attendance`     | Laporan absensi                          |
| GET    | `/api/reports/finance`        | Laporan keuangan                         |
| GET    | `/api/reports/finance/detail` | Rincian pendapatan, HPP, dan pengeluaran |

Untuk laporan keuangan, definisi perhitungannya perlu konsisten. Secara umum:

* Laba kotor = pendapatan penjualan bersih − HPP produk terjual.

* Laba bersih operasional = laba kotor − pengeluaran operasional yang relevan.

* Rugi ditampilkan jika hasil perhitungan bersih negatif.

Pastikan perlakuan diskon, pengembalian dana, dan transaksi yang dibatalkan konsisten dalam laporan. Ekspor Excel bisa dilakukan di JavaFX dari data API yang sudah difilter, atau di server jika dibutuhkan; keduanya tidak harus dibuat sekaligus.

# 3. Endpoint umum yang sering terlupakan

Selain endpoint bisnis di atas, siapkan juga:

| Kebutuhan               | Rancangan                        |
| ----------------------- | -------------------------------- |
| Kesehatan server        | `GET /api/health`                |
| Registrasi perangkat    | `POST /api/devices/register`     |
| Daftar perangkat        | `GET /api/devices`               |
| Menonaktifkan perangkat | `PATCH /api/devices/{id}/status` |
| Sinkronisasi            | `POST /api/sync`                 |
| Status sinkronisasi     | `GET /api/sync/status`           |
| Pengaturan aplikasi     | `GET /api/settings`              |
| Melaporkan bug          | `POST /api/bug-reports`          |
| Melihat laporan bug     | `GET /api/bug-reports`           |
| Detail laporan bug      | `GET /api/bug-reports/{id}`      |

Endpoint bug report sebaiknya menerima versi aplikasi, jenis perangkat, waktu kejadian, pesan error yang sudah disaring, dan keterangan pengguna. Jangan mengirim password, API key, atau data sensitif lain ke laporan bug.

Untuk theme Light/Dark dan bahasa, preferensi yang hanya memengaruhi tampilan dapat disimpan secara lokal. Gunakan API pengaturan hanya untuk preferensi yang memang ingin disinkronkan antarkomputer atau dikelola terpusat.

# 4. Aturan database yang harus disepakati sebelum API dikembangkan

Ini bagian krusial karena semua modul saling terhubung.

Identitas data konsisten

Gunakan ID stabil, idealnya UUID jika itu sudah menjadi standar `senja-core`, agar data SQLite dan MySQL dapat dikenali sebagai entitas yang sama.

Waktu dan zona waktu konsisten

Sepakati format `created_at`, `updated_at`, dan waktu transaksi. Simpan waktu dengan konvensi yang jelas dan tampilkan dalam zona waktu operasional coffee shop.

Data historis tidak berubah sembarangan

Transaksi yang sudah dibayar, riwayat absensi, dan mutasi stok harus tetap dapat diaudit. Hindari hard delete terhadap data yang masih dibutuhkan dalam laporan.

Perubahan terkait diproses secara atomik

Misalnya, penyimpanan transaksi, detail item, pembayaran, dan mutasi stok yang terkait harus dikelola dengan transaksi database agar tidak tersimpan sebagian.

Offline dan online punya aturan yang jelas

Tentukan modul yang boleh berjalan offline, bagaimana transaksi lokal disimpan, bagaimana perubahan dikirim ulang, serta bagaimana konflik diselesaikan. Jangan mengandalkan angka stok lokal sebagai jaminan stok server selalu tersedia.

# 5. Urutan implementasi paling realistis

Gw menyarankan urutan berikut supaya pengerjaan API tidak berputar-putar.

1. Audit kode API yang ada. Periksa controller, service, repository, entity, konfigurasi keamanan, dan skema MySQL.

2. Tetapkan kontrak data. Cocokkan nama kolom dan ID antara `senja-core`/SQLite dengan entity serta tabel MySQL.

3. Selesaikan autentikasi dan hak akses. Uji login, role, dan registrasi perangkat sebelum membuka seluruh operasi bisnis.

4. Selesaikan transaksi POS, pembayaran, diskon, dan stok. Ini inti operasional dan paling berdampak bila datanya salah.

5. Selesaikan shift, absensi, pembelian, resep, dan pengeluaran. Pastikan setiap modul terhubung dengan riwayat dan validasi yang tepat.

6. Selesaikan dashboard dan laporan. Bangun setelah data sumber dan definisi perhitungannya konsisten.

7. Uji sinkronisasi serta pemulihan. Uji dua atau lebih perangkat, koneksi putus, request berulang, konflik, dan server yang restart.

8. Siapkan deployment. Backup, konfigurasi produksi, HTTPS, logging, migrasi database, dan dokumentasi API.

## 6. Yang perlu gw lihat untuk melengkapi API lu secara akurat

Daftar di atas adalah target fitur. Supaya kita tidak membuat endpoint duplikat atau merusak aplikasi yang sudah jadi, langkah berikutnya adalah memeriksa kode nyata di `senja-api` dan `senja-core`.

Kirim ZIP folder `senja-api` dan `senja-core` tanpa folder `target`, file rahasia, password, atau API key aktif. Kalau belum bisa mengirim ZIP, mulai dari struktur folder dan isi controller API yang sekarang ada.

Dari kode tersebut, kita bisa menyusun matriks final setiap endpoint: sudah tersedia, perlu diperbaiki, belum tersedia, tabel yang dipakai, hak akses, dan halaman JavaFX yang memakainya. Itu yang akan menjadi daftar kerja implementasi API Senja, bukan sekadar daftar fitur umum.
