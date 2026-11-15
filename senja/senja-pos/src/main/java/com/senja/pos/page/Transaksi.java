package com.senja.pos.page;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senja.core.Const;
import com.senja.core.Db;
import com.senja.pos.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Transaksi {

    private static final ObjectMapper M=new ObjectMapper();
    private final TextField cari=new TextField();
    private final ComboBox<String> katF=new ComboBox<>();
    private final FlowPane grid=new FlowPane(10,10);
    private final CartBox cartBox=new CartBox();

    public Node view(){
        cari.getStyleClass().add("inp"); cari.setPromptText("Cari menu…"); cari.setPrefWidth(200);
        katF.getStyleClass().add("inp"); katF.setPrefWidth(160);
        katF.getItems().add("Semua");
        Db.query("SELECT nama FROM kategori WHERE is_deleted=0 ORDER BY nama")
          .forEach(k->katF.getItems().add(Ui.str(k.get("nama"))));
        katF.getSelectionModel().select(0);
        cari.textProperty().addListener((o,ov,nv)->renderGrid());
        katF.valueProperty().addListener((o,ov,nv)->renderGrid());

        HBox toolbar=new HBox(10,katF,cari);
        renderGrid();
        cartBox.render();

        ScrollPane sp=new ScrollPane(grid);
        sp.setFitToWidth(true); sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.getStyleClass().add("scr");
        sp.setPrefHeight(2000);
        VBox.setVgrow(sp,Priority.ALWAYS);

        HBox wrap=new HBox(14);
        HBox.setHgrow(sp,Priority.ALWAYS);
        wrap.getChildren().addAll(sp,cartBox);
        return new VBox(12,toolbar,wrap);
    }

    private void renderGrid(){
        grid.getChildren().clear();
        String q="%"+cari.getText().toLowerCase()+"%";
        String kn=katF.getValue()==null?"Semua":katF.getValue();
        var rows=Db.query("""
            SELECT p.*, k.nama knama, k.jenis kjenis FROM produk p
            JOIN kategori k ON k.id=p.kategori_id
            WHERE p.is_deleted=0 AND p.status='aktif'
              AND lower(p.nama) LIKE ? AND (?='Semua' OR k.nama=?)
            ORDER BY p.kode""",q,kn,kn);
        if(rows.isEmpty()){ grid.getChildren().add(Ui.label("Menu tidak ditemukan","mut")); return; }
        for(var p:rows) grid.getChildren().add(tile(p));
    }

    private VBox tile(Map<String,Object> p){
        VBox t=new VBox(5);
        t.getStyleClass().add("tile"); t.setPrefWidth(128);
        StackPane imgW=new StackPane();
        imgW.getStyleClass().add("tile-img-wrap"); imgW.setPrefSize(120,90);
        imgW.getChildren().add(Ui.imgRounded(Ui.str(p.get("gambar")),120,10));
        Label nm=new Label(Ui.str(p.get("nama")));
        nm.getStyleClass().add("tile-n"); nm.setWrapText(true);
        Label pr=new Label(Ui.money(Ui.lng(p.get("harga"))));
        pr.getStyleClass().add("tile-p");
        t.getChildren().addAll(imgW,nm,pr);
        t.setOnMouseClicked(e->omOpen(Ui.str(p.get("id")),-1));
        return t;
    }

    /* ================= KERANJANG ================= */
    private final class CartBox extends VBox {
        final VBox list=new VBox(8);
        CartBox(){
            getStyleClass().add("cart");
            Label t=new Label("Keranjang"); t.getStyleClass().add("t");
            Label s=new Label(); s.getStyleClass().add("s");
            s.textProperty().bind(javafx.beans.binding.Bindings.createStringBinding(
                    ()->PosApp.CART.items.size()+" jenis item",
                    javafx.collections.FXCollections.observableArrayList()));
            getChildren().addAll(new HBox(8,t,s),list);
        }
        void render(){
            list.getChildren().clear();
            var items=PosApp.CART.items;
            if(items.isEmpty()){
                Label e=new Label("Keranjang kosong.\nKlik menu untuk mengatur opsi.");
                e.getStyleClass().add("cart-empty");
                list.getChildren().add(e);
                renderFooter(0,0);
                return;
            }
            long sub=0; int n=0;
            for(int i=0;i<items.size();i++){
                final int idx=i;
                var it=items.get(i);
                sub+=(long)it.harga()*it.qty(); n+=it.qty();
                Label nm=new Label(it.nama()); nm.getStyleClass().add("nm"); nm.setWrapText(true);
                VBox left=new VBox(2,nm);
                if(!it.opsi().isEmpty()){
                    Label ops=new Label(String.join(" · ",it.opsi()));
                    ops.getStyleClass().add("ops");
                    left.getChildren().add(ops);
                }
                HBox qty=new HBox(8,
                        qBtn("−",()->{ if(it.qty()>1){ items.set(idx,withQty(it,it.qty()-1)); render(); } }),
                        new Label(String.valueOf(it.qty())){{getStyleClass().add("qnty");}},
                        qBtn("+",()->{ items.set(idx,withQty(it,it.qty()+1)); render(); }));
                left.getChildren().add(qty);
                Label st=new Label(Ui.money(it.harga()*it.qty())); st.getStyleClass().add("st");
                HBox acts=new HBox(5,
                        new Button("✎"){{getStyleClass().add("cact"); setOnAction(e->omOpen(it.pid(),idx));}},
                        new Button("✕"){{getStyleClass().add("cact"); setOnAction(e->{ items.remove(idx); render(); });}});
                VBox right=new VBox(6,st,acts);
                right.setAlignment(Pos.TOP_RIGHT);
                HBox row=new HBox(8,left,right);
                row.getStyleClass().add("citem");
                HBox.setHgrow(left,Priority.ALWAYS);
                list.getChildren().add(row);
            }
            renderFooter(sub,n);
        }
        private void renderFooter(long sub,int n){
            getChildren().removeIf(c->c.getStyleClass().contains("cart-f"));
            VBox f=new VBox(8);
            f.getStyleClass().add("cart-f");
            Label s=new Label("Subtotal");
            Label sv=new Label(Ui.money(sub)); sv.getStyleClass().add("b");
            HBox subR=new HBox(8,s,Ui.spacer(),sv);
            subR.getStyleClass().add("cart-sub");
            Button lanjut=Ui.btn("Lanjut Pembayaran →","btn-acc btn-xxl",()->{
                if(PosApp.CART.items.isEmpty()){ Toast.bad("Keranjang masih kosong"); return; }
                PosApp.go("pembayaran");
            });
            lanjut.setMaxWidth(Double.MAX_VALUE);
            lanjut.setDisable(n==0);
            f.getChildren().addAll(subR,lanjut);
            getChildren().add(f);
        }
    }

    private static PosService.Item withQty(PosService.Item it,int q){
        return new PosService.Item(it.pid(),it.nama(),it.opsi(),q,it.harga());
    }

    private Button qBtn(String t,Runnable r){
        Button b=new Button(t);
        b.getStyleClass().add("qbtn");
        b.setOnAction(e->r.run());
        return b;
    }

    /* ================= MODAL OPSI ================= */
    private void omOpen(String pid,int editIdx){
        var p=Db.query("""
            SELECT p.*, k.jenis kjenis FROM produk p JOIN kategori k ON k.id=p.kategori_id
            WHERE p.id=?""",pid);
        if(p.isEmpty()) return;
        var pr=p.get(0);
        boolean minuman="minuman".equals(Ui.str(pr.get("kjenis")));
        List<String> pen=list(Ui.str(pr.get("penyajian")));
        List<String> opsiDef=list(Ui.str(pr.get("opsi")));
        long base=Ui.lng(pr.get("harga"));

        List<String> awal=editIdx>=0?PosApp.CART.items.get(editIdx).opsi():List.of();

        ComboBox<String> penCb=new ComboBox<>(); penCb.getStyleClass().add("inp");
        penCb.getItems().addAll(pen);
        if(minuman&&!pen.isEmpty())
            penCb.getSelectionModel().select(awal.contains("Hot")?"hot":(pen.contains("cold")?"cold":"hot"));

        ComboBox<String> ukCb=new ComboBox<>(); ukCb.getStyleClass().add("inp");
        boolean adaUkuran=minuman&&(opsiDef.contains("regular")||opsiDef.contains("large"));
        if(adaUkuran){
            ukCb.getItems().addAll("Regular","Large");
            ukCb.getSelectionModel().select(awal.contains("Large")?"Large":"Regular");
        }

        ComboBox<String> esCb=new ComboBox<>(); esCb.getStyleClass().add("inp");
        boolean adaEs=minuman&&(opsiDef.contains("less_ice")||opsiDef.contains("normal_ice"));

        ComboBox<String> gulaCb=new ComboBox<>(); gulaCb.getStyleClass().add("inp");
        boolean adaGula=minuman&&(opsiDef.contains("less_sugar")||opsiDef.contains("normal_sugar"));
        if(adaGula){
            gulaCb.getItems().addAll("Normal Sugar","Less Sugar");
            gulaCb.getSelectionModel().select(awal.contains("Less Sugar")?"Less Sugar":"Normal Sugar");
        }

        int[] qty={editIdx>=0?PosApp.CART.items.get(editIdx).qty():1};
        Label qtyL=new Label(String.valueOf(qty[0]));
        qtyL.getStyleClass().add("qnty"); qtyL.setMinWidth(30); qtyL.setAlignment(Pos.CENTER);

        /* harga live: Large = base + HARGA_LARGE */
        java.util.function.LongSupplier harga=()->{
            long h=base;
            if("Large".equals(ukCb.getValue())) h+=Const.HARGA_LARGE;
            return h;
        };
        Label hargaL=new Label(Ui.money(harga.getAsLong()*qty[0]));
        hargaL.getStyleClass().add("h3");
        Runnable upd=()->hargaL.setText(Ui.money(harga.getAsLong()*qty[0]));
        ukCb.valueProperty().addListener((o,ov,nv)->upd.run());

        Button min=Ui.btn("−","btn-line btn-sm",()->{ if(qty[0]>1){qty[0]--;qtyL.setText(String.valueOf(qty[0]));upd.run();} });
        Button plus=Ui.btn("+","btn-line btn-sm",()->{ qty[0]++;qtyL.setText(String.valueOf(qty[0]));upd.run(); });

        /* es hanya aktif untuk Cold */
        Runnable esSync=()->{
            if(!adaEs) return;
            if("Cold".equals(labelPen(penCb.getValue()))){
                if(esCb.getItems().isEmpty()) esCb.getItems().addAll("Normal Ice","Less Ice");
                if(esCb.getValue()==null)
                    esCb.getSelectionModel().select(awal.contains("Less Ice")?"Less Ice":"Normal Ice");
            }else esCb.getItems().clear();
        };
        penCb.valueProperty().addListener((o,ov,nv)->esSync.run());
        esSync.run();

        Dlg.modal(PosApp.stage,Ui.str(pr.get("nama"))+"  ·  "+Ui.money(base),420,dlg->{
            var g=Ui.grid();
            int r=0;
            if(minuman){
                if(!pen.isEmpty()) Ui.frow(g,r++,"Penyajian",penCb);
                if(adaUkuran) Ui.frow(g,r++,"Ukuran",ukCb);
                if(adaEs) Ui.frow(g,r++,"Es",esCb);
                if(adaGula) Ui.frow(g,r++,"Gula",gulaCb);
            }else{
                g.add(Ui.label("Makanan — tanpa opsi tambahan.","mut"),0,r++,2,1);
            }
            Ui.frow(g,r++,"Jumlah",new HBox(10,min,qtyL,plus));
            g.add(Ui.label("TOTAL","flbl"),0,r);
            g.add(hargaL,1,r);

            HBox btns=new HBox(10,Ui.spacer(),
                Ui.btn("Batal","btn-line",dlg::close),
                Ui.btn(editIdx>=0?"Ubah Item":"Tambah ke Keranjang","btn-acc",()->{
                    List<String> opsi=new ArrayList<>();
                    if(minuman){
                        if(penCb.getValue()!=null) opsi.add(labelPen(penCb.getValue()));
                        if(ukCb.getValue()!=null) opsi.add(ukCb.getValue());
                        if(esCb.getValue()!=null) opsi.add(esCb.getValue());
                        if(gulaCb.getValue()!=null) opsi.add(gulaCb.getValue());
                    }
                    var line=new PosService.Item(pid,Ui.str(pr.get("nama")),opsi,qty[0],harga.getAsLong());
                    if(editIdx>=0){
                        PosApp.CART.items.set(editIdx,line);
                    }else{
                        for(int i=0;i<PosApp.CART.items.size();i++){
                            var x=PosApp.CART.items.get(i);
                            if(x.pid().equals(pid)&&x.opsi().equals(opsi)&&x.harga()==line.harga()){
                                PosApp.CART.items.set(i,withQty(x,x.qty()+qty[0]));
                                dlg.close(); cartBox.render();
                                Toast.ok(line.nama()+" ×"+qty[0]+" diperbarui");
                                return;
                            }
                        }
                        PosApp.CART.items.add(line);
                    }
                    dlg.close(); cartBox.render();
                    Toast.ok(line.nama()+" masuk keranjang");
                }));
            btns.setAlignment(Pos.CENTER_RIGHT);
            return new VBox(10,g,btns);
        });
    }

    private static String labelPen(String v){ return "cold".equals(v)?"Cold":"Hot"; }

    private List<String> list(String json){
        try{ return M.readValue(json==null||json.isBlank()?"[]":json,new TypeReference<List<String>>(){}); }
        catch(Exception e){ return new ArrayList<>(); }
    }
}