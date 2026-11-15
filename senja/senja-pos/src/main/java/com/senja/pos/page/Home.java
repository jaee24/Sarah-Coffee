package com.senja.pos.page;

import com.senja.core.Const;
import com.senja.core.Db;
import com.senja.pos.PosApp;
import com.senja.pos.Ui;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

import java.time.LocalDate;
import java.time.ZoneId;

public class Home {

    public Node view(){
        long dayStart=LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long dayEnd=dayStart+86400000L;
        String uid=Ui.str(PosApp.user.get("id"));

        long terjual=Ui.lng(Db.query(
                "SELECT COALESCE(SUM(d.qty),0) n FROM transaksi_detail d "
              + "JOIN transaksi t ON t.id=d.transaksi_id "
              + "WHERE t.is_deleted=0 AND d.is_deleted=0 AND t.waktu>=? AND t.waktu<?",
                dayStart,dayEnd).get(0).get("n"));

        var q2=Db.query("SELECT COUNT(*) n, COALESCE(SUM(total),0) om FROM transaksi "
              + "WHERE is_deleted=0 AND user_id=? AND waktu>=? AND waktu<?",
              uid,dayStart,dayEnd).get(0);
        long trxSaya=Ui.lng(q2.get("n")), omzetSaya=Ui.lng(q2.get("om"));

        var sh=Db.query("SELECT jenis FROM shift WHERE user_id=? AND tanggal=? AND is_deleted=0",
              uid,LocalDate.now().toString());
        String shiftTxt=sh.isEmpty()?"—"
              :Ui.str(sh.get(0).get("jenis"))+" · "+Const.SHIFT_LABEL.get(Ui.str(sh.get(0).get("jenis")));

        HBox stats=new HBox(12);
        VBox s1=stat("PRODUK TERJUAL HARI INI",String.valueOf(terjual),"semua kasir",true);
        VBox s2=stat("OMZET SAYA HARI INI",Ui.money(omzetSaya),trxSaya+" transaksi",false);
        VBox s3=stat("SHIFT SAYA",shiftTxt,sh.isEmpty()?"tidak terjadwal":"hari ini",false);
        for(VBox s:new VBox[]{s1,s2,s3}) HBox.setHgrow(s,Priority.ALWAYS);
        stats.getChildren().addAll(s1,s2,s3);

        /* 2 kolom: kiri = Semua Kategori + Cemilan | kanan = Minuman (5) */
        VBox kiri=new VBox(14,
                sellers("Paling Laris — Semua Kategori","",5),
                sellers("Paling Laris — Kategori Cemilan","WHERE k.nama='Cemilan'",5));
        VBox kanan=new VBox(14,
                sellers("Paling Laris — Kategori Minuman","WHERE k.jenis='minuman'",5));
        HBox cols=new HBox(14,kiri,kanan);
        HBox.setHgrow(kiri,Priority.ALWAYS);
        HBox.setHgrow(kanan,Priority.ALWAYS);

        return new VBox(14,stats,cols);
    }

    private VBox stat(String cap,String val,String sub,boolean big){
        Label v=new Label(val);
        v.getStyleClass().add(big?"stat-big":"stat-mid");
        VBox s=new VBox(2,Ui.label(cap,"stat-cap"),v,Ui.label(sub,"ph-sub"));
        s.getStyleClass().add("stat");
        return s;
    }

    private Node sellers(String title,String where,int limit){
        // query pakai string concat biasa — JANGAN text block (membuang spasi akhir baris → SQL rusak)
        String sql=
              "SELECT p.id,p.nama,p.gambar,SUM(d.qty) qty FROM transaksi_detail d "
            + "JOIN transaksi t ON t.id=d.transaksi_id AND t.is_deleted=0 "
            + "JOIN produk p ON p.id=d.produk_id AND p.is_deleted=0 AND p.status='aktif' "
            + "JOIN kategori k ON k.id=p.kategori_id "+where
            + " GROUP BY p.id ORDER BY qty DESC LIMIT "+limit;
        var rows=Db.query(sql);
        if(rows.isEmpty()) return Ui.card(Ui.h3(title),Ui.label("Belum ada data penjualan","mut"));

        FlowPane fp=new FlowPane(10,10);
        int rank=1;
        for(var m:rows){
            VBox tile=new VBox(5);
            tile.getStyleClass().add("tile");
            StackPane imgW=new StackPane();
            imgW.getStyleClass().add("tile-img-wrap");
            imgW.setMinSize(96,96); imgW.setPrefSize(96,96);
            imgW.getChildren().add(Ui.imgRounded(Ui.str(m.get("gambar")),96,12));
            Label r=new Label(String.valueOf(rank));
            r.getStyleClass().addAll("bdg",rank==1?"bdg-a":"bdg-mut");
            StackPane.setAlignment(r,Pos.TOP_LEFT);
            StackPane.setMargin(r,new javafx.geometry.Insets(5));
            imgW.getChildren().add(r);
            Label nm=new Label(Ui.str(m.get("nama")));
            nm.getStyleClass().add("tile-n"); nm.setWrapText(true); nm.setMaxWidth(108);
            nm.setTextAlignment(TextAlignment.CENTER); nm.setAlignment(Pos.CENTER);
            tile.getChildren().addAll(imgW,nm,Ui.label(Ui.num(Ui.dbl(m.get("qty")))+" terjual","tile-p"));
            fp.getChildren().add(tile);
            rank++;
        }
        return Ui.card(Ui.h3(title),fp);
    }
}