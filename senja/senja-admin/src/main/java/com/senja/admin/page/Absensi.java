package com.senja.admin.page;

import com.senja.admin.*;
import com.senja.core.Db;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Absensi implements Page {
    @Override public String id(){ return "absensi"; }
    @Override public String title(){ return "Absensi"; }
    @Override public String subtitle(){ return "Rekap kehadiran pegawai"; }

    @Override public Node view(){
        TextField cari=Ui.tf(""); cari.setPromptText("ID / nama pegawai…"); cari.setPrefWidth(220);
        DatePicker dari=Ui.dp(LocalDate.now().minusDays(6));
        DatePicker ke=Ui.dp(LocalDate.now());
        TableView<Map<String,Object>> tbl=Ui.table();
        Runnable refresh=()->{
            StringBuilder w=new StringBuilder("a.is_deleted=0");
            List<Object> args=new ArrayList<>();
            if(!cari.getText().isBlank()){
                String q="%"+cari.getText().toLowerCase()+"%";
                w.append(" AND (lower(u.nama) LIKE ? OR lower(u.kode) LIKE ?)"); args.add(q); args.add(q);
            }
            if(dari.getValue()!=null){ w.append(" AND a.tanggal>=?"); args.add(dari.getValue().toString()); }
            if(ke.getValue()!=null){ w.append(" AND a.tanggal<=?"); args.add(ke.getValue().toString()); }
            tbl.setItems(FXCollections.observableArrayList(Db.query(
                "SELECT a.*, u.kode ukode, u.nama unama FROM absensi a LEFT JOIN users u ON u.id=a.user_id WHERE "+w+
                " ORDER BY a.tanggal DESC, ukode",args.toArray())));
        };
        cari.textProperty().addListener((o,ov,nv)->refresh.run());
        dari.valueProperty().addListener((o,ov,nv)->refresh.run());
        ke.valueProperty().addListener((o,ov,nv)->refresh.run());

        var cShift=Ui.colF("Shift",m->Ui.str(m.get("shift_jenis")),70);
        Ui.badgeCol(cShift,v->v.equals("P")?"bdg-w":"bdg-d");
        var cStat=Ui.colF("Status",m->Ui.str(m.get("status")),100);
        Ui.badgeCol(cStat,v->v.equals("hadir")?"bdg-ok":v.equals("terlambat")?"bdg-w":"bdg-bad");

        tbl.getColumns().addAll(
            Ui.col("ID Pegawai","ukode",110),
            Ui.col("Nama Pegawai","unama",210),
            Ui.colF("Tanggal",m->Ui.fmtD(Ui.str(m.get("tanggal"))),130),
            cShift,
            Ui.col("Jam Masuk","masuk",100),
            Ui.col("Jam Pulang","pulang",100),
            cStat);

        HBox toolbar=new HBox(10,cari,dari,Ui.label("–","mut"),ke,
            Ui.btn("Reset","btn-line btn-sm",()->{ cari.clear(); dari.setValue(LocalDate.now().minusDays(6)); ke.setValue(LocalDate.now()); }));
        refresh.run();
        return new VBox(12,toolbar,tbl);
    }
}