package com.senja.admin;

import com.senja.admin.page.*;
import com.senja.core.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.util.LinkedHashMap;
import java.util.Map;

public class AdminApp extends Application {

    public static Stage stage;
    public static Scene scene;
    public static Map<String,Object> user;
    public static boolean dark=false;

    private static StackPane content;
    private static BorderPane shell;
    private static final Map<String,Button> navBtns=new LinkedHashMap<>();
    private static final Map<String,String[]> TITLES=new LinkedHashMap<>();

    static{
        TITLES.put("home",new String[]{"Home","Ringkasan performa toko hari ini"});
        TITLES.put("karyawan",new String[]{"Data Karyawan","Kelola akun & data pengguna"});
        TITLES.put("absensi",new String[]{"Absensi","Rekap kehadiran pegawai"});
        TITLES.put("shift",new String[]{"Shift","Penjadwalan shift pegawai per 7 hari"});
        TITLES.put("menu",new String[]{"Menu","Kelola menu, resep, & harga"});
        TITLES.put("kategori",new String[]{"Kategori","Kelola kategori produk"});
        TITLES.put("stok",new String[]{"Stok Bahan Baku","Pantau stok & stok minimum"});
        TITLES.put("supplier",new String[]{"Supplier","Data pemasok bahan baku"});
        TITLES.put("pembelian",new String[]{"Pembelian","Pengadaan bahan baku"});
        TITLES.put("penjualan",new String[]{"Penjualan","Riwayat transaksi semua kasir"});
        TITLES.put("pengeluaran",new String[]{"Pengeluaran","Catat pengeluaran operasional toko"});
        TITLES.put("lap-penjualan",new String[]{"Laporan Penjualan","Filter tanggal lalu ekspor"});
        TITLES.put("lap-stok",new String[]{"Laporan Stok","Data stok bahan baku"});
        TITLES.put("lap-absensi",new String[]{"Laporan Absensi","Rekap kehadiran pegawai"});
        TITLES.put("lap-keuangan",new String[]{"Laporan Keuangan","Pendapatan, HPP, & laba bersih"});
        TITLES.put("pengaturan",new String[]{"Pengaturan","Tema, sinkronisasi, & akun"});
    }

    private static final String[][] NAV={
        {null,"Utama",null},
        {"home","Home","home"},
        {null,"Karyawan",null},
        {"users","Data Karyawan","karyawan"},
        {"clipboard","Absensi","absensi"},
        {"clock","Shift","shift"},
        {null,"Produk",null},
        {"coffee","Menu","menu"},
        {"tag","Kategori","kategori"},
        {null,"Persediaan",null},
        {"package","Stok Bahan Baku","stok"},
        {"truck","Supplier","supplier"},
        {"cart","Pembelian","pembelian"},
        {"receipt","Penjualan","penjualan"},
        {"wallet","Pengeluaran","pengeluaran"},
        {null,"Laporan",null},
        {"trending","Laporan Penjualan","lap-penjualan"},
        {"layers","Laporan Stok","lap-stok"},
        {"calendar","Laporan Absensi","lap-absensi"},
        {"chart","Laporan Keuangan","lap-keuangan"},
        {null,"Sistem",null},
        {"sliders","Pengaturan","pengaturan"},
    };

    @Override public void start(Stage st){
        Db.init(); Seed.seedMasterIfEmpty(); Seed.seedDemoIfEmpty();
        dark="dark".equals(pengaturanVal("theme"));
        stage=st; st.setTitle("SENJA — Admin Console");
        st.setMinWidth(1080); st.setMinHeight(660);

        content=new StackPane();
        VBox toastBox=new VBox(8);
        toastBox.setAlignment(Pos.BOTTOM_RIGHT);
        toastBox.setPickOnBounds(false); toastBox.setMouseTransparent(true);
        StackPane.setMargin(toastBox,new Insets(0,18,18,0));

        StackPane root=new StackPane(content,toastBox);
        scene=new Scene(root,1280,800);
        scene.getStylesheets().add(cssUrl());
        applyTheme(root);
        st.setScene(scene); st.show();
        Toast.init(toastBox);
        startAutoSync();

        showLogin();
    }

    /* ================= LOGIN ================= */
    private void showLogin(){
        StackPane bg=new StackPane();
        bg.getStyleClass().add("login-bg2");

        /* coba muat foto background */
        Image foto=null;
        try(var in=AdminApp.class.getResourceAsStream("/BackgroundLogin.png")){   // PosApp: AdminApp.class → PosApp.class
            if(in!=null) foto=new Image(in);
        }catch(Exception ignore){}

        VBox card=loginCard(
            "Halo, Boss! 👋",
            "Semua kendali toko ada di sini ☕",
            "",    
            "👤  User ID");

        if(foto!=null){
            /* ---- MODE FOTO: cover full-screen + overlay + brand kiri bawah ---- */
            ImageView img=new ImageView(foto);
            img.setPreserveRatio(false);

                        img.getStyleClass().add("login-bg-blur");
            /* perluas sedikit agar tepi blur tidak kelihatan garis putih */
            img.setTranslateX(-20); img.setTranslateY(-20);
            if(scene!=null){
                img.fitWidthProperty().bind(scene.widthProperty().add(40));
                img.fitHeightProperty().bind(scene.heightProperty().add(40));
            }

            if(scene!=null){
                img.fitWidthProperty().bind(scene.widthProperty());
                img.fitHeightProperty().bind(scene.heightProperty());
            }else{ img.setFitWidth(1280); img.setFitHeight(800); }
            Region ov=new Region(); ov.getStyleClass().add("login-photo-overlay");
            bg.getChildren().addAll(img,ov);

            StackPane.setAlignment(card,Pos.CENTER);
            bg.getChildren().add(card);
        }else{
            /* ---- FALLBACK: gradasi + blob + emoji (versi casual lama) ---- */
            Region b1=new Region(); b1.getStyleClass().addAll("blob","blob1");
            Region b2=new Region(); b2.getStyleClass().addAll("blob","blob2");
            Region b3=new Region(); b3.getStyleClass().addAll("blob","blob3");
            Label d1=new Label("☕"); d1.getStyleClass().addAll("deco","deco1");
            Label d2=new Label("🫘"); d2.getStyleClass().addAll("deco","deco2");
            Label d3=new Label("✨"); d3.getStyleClass().addAll("deco","deco3");
            StackPane.setAlignment(b1,Pos.TOP_LEFT);     StackPane.setMargin(b1,new Insets(-120,-90,0,0));
            StackPane.setAlignment(b2,Pos.BOTTOM_RIGHT); StackPane.setMargin(b2,new Insets(0,-70,-140,0));
            StackPane.setAlignment(b3,Pos.BOTTOM_LEFT);  StackPane.setMargin(b3,new Insets(0,0,-90,-60));
            StackPane.setAlignment(d1,Pos.TOP_RIGHT);    StackPane.setMargin(d1,new Insets(70,120,0,0));
            StackPane.setAlignment(d2,Pos.TOP_LEFT);     StackPane.setMargin(d2,new Insets(140,0,0,90));
            StackPane.setAlignment(d3,Pos.BOTTOM_RIGHT); StackPane.setMargin(d3,new Insets(0,60,150,0));
            bg.getChildren().addAll(b1,b2,b3,d1,d2,d3,card);
        }
        content.getChildren().setAll(bg);
    }

    private void doLogin(String kode,String pass,Label err){
        var rows=Db.query("SELECT * FROM users WHERE is_deleted=0 AND lower(kode)=?",kode.trim().toLowerCase());
        if(rows.isEmpty()){ err.setText("User ID tidak ditemukan."); return; }
        var u=rows.get(0);
        if(!BCrypt.checkpw(pass,Ui.str(u.get("password_hash")))){ err.setText("Password salah."); return; }
        if(!"aktif".equals(Ui.str(u.get("status")))){ err.setText("Akun dinonaktifkan."); return; }
        if(!"admin".equals(Ui.str(u.get("level")))){ err.setText("Akun level Kasir — gunakan aplikasi Kasir."); return; }
        user=u;
        showShell();
        syncAsync();
        Toast.ok("Selamat datang, "+Ui.str(u.get("nama")).split(" ")[0]+"!");
    }

        /* kartu login — dipakai mode foto & mode fallback */
    private VBox loginCard(String hello,String sub,String demo,String phUser){
        VBox card=new VBox(9);
        card.getStyleClass().add("login-card2");
        card.setMaxHeight(Region.USE_PREF_SIZE);

        Label h=new Label(hello); h.getStyleClass().add("login-hello");
        Label s=new Label(sub); s.getStyleClass().add("login-sub-sm"); s.setWrapText(false);
        card.getChildren().addAll(h,s);

        TextField userF=new TextField(); userF.getStyleClass().add("login-inp");
        userF.setPromptText(phUser);

        PasswordField passF=new PasswordField(); passF.getStyleClass().add("login-inp");
        TextField passShow=new TextField(); passShow.getStyleClass().add("login-inp");
        passShow.setPromptText("🔒  Password"); passF.setPromptText("🔒  Password");
        passShow.textProperty().bindBidirectional(passF.textProperty());
        passShow.setManaged(false); passShow.setVisible(false);
        Button eye=new Button("👁"); eye.getStyleClass().add("eye2");
        StackPane.setAlignment(eye,Pos.CENTER_RIGHT);
        StackPane.setMargin(eye,new Insets(0,12,0,0));
        eye.setOnAction(e->{
            boolean show=!passShow.isVisible();
            passShow.setManaged(show); passShow.setVisible(show);
            passF.setManaged(!show);  passF.setVisible(!show);
            eye.setText(show?"🙈":"👁");
        });
        StackPane pwWrap=new StackPane(passF,passShow,eye);

        Label err=new Label(" "); err.getStyleClass().add("login-err");

        Button masuk=new Button("Masuk  →");
        masuk.getStyleClass().add("login-btn");
        masuk.setMaxWidth(Double.MAX_VALUE);
        Runnable login=()->doLogin(userF.getText(),passF.getText(),err);
        masuk.setOnAction(e->login.run());
        userF.setOnAction(e->login.run());
        passF.setOnAction(e->login.run());
        passShow.setOnAction(e->login.run());

        Label dm=new Label(demo); dm.getStyleClass().add("login-chip");

        card.getChildren().addAll(userF,pwWrap,err,masuk,dm);
        return card;
    }

    private void showShell(){
        BorderPane side=new BorderPane();
        side.getStyleClass().add("side");

        /* ---- brand: logo dari file PNG ---- */
        ImageView logoImg=new ImageView();
        boolean adaGambar=false;
        try(var in=AdminApp.class.getResourceAsStream("/LogoSara.png")){
            if(in!=null){
                logoImg.setImage(new Image(in));
                adaGambar=true;
            }
        }catch(Exception ignore){}

        HBox brandRow=new HBox(10);
        brandRow.setAlignment(Pos.CENTER_LEFT);
        brandRow.setPadding(new Insets(2,8,14,8));
        if(adaGambar){
            logoImg.setFitWidth(185);          // sesuaikan kalau kebesaran/kekecilan
            logoImg.setPreserveRatio(true);
            logoImg.getStyleClass().add("side-brand-img");
            brandRow.getChildren().add(logoImg);
        }else{
            // fallback kalau file tidak ditemukan — teks seperti semula
            Label brand=new Label("SENJA"); brand.getStyleClass().add("side-title");
            Label sub=new Label("ADMIN CONSOLE"); sub.getStyleClass().add("side-sub");
            VBox bt=new VBox(1,brand,sub);
            brandRow.getChildren().add(bt);
        }
        side.setTop(brandRow);

        /* ---- nav ---- */
        VBox navBox=new VBox(3);
        for(String[] n:NAV){
            if(n[2]==null){ navBox.getChildren().add(groupLabel(n[1])); continue; }
            navBox.getChildren().add(navBtn(n[0],n[1],n[2]));
        }
        ScrollPane navScroll=new ScrollPane(navBox);
        navScroll.setFitToWidth(true);
        navScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        navScroll.getStyleClass().add("scr");
        navScroll.setMinHeight(0);
        side.setCenter(navScroll);

        /* ---- kartu user + aksi ---- */
        Label av=new Label(Ui.str(user.get("nama")).substring(0,1).toUpperCase());
        av.getStyleClass().add("avatar");
        VBox uinfo=new VBox(1,
                Ui.label(Ui.str(user.get("nama")),"side-name"),
                Ui.label("Administrator","side-sub"));
        HBox urow=new HBox(10,av,uinfo);
        urow.getStyleClass().add("side-user2");

        Button syncB=new Button("⟳   Sinkron");
        syncB.getStyleClass().addAll("side-act");
        syncB.setMaxWidth(Double.MAX_VALUE);
        syncB.setOnAction(e->AdminApp.syncAsync());
        Button outB=new Button("⏻   Keluar");
        outB.getStyleClass().addAll("side-act","red");
        outB.setMaxWidth(Double.MAX_VALUE);
        outB.setOnAction(e->{ user=null; showLogin(); });

        VBox bottom=new VBox(8,urow,syncB,outB);
        bottom.setPadding(new Insets(10,2,2,2));
        side.setBottom(bottom);

        shell=new BorderPane();
        shell.setLeft(side);
        content.getChildren().setAll(shell);
        navigate("home");
    }

    private Label groupLabel(String text){
        Label l=new Label(text.toUpperCase());
        l.getStyleClass().add("nav-grp");
        return l;
    }

    private Button navBtn(String ico,String text,String id){
        StackPane tile=new StackPane();
        tile.getStyleClass().add("nav-tile");
        Node i=Ico.get(ico);
        tile.getChildren().add(i);
        Label t=new Label(text); t.getStyleClass().add("nav-lbl");
        HBox g=new HBox(11,tile,t);
        g.setAlignment(Pos.CENTER_LEFT);
        Button b=new Button();
        b.getStyleClass().add("nav-btn2");
        b.setGraphic(g);
        b.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setOnAction(e->navigate(id));
        navBtns.put(id,b);
        return b;
    }

    public static void navigate(String id){
        navBtns.values().forEach(b->b.getStyleClass().remove("on"));
        Button b=navBtns.get(id);
        if(b!=null) b.getStyleClass().add("on");
        Page p=pageFor(id);
        VBox box=new VBox(8);
        box.getStyleClass().add("page");
        box.getChildren().addAll(Ui.label(p.title(),"h1","fr"),Ui.label(p.subtitle(),"ph-sub"),p.view());
        ScrollPane sp=new ScrollPane(box);
        sp.setFitToWidth(true);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.getStyleClass().add("scr");
        if(shell!=null) shell.setCenter(sp);
    }

    private static Page pageFor(String id){
        return switch(id){
            case "home" -> new Home();
            case "karyawan" -> new Karyawan();
            case "absensi" -> new Absensi();
            case "shift" -> new Shift();
            case "menu" -> new Menu();
            case "kategori" -> new Kategori();
            case "stok" -> new Stok();
            case "supplier" -> new Supplier();
            case "pembelian" -> new Pembelian();
            case "penjualan" -> new Penjualan();
            case "pengeluaran" -> new Pengeluaran();
            case "lap-penjualan" -> new Laporan.Penjualan();
            case "lap-stok" -> new Laporan.Stok();
            case "lap-absensi" -> new Laporan.Absensi();
            case "lap-keuangan" -> new Laporan.Keuangan();
            case "pengaturan" -> new Pengaturan();
            default -> { var tt=TITLES.get(id); yield new Coming(id,tt[0],tt[1]); }
        };
    }

    private static final java.util.concurrent.atomic.AtomicBoolean SYNCING =
            new java.util.concurrent.atomic.AtomicBoolean(false);
    private static final java.util.concurrent.ScheduledExecutorService AUTO =
            java.util.concurrent.Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "admin-auto-sync");
                t.setDaemon(true);
                return t;
            });

    public static void syncAsync(){ syncAsync(true); }

    public static void syncAsync(boolean loud){
        if (!SYNCING.compareAndSet(false, true)) return;
        new Thread(() -> {
            SyncResult r;
            try { r = SyncEngine.sync("admin"); }
            finally { SYNCING.set(false); }
            Platform.runLater(() -> {
                if (r.online()) {
                    boolean ada = r.totalPushed() > 0 || r.totalPulled() > 0;
                    if (loud || ada)
                        Toast.ok("Sinkron: push " + r.totalPushed() + ", pull " + r.totalPulled());
                } else if (loud) {
                    Toast.bad("Offline: " + r.error());
                }
            });
        }, "admin-sync").start();
    }

    private static void startAutoSync(){
        AUTO.scheduleWithFixedDelay(() -> {
            try { if (user != null) syncAsync(false); }
            catch (Exception ignore) {}
        }, 20, 120, java.util.concurrent.TimeUnit.SECONDS);
    }

    public static String pengaturanVal(String k){
        var r=Db.query("SELECT nilai FROM pengaturan WHERE kunci=? AND is_deleted=0",k);
        return r.isEmpty()?null:Ui.str(r.get(0).get("nilai"));
    }
    public static void setPengaturan(String k,String v){
        long now=System.currentTimeMillis();
        if(Db.exec("UPDATE pengaturan SET nilai=?,updated_at=?,dirty=1 WHERE kunci=?",v,now,k)==0)
            Db.exec("INSERT INTO pengaturan(id,kunci,nilai,created_at,updated_at,is_deleted,dirty) VALUES(?,?,?,?,?,0,1)",Uuid.rand(),k,v,now,now);
    }
    public static void setDark(boolean d){
        dark=d;
        setPengaturan("theme",d?"dark":"light");
        if(scene!=null) applyTheme(scene.getRoot());
    }
    public static void applyTheme(Parent p){
        p.getStyleClass().remove("dark");
        if(dark) p.getStyleClass().add("dark");
    }
    public static String cssUrl(){ return AdminApp.class.getResource("/app.css").toExternalForm(); }

    public static void main(String[] args){ launch(args); }
}