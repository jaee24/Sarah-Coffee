package com.senja.admin.page;

import com.senja.admin.*;
import com.senja.core.*;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class Pengeluaran implements Page {
    private static final List<String> KATS=List.of("Operasional","Peralatan","Perawatan","Transportasi","Lainnya");

    @Override public String id(){ return "pengeluaran"; }
    @Override public String title(){ return "Pengeluaran"; }
    @Override public String subtitle(){ return "Catat pengeluaran operasional toko"; }

    @Override public Node view(){
        TextField cari=Ui.tf(""); cari.setPromptText("Keterangan / kategori…"); cari.setPrefWidth(200);
        DatePicker dari=new DatePicker(); dari.setPromptText("Dari"); dari.getStyleClass().add("inp"); dari.setPrefWidth(140);
        DatePicker ke=new DatePicker(); ke.setPromptText("Sampai"); ke.getStyleClass().add("inp"); ke.setPrefWidth(140);
        ComboBox<String> katF=Ui.combo("Semua Kategori"); katF.getItems().addAll(KATS); katF.setPrefWidth(150);
        ComboBox<String> jenisF=Ui.combo("Semua Jenis","Rutin","Tak Terduga"); jenisF.setPrefWidth(140);

        TableView<Map<String,Object>> tbl=Ui.table();
        HBox stats=new HBox(10);
        Runnable refresh=()->{
            StringBuilder w=new StringBuilder("e.is_deleted=0");
            List<Object> args=new ArrayList<>();
            if(!cari.getText().isBlank()){
                String q="%"+cari.getText().toLowerCase()+"%";
                w.append(" AND (lower(e.keterangan) LIKE ? OR lower(e.kategori) LIKE ?)"); args.add(q); args.add(q);
            }
            if(dari.getValue()!=null){ w.append(" AND e.tanggal>=?"); args.add(dari.getValue().toString()); }
            if(ke.getValue()!=null){ w.append(" AND e.tanggal<=?"); args.add(ke.getValue().toString()); }
            if(!katF.getValue().equals("Semua Kategori")){ w.append(" AND e.kategori=?"); args.add(katF.getValue()); }
            if(!"Semua Jenis".equals(jenisF.getValue())){
                w.append(" AND e.jenis=?");
                args.add("Rutin".equals(jenisF.getValue())?"rutin":"tak_terduga");
            }
            String sql="SELECT e.* FROM pengeluaran e WHERE "+w+" ORDER BY e.tanggal DESC, e.kode DESC";
            var rows=Db.query(sql,args.toArray());
            tbl.setItems(FXCollections.observableArrayList(rows));
            long totToday=Ui.lng(Db.query("SELECT COALESCE(SUM(jumlah),0) n FROM pengeluaran WHERE is_deleted=0 AND tanggal=?",LocalDate.now().toString()).get(0).get("n"));
            long rutin=rows.stream().filter(e->"rutin".equals(Ui.str(e.get("jenis")))).mapToLong(e->Ui.lng(e.get("jumlah"))).sum();
            long tak=rows.stream().filter(e->"tak_terduga".equals(Ui.str(e.get("jenis")))).mapToLong(e->Ui.lng(e.get("jumlah"))).sum();
            stats.getChildren().setAll(
                mini("TOTAL HARI INI",Ui.money(totToday)),
                mini("RUTIN (terfilter)",Ui.money(rutin)),
                mini("TAK TERDUGA (terfilter)",Ui.money(tak)));
        };
        cari.textProperty().addListener((o,ov,nv)->refresh.run());
        dari.valueProperty().addListener((o,ov,nv)->refresh.run());
        ke.valueProperty().addListener((o,ov,nv)->refresh.run());
        katF.valueProperty().addListener((o,ov,nv)->refresh.run());
        jenisF.valueProperty().addListener((o,ov,nv)->refresh.run());

        var cJenis=Ui.colF("Jenis",m->"rutin".equals(Ui.str(m.get("jenis")))?"Rutin":"Tak Terduga",110);
        Ui.badgeCol(cJenis,v->v.equals("Rutin")?"bdg-ok":"bdg-bad");
        var cNom=Ui.colF("Nominal",m->Ui.money(Ui.lng(m.get("jumlah"))),130);
        cNom.setStyle("-fx-alignment:CENTER-RIGHT;");
        tbl.getColumns().addAll(
            Ui.col("ID","kode",100),
            Ui.colF("Tanggal",m->Ui.fmtD(Ui.str(m.get("tanggal"))),120),
            Ui.col("Keterangan","keterangan",230),
            Ui.col("Kategori","kategori",120),
            cJenis,cNom,
            Ui.actCol(e->{ form(e); refresh.run(); },
                e->Dlg.confirm(AdminApp.stage,"Hapus Pengeluaran","Entri \""+e.get("keterangan")+"\" akan dihapus.",()->{
                    Db.exec("UPDATE pengeluaran SET is_deleted=1,updated_at=?,dirty=1 WHERE id=?",System.currentTimeMillis(),e.get("id"));
                    Toast.ok("Pengeluaran dihapus"); refresh.run();
                })));
        HBox toolbar=new HBox(8,cari,dari,Ui.label("–","mut"),ke,katF,jenisF,
            Ui.btn("Reset","btn-line btn-sm",()->{ cari.clear(); dari.setValue(null); ke.setValue(null); katF.getSelectionModel().select(0); jenisF.getSelectionModel().select(0); }),
            Ui.spacer(),Ui.btn("+ Pengeluaran","btn-acc",()->{ form(null); refresh.run(); }));
        refresh.run();
        return new VBox(12,stats,toolbar,tbl);
    }

    private VBox mini(String cap,String val){
        Label v=new Label(val); v.getStyleClass().add("stat-mid");
        VBox s=new VBox(2,Ui.label(cap,"stat-cap"),v);
        s.getStyleClass().add("stat");
        HBox.setHgrow(s,javafx.scene.layout.Priority.ALWAYS);
        return s;
    }

    private byte[] bukti=null;

    private void form(Map<String,Object> e){
        bukti=e!=null&&e.get("bukti") instanceof byte[] b?b:null;
        DatePicker tgl=new DatePicker(e!=null?LocalDate.parse(Ui.str(e.get("tanggal"))):LocalDate.now());
        tgl.getStyleClass().add("inp");
        TextField ket=Ui.tf(e!=null?Ui.str(e.get("keterangan")):"");
        ComboBox<String> kat=Ui.combo(KATS.toArray(new String[0]));
        if(e!=null) kat.getSelectionModel().select(Ui.str(e.get("kategori")));
        ComboBox<String> jenis=Ui.combo("Rutin","Tak Terduga");
        if(e!=null) jenis.getSelectionModel().select("rutin".equals(Ui.str(e.get("jenis")))?"Rutin":"Tak Terduga");
        TextField nom=Ui.tf(e!=null?String.valueOf(Ui.lng(e.get("jumlah"))):"");

        ImageView imgView=new ImageView(); imgView.setFitWidth(240); imgView.setFitHeight(150); imgView.setPreserveRatio(true);
        Runnable showImg=()->{
            if(bukti!=null){
                try{ imgView.setImage(new Image(new ByteArrayInputStream(bukti))); }
                catch(Exception ignore){ imgView.setImage(null); }
            }else imgView.setImage(null);
        };
        showImg.run();
        Button pilih=new Button("Pilih Foto…"); pilih.getStyleClass().addAll("btn","btn-line","btn-sm");
        pilih.setOnAction(ev->{
            FileChooser fc=new FileChooser();
            fc.setTitle("Pilih Bukti Transaksi");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Gambar","*.png","*.jpg","*.jpeg"));
            File f=fc.showOpenDialog(AdminApp.stage);
            if(f==null) return;
            try{
                BufferedImage bi=ImageIO.read(f);
                double sc=Math.min(1,800.0/Math.max(bi.getWidth(),bi.getHeight()));
                BufferedImage scaled=new BufferedImage((int)(bi.getWidth()*sc),(int)(bi.getHeight()*sc),BufferedImage.TYPE_INT_RGB);
                java.awt.Graphics2D g2=scaled.createGraphics();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(bi,0,0,scaled.getWidth(),scaled.getHeight(),null);
                g2.dispose();
                ByteArrayOutputStream out=new ByteArrayOutputStream();
                ImageIO.write(scaled,"jpg",out);
                bukti=out.toByteArray();
                showImg.run();
                Toast.ok("Bukti dimuat ("+(bukti.length/1024)+" KB)");
            }catch(Exception ex){ Toast.bad("Gagal membaca gambar: "+ex.getMessage()); }
        });
        Button hapusFoto=new Button("Hapus Bukti"); hapusFoto.getStyleClass().addAll("btn","btn-danger","btn-sm");
        hapusFoto.setOnAction(ev->{ bukti=null; showImg.run(); });

        Dlg.modal(AdminApp.stage,e!=null?"Edit Pengeluaran — "+e.get("kode"):"Tambah Pengeluaran",480,dlg->{
            var g=Ui.grid();
            Ui.frow(g,0,"Tanggal",tgl);
            Ui.frow(g,1,"Keterangan",ket);
            Ui.frow(g,2,"Kategori",kat);
            Ui.frow(g,3,"Jenis",jenis);
            Ui.frow(g,4,"Nominal (Rp)",nom);
            g.add(Ui.label("Bukti Transaksi (opsional)","flbl"),0,5);
            g.add(new VBox(6,new HBox(8,pilih,hapusFoto),imgView),1,5);
            HBox btns=new HBox(10,Ui.spacer(),
                Ui.btn("Batal","btn-line",dlg::close),
                Ui.btn("Simpan","btn-acc",()->{
                    String k=ket.getText().trim();
                    if(k.isEmpty()){ Toast.bad("Keterangan wajib diisi"); return; }
                    long n; try{ n=Long.parseLong(nom.getText().trim()); }catch(Exception ex){ Toast.bad("Nominal tidak valid"); return; }
                    if(n<=0){ Toast.bad("Nominal harus lebih dari 0"); return; }
                    long now=System.currentTimeMillis();
                    if(e==null)
                        Db.exec("INSERT INTO pengeluaran(id,kode,tanggal,keterangan,kategori,jenis,jumlah,bukti,created_at,updated_at,is_deleted,dirty) VALUES(?,?,?,?,?,?,?,?,?,?,0,1)",
                            Uuid.rand(),nextKode(),tgl.getValue().toString(),k,kat.getValue(),
                            jenis.getValue().equals("Rutin")?"rutin":"tak_terduga",n,bukti,now,now);
                    else
                        Db.exec("UPDATE pengeluaran SET tanggal=?,keterangan=?,kategori=?,jenis=?,jumlah=?,bukti=?,updated_at=?,dirty=1 WHERE id=?",
                            tgl.getValue().toString(),k,kat.getValue(),jenis.getValue().equals("Rutin")?"rutin":"tak_terduga",n,bukti,now,e.get("id"));
                    Toast.ok("Pengeluaran tersimpan"); dlg.close();
                }));
            btns.setAlignment(Pos.CENTER_RIGHT);
            return new VBox(10,g,btns);
        });
    }

    private String nextKode(){
        int max=0;
        for(var m:Db.query("SELECT kode FROM pengeluaran WHERE kode LIKE 'EXP-%'")){
            try{ max=Math.max(max,Integer.parseInt(Ui.str(m.get("kode")).substring(4))); }catch(Exception ignore){}
        }
        return "EXP-"+String.format("%03d",max+1);
    }
}