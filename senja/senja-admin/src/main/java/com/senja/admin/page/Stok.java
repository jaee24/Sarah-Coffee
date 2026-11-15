package com.senja.admin.page;

import com.senja.admin.*;
import com.senja.core.*;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.sql.Connection;
import java.util.Map;

public class Stok implements Page {
    @Override public String id(){ return "stok"; }
    @Override public String title(){ return "Stok Bahan Baku"; }
    @Override public String subtitle(){ return "Pantau stok & stok minimum"; }

    @Override public Node view(){
        TextField cari=Ui.tf(""); cari.setPromptText("Nama bahan baku…"); cari.setPrefWidth(240);
        TableView<Map<String,Object>> tbl=Ui.table();
        Runnable refresh=()->{
            String q="%"+cari.getText().toLowerCase()+"%";
            tbl.setItems(FXCollections.observableArrayList(Db.query(
                "SELECT * FROM bahan WHERE is_deleted=0 AND lower(nama) LIKE ? ORDER BY kode",q)));
        };
        cari.textProperty().addListener((o,ov,nv)->refresh.run());

        var cStok=Ui.colF("Stok",m->Ui.trim(Ui.dbl(m.get("stok")))+" "+Ui.str(m.get("satuan")),120);
        cStok.setCellFactory(cl->new TableCell<>(){
            @Override protected void updateItem(String v,boolean e){
                super.updateItem(v,e);
                if(e||v==null){ setGraphic(null); return; }
                var m=getTableRow()==null?null:getTableRow().getItem();
                boolean low=m!=null&&Ui.dbl(m.get("stok"))<=Ui.dbl(m.get("stok_min"));
                Label l=new Label(v);
                l.getStyleClass().add("cell-l");
                if(low) l.getStyleClass().add("low");
                setGraphic(l);
            }
        });
        var cStatus=Ui.colF("Status",m->Ui.dbl(m.get("stok"))<=Ui.dbl(m.get("stok_min"))?"Kritis":"Aman",90);
        Ui.badgeCol(cStatus,v->v.equals("Kritis")?"bdg-bad":"bdg-ok");

        tbl.getColumns().addAll(
            Ui.col("ID","kode",80),
            Ui.col("Nama Bahan Baku","nama",210),
            Ui.col("Satuan","satuan",80),
            cStok,
            Ui.colF("Min. Stok",m->Ui.trim(Ui.dbl(m.get("stok_min"))),90),
            Ui.colF("Harga Modal",m->Ui.money(Ui.lng(m.get("harga")))+" /"+Ui.str(m.get("satuan")),140),
            cStatus,
            Ui.actCol(b->{ form(b); refresh.run(); },null));

        HBox toolbar=new HBox(10,cari,Ui.spacer(),Ui.btn("+ Bahan Baku","btn-acc",()->{ form(null); refresh.run(); }));
        refresh.run();
        return new VBox(12,toolbar,tbl);
    }

    private void form(Map<String,Object> b){
        boolean edit=b!=null;
        TextField nama=Ui.tf(edit?Ui.str(b.get("nama")):"");
        ComboBox<String> sat=Ui.combo("gram","ml","pcs","kg","liter");
        if(edit) sat.getSelectionModel().select(Ui.str(b.get("satuan")));
        TextField harga=Ui.tf(edit?String.valueOf(Ui.lng(b.get("harga"))):"");
        TextField stok=Ui.tf(edit?Ui.trim(Ui.dbl(b.get("stok"))):"0");
        TextField min=Ui.tf(edit?Ui.trim(Ui.dbl(b.get("stok_min"))):"0");
        Label info=Ui.label(edit
            ?"Mengubah nilai stok otomatis membuat mutasi penyesuaian (buku besar stok)."
            :"Stok awal akan dicatat sebagai mutasi awal di buku besar.","mut");
        Dlg.modal(AdminApp.stage,edit?"Edit Bahan Baku — "+b.get("kode"):"Tambah Bahan Baku",440,dlg->{
            var g=Ui.grid();
            Ui.frow(g,0,"Nama Bahan Baku",nama);
            Ui.frow(g,1,"Satuan",sat);
            Ui.frow(g,2,"Harga Modal /satuan (Rp)",harga);
            Ui.frow(g,3,"Stok",stok);
            Ui.frow(g,4,"Minimum Stok",min);
            HBox btns=new HBox(10,Ui.spacer(),
                Ui.btn("Batal","btn-line",dlg::close),
                Ui.btn("Simpan","btn-acc",()->{
                    String n=nama.getText().trim();
                    if(n.isEmpty()){ Toast.bad("Nama bahan wajib diisi"); return; }
                    boolean dup=!Db.query("SELECT id FROM bahan WHERE is_deleted=0 AND lower(nama)=? AND id<>?",
                            n.toLowerCase(),edit?b.get("id"):"").isEmpty();
                    if(dup){ Toast.bad("Nama bahan baku tidak boleh sama"); return; }
                    double st=parse(stok.getText()), mn=parse(min.getText());
                    if(st<0||mn<0){ Toast.bad("Stok tidak boleh negatif"); return; }
                    long h; try{ h=Long.parseLong(harga.getText().trim()); }catch(Exception ex){ Toast.bad("Harga tidak valid"); return; }
                    if(h<=0){ Toast.bad("Harga modal harus lebih dari 0"); return; }
                    long now=System.currentTimeMillis();
                    String satuan=sat.getSelectionModel().getSelectedItem();
                    if(!edit){
                        String id=Uuid.rand();
                        Db.tx(c->{
                            try(var ps=c.prepareStatement("INSERT INTO bahan(id,kode,nama,satuan,stok,stok_min,harga,created_at,updated_at,is_deleted,dirty) VALUES(?,?,?,?,?,?,?,?,?,0,1)")){
                                ps.setString(1,id); ps.setString(2,nextKode()); ps.setString(3,n); ps.setString(4,satuan);
                                ps.setDouble(5,st); ps.setDouble(6,mn); ps.setLong(7,h); ps.setLong(8,now); ps.setLong(9,now);
                                ps.executeUpdate();
                            }
                            if(st>0) mutasi(c,id,"masuk",st,"awal","Stok awal",now);
                            SyncEngine.recalcStok(c);
                        });
                    }else{
                        double delta=st-Ui.dbl(b.get("stok"));
                        Db.tx(c->{
                            try(var ps=c.prepareStatement("UPDATE bahan SET nama=?,satuan=?,stok=?,stok_min=?,harga=?,updated_at=?,dirty=1 WHERE id=?")){
                                ps.setString(1,n); ps.setString(2,satuan); ps.setDouble(3,st); ps.setDouble(4,mn);
                                ps.setLong(5,h); ps.setLong(6,now); ps.setString(7,Ui.str(b.get("id")));
                                ps.executeUpdate();
                            }
                            if(Math.abs(delta)>1e-9) mutasi(c,Ui.str(b.get("id")),delta>0?"masuk":"keluar",Math.abs(delta),"manual","Penyesuaian manual stok",now);
                            SyncEngine.recalcStok(c);
                        });
                    }
                    Toast.ok("Bahan baku tersimpan"); dlg.close();
                }));
            btns.setAlignment(Pos.CENTER_RIGHT);
            return new VBox(10,g,info,btns);
        });
    }

    private double parse(String s){ try{ return Double.parseDouble(s.trim()); }catch(Exception e){ return -1; } }

    private void mutasi(Connection c,String bahanId,String arah,double jml,String sumber,String ket,long now) throws Exception{
        try(var ps=c.prepareStatement("INSERT INTO stok_mutasi(id,bahan_id,arah,jumlah,sumber,ref_id,keterangan,waktu,device_id,created_at,updated_at,is_deleted,dirty) VALUES(?,?,?,?,?,NULL,?,?,?,?,?,0,1)")){
            ps.setString(1,Uuid.rand()); ps.setString(2,bahanId); ps.setString(3,arah); ps.setDouble(4,jml);
            ps.setString(5,sumber); ps.setString(6,ket); ps.setLong(7,now);
            String dev=SyncMeta.get("device_id"); ps.setString(8,dev==null?"local":dev);
            ps.setLong(9,now); ps.setLong(10,now);
            ps.executeUpdate();
        }
    }

    private String nextKode(){
        int max=0;
        for(var m:Db.query("SELECT kode FROM bahan WHERE kode LIKE 'BB-%'")){
            try{ max=Math.max(max,Integer.parseInt(Ui.str(m.get("kode")).substring(3))); }catch(Exception ignore){}
        }
        return "BB-"+String.format("%02d",max+1);
    }
}