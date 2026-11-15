package com.senja.pos.page;

import com.senja.pos.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;
import java.util.Map;

public class Pembayaran {

    private final String[] metode={"cash","qris","debit"};
    private final ComboBox<String> metCb=Ui.combo("Cash","QRIS","Debit");
    private final TextField bayar=Ui.tf(""); 
    private final TextField ref=Ui.tf("");
    private final Label kembaliL=new Label("—");
    private boolean processing=false;

    public Node view(){
        var items=PosApp.CART.items;
        if(items.isEmpty()){
            VBox c=Ui.card(Ui.label("Belum ada pesanan. Kembali ke halaman Transaksi untuk memilih menu.","ph-sub"),
                    Ui.btn("← Ke Transaksi","btn-acc",()->PosApp.go("transaksi")));
            return c;
        }
        long sub=PosApp.CART.subtotal();
        long pot=PosApp.CART.potongan;
        long total=sub-pot;

        /* ---- ringkasan + diskon ---- */
        VBox ringkasan=Ui.card(Ui.h3("Ringkasan Pesanan"));
        for(var it:items){
            Label nm=new Label(it.nama()+"  ×"+it.qty());
            Label ops=Ui.label(it.opsi().isEmpty()?"":String.join(" · ",it.opsi()),"mut");
            VBox l=new VBox(1,nm,ops);
            Label v=new Label(Ui.money(it.harga()*it.qty()));
            HBox row=new HBox(10,l,Ui.spacer(),v);
            ringkasan.getChildren().add(row);
        }
        TextField kode=Ui.tf(PosApp.CART.kodeDiskon);
        kode.setPromptText("Kode diskon (cth. SENJA10)");
        Label kdMsg=Ui.label(pot>0?"✓ "+PosApp.CART.kodeDiskon+" — hemat "+Ui.money(pot):"Diskon Rp 0","mut");
        if(pot>0) kdMsg.getStyleClass().add("kd-ok");
        Button terapkan=Ui.btn("Terapkan","btn-line btn-sm",()->{
            PosService.Disc d=PosService.cekDiskon(kode.getText(),PosApp.CART.subtotal());
            PosApp.CART.kodeDiskon=kode.getText().trim().toUpperCase();
            if(d.valid()){ PosApp.CART.potongan=d.potongan(); Toast.ok("Diskon "+Ui.money(d.potongan())+" diterapkan"); }
            else{ PosApp.CART.potongan=0; if(!kode.getText().isBlank()) Toast.bad(d.alasan()); }
            PosApp.go("pembayaran"); // re-render
        });
        ringkasan.getChildren().addAll(new Separator(),kode,new HBox(8,terapkan,kdMsg));

        /* ---- pembayaran ---- */
        VBox bayarCard=Ui.card(Ui.h3("Pembayaran"));
        metCb.valueProperty().addListener((o,ov,nv)->PosApp.go("pembayaran"));
        HBox mets=new HBox(8);
        for(String m:metode){
            Button b=Ui.btn(m.toUpperCase(),"met"+(metCb.getValue().toLowerCase().startsWith(m.substring(0,1))?" on":""),()->{
                metCb.getSelectionModel().select(m.equals("cash")?"Cash":m.equals("qris")?"QRIS":"Debit");
            });
            b.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(b,Priority.ALWAYS);
            mets.getChildren().add(b);
        }
        bayarCard.getChildren().add(mets);
        String met=metCb.getValue().toLowerCase();
        if(met.startsWith("cash")){
            bayar.setPromptText("Jumlah uang diterima");
            Runnable upd=()->{
                try{
                    long b=Long.parseLong(bayar.getText().trim());
                    kembaliL.setText(b>=total?Ui.money(b-total):"KURANG "+Ui.money(total-b));
                }catch(Exception e){ kembaliL.setText("—"); }
            };
            bayar.textProperty().addListener((o,ov,nv)->upd.run());
            HBox chips=new HBox(6,
                Ui.btn("Uang Pas","chip",()->{ bayar.setText(String.valueOf(total)); }),
                Ui.btn("50.000","chip",()->bayar.setText("50000")),
                Ui.btn("100.000","chip",()->bayar.setText("100000")));
            bayarCard.getChildren().addAll(new VBox(6,Ui.label("JUMLAH BAYAR","flbl"),bayar,chips),
                new VBox(6,Ui.label("KEMBALIAN","flbl"),kembaliL));
        }else if(met.startsWith("debit")){
            ref.setPromptText("No. referensi kartu (opsional)");
            bayarCard.getChildren().add(new VBox(6,Ui.label("REFERENSI","flbl"),ref));
        }else{
            bayarCard.getChildren().add(Ui.label("QRIS: tunjukkan QR statis toko / tagihan di EDC, lalu tekan Bayar setelah pembayaran berhasil dicatat.","ph-sub"));
        }

        /* ---- totals ---- */
        VBox totals=new VBox(6);
        totals.getChildren().add(prow("Subtotal",Ui.money(sub)));
        if(pot>0) totals.getChildren().add(prow("Diskon "+PosApp.CART.kodeDiskon,"−"+Ui.money(pot)));
        totals.getChildren().add(prow("TOTAL",Ui.money(total)));
        Label tot=new Label(Ui.money(total)); tot.getStyleClass().add("pay-grand");
        bayarCard.getChildren().addAll(new Separator(),totals,new HBox(8,Ui.label("TOTAL","flbl"),Ui.spacer(),tot));

        /* ---- tombol ---- */
            Button batal=Ui.btn("Batal","btn-danger",()->Dlg.confirm(PosApp.stage,"Batalkan Pesanan",
                "Semua item di keranjang akan dihapus.",
                ()->{ PosApp.CART.reset(); PosApp.go("transaksi"); }));
        Button bayarBtn=Ui.btn("BAYAR "+Ui.money(total),"btn-acc btn-xxl",()->doBayar(met,total));
        bayarBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(bayarBtn,Priority.ALWAYS);
        bayarCard.getChildren().addAll(new Separator(),new HBox(10,batal,bayarBtn));

        HBox wrap=new HBox(14);
        HBox.setHgrow(ringkasan,Priority.ALWAYS);
        HBox.setHgrow(bayarCard,Priority.ALWAYS);
        wrap.getChildren().addAll(ringkasan,bayarCard);
        return wrap;
    }

    private HBox prow(String l,String v){
        HBox h=new HBox(10,Ui.label(l,"pay-row"),Ui.spacer(),Ui.label(v,"pay-row"));
        return h;
    }

    private void doBayar(String met,long total){
        if(processing) return; // cegah double-submit
        final long dibayar;
        if(met.startsWith("cash")){
            long b;
            try{ b=Long.parseLong(bayar.getText().trim()); }
            catch(Exception e){ Toast.bad("Jumlah bayar tidak valid"); return; }
            if(b<total){ Toast.bad("Jumlah bayar kurang dari total"); return; }
            dibayar=b;
        }else{
            dibayar=total;
        }
        final String metodeApi=met.startsWith("cash")?"cash":met.startsWith("qris")?"qris":"debit";
        final String kodeDiskon=PosApp.CART.potongan>0?PosApp.CART.kodeDiskon:null;
        final String refFinal=ref.getText().trim();
        processing=true;
        Toast.show("Memproses pembayaran…",false);
        new Thread(()->{
            try{
                PosService.Hasil h=PosService.checkout(PosApp.CART.items,metodeApi,dibayar,kodeDiskon,refFinal);
                javafx.application.Platform.runLater(()->sukses(h));
            }catch(ApiClient.ApiException e){
                javafx.application.Platform.runLater(()->{ processing=false; Toast.bad(e.getMessage()); });
            }catch(Exception e){
                javafx.application.Platform.runLater(()->{ processing=false; Toast.bad("Gagal: "+e.getMessage()); });
            }
        },"pos-bayar").start();
    }

    private void sukses(PosService.Hasil h){
        processing=false;
        List<Map<String,Object>> det=h.detail();
        var t=h.transaksi();
        PosApp.CART.reset();
        Dlg.modal(PosApp.stage,"Transaksi Berhasil — "+t.get("kode"),420,dlg->{
            StringBuilder ket=new StringBuilder("Mode: "+h.mode());
            if(h.warnings()!=null&&!h.warnings().isEmpty())
                ket.append("\n⚠ ").append(String.join("; ",h.warnings()));
            Label info=new Label(ket.toString()); info.setWrapText(true); info.getStyleClass().add("ph-sub");
            VBox rc=Struk.build(t,det);
            HBox btns=new HBox(10,Ui.spacer(),
                Ui.btn("Cetak Struk","btn-acc",()->Struk.cetak(t,det)),
                Ui.btn("Selesai","btn-line",()->{ dlg.close(); PosApp.go("transaksi"); }));
            btns.setAlignment(Pos.CENTER_RIGHT);
            return new VBox(8,info,rc,btns);
        });
                PosApp.syncAsync(false); // segera push/pull setelah bayar (diam-diam)
    }
}