package com.senja.admin.page;

import com.senja.admin.*;
import com.senja.core.Db;
import com.senja.core.Uuid;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class Shift implements Page {
    @Override public String id(){ return "shift"; }
    @Override public String title(){ return "Shift"; }
    @Override public String subtitle(){ return "Penjadwalan shift pegawai per 7 hari"; }

    private final VBox list=new VBox(12);
    private final TextField cari=new TextField();
    private final DatePicker tgl=new DatePicker();

    @Override public Node view(){
        cari.getStyleClass().add("inp"); cari.setPromptText("Nama pegawai…"); cari.setPrefWidth(200);
        tgl.getStyleClass().add("inp"); tgl.setPrefWidth(150); tgl.setPromptText("Filter tanggal");
        cari.textProperty().addListener((o,ov,nv)->render());
        tgl.valueProperty().addListener((o,ov,nv)->render());
        HBox toolbar=new HBox(10,cari,tgl,Ui.spacer(),Ui.btn("+ Shift","btn-acc",this::formTambah));
        render();
        return new VBox(12,toolbar,list);
    }

    private void render(){
        list.getChildren().clear();
        String q="%"+cari.getText().toLowerCase()+"%";
        boolean filterNama=!cari.getText().isBlank();
        var tanggalan=new TreeSet<String>();
        Db.query("SELECT DISTINCT tanggal FROM shift WHERE is_deleted=0")
          .forEach(m->tanggalan.add(Ui.str(m.get("tanggal"))));
        List<String> dates=new ArrayList<>(tanggalan);
        for(int i=0;i<dates.size();i+=7){
            List<String> ch=dates.subList(i,Math.min(dates.size(),i+7));
            List<Map<String,Object>> rows=Db.query("""
                SELECT s.*, u.kode ukode, u.nama unama FROM shift s
                JOIN users u ON u.id=s.user_id AND u.is_deleted=0
                WHERE s.is_deleted=0 AND s.tanggal>=? AND s.tanggal<=?
                  AND lower(u.nama) LIKE ?
                ORDER BY s.tanggal, u.kode""",ch.get(0),ch.get(ch.size()-1),q);
            if(tgl.getValue()!=null){
                String want=tgl.getValue().toString();
                rows=rows.stream().filter(r->want.equals(Ui.str(r.get("tanggal")))).toList();
            }
            if((filterNama||tgl.getValue()!=null)&&rows.isEmpty()) continue;
            VBox card=Ui.card(
                new HBox(10,Ui.label(Ui.fmtD(ch.get(0))+" — "+Ui.fmtD(ch.get(ch.size()-1)),"h3"),
                    Ui.label(rows.size()+" shift","bdg bdg-mut"),Ui.spacer(),
                    Ui.btn("Hapus Periode","btn-danger btn-sm",()->
                        Dlg.confirm(AdminApp.stage,"Hapus Periode",
                            "Semua shift pada "+Ui.fmtD(ch.get(0))+" — "+Ui.fmtD(ch.get(ch.size()-1))+" akan dihapus.",
                            ()->{
                                long now=System.currentTimeMillis();
                                for(String d:ch)
                                    Db.exec("UPDATE shift SET is_deleted=1,updated_at=?,dirty=1 WHERE tanggal=?",now,d);
                                Toast.ok("Periode shift dihapus"); render();
                            }))));
            for(var r:rows){
                String jenis=Ui.str(r.get("jenis"));
                Label bdg=new Label("P".equals(jenis)?"P · Pagi":"S · Sore");
                bdg.getStyleClass().addAll("bdg","P".equals(jenis)?"bdg-w":"bdg-d");
                HBox row=new HBox(12,
                    Ui.label(Ui.str(r.get("unama"))+"  ("+r.get("ukode")+")"),
                    Ui.spacer(),
                    Ui.label(Ui.fmtD(Ui.str(r.get("tanggal"))),"mut"),
                    bdg,
                    Ui.btn("Hapus","btn-line btn-sm",()->{
                        Db.exec("UPDATE shift SET is_deleted=1,updated_at=?,dirty=1 WHERE id=?",
                            System.currentTimeMillis(),r.get("id"));
                        Toast.ok("Entri shift dihapus"); render();
                    }));
                row.setAlignment(Pos.CENTER_LEFT);
                card.getChildren().add(row);
            }
            if(rows.isEmpty()) card.getChildren().add(Ui.label("Tidak ada entri pada periode ini.","mut"));
            list.getChildren().add(card);
        }
        if(list.getChildren().isEmpty())
            list.getChildren().add(Ui.card(Ui.label("Belum ada jadwal shift. Tambahkan dengan tombol + Shift.","mut")));
    }

    /* ==== picker pegawai: state di inner class, method bisa dipanggil dari lambda-nya sendiri ==== */
    private final class PegawaiPicker {
        final List<String> terpilih=new ArrayList<>();
        final TextField cariField=new TextField();
        final VBox daftar=new VBox(4);
        final VBox selBox=new VBox(4);

        PegawaiPicker(){
            cariField.getStyleClass().add("inp");
            cariField.setPromptText("ID / nama pegawai…");
            cariField.textProperty().addListener((o,ov,nv)->refresh());
            refresh();
        }

        void refresh(){
            daftar.getChildren().clear();
            String q="%"+cariField.getText().toLowerCase()+"%";
            boolean any=false;
            for(var u:Db.query("SELECT * FROM users WHERE is_deleted=0 AND level='kasir' AND (lower(nama) LIKE ? OR lower(kode) LIKE ?) ORDER BY kode",q,q)){
                String uid=Ui.str(u.get("id"));
                if(terpilih.contains(uid)) continue;
                any=true;
                daftar.getChildren().add(new HBox(10,
                    Ui.label(Ui.str(u.get("nama"))+"  ("+u.get("kode")+")"),
                    Ui.spacer(),
                    Ui.btn("Pilih","btn-line btn-sm",()->{ terpilih.add(uid); refresh(); })));
            }
            if(!any) daftar.getChildren().add(Ui.label("Pegawai sudah dipilih / tidak ditemukan.","mut"));

            selBox.getChildren().clear();
            if(terpilih.isEmpty()) selBox.getChildren().add(Ui.label("Belum ada pegawai dipilih.","mut"));
            for(String uid:terpilih){
                var u=Db.query("SELECT kode,nama FROM users WHERE id=?",uid).get(0);
                selBox.getChildren().add(new HBox(10,
                    Ui.label(u.get("nama")+"  ("+u.get("kode")+")"),
                    Ui.btn("Hapus","btn-line btn-sm",()->{ terpilih.remove(uid); refresh(); })));
            }
        }
    }

    /* ==== picker tanggal ==== */
    private final class TanggalPicker {
        final List<String> dates=new ArrayList<>();
        final VBox box=new VBox(4);
        final DatePicker dp=new DatePicker(LocalDate.now());

        TanggalPicker(){
            dp.getStyleClass().add("inp");
            refresh();
        }

        void add(){
            if(dp.getValue()==null) return;
            String d=dp.getValue().toString();
            if(!dates.contains(d)){ dates.add(d); dates.sort(String::compareTo); }
            refresh();
        }

        void remove(String d){ dates.remove(d); refresh(); }

        void refresh(){
            box.getChildren().clear();
            for(String d:dates)
                box.getChildren().add(new HBox(10,Ui.label(Ui.fmtD(d)),
                    Ui.btn("Hapus","btn-line btn-sm",()->remove(d))));
            if(dates.isEmpty()) box.getChildren().add(Ui.label("Belum ada tanggal.","mut"));
        }
    }

    private void formTambah(){
        PegawaiPicker peg=new PegawaiPicker();
        TanggalPicker tp=new TanggalPicker();
        ComboBox<String> jenis=Ui.combo("P · Pagi (10:00–16:30)","S · Sore (16:30–23:00)");

        Dlg.modal(AdminApp.stage,"Tambah Shift",520,dlg->{
            VBox b=Ui.card(Ui.h3("Cari & Pilih Pegawai"),peg.cariField,peg.daftar,
                           Ui.h3("Pegawai Terpilih"),peg.selBox);
            VBox t=Ui.card(Ui.h3("Jenis & Tanggal Shift"),jenis,
                new HBox(10,tp.dp,Ui.btn("Tambah Tanggal","btn-line btn-sm",tp::add)),
                tp.box);
            HBox btns=new HBox(10,Ui.spacer(),
                Ui.btn("Batal","btn-line",dlg::close),
                Ui.btn("Simpan","btn-acc",()->{
                    if(peg.terpilih.isEmpty()){ Toast.bad("Pilih minimal satu pegawai"); return; }
                    if(tp.dates.isEmpty()){ Toast.bad("Tambahkan minimal satu tanggal"); return; }
                    String j=jenis.getSelectionModel().getSelectedItem().startsWith("P")?"P":"S";
                    long now=System.currentTimeMillis();
                    for(String uid:peg.terpilih) for(String d:tp.dates){
                        var ex=Db.query("SELECT id FROM shift WHERE user_id=? AND tanggal=? AND is_deleted=0",uid,d);
                        if(!ex.isEmpty())
                            Db.exec("UPDATE shift SET jenis=?,updated_at=?,dirty=1 WHERE id=?",j,now,ex.get(0).get("id"));
                        else
                            Db.exec("INSERT INTO shift(id,user_id,tanggal,jenis,created_at,updated_at,is_deleted,dirty) VALUES(?,?,?,?,?,?,0,1)",
                                Uuid.rand(),uid,d,j,now,now);
                    }
                    Toast.ok("Jadwal shift tersimpan"); dlg.close(); render();
                }));
            btns.setAlignment(Pos.CENTER_RIGHT);
            return new VBox(10,b,t,btns);
        });
    }
}