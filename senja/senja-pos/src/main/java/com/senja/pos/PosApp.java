package com.senja.pos;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.senja.core.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import javafx.scene.Node;

import java.util.LinkedHashMap;
import java.util.Map;

public class PosApp extends Application {

    public static Stage stage;
    public static Scene scene;
    public static Map<String,Object> user;
    public static boolean dark=false;
    /** navigasi: halaman aktif + refresh keranjang badge */
    public static String page="home";
    public static final PosState CART=new PosState();

    private static StackPane content;
    private static HBox topbar;
    private static final Map<String,Button> tabs=new LinkedHashMap<>();
    private static Region dotOnline;

    public static final class PosState {
        public java.util.List<PosService.Item> items=new java.util.ArrayList<>();
        public String kodeDiskon="";
        public long potongan=0;
        public void reset(){ items=new java.util.ArrayList<>(); kodeDiskon=""; potongan=0; }
        public int jumlah(){ return items.stream().mapToInt(PosService.Item::qty).sum(); }
        public long subtotal(){ return PosService.subtotal(items); }
    }

    private static final String[][] TABS={
        {"Home","home"},{"Transaksi","transaksi"},{"Pembayaran","pembayaran"},
        {"Riwayat","riwayat"},{"Shift","shift"},{"Pengaturan","pengaturan"}
    };

    @Override public void start(Stage st){
        Db.init(); Seed.seedMasterIfEmpty();
        dark="dark".equals(pengaturan("theme"));
        stage=st; st.setTitle("SENJA — Kasir POS");
        st.setMinWidth(1024); st.setMinHeight(640);

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

        showLogin();
        cekOnlineAsync();
    }

    /* ================= LOGIN ================= */
    private void showLogin(){
        StackPane bg=new StackPane();
        bg.getStyleClass().add("login-bg2");

        /* coba muat foto background */
        Image foto=null;
        try(var in=PosApp.class.getResourceAsStream("/BackgroundLogin.png")){ 
            if(in!=null) foto=new Image(in);
        }catch(Exception ignore){}

        VBox card=loginCard(
            "Halo! 👋",
            "Siap buka shift?☕",  
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
        String k=kode.trim();
        // 1) coba REST (terpusat) bila ada koneksi
        if(ApiClient.online()){
            try{
                Map<String,Object> u=ApiClient.login(k,pass);
                if(!"kasir".equals(Ui.str(u.get("level")))){
                    err.setText("Akun level Admin — gunakan aplikasi Admin.");
                    ApiClient.clearToken();
                    return;
                }
                user=u; masukSukses(); return;
            }catch(ApiClient.ApiException e){
                if(e.status==401||e.status==403){ err.setText(e.getMessage()); return; }
                // offline di tengah jalan → fallback lokal
            }
        }
        // 2) fallback: verifikasi BCrypt di SQLite lokal
        var rows=Db.query("SELECT * FROM users WHERE is_deleted=0 AND lower(kode)=?",k.toLowerCase());
        if(rows.isEmpty()){ err.setText("User ID tidak ditemukan."); return; }
        var u=rows.get(0);
        if(!BCrypt.checkpw(pass,Ui.str(u.get("password_hash")))){ err.setText("Password salah."); return; }
        if(!"aktif".equals(Ui.str(u.get("status")))){ err.setText("Akun dinonaktifkan."); return; }
        if(!"kasir".equals(Ui.str(u.get("level")))){ err.setText("Akun level Admin — gunakan aplikasi Admin."); return; }
        ApiClient.clearToken(); // login lokal → tanpa token REST (mode offline)
        user=u; masukSukses();
    }

    private void masukSukses(){
        CART.reset();
        showShell();
        go("home");
        syncAsync();
        Toast.ok("Selamat datang, "+Ui.str(user.get("nama")).split(" ")[0]+"!");
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

    /* ================= SHELL ================= */
    private void showShell(){
        topbar=new HBox(14);
        topbar.getStyleClass().add("topbar");
        /* brand: logo PNG (fallback teks) */
        ImageView logoImg=new ImageView();
        boolean adaGambar=false;
        try(var in=PosApp.class.getResourceAsStream("/LogoSara.png")){
            if(in!=null){ logoImg.setImage(new Image(in)); adaGambar=true; }
        }catch(Exception ignore){}

        javafx.scene.layout.HBox brandRow=new javafx.scene.layout.HBox(8);
        brandRow.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        if(adaGambar){
            logoImg.setFitHeight(28);           // logo di dalam chip
            logoImg.setPreserveRatio(true);
            StackPane chip=new StackPane(logoImg);
            chip.getStyleClass().add("logo-chip");
            brandRow.getChildren().add(chip);
        }else{
            VBox brandBox=new VBox(new Label("SENJA"){{getStyleClass().add("brand");}},
                    Ui.label("KASIR POS","brand-sub"));
            brandRow.getChildren().add(brandBox);
        }
        topbar.getChildren().add(brandRow);
        for(String[] t:TABS){
            Button b=new Button(t[0]);
            b.getStyleClass().add("tab");
            String id=t[1];
            b.setOnAction(e->go(id));
            tabs.put(id,b);
            topbar.getChildren().add(b);
        }
        Region sp=Ui.spacer();
        HBox.setHgrow(sp,Priority.ALWAYS);
        dotOnline=new Region(); dotOnline.getStyleClass().addAll("dot","off");
        Label clock=new Label(); clock.getStyleClass().add("clock");
        topbar.getChildren().addAll(sp,dotOnline,clock);
        Label av=new Label(Ui.str(user.get("nama")).substring(0,1).toUpperCase());
        av.getStyleClass().add("avatar");
        VBox uin=new VBox(Ui.label(Ui.str(user.get("nama")).split(" ")[0],"h3"),Ui.label("Kasir","mut"));
        Button keluar=Ui.btn("Keluar","btn-danger btn-sm",()->{
            user=null; ApiClient.clearToken(); CART.reset(); showLogin();
        });
        topbar.getChildren().addAll(av,uin,keluar);

        // jam berjalan
        javafx.animation.Timeline tl=new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1),e->
                        clock.setText(java.time.LocalTime.now().format(
                                java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")))));
        tl.setCycleCount(javafx.animation.Animation.INDEFINITE);
        tl.play();

        BorderPane shell=new BorderPane();
        shell.setTop(topbar);
        content.getChildren().setAll(shell);
    }

    /* ================= NAVIGASI ================= */
    public static void go(String id){
        page=id;
        tabs.values().forEach(b->b.getStyleClass().remove("on"));
        Button b=tabs.get(id);
        if(b!=null) b.getStyleClass().add("on");
        if(content.getChildren().size()>0&&content.getChildren().get(0) instanceof BorderPane shell){
            Node view;
            try{
                view=switch(id){
                    case "transaksi" -> new com.senja.pos.page.Transaksi().view();
                    case "pembayaran" -> new com.senja.pos.page.Pembayaran().view();
                    case "riwayat" -> new com.senja.pos.page.Riwayat().view();
                    case "shift" -> new com.senja.pos.page.Shift().view();
                    case "pengaturan" -> new com.senja.pos.page.Pengaturan().view();
                    default -> new com.senja.pos.page.Home().view();
                };
            }catch(Exception e){
                e.printStackTrace();
                view=Ui.card(Ui.label("Gagal memuat halaman: "+e.getMessage(),"err"),
                        Ui.label("Detail error ada di console.","mut"));
            }
            ScrollPane sp=new ScrollPane(view);
            sp.setFitToWidth(true);
            sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            sp.getStyleClass().add("scr");
            shell.setCenter(sp);
        }
    }
    /* ================= online/sync ================= */
    public static void cekOnlineAsync(){
        new Thread(()->{
            boolean on=ApiClient.online();
            Platform.runLater(()->{
                if(dotOnline!=null){
                    dotOnline.getStyleClass().removeAll("on","off");
                    dotOnline.getStyleClass().add(on?"on":"off");
                }
            });
        },"pos-online").start();
                startAutoSync();
    }

    /* ================= sync (manual + otomatis) ================= */
    private static final java.util.concurrent.atomic.AtomicBoolean SYNCING =
            new java.util.concurrent.atomic.AtomicBoolean(false);
    private static final java.util.concurrent.ScheduledExecutorService AUTO =
            java.util.concurrent.Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "pos-auto-sync");
                t.setDaemon(true);
                return t;
            });

    /** Sinkron manual — menampilkan hasil apa pun yang terjadi. */
    public static void syncAsync(){ syncAsync(true); }

    /** loud=false dipakai auto-sync: hanya toast bila ada perubahan. */
    public static void syncAsync(boolean loud){
        if (!SYNCING.compareAndSet(false, true)) return; // sedang jalan → lewati
        new Thread(() -> {
            SyncResult r;
            try { r = SyncEngine.sync("kasir"); }
            finally { SYNCING.set(false); }
            Platform.runLater(() -> {
                if (r.online()) {
                    boolean ada = r.totalPushed() > 0 || r.totalPulled() > 0;
                    if (loud || ada)
                        Toast.ok("Sinkron: push " + r.totalPushed() + ", pull " + r.totalPulled());
                    cekOnlineAsync();
                } else if (loud) {
                    Toast.bad("Offline: " + r.error());
                }
            });
        }, "pos-sync").start();
    }

    /** Auto-sync tiap 2 menit (diam-diam, hanya bila sudah login). */
    private static void startAutoSync(){
        AUTO.scheduleWithFixedDelay(() -> {
            try { if (user != null) syncAsync(false); }
            catch (Exception ignore) {}
        }, 20, 120, java.util.concurrent.TimeUnit.SECONDS);
    }

    public static String pengaturan(String k){
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
    public static void applyTheme(javafx.scene.Parent p){
        p.getStyleClass().remove("dark");
        if(dark) p.getStyleClass().add("dark");
    }
    public static String cssUrl(){ return PosApp.class.getResource("/app.css").toExternalForm(); }

    public static void main(String[] args){ launch(args); }
}