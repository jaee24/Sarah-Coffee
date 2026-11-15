package com.senja.admin.page;

import com.senja.admin.*;
import com.senja.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Kategori implements Page {
    private static final ObjectMapper M=new ObjectMapper();
    @Override public String id(){ return "kategori"; }
    @Override public String title(){ return "Kategori"; }
    @Override public String subtitle(){ return "Kelola kategori produk"; }

    @Override public Node view(){
        TableView<Map<String,Object>> tbl=Ui.table();
        Runnable refresh=()->tbl.setItems(FXCollections.observableArrayList(Db.query(
            "SELECT k.*, (SELECT COUNT(*) FROM produk p WHERE p.kategori_id=k.id AND p.is_deleted=0) nmenu "+
            "FROM kategori k WHERE k.is_deleted=0 ORDER BY k.nama")));
        var cJenis=Ui.colF("Jenis",m->Ui.str(m.get("jenis")),100);
        Ui.badgeCol(cJenis,v->v.equals("minuman")?"bdg-a":"bdg-w");
        var act=Ui.actCol(
            k->{ form(k); refresh.run(); },
            k->{
                long n=Ui.lng(k.get("nmenu"));
                if(n>0){ Toast.bad("Kategori masih dipakai "+n+" menu — pindahkan menu dulu"); return; }
                Dlg.confirm(AdminApp.stage,"Hapus Kategori","Kategori \""+k.get("nama")+"\" akan dihapus.",()->{
                    Db.exec("UPDATE kategori SET is_deleted=1,dirty=1,updated_at=? WHERE id=?",System.currentTimeMillis(),k.get("id"));
                    Toast.ok("Kategori dihapus"); refresh.run();
                });
            });
        tbl.getColumns().addAll(
            Ui.col("Nama","nama",180),cJenis,
            Ui.colF("Penyajian",m->penText(Ui.str(m.get("penyajian"))),140),
            Ui.colF("Jumlah Menu",m->Ui.lng(m.get("nmenu"))+" menu",110),
            act);
        HBox toolbar=new HBox(10,Ui.spacer(),Ui.btn("+ Kategori","btn-acc",()->{ form(null); refresh.run(); }));
        refresh.run();
        return new VBox(12,toolbar,tbl);
    }

    private void form(Map<String,Object> k){
        TextField nama=Ui.tf(k==null?"":Ui.str(k.get("nama")));
        ComboBox<String> jenis=Ui.combo("minuman","makanan");
        CheckBox hot=new CheckBox("Hot"), cold=new CheckBox("Cold");
        if(k!=null){
            jenis.getSelectionModel().select(Ui.str(k.get("jenis")));
            var pen=penList(Ui.str(k.get("penyajian")));
            hot.setSelected(pen.contains("hot")); cold.setSelected(pen.contains("cold"));
        }
        Runnable sync=()->{
            boolean m="minuman".equals(jenis.getSelectionModel().getSelectedItem());
            hot.setDisable(!m); cold.setDisable(!m);
        };
        jenis.getSelectionModel().selectedItemProperty().addListener((o,ov,nv)->sync.run());
        sync.run();
        Dlg.modal(AdminApp.stage,k==null?"Tambah Kategori":"Edit Kategori",400,dlg->{
            var g=Ui.grid();
            Ui.frow(g,0,"Nama Kategori",nama);
            Ui.frow(g,1,"Jenis",jenis);
            Ui.frow(g,2,"Penyajian",new HBox(10,hot,cold));
            HBox btns=new HBox(10,Ui.spacer(),
                Ui.btn("Batal","btn-line",dlg::close),
                Ui.btn("Simpan","btn-acc",()->{
                    String n=nama.getText().trim();
                    if(n.isEmpty()){ Toast.bad("Nama kategori wajib diisi"); return; }
                    boolean dup=!Db.query("SELECT id FROM kategori WHERE is_deleted=0 AND lower(nama)=? AND id<>?",
                            n.toLowerCase(),k==null?"":k.get("id")).isEmpty();
                    if(dup){ Toast.bad("Kategori dengan nama itu sudah ada"); return; }
                    boolean m="minuman".equals(jenis.getSelectionModel().getSelectedItem());
                    if(m&&!hot.isSelected()&&!cold.isSelected()){ Toast.bad("Kategori minuman wajib punya penyajian Hot/Cold"); return; }
                    String penJson=m?penJson(hot.isSelected(),cold.isSelected()):"[]";
                    long now=System.currentTimeMillis();
                    if(k==null)
                        Db.exec("INSERT INTO kategori(id,nama,jenis,penyajian,created_at,updated_at,is_deleted,dirty) VALUES(?,?,?,?,?,?,0,1)",
                            Uuid.rand(),n,jenis.getSelectionModel().getSelectedItem(),penJson,now,now);
                    else
                        Db.exec("UPDATE kategori SET nama=?,jenis=?,penyajian=?,updated_at=?,dirty=1 WHERE id=?",
                            n,jenis.getSelectionModel().getSelectedItem(),penJson,now,k.get("id"));
                    Toast.ok("Kategori tersimpan"); dlg.close();
                }));
            btns.setAlignment(Pos.CENTER_RIGHT);
            return new VBox(10,g,btns);
        });
    }

    private List<String> penList(String json){
        try{ return M.readValue(json==null||json.isBlank()?"[]":json,new TypeReference<List<String>>(){}); }
        catch(Exception e){ return new ArrayList<>(); }
    }
    private String penJson(boolean hot,boolean cold){
        List<String> l=new ArrayList<>();
        if(hot) l.add("hot"); if(cold) l.add("cold");
        try{ return M.writeValueAsString(l); }catch(Exception e){ return "[]"; }
    }
    private String penText(String json){
        var l=penList(json);
        if(l.isEmpty()) return "—";
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<l.size();i++){
            if(i>0) sb.append(" + ");
            sb.append(l.get(i).equals("hot")?"Hot":"Cold");
        }
        return sb.toString();
    }
}