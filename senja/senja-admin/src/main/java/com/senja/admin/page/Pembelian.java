package com.senja.admin.page;

import com.senja.admin.*;
import com.senja.core.*;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Pembelian implements Page {
    @Override public String id(){ return "pembelian"; }
    @Override public String title(){ return "Pembelian"; }
    @Override public String subtitle(){ return "Pengadaan bahan baku — stok otomatis bertambah"; }

    @Override public Node view(){
        TableView<Map<String,Object>> tbl=Ui.table();
        Runnable refresh=()->tbl.setItems(FXCollections.observableArrayList(Db.query("""
            SELECT p.*, s.nama snama,
              (SELECT COUNT(*) FROM pembelian_detail pd WHERE pd.pembelian_id=p.id AND pd.is_deleted=0) nitem
            FROM pembelian p LEFT JOIN supplier s ON s.id=p.supplier_id
            WHERE p.is_deleted=0 ORDER BY p.tanggal DESC, p.kode DESC""")));
        var cTot=Ui.colF("Total",m->Ui.money(Ui.lng(m.get("total"))),140);
        cTot.setStyle("-fx-alignment:CENTER-RIGHT;");
        tbl.getColumns().addAll(
            Ui.col("ID","kode",100),
            Ui.colF("Tanggal",m->Ui.fmtD(Ui.str(m.get("tanggal"))),130),
            Ui.col("Supplier","snama",200),
            Ui.colF("Bahan",m->bahanRingkas(m),260),
            cTot);
        tbl.setRowFactory(tv->{
            TableRow<Map<String,Object>> r=new TableRow<>();
            r.setOnMouseClicked(e->{ if(!r.isEmpty()) detail(r.getItem(),refresh); });
            return r;
        });
        HBox toolbar=new HBox(10,Ui.spacer(),Ui.btn("+ Pembelian","btn-acc",()->{ form(); refresh.run(); }));
        refresh.run();
        return new VBox(12,toolbar,tbl,
            Ui.label("Klik baris untuk melihat detail. Stok bahan otomatis bertambah saat pembelian disimpan.","mut"));
    }

    private String bahanRingkas(Map<String,Object> m){
        var d=Db.query("""
            SELECT b.nama FROM pembelian_detail pd JOIN bahan b ON b.id=pd.bahan_id
            WHERE pd.pembelian_id=? AND pd.is_deleted=0 ORDER BY b.nama LIMIT 2""",m.get("id"));
        long n=Ui.lng(m.get("nitem"));
        if(d.isEmpty()) return "—";
        String s=String.join(", ",d.stream().map(x->Ui.str(x.get("nama"))).toList());
        return n>2?s+" +"+(n-2):s;
    }

    /* ============ editor baris bahan ============ */
    private final class RowsEditor {
        final List<Map<String,Object>> rows=new ArrayList<>();
        final VBox box=new VBox(6);
        final Label totLbl=new Label();

        RowsEditor(){ totLbl.getStyleClass().add("h3"); render(); }

        void render(){
            box.getChildren().clear();
            long t=0;
            for(int i=0;i<rows.size();i++){
                final int idx=i;
                var r=rows.get(i);
                long st=Ui.lng(r.get("harga"))*(long)Ui.dbl(r.get("jumlah"));
                t+=st;
                HBox row=new HBox(8,
                    Ui.label(Ui.str(r.get("bnama"))+" — "+Ui.trim(Ui.dbl(r.get("jumlah")))+" "+Ui.str(r.get("satuan"))
                        +" × "+Ui.money(Ui.lng(r.get("harga")))+" = "+Ui.money(st)),
                    Ui.spacer(),
                    Ui.btn("Hapus","btn-line btn-sm",()->{ rows.remove(idx); render(); }));
                row.setAlignment(Pos.CENTER_LEFT);
                box.getChildren().add(row);
            }
            if(rows.isEmpty()) box.getChildren().add(Ui.label("Belum ada bahan.","mut"));
            totLbl.setText("Total Pembelian: "+Ui.money(t));
        }
    }

    private void detail(Map<String,Object> p,Runnable after){
        var det=Db.query("""
            SELECT pd.*, b.nama bnama, b.satuan FROM pembelian_detail pd
            JOIN bahan b ON b.id=pd.bahan_id
            WHERE pd.pembelian_id=? AND pd.is_deleted=0 ORDER BY b.nama""",p.get("id"));
        Dlg.modal(AdminApp.stage,"Detail Pembelian — "+p.get("kode"),560,dlg->{
            TableView<Map<String,Object>> it=Ui.table();
            it.setFixedCellSize(30);
            it.getColumns().addAll(
                Ui.col("Bahan","bnama",180),
                Ui.colF("Jumlah",m->Ui.trim(Ui.dbl(m.get("jumlah")))+" "+Ui.str(m.get("satuan")),110),
                Ui.colF("Harga",m->Ui.money(Ui.lng(m.get("harga"))),100),
                Ui.colF("Subtotal",m->Ui.money(Ui.lng(m.get("subtotal"))),110));
            it.setItems(FXCollections.observableArrayList(det));
            it.setPrefHeight(Math.min(280,36+det.size()*30));
            Label tot=new Label("Total: "+Ui.money(Ui.lng(p.get("total"))));
            tot.getStyleClass().add("h3");
            HBox btns=new HBox(10,Ui.spacer(),
                Ui.btn("Hapus","btn-danger",()->Dlg.confirm(AdminApp.stage,"Hapus Pembelian",
                    "Pembelian \""+p.get("kode")+"\" akan dihapus dan stoknya dikembalikan.",()->{
                    var drows=Db.query("SELECT id,bahan_id,jumlah FROM pembelian_detail WHERE pembelian_id=? AND is_deleted=0",p.get("id"));
                    long now=System.currentTimeMillis();
                    Db.tx(c->{
                        try(var ps=c.prepareStatement("UPDATE pembelian SET is_deleted=1,updated_at=?,dirty=1 WHERE id=?")){
                            ps.setLong(1,now); ps.setString(2,Ui.str(p.get("id"))); ps.executeUpdate();
                        }
                        for(var d:drows){
                            try(var ps=c.prepareStatement("UPDATE pembelian_detail SET is_deleted=1,updated_at=?,dirty=1 WHERE id=?")){
                                ps.setLong(1,now); ps.setString(2,Ui.str(d.get("id"))); ps.executeUpdate();
                            }
                            try(var ps=c.prepareStatement(
                                    "INSERT INTO stok_mutasi(id,bahan_id,arah,jumlah,sumber,ref_id,keterangan,waktu,device_id,created_at,updated_at,is_deleted,dirty) VALUES(?,?,?,?,?,?,?,?,?,?,?,0,1)")){
                                ps.setString(1,Uuid.rand()); ps.setString(2,Ui.str(d.get("bahan_id")));
                                ps.setString(3,"keluar"); ps.setDouble(4,Ui.dbl(d.get("jumlah")));
                                ps.setString(5,"manual"); ps.setString(6,Ui.str(p.get("id")));
                                ps.setString(7,"Pembatalan "+p.get("kode")); ps.setLong(8,now);
                                ps.setString(9,deviceId()); ps.setLong(10,now); ps.setLong(11,now);
                                ps.executeUpdate();
                            }
                        }
                        SyncEngine.recalcStok(c);
                    });
                    Toast.ok("Pembelian dihapus — stok dikembalikan");
                    dlg.close();
                    after.run();
                })),
                Ui.btn("Tutup","btn-line",dlg::close));
            btns.setAlignment(Pos.CENTER_RIGHT);
            return new VBox(10,it,tot,btns);
        });
    }

    private String deviceId(){
        String d=SyncMeta.get("device_id");
        return d==null?"local":d;
    }

    private void form(){
        /* supplier: tampil nama saja */
        final ComboBox<Ui.ComboItem> sup=new ComboBox<>();
        sup.getStyleClass().add("inp"); sup.setMaxWidth(Double.MAX_VALUE);
        for(var s:Db.query("SELECT id,nama FROM supplier WHERE is_deleted=0 ORDER BY nama"))
            sup.getItems().add(new Ui.ComboItem(Ui.str(s.get("id")),Ui.str(s.get("nama"))));
        if(sup.getItems().isEmpty()){ Toast.bad("Tambahkan supplier terlebih dahulu"); return; }
        sup.getSelectionModel().select(0);
        final DatePicker tgl=new DatePicker(LocalDate.now()); tgl.getStyleClass().add("inp");

        final RowsEditor editor=new RowsEditor();

        /* bahan: tampil "Nama (satuan)" saja */
        final ComboBox<Ui.ComboItem> bahanCb=new ComboBox<>();
        bahanCb.getStyleClass().add("inp"); bahanCb.setMaxWidth(Double.MAX_VALUE);
        for(var b:Db.query("SELECT id,nama,satuan FROM bahan WHERE is_deleted=0 ORDER BY nama"))
            bahanCb.getItems().add(new Ui.ComboItem(Ui.str(b.get("id")),
                Ui.str(b.get("nama"))+" ("+Ui.str(b.get("satuan"))+")"));
        if(!bahanCb.getItems().isEmpty()) bahanCb.getSelectionModel().select(0);
        final TextField jml=Ui.tf(""); jml.setPrefWidth(90); jml.setPromptText("Jumlah");
        final TextField hrg=Ui.tf(""); hrg.setPrefWidth(110); hrg.setPromptText("Harga/sat");

        Dlg.modal(AdminApp.stage,"Tambah Pembelian",560,dlg->{
            HBox addRow=new HBox(8,bahanCb,jml,hrg,
                Ui.btn("Tambah","btn-line btn-sm",()->{
                    Ui.ComboItem sel=bahanCb.getValue();
                    if(sel==null) return;
                    final String bid=sel.id;
                    double j; long h;
                    try{
                        j=Double.parseDouble(jml.getText().trim());
                        h=Long.parseLong(hrg.getText().trim());
                    }catch(Exception ex){ Toast.bad("Jumlah / harga tidak valid"); return; }
                    if(j<=0||h<=0){ Toast.bad("Jumlah & harga harus lebih dari 0"); return; }
                    var b=Db.query("SELECT nama,satuan FROM bahan WHERE id=?",bid).get(0);
                    Map<String,Object> nr=new LinkedHashMap<>();
                    nr.put("bid",bid); nr.put("bnama",b.get("nama"));
                    nr.put("satuan",b.get("satuan")); nr.put("jumlah",j); nr.put("harga",h);
                    editor.rows.add(nr); jml.clear(); editor.render();
                }));

            HBox btns=new HBox(10,Ui.spacer(),
                Ui.btn("Batal","btn-line",dlg::close),
                Ui.btn("Simpan","btn-acc",()->{
                    if(editor.rows.isEmpty()){ Toast.bad("Tambahkan minimal satu bahan"); return; }
                    if(tgl.getValue()==null){ Toast.bad("Pilih tanggal"); return; }
                    if(sup.getValue()==null){ Toast.bad("Pilih supplier"); return; }
                    long now=System.currentTimeMillis();
                    String poId=Uuid.rand();
                    String kode=nextKode();
                    String supId=sup.getValue().id;
                    Db.tx(c->{
                        long total=0;
                        for(var r:editor.rows) total+=Ui.lng(r.get("harga"))*(long)Ui.dbl(r.get("jumlah"));
                        try(var ps=c.prepareStatement(
                                "INSERT INTO pembelian(id,kode,tanggal,supplier_id,user_id,total,created_at,updated_at,is_deleted,dirty) VALUES(?,?,?,?,?,?,?,?,0,1)")){
                            ps.setString(1,poId); ps.setString(2,kode); ps.setString(3,tgl.getValue().toString());
                            ps.setString(4,supId); ps.setString(5,Ui.str(AdminApp.user.get("id")));
                            ps.setLong(6,total); ps.setLong(7,now); ps.setLong(8,now);
                            ps.executeUpdate();
                        }
                        for(var r:editor.rows){
                            try(var ps=c.prepareStatement(
                                    "INSERT INTO pembelian_detail(id,pembelian_id,bahan_id,jumlah,harga,subtotal,created_at,updated_at,is_deleted,dirty) VALUES(?,?,?,?,?,?,?,?,0,1)")){
                                ps.setString(1,Uuid.rand()); ps.setString(2,poId); ps.setString(3,Ui.str(r.get("bid")));
                                ps.setDouble(4,Ui.dbl(r.get("jumlah"))); ps.setLong(5,Ui.lng(r.get("harga")));
                                ps.setLong(6,Ui.lng(r.get("harga"))*(long)Ui.dbl(r.get("jumlah")));
                                ps.setLong(7,now); ps.setLong(8,now);
                                ps.executeUpdate();
                            }
                            try(var ps=c.prepareStatement(
                                    "INSERT INTO stok_mutasi(id,bahan_id,arah,jumlah,sumber,ref_id,keterangan,waktu,device_id,created_at,updated_at,is_deleted,dirty) VALUES(?,?,?,?,?,?,?,?,?,?,?,0,1)")){
                                ps.setString(1,Uuid.rand()); ps.setString(2,Ui.str(r.get("bid")));
                                ps.setString(3,"masuk"); ps.setDouble(4,Ui.dbl(r.get("jumlah")));
                                ps.setString(5,"pembelian"); ps.setString(6,poId); ps.setString(7,kode);
                                ps.setLong(8,now); ps.setString(9,deviceId());
                                ps.setLong(10,now); ps.setLong(11,now);
                                ps.executeUpdate();
                            }
                        }
                        SyncEngine.recalcStok(c);
                    });
                    Toast.ok("Pembelian "+kode+" disimpan — stok diperbarui");
                    dlg.close();
                }));
            btns.setAlignment(Pos.CENTER_RIGHT);
            return new VBox(10,
                Ui.card(Ui.h3("Supplier & Tanggal"),new HBox(10,sup,tgl)),
                Ui.card(Ui.h3("Bahan Baku"),addRow,editor.box,editor.totLbl),
                btns);
        });
    }

    private String nextKode(){
        int max=0;
        for(var m:Db.query("SELECT kode FROM pembelian WHERE kode LIKE 'PO-%'")){
            try{ max=Math.max(max,Integer.parseInt(Ui.str(m.get("kode")).substring(3))); }catch(Exception ignore){}
        }
        return "PO-"+String.format("%03d",max+1);
    }
}