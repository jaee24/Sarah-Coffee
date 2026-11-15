package com.senja.admin.page;

import com.senja.admin.*;
import com.senja.core.*;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Map;

public class Karyawan implements Page {
    @Override public String id(){ return "karyawan"; }
    @Override public String title(){ return "Data Karyawan"; }
    @Override public String subtitle(){ return "Kelola akun & data pengguna"; }

    @Override public Node view(){
        TextField cari=Ui.tf(""); cari.setPromptText("User ID / nama / posisi…"); cari.setPrefWidth(260);
        TableView<Map<String,Object>> tbl=Ui.table();
        Runnable refresh=()->{
            String q="%"+cari.getText().toLowerCase()+"%";
            tbl.setItems(FXCollections.observableArrayList(Db.query(
                "SELECT * FROM users WHERE is_deleted=0 AND (lower(kode) LIKE ? OR lower(nama) LIKE ? OR lower(level) LIKE ?) ORDER BY kode",q,q,q)));
        };
        cari.textProperty().addListener((o,ov,nv)->refresh.run());

        var cLevel=Ui.colF("Level",m->Ui.str(m.get("level")),90);
        Ui.badgeCol(cLevel,v->v.equals("admin")?"bdg-w":"bdg-a");
        var act=Ui.actCol(
            u->{ form(u); refresh.run(); },
            u->{
                if(Ui.str(u.get("kode")).equals(Ui.str(AdminApp.user.get("kode")))){ Toast.bad("Tidak bisa menghapus akun yang sedang login"); return; }
                Dlg.confirm(AdminApp.stage,"Hapus User","User \""+u.get("nama")+"\" akan dihapus (beserta data absensi & shift-nya di laporan).",()->{
                    Db.exec("UPDATE users SET is_deleted=1, dirty=1, updated_at=? WHERE id=?",System.currentTimeMillis(),u.get("id"));
                    Toast.ok("User dihapus"); refresh.run();
                });
            });

        tbl.getColumns().addAll(
            Ui.col("User ID","kode",90),
            Ui.col("Nama","nama",190),
            cLevel,
            Ui.colF("Password",m->"••••••",110),
            Ui.col("Nomor HP","no_hp",130),
            Ui.col("Alamat","alamat",220),
            act);

        HBox toolbar=new HBox(10,cari,Ui.spacer(),Ui.btn("+ User","btn-acc",()->{ form(null); refresh.run(); }));
        refresh.run();
        return new VBox(12,toolbar,tbl);
    }

    private void form(Map<String,Object> u){
        TextField nama=Ui.tf(u==null?"":Ui.str(u.get("nama")));
        ComboBox<String> level=Ui.combo("kasir","admin");
        if(u!=null) level.getSelectionModel().select(Ui.str(u.get("level")));
        PasswordField pw=Ui.pf();
        pw.setPromptText(u==null?"Password (min 4 karakter)":"Kosongkan jika tidak diubah");
        TextField hp=Ui.tf(u==null?"":Ui.str(u.get("no_hp")));
        TextArea al=Ui.ta(u==null?"":Ui.str(u.get("alamat")));
        Dlg.modal(AdminApp.stage,u==null?"Tambah User":"Edit User — "+u.get("kode"),440,dlg->{
            var g=Ui.grid();
            Ui.frow(g,0,"Nama",nama);
            Ui.frow(g,1,"Level",level);
            Ui.frow(g,2,"Password",pw);
            Ui.frow(g,3,"Nomor HP",hp);
            Ui.frow(g,4,"Alamat",al);
            HBox btns=new HBox(10,Ui.spacer(),
                Ui.btn("Batal","btn-line",dlg::close),
                Ui.btn("Simpan","btn-acc",()->{
                    String n=nama.getText().trim(), lv=level.getSelectionModel().getSelectedItem(), p=pw.getText();
                    if(n.isEmpty()){ Toast.bad("Nama user wajib diisi"); return; }
                    if(u==null&&p.length()<4){ Toast.bad("Password minimal 4 karakter"); return; }
                    if(u!=null&&!p.isEmpty()&&p.length()<4){ Toast.bad("Password baru minimal 4 karakter"); return; }
                    long now=System.currentTimeMillis();
                    if(u==null){
                        Db.exec("INSERT INTO users(id,kode,nama,level,password_hash,no_hp,alamat,created_at,updated_at,is_deleted,dirty) VALUES(?,?,?,?,?,?,?,?,?,0,1)",
                            Uuid.rand(),nextKode(lv),n,lv,BCrypt.hashpw(p,BCrypt.gensalt()),hp.getText().trim(),al.getText().trim(),now,now);
                    }else if(p.isEmpty()){
                        Db.exec("UPDATE users SET nama=?,level=?,no_hp=?,alamat=?,updated_at=?,dirty=1 WHERE id=?",
                            n,lv,hp.getText().trim(),al.getText().trim(),now,u.get("id"));
                    }else{
                        Db.exec("UPDATE users SET nama=?,level=?,password_hash=?,no_hp=?,alamat=?,updated_at=?,dirty=1 WHERE id=?",
                            n,lv,BCrypt.hashpw(p,BCrypt.gensalt()),hp.getText().trim(),al.getText().trim(),now,u.get("id"));
                    }
                    if(u!=null&&Ui.str(u.get("id")).equals(Ui.str(AdminApp.user.get("id"))))
                        AdminApp.user=Db.query("SELECT * FROM users WHERE id=?",u.get("id")).get(0);
                    Toast.ok("User tersimpan"); dlg.close();
                }));
            btns.setAlignment(Pos.CENTER_RIGHT);
            return new VBox(10,g,btns);
        });
    }

    private String nextKode(String level){
        String pre=level.equals("admin")?"ADM":"KSR";
        int max=0;
        for(var m:Db.query("SELECT kode FROM users WHERE kode LIKE ?",pre+"%")){
            try{ max=Math.max(max,Integer.parseInt(Ui.str(m.get("kode")).substring(3))); }catch(Exception ignore){}
        }
        return pre+String.format("%02d",max+1);
    }
}