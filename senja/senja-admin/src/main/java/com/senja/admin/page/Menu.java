package com.senja.admin.page;

import com.senja.admin.*;
import com.senja.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Menu implements Page {

    private static final ObjectMapper M=new ObjectMapper();
    private static final String[][] OPSI={
        {"regular","Regular"},{"large","Large (+5.000)"},
        {"normal_ice","Normal Ice"},{"less_ice","Less Ice"},
        {"normal_sugar","Normal Sugar"},{"less_sugar","Less Sugar"}
    };

    @Override public String id(){ return "menu"; }
    @Override public String title(){ return "Menu"; }
    @Override public String subtitle(){ return "Kelola menu, resep, & harga"; }

    private final TextField cari=new TextField();
    private final ComboBox<String> katF=new ComboBox<>();
    private final FlowPane grid=new FlowPane(12,12);

    @Override public Node view(){
        cari.getStyleClass().add("inp"); cari.setPromptText("Cari menu…"); cari.setPrefWidth(220);
        katF.getStyleClass().add("inp"); katF.setPrefWidth(170);
        katF.getItems().add("Semua Kategori");
        Db.query("SELECT nama FROM kategori WHERE is_deleted=0 ORDER BY nama")
          .forEach(k->katF.getItems().add(Ui.str(k.get("nama"))));
        katF.getSelectionModel().select(0);
        cari.textProperty().addListener((o,ov,nv)->render());
        katF.valueProperty().addListener((o,ov,nv)->render());
        HBox toolbar=new HBox(10,cari,katF,Ui.spacer(),
            Ui.btn("+ Items","btn-acc",()->{ form(null); render(); }));
        render();
        return new VBox(12,toolbar,grid);
    }

    private void render(){
        grid.getChildren().clear();
        String q="%"+cari.getText().toLowerCase()+"%";
        final String kn=katF.getValue()==null?"Semua Kategori":katF.getValue();
        var rows=Db.query("""
            SELECT p.*, k.nama knama,
              (SELECT COALESCE(SUM(b.harga*r.jumlah),0) FROM resep r JOIN bahan b ON b.id=r.bahan_id
               WHERE r.produk_id=p.id AND r.is_deleted=0 AND b.is_deleted=0) hpp
            FROM produk p JOIN kategori k ON k.id=p.kategori_id
            WHERE p.is_deleted=0 AND lower(p.nama) LIKE ? AND (?='Semua Kategori' OR k.nama=?)
            ORDER BY p.kode""",q,kn,kn);
        if(rows.isEmpty()){
            grid.getChildren().add(Ui.label("Menu tidak ditemukan","mut"));
            return;
        }
        for(var p:rows) grid.getChildren().add(tile(p));
    }

    private VBox tile(Map<String,Object> p){
        VBox t=new VBox(6);
        t.getStyleClass().add("tile"); t.setPrefWidth(190);
        StackPane imgW=new StackPane();
        imgW.getStyleClass().add("tile-img-wrap"); imgW.setPrefSize(180,135);
        ImageView iv=new ImageView();
        try{ iv.setImage(new Image(Ui.str(p.get("gambar")),180,135,true,true,true)); }catch(Exception ignore){}
        iv.setFitWidth(180); iv.setFitHeight(135); iv.setPreserveRatio(false);
        Rectangle clip=new Rectangle(180,135); clip.setArcWidth(14); clip.setArcHeight(14);
        iv.setClip(clip);
        imgW.getChildren().add(iv);
        if(!"aktif".equals(Ui.str(p.get("status")))){
            Label st=new Label("nonaktif");
            st.getStyleClass().addAll("bdg","bdg-mut");
            StackPane.setAlignment(st,Pos.TOP_RIGHT);
            StackPane.setMargin(st,new javafx.geometry.Insets(6));
            imgW.getChildren().add(st);
        }
        Label nm=new Label(Ui.str(p.get("nama"))); nm.getStyleClass().add("tile-n"); nm.setWrapText(true);
        Label kt=Ui.label(Ui.str(p.get("knama")),"mut");
        Label price=new Label(Ui.money(Ui.lng(p.get("harga"))));
        price.setStyle("-fx-text-fill:-acc; -fx-font-weight:bold; -fx-font-size:13px;");
        HBox pr=new HBox(8,price,Ui.spacer(),Ui.label("HPP "+Ui.money(Ui.lng(p.get("hpp"))),"mut"));
        t.getChildren().addAll(imgW,nm,kt,pr);
        t.setOnMouseClicked(e->form(p));
        return t;
    }

    /* ============ editor resep ============ */
    private final class ResepEditor {
        final List<Map<String,Object>> resep;
        final VBox box=new VBox(6);
        final Label hppLbl=new Label();

        ResepEditor(List<Map<String,Object>> initial){ resep=initial; render(); }

        void render(){
            box.getChildren().clear();
            for(int i=0;i<resep.size();i++){
                final int idx=i;
                var r=resep.get(i);
                HBox row=new HBox(10,
                    Ui.label(Ui.str(r.get("bnama"))+" — "+Ui.trim(Ui.dbl(r.get("jumlah")))+" "+Ui.str(r.get("satuan"))),
                    Ui.spacer(),
                    Ui.btn("✕","btn-danger btn-sm",()->{ resep.remove(idx); render(); }));
                row.setAlignment(Pos.CENTER_LEFT);
                box.getChildren().add(row);
            }
            if(resep.isEmpty()) box.getChildren().add(Ui.label("Belum ada bahan — HPP = Rp 0","mut"));
            double h=0;
            for(var r:resep){
                var b=Db.query("SELECT harga FROM bahan WHERE id=?",r.get("bid"));
                if(!b.isEmpty()) h+=Ui.lng(b.get(0).get("harga"))*Ui.dbl(r.get("jumlah"));
            }
            hppLbl.setText("HPP otomatis: "+Ui.money((long)h));
        }
    }

    /* ================= FORM ================= */
    private void form(Map<String,Object> p){
        final boolean edit=p!=null;
        final TextField nama=Ui.tf(edit?Ui.str(p.get("nama")):"");
        final TextField gambar=Ui.tf(edit?Ui.str(p.get("gambar")):"");
        final TextField harga=Ui.tf(edit?String.valueOf(Ui.lng(p.get("harga"))):"");
        final ComboBox<String> status=Ui.combo("aktif","nonaktif");
        if(edit) status.getSelectionModel().select(Ui.str(p.get("status")));

        /* kategori: tampil nama saja, id disimpan di ComboItem */
        final ComboBox<Ui.ComboItem> kat=new ComboBox<>();
        kat.getStyleClass().add("inp"); kat.setMaxWidth(Double.MAX_VALUE);
        for(var k:Db.query("SELECT id,nama FROM kategori WHERE is_deleted=0 ORDER BY nama"))
            kat.getItems().add(new Ui.ComboItem(Ui.str(k.get("id")),Ui.str(k.get("nama"))));
        if(!kat.getItems().isEmpty()) kat.getSelectionModel().select(0);
        if(edit){
            String kid=Ui.str(p.get("kategori_id"));
            for(Ui.ComboItem it:kat.getItems())
                if(it.id.equals(kid)){ kat.getSelectionModel().select(it); break; }
        }

        final CheckBox hot=new CheckBox("Hot"), cold=new CheckBox("Cold");
        if(edit){
            var pen=penList(Ui.str(p.get("penyajian")));
            hot.setSelected(pen.contains("hot"));
            cold.setSelected(pen.contains("cold"));
        }

        final FlowPane opsiBox=new FlowPane(8,8);
        final List<CheckBox> opsiCb=new ArrayList<>();
        for(String[] o:OPSI){
            CheckBox cb=new CheckBox(o[1]);
            cb.setUserData(o[0]);
            opsiCb.add(cb);
            opsiBox.getChildren().add(cb);
        }
        if(edit){
            var op=penList(Ui.str(p.get("opsi")));
            for(CheckBox cb:opsiCb) cb.setSelected(op.contains(String.valueOf(cb.getUserData())));
        }

        /* box opsi minuman: HILANG total untuk makanan/cemilan (bukan sekadar abu-abu) */
        final VBox drinkBox=new VBox(8,
            Ui.label("PENYAJIAN (MINUMAN)","flbl"),
            new HBox(12,hot,cold),
            Ui.label("OPSI MINUMAN","flbl"),
            opsiBox);

        final Runnable katSync=()->{
            Ui.ComboItem sel=kat.getValue();
            boolean minuman=sel!=null&&isMinuman(sel.id);
            drinkBox.setManaged(minuman);
            drinkBox.setVisible(minuman);
            if(minuman){
                boolean iceOk=cold.isSelected();
                for(CheckBox cb:opsiCb){
                    boolean ice=String.valueOf(cb.getUserData()).contains("ice");
                    cb.setDisable(ice&&!iceOk);
                }
            }
        };
        kat.valueProperty().addListener((o,ov,nv)->katSync.run());
        cold.selectedProperty().addListener((o,ov,nv)->katSync.run());
        katSync.run();

        final List<Map<String,Object>> resepAwal=new ArrayList<>();
        if(edit) resepAwal.addAll(Db.query("""
            SELECT b.id bid,b.nama bnama,b.satuan,r.jumlah
            FROM resep r JOIN bahan b ON b.id=r.bahan_id
            WHERE r.produk_id=? AND r.is_deleted=0 ORDER BY b.nama""",p.get("id")));
        final ResepEditor editor=new ResepEditor(resepAwal);

        final ComboBox<Ui.ComboItem> bahanCb=new ComboBox<>();
        bahanCb.getStyleClass().add("inp"); bahanCb.setMaxWidth(Double.MAX_VALUE);
        for(var b:Db.query("SELECT id,nama,satuan FROM bahan WHERE is_deleted=0 ORDER BY nama"))
            bahanCb.getItems().add(new Ui.ComboItem(Ui.str(b.get("id")),
                Ui.str(b.get("nama"))+" ("+Ui.str(b.get("satuan"))+")"));
        if(!bahanCb.getItems().isEmpty()) bahanCb.getSelectionModel().select(0);
        final TextField jml=Ui.tf(""); jml.setPrefWidth(90); jml.setPromptText("Jumlah");

        Dlg.modal(AdminApp.stage,edit?"Edit Items — "+p.get("kode"):"Tambah Items",560,dlg->{
            var g=Ui.grid();
            Ui.frow(g,0,"Nama Menu",nama);
            Ui.frow(g,1,"Kategori",kat);
            Ui.frow(g,2,"URL Gambar",gambar);
            Ui.frow(g,3,"Harga Jual (Rp)",harga);
            Ui.frow(g,4,"Status",status);

            VBox rcard=Ui.card(
                Ui.h3("Komposisi Bahan Baku / Resep"),
                new HBox(8,bahanCb,jml,Ui.btn("Tambah","btn-line btn-sm",()->{
                    Ui.ComboItem sel=bahanCb.getValue();
                    if(sel==null) return;
                    final String bid=sel.id;
                    double j;
                    try{ j=Double.parseDouble(jml.getText().trim()); }
                    catch(Exception ex){ Toast.bad("Jumlah tidak valid"); return; }
                    if(j<=0){ Toast.bad("Jumlah harus lebih dari 0"); return; }
                    for(var r:editor.resep)
                        if(Ui.str(r.get("bid")).equals(bid)){ Toast.bad("Bahan sudah ada di resep"); return; }
                    var b=Db.query("SELECT nama,satuan FROM bahan WHERE id=?",bid).get(0);
                    Map<String,Object> nr=new java.util.LinkedHashMap<>();
                    nr.put("bid",bid); nr.put("bnama",b.get("nama"));
                    nr.put("satuan",b.get("satuan")); nr.put("jumlah",j);
                    editor.resep.add(nr); jml.clear(); editor.render();
                })),
                editor.box,editor.hppLbl);

            HBox btns=new HBox(10,Ui.spacer(),Ui.btn("Batal","btn-line",dlg::close));
            if(edit) btns.getChildren().add(Ui.btn("Hapus","btn-danger",()->
                Dlg.confirm(AdminApp.stage,"Hapus Items",
                    "Menu \""+nama.getText()+"\" akan dihapus. Riwayat transaksi lama tidak terpengaruh.",
                    ()->{
                        long now=System.currentTimeMillis();
                        Db.exec("UPDATE produk SET is_deleted=1,updated_at=?,dirty=1 WHERE id=?",now,p.get("id"));
                        Db.exec("UPDATE resep SET is_deleted=1,updated_at=?,dirty=1 WHERE produk_id=?",now,p.get("id"));
                        Toast.ok("Menu dihapus"); dlg.close(); render();
                    })));
            btns.getChildren().add(Ui.btn("Simpan","btn-acc",
                ()->save(dlg,nama,kat,gambar,harga,status,hot,cold,opsiCb,editor.resep,p)));
            btns.setAlignment(Pos.CENTER_RIGHT);
            return new VBox(10,g,drinkBox,rcard,btns);
        });
    }

    private boolean isMinuman(String kid){
        var k=Db.query("SELECT jenis FROM kategori WHERE id=?",kid);
        return !k.isEmpty()&&"minuman".equals(Ui.str(k.get(0).get("jenis")));
    }

        /* ================= SAVE ================= */
    private void save(javafx.stage.Stage dlg,TextField nama,ComboBox<Ui.ComboItem> kat,
                      TextField gambar,TextField harga,ComboBox<String> status,CheckBox hot,CheckBox cold,
                      List<CheckBox> opsiCb,List<Map<String,Object>> resep,Map<String,Object> p){
        final String n=nama.getText().trim();
        if(n.isEmpty()){ Toast.bad("Nama menu wajib diisi"); return; }
        Ui.ComboItem selKat=kat.getValue();
        if(selKat==null){ Toast.bad("Pilih kategori"); return; }
        final String kid=selKat.id;
        final boolean minuman=isMinuman(kid);
        if(minuman&&!hot.isSelected()&&!cold.isSelected()){ Toast.bad("Pilih minimal satu penyajian (Hot/Cold)"); return; }
        for(var r:resep)
            if(Ui.dbl(r.get("jumlah"))<=0){ Toast.bad("Jumlah resep harus > 0"); return; }

        long hTmp;
        try{ hTmp=Long.parseLong(harga.getText().trim()); }
        catch(Exception e){ hTmp=-1; }
        final long h=hTmp;
        if(h<=0){ Toast.bad("Harga jual harus lebih dari 0"); return; }

        final String pen=minuman?penJson(hot.isSelected(),cold.isSelected()):"[]";
        final List<String> op=new ArrayList<>();
        if(minuman) for(CheckBox cb:opsiCb){
            if(!cb.isSelected()) continue;
            String v=String.valueOf(cb.getUserData());
            if(v.contains("ice")&&!cold.isSelected()) continue;
            op.add(v);
        }
        String opJsonTmp;
        try{ opJsonTmp=M.writeValueAsString(op); }
        catch(Exception e){ opJsonTmp="[]"; }
        final String opJson=opJsonTmp;

        final long now=System.currentTimeMillis();
        final String st=status.getSelectionModel().getSelectedItem();
        final String gm=gambar.getText().trim();
        Db.tx(c->{
            String pid;
            if(p==null){
                pid=Uuid.rand();
                try(var ps=c.prepareStatement(
                        "INSERT INTO produk(id,kode,nama,kategori_id,penyajian,opsi,harga,status,gambar,created_at,updated_at,is_deleted,dirty) VALUES(?,?,?,?,?,?,?,?,?,?,?,0,1)")){
                    ps.setString(1,pid); ps.setString(2,nextKode(c)); ps.setString(3,n);
                    ps.setString(4,kid); ps.setString(5,pen); ps.setString(6,opJson);
                    ps.setLong(7,h); ps.setString(8,st); ps.setString(9,gm);
                    ps.setLong(10,now); ps.setLong(11,now);
                    ps.executeUpdate();
                }
            }else{
                pid=Ui.str(p.get("id"));
                try(var ps=c.prepareStatement(
                        "UPDATE produk SET nama=?,kategori_id=?,penyajian=?,opsi=?,harga=?,status=?,gambar=?,updated_at=?,dirty=1 WHERE id=?")){
                    ps.setString(1,n); ps.setString(2,kid); ps.setString(3,pen); ps.setString(4,opJson);
                    ps.setLong(5,h); ps.setString(6,st); ps.setString(7,gm);
                    ps.setLong(8,now); ps.setString(9,pid);
                    ps.executeUpdate();
                }
                try(var ps=c.prepareStatement("UPDATE resep SET is_deleted=1,updated_at=?,dirty=1 WHERE produk_id=?")){
                    ps.setLong(1,now); ps.setString(2,pid); ps.executeUpdate();
                }
            }
            for(var r:resep){
                try(var ps=c.prepareStatement(
                        "INSERT INTO resep(id,produk_id,bahan_id,jumlah,created_at,updated_at,is_deleted,dirty) VALUES(?,?,?,?,?,?,0,1)")){
                    ps.setString(1,Uuid.rand()); ps.setString(2,pid); ps.setString(3,Ui.str(r.get("bid")));
                    ps.setDouble(4,Ui.dbl(r.get("jumlah"))); ps.setLong(5,now); ps.setLong(6,now);
                    ps.executeUpdate();
                }
            }
        });
        Toast.ok(p==null?"Menu baru ditambahkan":"Menu diperbarui");
        dlg.close();
        render();
    }

    private String nextKode(java.sql.Connection c) throws Exception{
        int max=0;
        try(var st=c.createStatement(); var rs=st.executeQuery("SELECT kode FROM produk WHERE kode LIKE 'PR-%'")){
            while(rs.next()){
                try{ max=Math.max(max,Integer.parseInt(rs.getString(1).substring(3))); }catch(Exception ignore){}
            }
        }
        return "PR-"+String.format("%02d",max+1);
    }

    private List<String> penList(String json){
        try{ return M.readValue(json==null||json.isBlank()?"[]":json,new TypeReference<List<String>>(){}); }
        catch(Exception e){ return new ArrayList<>(); }
    }
    private String penJson(boolean hot,boolean cold){
        List<String> l=new ArrayList<>();
        if(hot) l.add("hot");
        if(cold) l.add("cold");
        try{ return M.writeValueAsString(l); }catch(Exception e){ return "[]"; }
    }
}