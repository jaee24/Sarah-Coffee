package com.senja.admin.page;

import com.senja.admin.*;
import com.senja.core.*;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.Map;

public class Supplier implements Page {
    @Override public String id(){ return "supplier"; }
    @Override public String title(){ return "Supplier"; }
    @Override public String subtitle(){ return "Data pemasok bahan baku"; }

    @Override public Node view(){
        TextField cari=Ui.tf(""); cari.setPromptText("Nama supplier…"); cari.setPrefWidth(220);
        TableView<Map<String,Object>> tbl=Ui.table();
        Runnable refresh=()->{
            String q="%"+cari.getText().toLowerCase()+"%";
            tbl.setItems(FXCollections.observableArrayList(Db.query(
                "SELECT * FROM supplier WHERE is_deleted=0 AND lower(nama) LIKE ? ORDER BY kode",q)));
        };
        cari.textProperty().addListener((o,ov,nv)->refresh.run());
        tbl.getColumns().addAll(
            Ui.col("ID","kode",90),
            Ui.col("Nama","nama",200),
            Ui.col("Nomor HP","no_hp",150),
            Ui.col("Alamat","alamat",260),
            Ui.actCol(s->{ form(s); refresh.run(); },
                s->Dlg.confirm(AdminApp.stage,"Hapus Supplier","Supplier \""+s.get("nama")+"\" akan dihapus.",()->{
                    Db.exec("UPDATE supplier SET is_deleted=1,updated_at=?,dirty=1 WHERE id=?",System.currentTimeMillis(),s.get("id"));
                    Toast.ok("Supplier dihapus"); refresh.run();
                })));
        HBox toolbar=new HBox(10,cari,Ui.spacer(),Ui.btn("+ Supplier","btn-acc",()->{ form(null); refresh.run(); }));
        refresh.run();
        return new VBox(12,toolbar,tbl);
    }

    private void form(Map<String,Object> s){
        TextField nama=Ui.tf(s==null?"":Ui.str(s.get("nama")));
        TextField hp=Ui.tf(s==null?"":Ui.str(s.get("no_hp")));
        TextArea al=Ui.ta(s==null?"":Ui.str(s.get("alamat")));
        Dlg.modal(AdminApp.stage,s==null?"Tambah Supplier":"Edit Supplier — "+s.get("kode"),420,dlg->{
            var g=Ui.grid();
            Ui.frow(g,0,"Nama",nama);
            Ui.frow(g,1,"Nomor HP",hp);
            Ui.frow(g,2,"Alamat",al);
            HBox btns=new HBox(10,Ui.spacer(),
                Ui.btn("Batal","btn-line",dlg::close),
                Ui.btn("Simpan","btn-acc",()->{
                    String n=nama.getText().trim();
                    if(n.isEmpty()){ Toast.bad("Nama supplier wajib diisi"); return; }
                    long now=System.currentTimeMillis();
                    if(s==null){
                        Db.exec("INSERT INTO supplier(id,kode,nama,no_hp,alamat,created_at,updated_at,is_deleted,dirty) VALUES(?,?,?,?,?,?,?,0,1)",
                            Uuid.rand(),nextKode(),n,hp.getText().trim(),al.getText().trim(),now,now);
                    }else{
                        Db.exec("UPDATE supplier SET nama=?,no_hp=?,alamat=?,updated_at=?,dirty=1 WHERE id=?",
                            n,hp.getText().trim(),al.getText().trim(),now,s.get("id"));
                    }
                    Toast.ok("Supplier tersimpan"); dlg.close();
                }));
            btns.setAlignment(Pos.CENTER_RIGHT);
            return new VBox(10,g,btns);
        });
    }

    private String nextKode(){
        int max=0;
        for(var m:Db.query("SELECT kode FROM supplier WHERE kode LIKE 'SUP-%'")){
            try{ max=Math.max(max,Integer.parseInt(Ui.str(m.get("kode")).substring(4))); }catch(Exception ignore){}
        }
        return "SUP-"+String.format("%02d",max+1);
    }
}