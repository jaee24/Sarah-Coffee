package com.senja.pos.page;

import com.senja.core.*;
import com.senja.pos.*;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Map;

public class Pengaturan {

    public Node view(){

        /* Tema */
        Button light=Ui.btn("Light","chip"+(PosApp.dark?"":" on"),()->{ PosApp.setDark(false); PosApp.go("pengaturan"); });
        Button darkB=Ui.btn("Dark","chip"+(PosApp.dark?" on":""),()->{ PosApp.setDark(true); PosApp.go("pengaturan"); });
        VBox theme=Ui.card(Ui.h3("Tampilan"),Ui.label("Tersimpan di database bersama ; berlaku juga di aplikasi Admin.","mut"),
                new HBox(10,light,darkB));

        /* Koneksi */
        TextField server=Ui.tf(SyncMeta.get("server_url","http://localhost:8080"));
        Button cek=Ui.btn("Cek Koneksi","btn-line btn-sm",()->{
            Toast.show(ApiClient.online()?"Server terjangkau ✓":"Server tidak terjangkau (mode offline)",ApiClient.online());
        });
        VBox conn=Ui.card(Ui.h3("Koneksi & Sinkronisasi"),
                Ui.label("Device: "+(SyncMeta.get("device_id")==null?"belum terdaftar":SyncMeta.get("device_id")),"mut"),
                new HBox(8,server,cek),
                Ui.btn("Simpan URL","btn-line btn-sm",()->{ SyncMeta.set("server_url",server.getText().trim()); Toast.ok("URL server disimpan"); }),
                Ui.btn("Sinkron Sekarang","btn-acc",PosApp::syncAsync));

        /* Akun */
        var u=Db.query("SELECT * FROM users WHERE id=?",PosApp.user.get("id")).get(0);
        TextField hp=Ui.tf(Ui.str(u.get("no_hp")));
        PasswordField po=Ui.pf(), pn=Ui.pf(), pc=Ui.pf();
        po.setPromptText("Password lama"); pn.setPromptText("Password baru"); pc.setPromptText("Konfirmasi");
        VBox acc=Ui.card(Ui.h3("Akun ; "+Ui.str(u.get("nama"))),
                new HBox(8,Ui.label("Nomor HP","flbl"),hp),
                new HBox(8,po,pn,pc),
                Ui.btn("Simpan Perubahan","btn-acc",()->{
                    long now=System.currentTimeMillis();
                    if(po.getText().isEmpty()&&pn.getText().isEmpty()&&pc.getText().isEmpty()){
                        Db.exec("UPDATE users SET no_hp=?,updated_at=?,dirty=1 WHERE id=?",hp.getText().trim(),now,u.get("id"));
                        Toast.ok("Nomor HP disimpan"); return;
                    }
                    if(!BCrypt.checkpw(po.getText(),Ui.str(u.get("password_hash")))){ Toast.bad("Password lama salah"); return; }
                    if(pn.getText().length()<4){ Toast.bad("Password baru minimal 4 karakter"); return; }
                    if(!pn.getText().equals(pc.getText())){ Toast.bad("Konfirmasi password tidak sama"); return; }
                    String hash=BCrypt.hashpw(pn.getText(),BCrypt.gensalt());
                    Db.exec("UPDATE users SET no_hp=?,password_hash=?,updated_at=?,dirty=1 WHERE id=?",
                            hp.getText().trim(),hash,now,u.get("id"));
                    // coba samakan di server bila online
                    if(ApiClient.hasToken()){
                        new Thread(()->{
                            try{ ApiClient.post("/api/auth/password",Map.of(
                                    "password_lama",po.getText(),"password_baru",pn.getText())); }
                            catch(Exception ignore){}
                        },"pw-sync").start();
                    }
                    Toast.ok("Perubahan akun disimpan");
                    PosApp.user=Db.query("SELECT * FROM users WHERE id=?",u.get("id")).get(0);
                }));

        /* Lapor bug */
        TextField judul=Ui.tf(""); judul.setPromptText("Judul singkat masalah…");
        TextArea desk=Ui.ta(""); desk.setPromptText("Langkah memunculkan bug, pesan error, dll.");
        VBox bug=Ui.card(Ui.h3("Lapor Bug"),
                Ui.label("Tersimpan lokal & ikut tersinkron ke server.","mut"),
                judul,desk,
                Ui.btn("Kirim Laporan","btn-acc",()->{
                    if(judul.getText().trim().isEmpty()){ Toast.bad("Judul wajib diisi"); return; }
                    long now=System.currentTimeMillis();
                    Db.exec("INSERT INTO laporan_bug(id,app,user_id,judul,deskripsi,waktu,created_at,updated_at,is_deleted,dirty) VALUES(?,?,?,?,?,?,?,?,0,1)",
                            Uuid.rand(),"kasir",PosApp.user.get("id"),judul.getText().trim(),desk.getText().trim(),now,now,now);
                    judul.clear(); desk.clear();
                    Toast.ok("Laporan bug terkirim. Terima kasih!");
                }));

        return new VBox(12,theme,conn,acc,bug);
    }
}