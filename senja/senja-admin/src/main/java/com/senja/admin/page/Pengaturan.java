package com.senja.admin.page;

import com.senja.admin.*;
import com.senja.core.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.mindrot.jbcrypt.BCrypt;

public class Pengaturan implements Page {
    @Override public String id(){ return "pengaturan"; }
    @Override public String title(){ return "Pengaturan"; }
    @Override public String subtitle(){ return "Tema, sinkronisasi, & akun"; }

    @Override public Node view(){

        /* Tampilan */
        Button light=new Button("Light"), darkB=new Button("Dark");
        light.getStyleClass().add("chip"); darkB.getStyleClass().add("chip");
        Runnable mark=()->{
            light.getStyleClass().remove("on"); darkB.getStyleClass().remove("on");
            (AdminApp.dark?darkB:light).getStyleClass().add("on");
        };
        light.setOnAction(e->{ AdminApp.setDark(false); mark.run(); });
        darkB.setOnAction(e->{ AdminApp.setDark(true); mark.run(); });
        mark.run();
        VBox themeCard=Ui.card(Ui.h3("Tampilan"),
                Ui.label("Tema tersimpan ke database bersama — berlaku juga di aplikasi Kasir.","mut"),
                new HBox(10,light,darkB));

        /* Sinkronisasi */
        TextField server=Ui.tf(SyncMeta.get("server_url","http://localhost:8080"));
        String dev=SyncMeta.get("device_id");
        long last=SyncMeta.getLong("last_sync",0);
        VBox syncCard=Ui.card(Ui.h3("Sinkronisasi"),
                Ui.label("Device: "+(dev==null?"belum terdaftar (otomatis saat sinkron pertama)":dev),"mut"),
                Ui.label(last==0?"Belum pernah sinkron":"Sinkron terakhir: "+Ui.fmtDT(last),"mut"),
                new HBox(10,server,Ui.btn("Simpan","btn-line btn-sm",()->{
                    SyncMeta.set("server_url",server.getText().trim());
                    Toast.ok("URL server disimpan");
                })),
                Ui.btn("Sinkron sekarang","btn-acc",AdminApp::syncAsync));

        /* Akun */
        var u=Db.query("SELECT * FROM users WHERE id=?",AdminApp.user.get("id")).get(0);
        TextField hp=Ui.tf(Ui.str(u.get("no_hp")));
        PasswordField po=Ui.pf(), pn=Ui.pf(), pc=Ui.pf();
        po.setPromptText("Password lama"); pn.setPromptText("Password baru"); pc.setPromptText("Konfirmasi");
        VBox accCard=Ui.card(Ui.h3("Akun — "+Ui.str(u.get("nama"))),
                Ui.label("Perbarui nomor HP & password akunmu.","mut"),
                Ui.row(8,Ui.label("Nomor HP","flbl"),hp),
                new HBox(10,po,pn,pc),
                Ui.btn("Simpan Perubahan","btn-acc",()->{
                    boolean gantiPw=!po.getText().isEmpty()||!pn.getText().isEmpty()||!pc.getText().isEmpty();
                    long now=System.currentTimeMillis();
                    if(gantiPw){
                        if(!BCrypt.checkpw(po.getText(),Ui.str(u.get("password_hash")))){ Toast.bad("Password lama salah"); return; }
                        if(pn.getText().length()<4){ Toast.bad("Password baru minimal 4 karakter"); return; }
                        if(!pn.getText().equals(pc.getText())){ Toast.bad("Konfirmasi password tidak sama"); return; }
                        Db.exec("UPDATE users SET no_hp=?,password_hash=?,updated_at=?,dirty=1 WHERE id=?",
                            hp.getText().trim(),BCrypt.hashpw(pn.getText(),BCrypt.gensalt()),now,u.get("id"));
                    }else{
                        Db.exec("UPDATE users SET no_hp=?,updated_at=?,dirty=1 WHERE id=?",hp.getText().trim(),now,u.get("id"));
                    }
                    AdminApp.user=Db.query("SELECT * FROM users WHERE id=?",u.get("id")).get(0);
                    Toast.ok("Perubahan akun disimpan");
                }));

        /* Lapor bug */
        TextField judul=Ui.tf(""); judul.setPromptText("Judul singkat masalah…");
        TextArea desk=Ui.ta(""); desk.setPromptText("Langkah memunculkan bug, pesan error, dll.");
        VBox bugCard=Ui.card(Ui.h3("Lapor Bug"),
                Ui.label("Laporan tersimpan di database & ikut tersinkron.","mut"),
                judul,desk,
                Ui.btn("Kirim Laporan","btn-acc",()->{
                    if(judul.getText().trim().isEmpty()){ Toast.bad("Judul laporan wajib diisi"); return; }
                    long now=System.currentTimeMillis();
                    Db.exec("INSERT INTO laporan_bug(id,app,user_id,judul,deskripsi,waktu,created_at,updated_at,is_deleted,dirty) VALUES(?,?,?,?,?,?,?,?,0,1)",
                        Uuid.rand(),"admin",AdminApp.user.get("id"),judul.getText().trim(),desk.getText().trim(),now,now,now);
                    judul.clear(); desk.clear();
                    Toast.ok("Laporan bug terkirim. Terima kasih!");
                }));

        return new VBox(12,themeCard,syncCard,accCard,bugCard);
    }
}