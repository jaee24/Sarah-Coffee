package com.senja.pos;

import com.senja.core.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Checkout transaksi:
 *  - ONLINE : POST /api/transactions (idempoten by UUID) → respons di-upsert lokal dirty=0
 *  - OFFLINE: tulis lokal dirty=1 (PosIds) → sync yang mengirim
 *  - AMBIGU : timeout → GET /{id} : ada=adopt, tidak=buat lokal
 */
public final class PosService {
    private PosService() {}

    /** Satu baris keranjang. */
    public record Item(String pid,String nama,List<String> opsi,int qty,long harga){}

    public record Disc(boolean valid,long potongan,String alasan,String kode){}

    /* ---------- hitung diskon (online: server; offline: lokal) ---------- */
    public static Disc cekDiskon(String kode,long subtotal){
        if(kode==null||kode.isBlank()) return new Disc(false,0,"Kode kosong",null);
        try{
            if(ApiClient.hasToken()){
                Map<String,Object> r=ApiClient.post("/api/discounts/validate",
                        Map.of("kode",kode,"subtotal",subtotal));
                boolean ok=Boolean.TRUE.equals(r.get("valid"));
                return new Disc(ok,Ui.lng(r.get("potongan")),
                        Ui.str(r.get("alasan")),ok?Ui.str(r.get("kode")).toUpperCase():null);
            }
        }catch(ApiClient.ApiException e){
            if(e.status!=0) return new Disc(false,0,e.getMessage(),null); // 400 dst: tampilkan alasan
            // status 0 = offline → lanjut cek lokal
        }
        return diskonLokal(kode,subtotal);
    }

    private static Disc diskonLokal(String kode,long subtotal){
        var rows=Db.query("SELECT * FROM diskon WHERE is_deleted=0 AND upper(kode)=?",kode.toUpperCase());
        if(rows.isEmpty()) return new Disc(false,0,"Kode tidak ditemukan",null);
        var d=rows.get(0);
        if(!"aktif".equals(Ui.str(d.get("status")))) return new Disc(false,0,"Kode tidak aktif",null);
        String today=LocalDate.now().toString();
        if(today.compareTo(Ui.str(d.get("mulai")))<0) return new Disc(false,0,"Belum berlaku",null);
        if(today.compareTo(Ui.str(d.get("berakhir")))>0) return new Disc(false,0,"Kedaluwarsa",null);
        long min=Ui.lng(d.get("min_belanja"));
        if(subtotal<min) return new Disc(false,0,"Minimum belanja "+Ui.money(min),null);
        long pot;
        if("persen".equals(Ui.str(d.get("tipe")))){
            pot=Math.round(subtotal*Ui.lng(d.get("nilai"))/100.0);
            long maks=Ui.lng(d.get("maks_potongan"));
            if(maks>0&&pot>maks) pot=maks;
        }else pot=Math.min(Ui.lng(d.get("nilai")),subtotal);
        return new Disc(true,pot,null,kode.toUpperCase());
    }

    public static long subtotal(List<Item> items){
        return items.stream().mapToLong(i->i.harga()*i.qty()).sum();
    }

    /* ---------- CHECKOUT ---------- */
    public record Hasil(Map<String,Object> transaksi,List<Map<String,Object>> detail,
                        List<String> warnings,String mode){} // mode: "online"|"offline"|"adopt"

    public static Hasil checkout(List<Item> items,String metode,long dibayar,String kodeDiskon,String ref){
        long sub=subtotal(items);
        Disc d=kodeDiskon==null||kodeDiskon.isBlank()?new Disc(false,0,null,null):null;
        String trxId=Uuid.rand();

        if(ApiClient.hasToken()){
            List<Map<String,Object>> bodyItems=new ArrayList<>();
            for(Item i:items) bodyItems.add(Map.of(
                    "produk_id",i.pid(),"qty",i.qty(),
                    "opsi",String.join(" · ",i.opsi()),
                    "harga",i.harga()));
            Map<String,Object> body=new LinkedHashMap<>();
            body.put("id",trxId);
            body.put("metode",metode);
            body.put("dibayar",dibayar);
            if(kodeDiskon!=null&&!kodeDiskon.isBlank()) body.put("diskon_kode",kodeDiskon);
            if(ref!=null&&!ref.isBlank()) body.put("ref",ref);
            body.put("items",bodyItems);
            try{
                Map<String,Object> resp=ApiClient.post("/api/transactions",body);
                applyRemoteToLocal(resp);
                @SuppressWarnings("unchecked")
                List<Map<String,Object>> det=(List<Map<String,Object>>)resp.get("detail");
                @SuppressWarnings("unchecked")
                List<String> warn=resp.get("warnings") instanceof List<?> w?(List<String>)w:List.of();
                return new Hasil((Map<String,Object>)resp.get("transaksi"),det,warn,"online");
            }catch(ApiClient.ApiException e){
                if(e.status==0){ // timeout/koneksi — cek apakah ternyata masuk
                    try{
                        Map<String,Object> chk=ApiClient.get("/api/transactions/"+trxId);
                        applyRemoteToLocal(Map.of("transaksi",chk.get("transaksi"),
                                "detail",chk.get("items"),"mutasi",List.of()));
                        return new Hasil((Map<String,Object>)chk.get("transaksi"),
                                (List<Map<String,Object>>)chk.get("items"),List.of(),"adopt");
                    }catch(Exception ignore){ /* benar-benar tidak masuk */ }
                }else throw e; // 400 dsb: error validasi → munculkan ke kasir
            }
        }
        // ---- OFFLINE: buat lokal ----
        if(d==null) d=diskonLokal(kodeDiskon,sub);
        long potongan=d.valid()?d.potongan():0;
        long total=sub-potongan;
        if(dibayar<total) throw new ApiClient.ApiException(400,"Jumlah bayar kurang dari total");
        long bayar="cash".equals(metode)?dibayar:total;
        long kembali=bayar-total;
        long now=System.currentTimeMillis();
        LocalDateTime w=LocalDateTime.now();
        String kode=nextLocalKode();
        String dev=shortDev();

        Db.tx(c->{
            try(var ps=c.prepareStatement("""
                INSERT INTO transaksi(id,kode,waktu,user_id,subtotal,diskon,diskon_kode,total,metode,dibayar,kembalian,ref,status,device_id,created_at,updated_at,is_deleted,dirty)
                VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,0,1)""")){
                ps.setString(1,trxId); ps.setString(2,kode);
                ps.setLong(3,w.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                ps.setString(4,Ui.str(PosApp.user.get("id")));
                ps.setLong(5,sub); ps.setLong(6,potongan);
                ps.setString(7,potongan>0?kodeDiskon.toUpperCase():null);
                ps.setLong(8,total); ps.setString(9,metode);
                ps.setLong(10,bayar); ps.setLong(11,kembali);
                ps.setString(12,ref==null||ref.isBlank()?null:ref);
                ps.setString(13,"selesai"); ps.setString(14,dev);
                ps.setLong(15,now); ps.setLong(16,now);
                ps.executeUpdate();
            }
            for(int i=0;i<items.size();i++){
                Item it=items.get(i);
                long hpp=hppOf(it.pid())*it.qty();
                try(var ps=c.prepareStatement("""
                    INSERT INTO transaksi_detail(id,transaksi_id,produk_id,nama,opsi,qty,harga,subtotal,hpp,created_at,updated_at,is_deleted,dirty)
                    VALUES (?,?,?,?,?,?,?,?,?,?,?,?,0,1)""")){
                    ps.setString(1,PosIds.detailId(trxId,i));
                    ps.setString(2,trxId); ps.setString(3,it.pid());
                    ps.setString(4,it.nama());
                    ps.setString(5,it.opsi().isEmpty()?null:String.join(" · ",it.opsi()));
                    ps.setLong(6,it.qty()); ps.setLong(7,it.harga());
                    ps.setLong(8,it.harga()*it.qty()); ps.setLong(9,hpp);
                    ps.setLong(10,now); ps.setLong(11,now);
                    ps.executeUpdate();
                }
                int mi=0;
                for(var r:Db.query("SELECT bahan_id,jumlah FROM resep WHERE produk_id=? AND is_deleted=0",it.pid())){
                    try(var ps=c.prepareStatement("""
                        INSERT INTO stok_mutasi(id,bahan_id,arah,jumlah,sumber,ref_id,keterangan,waktu,device_id,created_at,updated_at,is_deleted,dirty)
                        VALUES (?,?,?,?,?,?,?,?,?,?,?,?,0,1)""")){
                        ps.setString(1,PosIds.mutasiId(trxId,i,Ui.str(r.get("bahan_id"))));
                        ps.setString(2,Ui.str(r.get("bahan_id"))); ps.setString(3,"keluar");
                        ps.setDouble(4,Ui.dbl(r.get("jumlah"))*it.qty());
                        ps.setString(5,"transaksi"); ps.setString(6,trxId);
                        ps.setString(7,kode); ps.setLong(8,now); ps.setString(9,dev);
                        ps.setLong(10,now); ps.setLong(11,now);
                        ps.executeUpdate();
                    }
                    mi++;
                }
            }
            SyncEngine.recalcStok(c);
        });
        var t=Db.query("SELECT * FROM transaksi WHERE id=?",trxId).get(0);
        var det=Db.query("SELECT * FROM transaksi_detail WHERE transaksi_id=?",trxId);
        return new Hasil(t,det,peringatanStok(items),"offline");
    }

    /* respons REST → upsert lokal dirty=0 */
    @SuppressWarnings("unchecked")
    private static void applyRemoteToLocal(Map<String,Object> resp){
        Map<String,Object> t=(Map<String,Object>)resp.get("transaksi");
        List<Map<String,Object>> det=(List<Map<String,Object>>)resp.getOrDefault("detail",List.of());
        List<Map<String,Object>> mut=(List<Map<String,Object>>)resp.getOrDefault("mutasi",List.of());
        if(t==null) return;
        long now=System.currentTimeMillis();
        Db.tx(c->{
            upsert(c,"transaksi",Tables.byName("transaksi").cols(),t,now);
            for(Map<String,Object> d:det) upsert(c,"transaksi_detail",Tables.byName("transaksi_detail").cols(),d,now);
            for(Map<String,Object> m:mut) upsert(c,"stok_mutasi",Tables.byName("stok_mutasi").cols(),m,now);
            SyncEngine.recalcStok(c);
        });
    }

    private static void upsert(Connection c,String table,java.util.List<String> cols,
                        Map<String,Object> row,long now) throws Exception{
        String cs=String.join(",",cols);
        StringBuilder set=new StringBuilder();
        for(String col:cols) if(!col.equals("id")&&!col.equals("created_at")) set.append(col).append("=excluded.").append(col).append(',');
        set.append("dirty=0");
        String sql="INSERT INTO "+table+" ("+cs+",dirty) VALUES ("+
                String.join(",",java.util.Collections.nCopies(cols.size(),"?"))+",0)"+
                " ON CONFLICT(id) DO UPDATE SET "+set;
        try(PreparedStatement ps=c.prepareStatement(sql)){
            for(int i=0;i<cols.size();i++){
                Object v=row.get(cols.get(i));
                if(v instanceof Integer n&&(cols.get(i).equals("created_at")||cols.get(i).equals("updated_at")||cols.get(i).equals("waktu"))) ps.setLong(i+1,n.longValue());
                else ps.setObject(i+1,v);
            }
            ps.executeUpdate();
        }
    }

    /* ---------- helpers ---------- */
    public static long hppOf(String pid){
        return Ui.lng(Db.query("""
            SELECT COALESCE(SUM(b.harga*r.jumlah),0) n FROM resep r
            JOIN bahan b ON b.id=r.bahan_id
            WHERE r.produk_id=? AND r.is_deleted=0 AND b.is_deleted=0""",pid).get(0).get("n"));
    }

    private static List<String> peringatanStok(List<Item> items){
        Set<String> bids=new LinkedHashSet<>();
        for(Item i:items)
            Db.query("SELECT bahan_id FROM resep WHERE produk_id=? AND is_deleted=0",i.pid())
            .forEach(r->bids.add(Ui.str(r.get("bahan_id"))));
        List<String> w=new ArrayList<>();
        for(String bid:bids){
            var b=Db.query("SELECT nama,stok,stok_min FROM bahan WHERE id=?",bid);
            if(!b.isEmpty()&&Ui.dbl(b.get(0).get("stok"))<=Ui.dbl(b.get(0).get("stok_min")))
                w.add("Stok "+b.get(0).get("nama")+" menipis");
        }
        return w;
    }

    private static String shortDev(){
        String d=SyncMeta.get("device_id");
        return d==null?"L":d.substring(0,8);
    }

    private static String nextLocalKode(){
        String ds=LocalDate.now().toString().replace("-","");
        int n=Db.query("SELECT kode FROM transaksi WHERE kode LIKE ?","TRX-"+ds+"-"+shortDev()+"-%").size();
        return "TRX-"+ds+"-"+shortDev()+"-"+String.format("%02d",n+1);
    }
}