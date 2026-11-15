package com.senja.admin.page;

import com.senja.admin.*;
import com.senja.core.Db;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Map;

public class Home implements Page {
    @Override public String id(){ return "home"; }
    @Override public String title(){ return "Home"; }
    @Override public String subtitle(){ return "Ringkasan performa toko hari ini"; }

    @Override public Node view(){
        long dayStart=LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long dayEnd=dayStart+86400000L;

        long terjual=Ui.lng(Db.query("SELECT COALESCE(SUM(d.qty),0) n FROM transaksi_detail d JOIN transaksi t ON t.id=d.transaksi_id WHERE t.is_deleted=0 AND d.is_deleted=0 AND t.waktu>=? AND t.waktu<?",dayStart,dayEnd).get(0).get("n"));
        var q2=Db.query("SELECT COUNT(*) n, COALESCE(SUM(total),0) om FROM transaksi WHERE is_deleted=0 AND waktu>=? AND waktu<?",dayStart,dayEnd).get(0);
        long trx=Ui.lng(q2.get("n")), omzet=Ui.lng(q2.get("om"));
        long menu=Ui.lng(Db.query("SELECT COUNT(*) n FROM produk WHERE is_deleted=0 AND status='aktif'").get(0).get("n"));
        long kris=Ui.lng(Db.query("SELECT COUNT(*) n FROM bahan WHERE is_deleted=0 AND stok<=stok_min").get(0).get("n"));

        Label big=new Label(Ui.num(terjual)); big.getStyleClass().add("stat-big");
        VBox s1=new VBox(2,Ui.label("PRODUK TERJUAL HARI INI","stat-cap"),big,spark());
        s1.getStyleClass().add("stat");
        HBox.setHgrow(s1,javafx.scene.layout.Priority.ALWAYS);

        HBox stats=new HBox(12,s1,
                statMid("OMZET HARI INI",Ui.money(omzet),trx+" transaksi"),
                statMid("MENU AKTIF",Ui.num(menu),"dari katalog"),
                statMid("STOK KRITIS",Ui.num(kris),kris>0?"perlu restock":"semua aman"));

        return new VBox(14,stats,
                sellers("Paling Laris : Semua Kategori","",5),
                sellers("Paling Laris : Kategori Cemilan","WHERE k.nama='Cemilan'",5),
                sellers("Paling Laris : Kategori Minuman","WHERE k.jenis='minuman'",7));
    }

    private VBox statMid(String cap,String val,String sub){
        Label v=new Label(val); v.getStyleClass().add("stat-mid");
        VBox s=new VBox(2,Ui.label(cap,"stat-cap"),v,Ui.label(sub,"ph-sub"));
        s.getStyleClass().add("stat");
        HBox.setHgrow(s,javafx.scene.layout.Priority.ALWAYS);
        return s;
    }

    private Node spark(){
        long[] v=new long[7];
        for(int i=0;i<7;i++){
            LocalDate d=LocalDate.now().minusDays(6L-i);
            long s=d.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            v[i]=Ui.lng(Db.query("SELECT COALESCE(SUM(d.qty),0) n FROM transaksi_detail d JOIN transaksi t ON t.id=d.transaksi_id WHERE t.is_deleted=0 AND d.is_deleted=0 AND t.waktu>=? AND t.waktu<?",s,s+86400000L).get(0).get("n"));
        }
        long max=Math.max(1,Arrays.stream(v).max().orElse(1));
        HBox bars=new HBox(4);
        for(int i=0;i<7;i++){
            Region r=new Region();
            r.getStyleClass().add("spark-bar");
            if(i==6) r.getStyleClass().add("hot");
            r.setPrefSize(12,Math.max(4,v[i]*44.0/max));
            bars.getChildren().add(r);
        }
        bars.setPadding(new Insets(8,0,0,0));
        return bars;
    }

    private Node sellers(String title,String where,int limit){
        var rows=Db.query("SELECT p.id,p.nama,p.gambar,SUM(d.qty) qty FROM transaksi_detail d "+
                "JOIN transaksi t ON t.id=d.transaksi_id AND t.is_deleted=0 "+
                "JOIN produk p ON p.id=d.produk_id AND p.is_deleted=0 "+
                "JOIN kategori k ON k.id=p.kategori_id "+where+
                " GROUP BY p.id ORDER BY qty DESC LIMIT "+limit);
        if(rows.isEmpty()) return Ui.card(Ui.h3(title),Ui.label("Belum ada data penjualan","mut"));
        FlowPane fp=new FlowPane(10,10);
        int[] i={1};
        for(var m:rows){
            VBox tile=new VBox(5);
            tile.getStyleClass().add("tile");
            StackPane imgWrap=new StackPane();
            imgWrap.getStyleClass().add("tile-img-wrap");
            imgWrap.setMinSize(100,100); imgWrap.setPrefSize(100,100);
            imgWrap.getChildren().add(Ui.imgRounded(Ui.str(m.get("gambar")),100,14));
            Label rank=new Label(String.valueOf(i[0])); rank.getStyleClass().add("rank");
            if(i[0]==1) rank.getStyleClass().add("gold");
            StackPane.setAlignment(rank,Pos.TOP_LEFT);
            StackPane.setMargin(rank,new Insets(5));
            imgWrap.getChildren().add(rank);
            Label nm=Ui.label(Ui.str(m.get("nama")),"tile-n");
            nm.setWrapText(true); nm.setMaxWidth(108);
            nm.setTextAlignment(TextAlignment.CENTER); nm.setAlignment(Pos.CENTER);
            tile.getChildren().addAll(imgWrap,nm,Ui.label(Ui.num(Ui.dbl(m.get("qty")))+" terjual","tile-q"));
            tile.setOnMouseClicked(e->info(m));
            fp.getChildren().add(tile);
            i[0]++;
        }
        return Ui.card(Ui.h3(title),fp);
    }

    private void info(Map<String,Object> m){
        var p=Db.query("SELECT p.*,k.nama AS cnama FROM produk p LEFT JOIN kategori k ON k.id=p.kategori_id WHERE p.id=?",m.get("id"));
        if(p.isEmpty()) return;
        var pr=p.get(0);
        long hpp=Ui.lng(Db.query("SELECT COALESCE(SUM(b.harga*r.jumlah),0) n FROM resep r JOIN bahan b ON b.id=r.bahan_id WHERE r.is_deleted=0 AND r.produk_id=?",m.get("id")).get(0).get("n"));
        long harga=Ui.lng(pr.get("harga"));
        Dlg.modal(AdminApp.stage,"Detail Produk",380,dlg->{
            var g=Ui.grid();
            Ui.frow(g,0,"Nama",new Label(Ui.str(pr.get("nama"))));
            Ui.frow(g,1,"Kategori",new Label(Ui.str(pr.get("cnama"))));
            Ui.frow(g,2,"Status",new Label(Ui.str(pr.get("status"))));
            Ui.frow(g,3,"Harga Jual",new Label(Ui.money(harga)));
            Ui.frow(g,4,"HPP (resep)",new Label(Ui.money(hpp)));
            Ui.frow(g,5,"Margin",new Label(Ui.money(harga-hpp)));
            Ui.frow(g,6,"Terjual",new Label(Ui.num(Ui.dbl(m.get("qty")))));
            HBox btns=new HBox(10,Ui.spacer(),Ui.btn("Tutup","btn-line",dlg::close));
            return new VBox(10,g,btns);
        });
    }
}