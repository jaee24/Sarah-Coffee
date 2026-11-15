package com.senja.api.sync;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class SyncTables {

    /** Definisi satu tabel sync: nama, kolom (urutan penting), kolom BLOB (Base64 via JSON). */
    public record T(String name, List<String> cols, List<String> blobs) {
        public T(String name, List<String> cols) { this(name, cols, List.of()); }
    }

    /** Urutan = urutan aman FK (induk sebelum anak). WAJIB identik dengan Tables.ORDER di senja-core. */
    public static final List<T> ORDER = List.of(
        new T("users", List.of("id","kode","nama","level","password_hash","no_hp","alamat","status","created_at","updated_at","is_deleted")),
        new T("kategori", List.of("id","nama","jenis","penyajian","created_at","updated_at","is_deleted")),
        new T("bahan", List.of("id","kode","nama","satuan","stok","stok_min","harga","created_at","updated_at","is_deleted")),
        new T("produk", List.of("id","kode","nama","kategori_id","penyajian","opsi","harga","status","gambar","created_at","updated_at","is_deleted")),
        new T("resep", List.of("id","produk_id","bahan_id","jumlah","created_at","updated_at","is_deleted")),
        new T("supplier", List.of("id","kode","nama","no_hp","alamat","created_at","updated_at","is_deleted")),
        new T("pembelian", List.of("id","kode","tanggal","supplier_id","user_id","total","created_at","updated_at","is_deleted")),
        new T("pembelian_detail", List.of("id","pembelian_id","bahan_id","jumlah","harga","subtotal","created_at","updated_at","is_deleted")),
        new T("pengeluaran", List.of("id","kode","tanggal","keterangan","kategori","jenis","jumlah","bukti","created_at","updated_at","is_deleted"), List.of("bukti")),
        new T("diskon", List.of("id","kode","tipe","nilai","maks_potongan","min_belanja","mulai","berakhir","status","created_at","updated_at","is_deleted")),
        new T("shift", List.of("id","user_id","tanggal","jenis","created_at","updated_at","is_deleted")),
        new T("absensi", List.of("id","user_id","tanggal","shift_jenis","masuk","pulang","status","created_at","updated_at","is_deleted")),
        new T("transaksi", List.of("id","kode","waktu","user_id","subtotal","diskon","diskon_kode","total","metode","dibayar","kembalian","ref","status","device_id","created_at","updated_at","is_deleted")),
        new T("transaksi_detail", List.of("id","transaksi_id","produk_id","nama","opsi","qty","harga","subtotal","hpp","created_at","updated_at","is_deleted")),
        new T("stok_mutasi", List.of("id","bahan_id","arah","jumlah","sumber","ref_id","keterangan","waktu","device_id","created_at","updated_at","is_deleted")),
        new T("laporan_bug", List.of("id","app","user_id","judul","deskripsi","waktu","created_at","updated_at","is_deleted")),
        new T("pengaturan", List.of("id","kunci","nilai","created_at","updated_at","is_deleted"))
    );

    private SyncTables() {}

    public static T byName(String n) {
        return ORDER.stream().filter(t -> t.name().equals(n)).findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tabel tidak dikenal: " + n));
    }
}