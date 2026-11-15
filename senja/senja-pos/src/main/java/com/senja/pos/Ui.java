package com.senja.pos;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import java.text.NumberFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

public final class Ui {
    private Ui() {}

    public static final String[] MB={"Jan","Feb","Mar","Apr","Mei","Jun","Jul","Agu","Sep","Okt","Nov","Des"};
    private static final NumberFormat NF=NumberFormat.getNumberInstance(new Locale("id","ID"));

    public static String money(long v){ return "Rp "+NF.format(v); }
    public static String num(double v){ return NF.format(Math.round(v)); }
    public static String trim(double d){ return d==Math.floor(d)?String.valueOf((long)d):String.valueOf(d); }
    public static String str(Object o){ return o==null?"":String.valueOf(o); }
    public static long lng(Object o){ return o instanceof Number n?n.longValue():0; }
    public static double dbl(Object o){ return o instanceof Number n?n.doubleValue():0; }
    public static String fmtD(String iso){
        if(iso==null||iso.isBlank()) return "-";
        var d=LocalDate.parse(iso);
        return d.getDayOfMonth()+" "+MB[d.getMonthValue()-1]+" "+d.getYear();
    }
    public static String fmtDT(long ms){
        var dt=LocalDateTime.ofInstant(Instant.ofEpochMilli(ms),ZoneId.systemDefault());
        return fmtD(dt.toLocalDate().toString())+", "+dt.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public static Label label(String text,String... cls){
        Label l=new Label(text);
        if(cls!=null)
            for(String c:cls)
                if(c!=null)
                    for(String p:c.trim().split("\\s+"))
                        if(!p.isBlank()) l.getStyleClass().add(p);
        return l;
    }
    public static Button btn(String text,String cls,Runnable r){
        Button b=new Button(text);
        b.getStyleClass().add("btn");
        if(cls!=null&&!cls.isBlank()) for(String c:cls.trim().split("\\s+")) b.getStyleClass().add(c);
        b.setOnAction(e->r.run());
        return b;
    }
    public static TextField tf(String v){ TextField t=new TextField(v); t.getStyleClass().add("inp"); return t; }
    public static PasswordField pf(){ PasswordField p=new PasswordField(); p.getStyleClass().add("inp"); return p; }
    public static TextArea ta(String v){ TextArea a=new TextArea(v); a.getStyleClass().add("inp"); a.setPrefRowCount(3); a.setWrapText(true); return a; }

    public static final class ComboItem {
        public final String id, label;
        public ComboItem(String id,String label){ this.id=id; this.label=label; }
        @Override public String toString(){ return label; }
    }

    @SafeVarargs
    public static <T> ComboBox<T> combo(T... items){
        ComboBox<T> c=new ComboBox<>();
        c.getItems().addAll(items);
        if(items.length>0) c.getSelectionModel().select(0);
        c.getStyleClass().add("inp");
        c.setMaxWidth(Double.MAX_VALUE);
        return c;
    }

    public static HBox row(Node... n){ HBox h=new HBox(10,n); h.setAlignment(Pos.CENTER_LEFT); return h; }
    public static HBox row(double sp,Node... n){ HBox h=new HBox(sp,n); h.setAlignment(Pos.CENTER_LEFT); return h; }
    public static Region spacer(){ Region r=new Region(); HBox.setHgrow(r,Priority.ALWAYS); return r; }
    public static Region spacerV(){ Region r=new Region(); VBox.setVgrow(r,Priority.ALWAYS); return r; }
    public static VBox card(Node... kids){ VBox v=new VBox(8,kids); v.getStyleClass().add("card"); return v; }
    public static Label h3(String s){ return label(s,"h3","fr"); }

    public static GridPane grid(){
        GridPane g=new GridPane(); g.setHgap(10); g.setVgap(10);
        ColumnConstraints c0=new ColumnConstraints(); c0.setPrefWidth(130); c0.setMinWidth(130);
        ColumnConstraints c1=new ColumnConstraints(); c1.setHgrow(Priority.ALWAYS);
        g.getColumnConstraints().addAll(c0,c1);
        return g;
    }
    public static void frow(GridPane g,int r,String lbl,Node ctrl){
        g.add(label(lbl,"flbl"),0,r); g.add(ctrl,1,r);
    }

    /* tabel (dipakai Riwayat) */
    public static TableView<Map<String,Object>> table(){
        TableView<Map<String,Object>> t=new TableView<>();
        t.getStyleClass().add("tbl");
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        t.setPlaceholder(label("Tidak ada data","mut"));
        t.setFixedCellSize(34);
        return t;
    }
    public static TableColumn<Map<String,Object>,String> col(String title,String key,double w){
        return colF(title,m->str(m.get(key)),w);
    }
    public static TableColumn<Map<String,Object>,String> colF(String title,Function<Map<String,Object>,String> fn,double w){
        TableColumn<Map<String,Object>,String> c=new TableColumn<>(title);
        c.setCellValueFactory(d->new SimpleStringProperty(fn.apply(d.getValue())));
        c.setPrefWidth(w);
        return c;
    }

    public static Node imgRounded(String url,double size,double arc){
        ImageView iv=new ImageView();
        if(url!=null&&!url.isBlank()){
            try{ iv.setImage(new Image(url,size,size,true,true,true)); }catch(Exception ignore){}
        }
        iv.setFitWidth(size); iv.setFitHeight(size); iv.setPreserveRatio(false);
        Rectangle clip=new Rectangle(size,size); clip.setArcWidth(arc); clip.setArcHeight(arc);
        iv.setClip(clip);
        return iv;
    }
}