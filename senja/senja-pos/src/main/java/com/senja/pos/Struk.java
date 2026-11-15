package com.senja.pos;

import com.senja.core.Db;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

public final class Struk {
    private Struk() {}

    public static void cetak(Map<String,Object> t,List<Map<String,Object>> det){
        VBox r=build(t,det);
        Scene tmp=new Scene(r);
        tmp.getStylesheets().add(PosApp.cssUrl());
        r.applyCss(); r.layout();
        PrinterJob job=PrinterJob.createPrinterJob();
        if(job==null){ Toast.bad("Tidak ada printer tersedia"); return; }
        if(job.showPrintDialog(PosApp.stage)){
            if(job.printPage(r)){ job.endJob(); Toast.ok("Struk dikirim ke printer"); }
            else Toast.bad("Gagal mencetak struk");
        }
    }

    public static VBox build(Map<String,Object> t,List<Map<String,Object>> det){
        VBox r=new VBox(1);
        r.getStyleClass().add("receipt");
        r.setPrefWidth(300);
        var kasir=Db.query("SELECT nama FROM users WHERE id=?",t.get("user_id"));
        r.getChildren().addAll(
                rc(set("toko_nama","SENJA COFFEE"),"rc rc-b rc-c"),
                rc(set("toko_alamat","Jl. Kenanga No. 12, Yogyakarta"),"rc rc-c"),
                rc(set("toko_telp","0812-3456-7890"),"rc rc-c"),
                dash(),
                rrow("No",Ui.str(t.get("kode"))),
                rrow("Tanggal",Ui.fmtDT(Ui.lng(t.get("waktu")))),
                rrow("Kasir",kasir.isEmpty()?"-":Ui.str(kasir.get(0).get("nama"))),
                dash());
        for(var d:det){
            VBox item=new VBox(0);
            item.getChildren().add(rc(Ui.str(d.get("nama"))+" x"+Ui.lng(d.get("qty")),"rc"));
            if(!Ui.str(d.get("opsi")).isBlank())
                item.getChildren().add(rc("  "+Ui.str(d.get("opsi")),"rc"));
            Label pr=rc(Ui.num(Ui.lng(d.get("subtotal"))),"rc");
            pr.setMaxWidth(Double.MAX_VALUE); pr.setAlignment(Pos.CENTER_RIGHT);
            item.getChildren().add(pr);
            r.getChildren().add(item);
        }
        r.getChildren().add(dash());
        r.getChildren().add(rrow("Subtotal",Ui.num(Ui.lng(t.get("subtotal")))));
        if(Ui.lng(t.get("diskon"))>0)
            r.getChildren().add(rrow("Diskon "+Ui.str(t.get("diskon_kode")),"-"+Ui.num(Ui.lng(t.get("diskon")))));
        r.getChildren().add(rrow("TOTAL","Rp "+Ui.num(Ui.lng(t.get("total")))));
        r.getChildren().add(dash());
        r.getChildren().add(rrow("Metode",Ui.str(t.get("metode")).toUpperCase()));
        r.getChildren().add(rrow("Dibayar",Ui.num(Ui.lng(t.get("dibayar")))));
        r.getChildren().add(rrow("Kembali",Ui.num(Ui.lng(t.get("kembalian")))));
        r.getChildren().add(dash());
        r.getChildren().add(rc(set("struk_footer","Terima kasih sudah mampir!"),"rc rc-c"));
        return r;
    }

    private static String set(String kunci,String def){
        var r=Db.query("SELECT nilai FROM pengaturan WHERE kunci=? AND is_deleted=0",kunci);
        return r.isEmpty()?def:Ui.str(r.get(0).get("nilai"));
    }
    private static Label rc(String s,String cls){
        Label l=new Label(s);
        for(String c:cls.split(" ")) l.getStyleClass().add(c);
        return l;
    }
    private static Label dash(){ return rc("--------------------------------","rc"); }
    private static HBox rrow(String l,String rv){
        return new HBox(8,rc(l,"rc"),Ui.spacer(),rc(rv,"rc"));
    }
}