package com.senja.admin.page;

import com.senja.admin.*;
import com.senja.core.Db;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Penjualan implements Page {
    @Override public String id(){ return "penjualan"; }
    @Override public String title(){ return "Penjualan"; }
    @Override public String subtitle(){ return "Riwayat transaksi semua kasir"; }

    @Override public Node view(){
        TextField cari=Ui.tf(""); cari.setPromptText("ID transaksi / nama kasir…"); cari.setPrefWidth(230);
        DatePicker dari=new DatePicker(); dari.setPromptText("Dari"); dari.getStyleClass().add("inp"); dari.setPrefWidth(150);
        DatePicker ke=new DatePicker(); ke.setPromptText("Sampai"); ke.getStyleClass().add("inp"); ke.setPrefWidth(150);
        TableView<Map<String,Object>> tbl=Ui.table();

        Runnable refresh=()->{
            StringBuilder w=new StringBuilder("t.is_deleted=0");
            List<Object> args=new ArrayList<>();
            if(!cari.getText().isBlank()){
                String q="%"+cari.getText().toLowerCase()+"%";
                w.append(" AND (lower(t.kode) LIKE ? OR lower(u.nama) LIKE ?)"); args.add(q); args.add(q);
            }
            if(dari.getValue()!=null){ w.append(" AND t.waktu>=?"); args.add(dari.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()); }
            if(ke.getValue()!=null){ w.append(" AND t.waktu<?"); args.add(ke.getValue().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()); }
            tbl.setItems(FXCollections.observableArrayList(Db.query(
                "SELECT t.*, u.nama unama FROM transaksi t LEFT JOIN users u ON u.id=t.user_id WHERE "+w+
                " ORDER BY t.waktu DESC",args.toArray())));
        };
        cari.textProperty().addListener((o,ov,nv)->refresh.run());
        dari.valueProperty().addListener((o,ov,nv)->refresh.run());
        ke.valueProperty().addListener((o,ov,nv)->refresh.run());

        tbl.setRowFactory(tv->{
            TableRow<Map<String,Object>> row=new TableRow<>();
            row.setOnMouseClicked(e->{ if(!row.isEmpty()) detail(row.getItem()); });
            return row;
        });

        var cMet=Ui.colF("Metode",m->Ui.str(m.get("metode")).toUpperCase(),90);
        Ui.badgeCol(cMet,v->"bdg-mut");
        tbl.getColumns().addAll(
            Ui.col("ID Transaksi","kode",170),
            Ui.colF("Waktu",m->Ui.fmtDT(Ui.lng(m.get("waktu"))),180),
            Ui.col("Kasir","unama",170),
            cMet,
            Ui.colF("Total",m->Ui.money(Ui.lng(m.get("total"))),130));

        HBox toolbar=new HBox(10,cari,dari,Ui.label("–","mut"),ke,
            Ui.btn("Reset","btn-line btn-sm",()->{ cari.clear(); dari.setValue(null); ke.setValue(null); }));
        refresh.run();
        VBox root=new VBox(12,toolbar,tbl,Ui.label("Klik baris transaksi untuk melihat detail & cetak struk.","mut"));
        return root;
    }

    public void detail(Map<String,Object> t){
        var det=Db.query("SELECT * FROM transaksi_detail WHERE transaksi_id=? AND is_deleted=0",t.get("id"));
        Dlg.modal(AdminApp.stage,"Detail Transaksi — "+t.get("kode"),600,dlg->{
            TableView<Map<String,Object>> it=Ui.table();
            it.setFixedCellSize(30);
            it.getColumns().addAll(
                Ui.col("Produk","nama",170),
                Ui.colF("Opsi",m->{ var o=Ui.str(m.get("opsi")); return o.isBlank()?"—":o; },170),
                Ui.colF("Qty",m->String.valueOf(Ui.lng(m.get("qty"))),50),
                Ui.colF("Harga",m->Ui.money(Ui.lng(m.get("harga"))),100),
                Ui.colF("Subtotal",m->Ui.money(Ui.lng(m.get("subtotal"))),100));
            it.setItems(FXCollections.observableArrayList(det));
            it.setPrefHeight(Math.min(320,36+det.size()*30));

            var tot=Ui.grid();
            int r=0;
            totRow(tot,r++,"Subtotal",Ui.money(Ui.lng(t.get("subtotal"))));
            if(Ui.lng(t.get("diskon"))>0) totRow(tot,r++,"Diskon "+Ui.str(t.get("diskon_kode")),"−"+Ui.money(Ui.lng(t.get("diskon"))));
            totRow(tot,r++,"Total",Ui.money(Ui.lng(t.get("total"))));
            totRow(tot,r++,"Dibayar",Ui.money(Ui.lng(t.get("dibayar"))));
            totRow(tot,r++,"Kembalian",Ui.money(Ui.lng(t.get("kembalian"))));

            HBox btns=new HBox(10,Ui.spacer(),
                Ui.btn("Tutup","btn-line",dlg::close),
                Ui.btn("Cetak Struk","btn-acc",()->print(t,det)));
            return new VBox(10,it,tot,btns);
        });
    }

    private void totRow(GridPane g,int r,String l,String v){
        g.add(Ui.label(l,"flbl"),0,r);
        Label b=new Label(v); b.getStyleClass().add("h3");
        g.add(b,1,r);
    }

    /* ---------- struk ---------- */
    private VBox receipt(Map<String,Object> t,List<Map<String,Object>> det){
        VBox r=new VBox(1);
        r.getStyleClass().add("receipt");
        r.setPrefWidth(300);
        r.getChildren().addAll(
            rc(str(AdminApp.pengaturanVal("toko_nama")),"rc rc-b rc-c"),
            rc(str(AdminApp.pengaturanVal("toko_alamat")),"rc rc-c"),
            rc(str(AdminApp.pengaturanVal("toko_telp")),"rc rc-c"),
            dash(),
            rrow("No",Ui.str(t.get("kode"))),
            rrow("Tanggal",Ui.fmtDT(Ui.lng(t.get("waktu")))),
            rrow("Kasir",Ui.str(t.get("unama"))),
            dash());
        for(var d:det){
            VBox item=new VBox(0);
            item.getChildren().add(rc(Ui.str(d.get("nama"))+" x"+Ui.lng(d.get("qty")),"rc"));
            if(!Ui.str(d.get("opsi")).isBlank()) item.getChildren().add(rc("  "+Ui.str(d.get("opsi")),"rc"));
            Label pr=rc(Ui.num(Ui.lng(d.get("subtotal"))),"rc");
            pr.setMaxWidth(Double.MAX_VALUE); pr.setAlignment(Pos.CENTER_RIGHT);
            item.getChildren().add(pr);
            r.getChildren().add(item);
        }
        r.getChildren().add(dash());
        r.getChildren().add(rrow("Subtotal",Ui.num(Ui.lng(t.get("subtotal")))));
        if(Ui.lng(t.get("diskon"))>0) r.getChildren().add(rrow("Diskon "+Ui.str(t.get("diskon_kode")),"-"+Ui.num(Ui.lng(t.get("diskon")))));
        r.getChildren().add(rrow("TOTAL","Rp "+Ui.num(Ui.lng(t.get("total")))));
        r.getChildren().add(dash());
        r.getChildren().add(rrow("Metode",Ui.str(t.get("metode")).toUpperCase()));
        r.getChildren().add(rrow("Dibayar",Ui.num(Ui.lng(t.get("dibayar")))));
        r.getChildren().add(rrow("Kembali",Ui.num(Ui.lng(t.get("kembalian")))));
        r.getChildren().add(dash());
        r.getChildren().add(rc(str(AdminApp.pengaturanVal("struk_footer")),"rc rc-c"));
        return r;
    }
    private String str(String s){ return s==null?"":s; }
    private Label rc(String s,String cls){
        Label l=new Label(s);
        for(String c:cls.split(" ")) l.getStyleClass().add(c);
        return l;
    }
    private Label dash(){ return rc("--------------------------------","rc"); }
    private HBox rrow(String l,String rv){
        return new HBox(8,rc(l,"rc"),Ui.spacer(),rc(rv,"rc"));
    }

    private void print(Map<String,Object> t,List<Map<String,Object>> det){
        VBox rc=receipt(t,det);
        // tempel ke scene sementara agar CSS diterapkan sebelum dicetak
        Scene tmp=new Scene(rc);
        tmp.getStylesheets().add(AdminApp.cssUrl());
        rc.applyCss(); rc.layout();
        PrinterJob job=PrinterJob.createPrinterJob();
        if(job==null){ Toast.bad("Tidak ada printer yang tersedia di sistem"); return; }
        if(job.showPrintDialog(AdminApp.stage)){
            if(job.printPage(rc)){ job.endJob(); Toast.ok("Struk dikirim ke printer"); }
            else Toast.bad("Gagal mencetak struk");
        }
    }
}