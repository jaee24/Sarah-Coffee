package com.senja.pos.page;

import com.senja.core.Db;
import com.senja.pos.*;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Riwayat {

    private final TextField cari=new TextField();
    private final ComboBox<String> mode=Ui.combo("Hari Ini","Semua");
    private final TableView<Map<String,Object>> tbl=Ui.table();

    public Node view(){
        cari.getStyleClass().add("inp"); cari.setPromptText("Cari kode transaksi…"); cari.setPrefWidth(200);
        Runnable refresh=this::render;
        cari.textProperty().addListener((o,ov,nv)->render());
        mode.valueProperty().addListener((o,ov,nv)->render());

        var cMet=Ui.colF("Metode",m->Ui.str(m.get("metode")).toUpperCase(),90);
        cMet.setCellFactory(cl->new TableCell<>(){
            @Override protected void updateItem(String v,boolean e){
                super.updateItem(v,e);
                if(e||v==null){ setGraphic(null); return; }
                Label l=new Label(v); l.getStyleClass().addAll("bdg","bdg-mut");
                setGraphic(l);
            }
        });
        var cSt=Ui.colF("Status",m->Ui.str(m.get("status")),90);
        cSt.setCellFactory(cl->new TableCell<>(){
            @Override protected void updateItem(String v,boolean e){
                super.updateItem(v,e);
                if(e||v==null){ setGraphic(null); return; }
                Label l=new Label(v);
                l.getStyleClass().addAll("bdg",v.equals("selesai")?"bdg-ok":"bdg-bad");
                setGraphic(l);
            }
        });
        tbl.getColumns().addAll(
            Ui.col("ID Transaksi","kode",170),
            Ui.colF("Waktu",m->Ui.fmtDT(Ui.lng(m.get("waktu"))),170),
            Ui.colF("Total",m->Ui.money(Ui.lng(m.get("total"))),120),
            cMet,cSt);
        tbl.setRowFactory(tv->{
            TableRow<Map<String,Object>> r=new TableRow<>();
            r.setOnMouseClicked(e->{ if(!r.isEmpty()) detail(r.getItem()); });
            return r;
        });

        HBox toolbar=new HBox(10,cari,mode);
        render();
        return new VBox(12,toolbar,tbl,Ui.label("Klik transaksi untuk detail & cetak ulang struk.","mut"));
    }

    private void render(){
        String uid=Ui.str(PosApp.user.get("id"));
        StringBuilder w=new StringBuilder("is_deleted=0 AND user_id=?");
        List<Object> args=new ArrayList<>(List.of(uid));
        if("Hari Ini".equals(mode.getValue())){
            long s=LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            w.append(" AND waktu>=?"); args.add(s);
        }
        if(!cari.getText().isBlank()){ w.append(" AND kode LIKE ?"); args.add("%"+cari.getText().trim().toUpperCase()+"%"); }
        tbl.setItems(FXCollections.observableArrayList(Db.query(
            "SELECT * FROM transaksi WHERE "+w+" ORDER BY waktu DESC",args.toArray())));
    }

    private void detail(Map<String,Object> t){
        var det=Db.query("SELECT * FROM transaksi_detail WHERE transaksi_id=? AND is_deleted=0",t.get("id"));
        Dlg.modal(PosApp.stage,"Detail — "+t.get("kode"),560,dlg->{
            TableView<Map<String,Object>> it=Ui.table();
            it.setFixedCellSize(30);
            it.getColumns().addAll(
                Ui.col("Produk","nama",170),
                Ui.colF("Opsi",m->{ var o=Ui.str(m.get("opsi")); return o.isBlank()?"—":o; },180),
                Ui.colF("Qty",m->String.valueOf(Ui.lng(m.get("qty"))),50),
                Ui.colF("Harga",m->Ui.money(Ui.lng(m.get("harga"))),100),
                Ui.colF("Subtotal",m->Ui.money(Ui.lng(m.get("subtotal"))),100));
            it.setItems(FXCollections.observableArrayList(det));
            it.setPrefHeight(Math.min(300,36+det.size()*30));

            var g=Ui.grid();
            int r=0;
            trow(g,r++,"Subtotal",Ui.money(Ui.lng(t.get("subtotal"))));
            if(Ui.lng(t.get("diskon"))>0) trow(g,r++,"Diskon "+Ui.str(t.get("diskon_kode")),"−"+Ui.money(Ui.lng(t.get("diskon"))));
            trow(g,r++,"Total",Ui.money(Ui.lng(t.get("total"))));
            trow(g,r++,"Dibayar / Kembali",Ui.money(Ui.lng(t.get("dibayar")))+" / "+Ui.money(Ui.lng(t.get("kembalian"))));

            HBox btns=new HBox(10,Ui.spacer(),
                Ui.btn("Tutup","btn-line",dlg::close),
                Ui.btn("Cetak Struk","btn-acc",()->Struk.cetak(t,det)));
            btns.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
            return new VBox(10,it,g,btns);
        });
    }

    private void trow(GridPane g,int r,String l,String v){
        g.add(Ui.label(l,"flbl"),0,r);
        Label b=new Label(v); b.getStyleClass().add("h3");
        g.add(b,1,r);
    }
}