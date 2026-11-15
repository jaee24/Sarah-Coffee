package com.senja.pos.page;

import com.senja.core.Const;
import com.senja.core.Db;
import com.senja.core.Uuid;
import com.senja.pos.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Shift {

    public Node view(){
        String today=LocalDate.now().toString();
        int nowMin=LocalTime.now().getHour()*60+LocalTime.now().getMinute();

        VBox box=new VBox(12);
        HBox cards=new HBox(12);
        for(String j:new String[]{"P","S"}) cards.getChildren().add(shiftCard(j,today,nowMin));
        HBox.setHgrow(cards.getChildren().get(0),javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(cards.getChildren().get(1),javafx.scene.layout.Priority.ALWAYS);
        box.getChildren().addAll(cards,absenBersama(today,nowMin));
        return box;
    }

    private String statusOf(String j,int nowMin){
        int s=toMin(Const.SHIFT_HOURS.get(j)[0]), e=toMin(Const.SHIFT_HOURS.get(j)[1]);
        return nowMin<s?"Belum Dimulai":(nowMin<e?"Berlangsung":"Selesai");
    }

    private VBox shiftCard(String j,String today,int nowMin){
        String st=statusOf(j,nowMin);
        var members=Db.query("""
            SELECT s.user_id,u.kode,u.nama FROM shift s
            JOIN users u ON u.id=s.user_id AND u.is_deleted=0
            WHERE s.is_deleted=0 AND s.tanggal=? AND s.jenis=? ORDER BY u.kode""",today,j);
        VBox c=Ui.card(
            new HBox(10,Ui.h3("Shift "+j+" · "+Const.SHIFT_LABEL.get(j)),
                Ui.spacer(),
                Ui.label(st,"bdg "+(st.equals("Berlangsung")?"bdg-ok":st.equals("Selesai")?"bdg-d":"bdg-mut"))),
            Ui.label(Const.SHIFT_HOURS.get(j)[0]+" – "+Const.SHIFT_HOURS.get(j)[1]
                +"  ·  "+members.size()+" pegawai","ph-sub"));
        if(members.isEmpty()){
            c.getChildren().add(Ui.label("Tidak ada pegawai terjadwal.","mut"));
            return c;
        }
        for(var m:members){
            var a=Db.query("SELECT * FROM absensi WHERE user_id=? AND tanggal=? AND shift_jenis=? AND is_deleted=0",
                    m.get("user_id"),today,j);
            String abs;
            if(a.isEmpty()||Ui.str(a.get(0).get("masuk")).isBlank()) abs="Belum Check-in";
            else if(!Ui.str(a.get(0).get("pulang")).isBlank()) abs="Check-out "+a.get(0).get("pulang");
            else abs="Check-in "+a.get(0).get("masuk");
            c.getChildren().add(new HBox(10,
                Ui.label(Ui.str(m.get("nama"))+"  ("+m.get("kode")+")"),
                Ui.spacer(),
                Ui.label(abs,"bdg "+(abs.startsWith("Check-out")?"bdg-ok":abs.startsWith("Check-in")?"bdg-a":"bdg-mut"))));
        }
        return c;
    }

    private VBox absenBersama(String today,int nowMin){
        ComboBox<Ui.ComboItem> peg=new ComboBox<>();
        peg.getStyleClass().add("inp"); peg.setMaxWidth(Double.MAX_VALUE);
        for(var s:Db.query("""
                SELECT s.user_id,s.jenis,u.kode,u.nama FROM shift s
                JOIN users u ON u.id=s.user_id AND u.is_deleted=0
                WHERE s.is_deleted=0 AND s.tanggal=? ORDER BY u.kode""",today))
            peg.getItems().add(new Ui.ComboItem(Ui.str(s.get("user_id"))+"|"+Ui.str(s.get("jenis")),
                    Ui.str(s.get("nama"))+" ("+s.get("kode")+") — Shift "+s.get("jenis")));
        PasswordField pw=Ui.pf(); pw.setPromptText("Password pribadi");

        Runnable[] reRender=new Runnable[1];
        Button masuk=Ui.btn("Absen Masuk","btn-acc",()->absen(peg,pw,"masuk",reRender));
        Button pulang=Ui.btn("Absen Pulang","btn-line",()->absen(peg,pw,"pulang",reRender));
        masuk.setMaxWidth(Double.MAX_VALUE); pulang.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(masuk,javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(pulang,javafx.scene.layout.Priority.ALWAYS);

        VBox c=Ui.card(Ui.h3("Check-in / Check-out Bersama"));
        if(peg.getItems().isEmpty()){
            c.getChildren().add(Ui.label("Belum ada jadwal shift hari ini — hubungi admin.","mut"));
            return c;
        }
        peg.getSelectionModel().select(0);
        c.getChildren().addAll(peg,pw,new HBox(10,masuk,pulang),
            Ui.label("Absen masuk dibuka 30 menit sebelum jam mulai; sekali tercatat tidak dapat diubah. Absen pulang aktif setelah jam selesai shift.","mut"));
        return c;
    }

    private void absen(ComboBox<Ui.ComboItem> peg,PasswordField pw,String aksi,Runnable[] reRender){
        Ui.ComboItem sel=peg.getValue();
        if(sel==null){ Toast.bad("Pilih pegawai"); return; }
        String[] uidJenis=sel.id.split("\\|");
        String uid=uidJenis[0], jenis=uidJenis[1];
        var u=Db.query("SELECT * FROM users WHERE id=? AND is_deleted=0",uid);
        if(u.isEmpty()){ Toast.bad("Pegawai tidak ditemukan"); return; }
        if(!BCrypt.checkpw(pw.getText(),Ui.str(u.get(0).get("password_hash")))){
            Toast.bad("Verifikasi gagal — password salah"); return;
        }
        String nama=Ui.str(u.get(0).get("nama"));
        String today=LocalDate.now().toString();
        int nowMin=LocalTime.now().getHour()*60+LocalTime.now().getMinute();
        int s=toMin(Const.SHIFT_HOURS.get(jenis)[0]), e=toMin(Const.SHIFT_HOURS.get(jenis)[1]);
        var rec=Db.query("SELECT * FROM absensi WHERE user_id=? AND tanggal=? AND shift_jenis=? AND is_deleted=0",uid,today,jenis);
        String jam=LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
        long now=System.currentTimeMillis();

        if(aksi.equals("masuk")){
            if(!rec.isEmpty()&&!Ui.str(rec.get(0).get("masuk")).isBlank()){
                Toast.bad(nama+": absen masuk sudah tercatat "+rec.get(0).get("masuk")); return;
            }
            if(nowMin<s-30){ Toast.bad("Absen masuk shift "+jenis+" dibuka pukul "+
                    Const.SHIFT_HOURS.get(jenis)[0]+" (30 menit sebelumnya)"); return; }
            if(nowMin>=e){ Toast.bad("Shift "+jenis+" sudah selesai"); return; }
            String status=nowMin>s?"terlambat":"hadir";
            if(!rec.isEmpty()){
                Db.exec("UPDATE absensi SET masuk=?,status=?,updated_at=?,dirty=1 WHERE id=?",jam,status,now,rec.get(0).get("id"));
            }else{
                Db.exec("INSERT INTO absensi(id,user_id,tanggal,shift_jenis,masuk,pulang,status,created_at,updated_at,is_deleted,dirty) VALUES(?,?,?,?,?,?,NULL,?,?,?,1)",
                        Uuid.rand(),uid,today,jenis,jam,status,now,now,0);
            }
            Toast.ok(nama+" absen masuk "+jam+(status.equals("terlambat")?" (terlambat)":""));
        }else{
            if(rec.isEmpty()||Ui.str(rec.get(0).get("masuk")).isBlank()){
                Toast.bad("Belum check-in — absen pulang hanya bisa setelah check-in"); return;
            }
            if(!Ui.str(rec.get(0).get("pulang")).isBlank()){
                Toast.bad(nama+": absen pulang sudah tercatat "+rec.get(0).get("pulang")); return;
            }
            if(nowMin<e){ Toast.bad("Absen pulang shift "+jenis+" baru aktif pukul "+Const.SHIFT_HOURS.get(jenis)[1]); return; }
            Db.exec("UPDATE absensi SET pulang=?,updated_at=?,dirty=1 WHERE id=?",jam,now,rec.get(0).get("id"));
            Toast.ok(nama+" absen pulang "+jam);
        }
        pw.clear();
        PosApp.go("shift"); // re-render halaman
    }

    private static int toMin(String s){
        String[] p=s.split(":");
        return Integer.parseInt(p[0])*60+Integer.parseInt(p[1]);
    }
}