package com.senja.admin.page;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import java.util.LinkedHashMap;

import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.shape.Circle;
import javafx.util.StringConverter;
import java.util.TreeMap;
import javafx.stage.FileChooser;

import com.senja.admin.*;
import com.senja.core.Db;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Laporan {
    private Laporan() {}

    /* ---------- helper ---------- */
    /* ================= EKSPOR: pilih lokasi + format ================= */
    private static void ekspor(String namaDefault,List<String[]> rows){
        FileChooser fc=new FileChooser();
        fc.setTitle("Simpan Laporan");
        fc.setInitialFileName(namaDefault.replace(".csv",""));
        fc.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Excel Workbook (*.xlsx)","*.xlsx"),
            new FileChooser.ExtensionFilter("CSV — Excel Indonesia (*.csv)","*.csv"));
        fc.setSelectedExtensionFilter(fc.getExtensionFilters().get(0));
        File f=fc.showSaveDialog(AdminApp.stage);
        if(f==null) return; // batal
        String path=f.getAbsolutePath();
        boolean xlsx=path.toLowerCase().endsWith(".xlsx");
        try{
            if(xlsx) simpanXlsx(new File(path),rows);
            else     simpanCsv(new File(path),rows);
            Toast.ok("Tersimpan: "+path);
        }catch(Exception e){ Toast.bad("Gagal ekspor: "+e.getMessage()); }
    }

    private static void simpanCsv(File f,List<String[]> rows) throws Exception{
        try(PrintWriter w=new PrintWriter(f,StandardCharsets.UTF_8)){
            w.write('\ufeff');
            for(String[] r:rows){
                StringBuilder sb=new StringBuilder();
                for(int i=0;i<r.length;i++){
                    if(i>0) sb.append(';');
                    String c=r[i]==null?"":r[i];
                    if(c.contains(";")||c.contains("\"")||c.contains("\n"))
                        c="\""+c.replace("\"","\"\"")+"\"";
                    sb.append(c);
                }
                w.println(sb);
            }
        }
    }

    private static void simpanXlsx(File f,List<String[]> rows) throws Exception{
        try(var wb=new org.apache.poi.xssf.usermodel.XSSFWorkbook()){
            var sh=wb.createSheet("Laporan");
            int rn=0, maxCol=0;
            for(String[] r:rows){
                var row=sh.createRow(rn++);
                for(int i=0;i<r.length;i++){
                    var cell=row.createCell(i);
                    String c=r[i]==null?"":r[i];
                    if(angka(c)){
                        try{ cell.setCellValue(Long.parseLong(c)); }
                        catch(Exception e){ cell.setCellValue(Double.parseDouble(c)); }
                    }else cell.setCellValue(c);
                }
                maxCol=Math.max(maxCol,r.length);
            }
            for(int i=0;i<maxCol;i++) sh.autoSizeColumn(i);
            try(var out=new java.io.FileOutputStream(f)){ wb.write(out); }
        }
    }

    /** angka murni (tanpa nol depan, bukan tanggal/kode) → jadi sel numerik di xlsx */
    private static boolean angka(String s){
        if(s.isEmpty()||!s.matches("-?\\d+(\\.\\d+)?")) return false;
        return !(s.startsWith("0")&&s.length()>1&&!s.startsWith("0."));
    }
    private static long dms(LocalDate d){ return d.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(); }
    private static long dme(LocalDate d){ return dms(d.plusDays(1)); }

    private static VBox mini(String cap,String val){
        Label v=new Label(val); v.getStyleClass().add("stat-mid");
        VBox s=new VBox(2,Ui.label(cap,"stat-cap"),v);
        s.getStyleClass().add("stat");
        HBox.setHgrow(s,Priority.ALWAYS);
        return s;
    }

        private static HBox legendDot(String colorVar,String text){
        Region dot=new Region();
        dot.setPrefSize(10,10);
        dot.setStyle("-fx-background-color:"+colorVar+";-fx-background-radius:5;");
        return new HBox(6,dot,Ui.label(text,"ph-sub"));
    }

    /* ================= PENJUALAN (ringkasan per tanggal) ================= */
    public static class Penjualan implements Page {
        @Override public String id(){ return "lap-penjualan"; }
        @Override public String title(){ return "Laporan Penjualan"; }
        @Override public String subtitle(){ return "Ringkasan penjualan per tanggal, tren, & metode bayar"; }

        @Override public Node view(){
            DatePicker dari=new DatePicker(LocalDate.now().minusDays(6));
            DatePicker ke=new DatePicker(LocalDate.now());
            dari.getStyleClass().add("inp"); ke.getStyleClass().add("inp");
            HBox stats=new HBox(10);
            TableView<Map<String,Object>> tbl=Ui.table();
            HBox charts=new HBox(12);
            final List<Map<String,Object>> rows=new ArrayList<>();

            Runnable refresh=()->{
                if(dari.getValue()==null||ke.getValue()==null||dari.getValue().isAfter(ke.getValue())) return;
                long s=dms(dari.getValue()),e=dme(ke.getValue());
                TreeMap<String,long[]> byDay=new TreeMap<>();
                for(LocalDate d=dari.getValue();!d.isAfter(ke.getValue());d=d.plusDays(1))
                    byDay.put(d.toString(),new long[4]); // [ntrx, nitem, diskon, omzet]
                for(var r:Db.query("""
                    SELECT strftime('%Y-%m-%d',waktu/1000,'unixepoch','localtime') day,
                           COUNT(*) n, COALESCE(SUM(diskon),0) dsk, COALESCE(SUM(total),0) om
                    FROM transaksi WHERE is_deleted=0 AND waktu>=? AND waktu<? GROUP BY day""",s,e)){
                    long[] a=byDay.get(Ui.str(r.get("day")));
                    if(a!=null){ a[0]=Ui.lng(r.get("n")); a[2]=Ui.lng(r.get("dsk")); a[3]=Ui.lng(r.get("om")); }
                }
                for(var r:Db.query("""
                    SELECT strftime('%Y-%m-%d',t.waktu/1000,'unixepoch','localtime') day, COALESCE(SUM(d.qty),0) ni
                    FROM transaksi t JOIN transaksi_detail d ON d.transaksi_id=t.id AND d.is_deleted=0
                    WHERE t.is_deleted=0 AND t.waktu>=? AND t.waktu<? GROUP BY day""",s,e)){
                    long[] a=byDay.get(Ui.str(r.get("day")));
                    if(a!=null) a[1]=Ui.lng(r.get("ni"));
                }

                rows.clear();
                long tN=0,tI=0,tD=0,tO=0;
                for(var en:byDay.entrySet()){
                    long[] a=en.getValue();
                    Map<String,Object> m=new LinkedHashMap<>();
                    m.put("day",en.getKey());
                    m.put("ntrx",a[0]); m.put("nitem",a[1]); m.put("diskon",a[2]); m.put("omzet",a[3]);
                    rows.add(m);
                    tN+=a[0]; tI+=a[1]; tD+=a[2]; tO+=a[3];
                }
                Map<String,Object> tot=new LinkedHashMap<>();
                tot.put("day","__TOTAL"); tot.put("ntrx",tN); tot.put("nitem",tI);
                tot.put("diskon",tD); tot.put("omzet",tO);
                rows.add(tot);
                tbl.setItems(FXCollections.observableArrayList(rows));

                stats.getChildren().setAll(
                    mini("PENDAPATAN",Ui.money(tO)),
                    mini("TRANSAKSI",String.valueOf(tN)),
                    mini("ITEM TERJUAL",String.valueOf(tI)),
                    mini("RATA-RATA /TRANSAKSI",Ui.money(tN==0?0:tO/tN)));

                /* --- bar chart harian --- */
                CategoryAxis x=new CategoryAxis();
                NumberAxis y=new NumberAxis();
                y.setTickLabelFormatter(new StringConverter<Number>(){
                    @Override public String toString(Number v){
                        double x2=v.doubleValue();
                        if(Math.abs(x2)>=1_000_000) return String.format("%.1fjt",x2/1_000_000);
                        if(Math.abs(x2)>=1_000) return String.format("%.0frb",x2/1_000);
                        return String.valueOf((long)x2);
                    }
                    @Override public Number fromString(String s){ return 0; }
                });
                BarChart<String,Number> bar=new BarChart<>(x,y);
                bar.setLegendVisible(false);
                bar.setAnimated(false);
                bar.setCategoryGap(4);
                bar.setPrefHeight(260);
                XYChart.Series<String,Number> ser=new XYChart.Series<>();
                ser.setName("Pendapatan");
                for(var m:rows){
                    if("__TOTAL".equals(m.get("day"))) continue;
                    LocalDate d=LocalDate.parse((String)m.get("day"));
                    ser.getData().add(new XYChart.Data<>(d.getDayOfMonth()+" "+Ui.MB[d.getMonthValue()-1],(Number)m.get("omzet")));
                }
                bar.getData().add(ser);
                VBox.setVgrow(bar,Priority.ALWAYS);
                VBox trenCard=Ui.card(Ui.h3("Tren Penjualan Harian"),bar);

                /* --- pie metode pembayaran --- */
                VBox metCard=Ui.card(Ui.h3("Metode Pembayaran"));
                metCard.setPrefWidth(290);
                var met=Db.query("""
                    SELECT metode, COUNT(*) n, SUM(total) v FROM transaksi
                    WHERE is_deleted=0 AND waktu>=? AND waktu<? GROUP BY metode""",s,e);
                if(met.isEmpty()){
                    metCard.getChildren().add(Ui.label("Belum ada transaksi pada periode ini.","mut"));
                }else{
                    PieChart pie=new PieChart();
                    pie.setLabelsVisible(false);
                    pie.setLegendVisible(false);
                    pie.setStartAngle(90);
                    pie.setAnimated(false);
                    pie.setPrefSize(180,180);
                    long totV=met.stream().mapToLong(r->Ui.lng(r.get("v"))).sum();
                    VBox legend=new VBox(7);
                    for(var r:met){
                        String nama=Ui.str(r.get("metode")).toUpperCase();
                        long v=Ui.lng(r.get("v"));
                        pie.getData().add(new PieChart.Data(nama,v));
                        legend.getChildren().add(legendDot("-acc",nama+"  "+(totV>0?String.format("%.0f%%",v*100.0/totV):"0%")
                            +"  ·  "+Ui.money(v)));
                    }
                    StackPane wrap=new StackPane(pie);
                    wrap.setPrefHeight(180);
                    metCard.getChildren().addAll(wrap,legend);
                }

                charts.getChildren().setAll(trenCard,metCard);
                HBox.setHgrow(trenCard,Priority.ALWAYS);
            };
            dari.valueProperty().addListener((o,ov,nv)->refresh.run());
            ke.valueProperty().addListener((o,ov,nv)->refresh.run());

            tbl.getColumns().addAll(
                Ui.colF("Tanggal",m->"__TOTAL".equals(m.get("day"))?"TOTAL":Ui.fmtD(Ui.str(m.get("day"))),150),
                Ui.colF("Transaksi",m->String.valueOf(Ui.lng(m.get("ntrx"))),100),
                Ui.colF("Item Terjual",m->String.valueOf(Ui.lng(m.get("nitem"))),110),
                Ui.colF("Diskon",m->Ui.lng(m.get("diskon"))>0?Ui.money(Ui.lng(m.get("diskon"))):"—",110),
                Ui.colF("Pendapatan",m->Ui.money(Ui.lng(m.get("omzet"))),140));
            tbl.setRowFactory(tv->new TableRow<>(){
                @Override protected void updateItem(Map<String,Object> m,boolean empty){
                    super.updateItem(m,empty);
                    getStyleClass().remove("row-total");
                    if(!empty&&m!=null&&"__TOTAL".equals(m.get("day"))) getStyleClass().add("row-total");
                }
            });

            HBox toolbar=new HBox(10,dari,Ui.label("–","mut"),ke,Ui.spacer(),
                Ui.btn("Ekspor Excel (CSV)","btn-acc",()->{
                    if(rows.isEmpty()){ Toast.bad("Tidak ada data"); return; }
                    List<String[]> out=new ArrayList<>();
                    out.add(new String[]{"Laporan Penjualan (per tanggal) — SENJA COFFEE"});
                    out.add(new String[]{"Periode",dari.getValue()+" s/d "+ke.getValue()});
                    out.add(new String[]{""});
                    out.add(new String[]{"Tanggal","Transaksi","Item Terjual","Diskon","Pendapatan"});
                    long tN=0,tI=0,tD=0,tO=0;
                    for(var m:rows){
                        if("__TOTAL".equals(m.get("day"))) continue;
                        out.add(new String[]{Ui.str(m.get("day")),String.valueOf(Ui.lng(m.get("ntrx"))),
                            String.valueOf(Ui.lng(m.get("nitem"))),String.valueOf(Ui.lng(m.get("diskon"))),
                            String.valueOf(Ui.lng(m.get("omzet")))});
                        tN+=Ui.lng(m.get("ntrx")); tI+=Ui.lng(m.get("nitem"));
                        tD+=Ui.lng(m.get("diskon")); tO+=Ui.lng(m.get("omzet"));
                    }
                    out.add(new String[]{"TOTAL",String.valueOf(tN),String.valueOf(tI),String.valueOf(tD),String.valueOf(tO)});
                    ekspor("laporan-penjualan_"+dari.getValue()+"_"+ke.getValue()+".csv",out);
                }));
            refresh.run();
            return new VBox(12,stats,toolbar,tbl,charts);
        }
    }

    /* ================= STOK ================= */
    public static class Stok implements Page {
        @Override public String id(){ return "lap-stok"; }
        @Override public String title(){ return "Laporan Stok"; }
        @Override public String subtitle(){ return "Data stok bahan baku"; }

        @Override public Node view(){
            TextField cari=Ui.tf(""); cari.setPromptText("Cari bahan…"); cari.setPrefWidth(200);
            TableView<Map<String,Object>> tbl=Ui.table();
            Runnable refresh=()->{
                String q="%"+cari.getText().toLowerCase()+"%";
                tbl.setItems(FXCollections.observableArrayList(Db.query(
                    "SELECT * FROM bahan WHERE is_deleted=0 AND lower(nama) LIKE ? ORDER BY kode",q)));
            };
            cari.textProperty().addListener((o,ov,nv)->refresh.run());
            var cStok=Ui.colF("Stok",m->Ui.trim(Ui.dbl(m.get("stok")))+" "+Ui.str(m.get("satuan")),110);
            var cStat=Ui.colF("Status",m->Ui.dbl(m.get("stok"))<=Ui.dbl(m.get("stok_min"))?"KRITIS":"AMAN",90);
            Ui.badgeCol(cStat,v->v.equals("KRITIS")?"bdg-bad":"bdg-ok");
            tbl.getColumns().addAll(
                Ui.col("ID","kode",80),
                Ui.col("Nama Bahan Baku","nama",210),
                Ui.col("Satuan","satuan",80),
                cStok,
                Ui.colF("Min. Stok",m->Ui.trim(Ui.dbl(m.get("stok_min"))),90),
                Ui.colF("Harga Modal",m->Ui.money(Ui.lng(m.get("harga"))),120),
                Ui.colF("Nilai Stok",m->Ui.money((long)(Ui.lng(m.get("harga"))*Ui.dbl(m.get("stok")))),130),
                cStat);
            HBox toolbar=new HBox(10,cari,Ui.spacer(),
                Ui.btn("Ekspor Excel (CSV)","btn-acc",()->{
                    List<String[]> out=new ArrayList<>();
                    out.add(new String[]{"Laporan Stok — SENJA COFFEE"});
                    out.add(new String[]{""});
                    out.add(new String[]{"ID","Nama","Satuan","Stok","Min Stok","Harga Modal","Nilai Stok","Status"});
                    for(var b:Db.query("SELECT * FROM bahan WHERE is_deleted=0 ORDER BY kode"))
                        out.add(new String[]{Ui.str(b.get("kode")),Ui.str(b.get("nama")),Ui.str(b.get("satuan")),
                            Ui.trim(Ui.dbl(b.get("stok"))),Ui.trim(Ui.dbl(b.get("stok_min"))),
                            String.valueOf(Ui.lng(b.get("harga"))),
                            String.valueOf((long)(Ui.lng(b.get("harga"))*Ui.dbl(b.get("stok")))),
                            Ui.dbl(b.get("stok"))<=Ui.dbl(b.get("stok_min"))?"KRITIS":"AMAN"});
                    ekspor("laporan-stok_"+LocalDate.now()+".csv",out);
                }));
            refresh.run();
            return new VBox(12,toolbar,tbl);
        }
    }

    /* ================= ABSENSI ================= */
    public static class Absensi implements Page {
        @Override public String id(){ return "lap-absensi"; }
        @Override public String title(){ return "Laporan Absensi"; }
        @Override public String subtitle(){ return "Rekap kehadiran pegawai"; }

        @Override public Node view(){
            TextField cari=Ui.tf(""); cari.setPromptText("ID / nama pegawai…"); cari.setPrefWidth(200);
            DatePicker dari=new DatePicker(LocalDate.now().minusDays(6));
            DatePicker ke=new DatePicker(LocalDate.now());
            dari.getStyleClass().add("inp"); ke.getStyleClass().add("inp");
            TableView<Map<String,Object>> tbl=Ui.table();
            Runnable refresh=()->{
                StringBuilder w=new StringBuilder("a.is_deleted=0");
                List<Object> args=new ArrayList<>();
                if(!cari.getText().isBlank()){
                    String q="%"+cari.getText().toLowerCase()+"%";
                    w.append(" AND (lower(u.nama) LIKE ? OR lower(u.kode) LIKE ?)"); args.add(q); args.add(q);
                }
                if(dari.getValue()!=null){ w.append(" AND a.tanggal>=?"); args.add(dari.getValue().toString()); }
                if(ke.getValue()!=null){ w.append(" AND a.tanggal<=?"); args.add(ke.getValue().toString()); }
                tbl.setItems(FXCollections.observableArrayList(Db.query(
                    "SELECT a.*,u.kode ukode,u.nama unama FROM absensi a LEFT JOIN users u ON u.id=a.user_id WHERE "+
                    w+" ORDER BY a.tanggal DESC,ukode",args.toArray())));
            };
            cari.textProperty().addListener((o,ov,nv)->refresh.run());
            dari.valueProperty().addListener((o,ov,nv)->refresh.run());
            ke.valueProperty().addListener((o,ov,nv)->refresh.run());
            var cStat=Ui.colF("Status",m->Ui.str(m.get("status")),100);
            Ui.badgeCol(cStat,v->v.equals("hadir")?"bdg-ok":v.equals("terlambat")?"bdg-w":"bdg-bad");
            tbl.getColumns().addAll(
                Ui.col("ID Pegawai","ukode",110),
                Ui.col("Nama Pegawai","unama",200),
                Ui.colF("Tanggal",m->Ui.fmtD(Ui.str(m.get("tanggal"))),130),
                Ui.col("Shift","shift_jenis",70),
                Ui.col("Jam Masuk","masuk",100),
                Ui.col("Jam Pulang","pulang",100),
                cStat);
            HBox toolbar=new HBox(10,cari,dari,Ui.label("–","mut"),ke,Ui.spacer(),
                Ui.btn("Cetak Absensi ke Excel (CSV)","btn-acc",()->{
                    List<String[]> out=new ArrayList<>();
                    out.add(new String[]{"Laporan Absensi — SENJA COFFEE"});
                    out.add(new String[]{"Periode",(dari.getValue()==null?"":dari.getValue().toString())
                        +" s/d "+(ke.getValue()==null?"":ke.getValue().toString())});
                    out.add(new String[]{""});
                    out.add(new String[]{"ID","Nama","Tanggal","Shift","Masuk","Pulang","Status"});
                    for(var a:tbl.getItems()) out.add(new String[]{
                        Ui.str(a.get("ukode")),Ui.str(a.get("unama")),Ui.str(a.get("tanggal")),
                        Ui.str(a.get("shift_jenis")),Ui.str(a.get("masuk")),Ui.str(a.get("pulang")),
                        Ui.str(a.get("status"))});
                    ekspor("laporan-stok_"+LocalDate.now()+".csv",out);
                }));
            refresh.run();
            return new VBox(12,toolbar,tbl);
        }
    }

    /* ================= KEUANGAN (dashboard profesional) ================= */
    public static class Keuangan implements Page {
        @Override public String id(){ return "lap-keuangan"; }
        @Override public String title(){ return "Laporan Keuangan"; }
        @Override public String subtitle(){ return "Pendapatan, HPP, tren harian, & margin bisnis"; }

        @Override public Node view(){
            DatePicker dari=new DatePicker(LocalDate.now().minusDays(29));
            DatePicker ke=new DatePicker(LocalDate.now());
            dari.getStyleClass().add("inp"); ke.getStyleClass().add("inp");
            VBox box=new VBox(12);
            final long[] v=new long[7];
            final TreeMap<String,long[]> perDay=new TreeMap<>();

            Runnable refresh=()->{
                if(dari.getValue()==null||ke.getValue()==null||dari.getValue().isAfter(ke.getValue())) return;
                long s=dms(dari.getValue()),e=dme(ke.getValue());
                perDay.clear();
                for(LocalDate d=dari.getValue();!d.isAfter(ke.getValue());d=d.plusDays(1))
                    perDay.put(d.toString(),new long[3]); // [pendapatan, hpp, pengeluaran]

                for(var r:Db.query("""
                    SELECT strftime('%Y-%m-%d',waktu/1000,'unixepoch','localtime') day, SUM(total) p
                    FROM transaksi WHERE is_deleted=0 AND waktu>=? AND waktu<? GROUP BY day""",s,e)){
                    long[] a=perDay.get(Ui.str(r.get("day")));
                    if(a!=null) a[0]=Ui.lng(r.get("p"));
                }
                for(var r:Db.query("""
                    SELECT strftime('%Y-%m-%d',t.waktu/1000,'unixepoch','localtime') day, COALESCE(SUM(x.hpp),0) h
                    FROM transaksi t
                    JOIN (SELECT d.transaksi_id tid, SUM(b.harga*r2.jumlah*d.qty) hpp
                          FROM transaksi_detail d
                          JOIN resep r2 ON r2.produk_id=d.produk_id AND r2.is_deleted=0
                          JOIN bahan b ON b.id=r2.bahan_id AND b.is_deleted=0
                          WHERE d.is_deleted=0 GROUP BY d.transaksi_id) x ON x.tid=t.id
                    WHERE t.is_deleted=0 AND t.waktu>=? AND t.waktu<? GROUP BY day""",s,e)){
                    long[] a=perDay.get(Ui.str(r.get("day")));
                    if(a!=null) a[1]=Ui.lng(r.get("h"));
                }
                for(var r:Db.query("""
                    SELECT tanggal day, SUM(jumlah) x FROM pengeluaran
                    WHERE is_deleted=0 AND tanggal>=? AND tanggal<=? GROUP BY tanggal""",
                        dari.getValue().toString(),ke.getValue().toString())){
                    long[] a=perDay.get(Ui.str(r.get("day")));
                    if(a!=null) a[2]=Ui.lng(r.get("x"));
                }
                long pendapatan=0,hpp=0,peng=0;
                for(long[] a:perDay.values()){ pendapatan+=a[0]; hpp+=a[1]; peng+=a[2]; }
                long ntrx=Ui.lng(Db.query("SELECT COUNT(*) n FROM transaksi WHERE is_deleted=0 AND waktu>=? AND waktu<?",s,e).get(0).get("n"));
                long npeng=Ui.lng(Db.query("SELECT COUNT(*) n FROM pengeluaran WHERE is_deleted=0 AND tanggal>=? AND tanggal<=?",
                        dari.getValue().toString(),ke.getValue().toString()).get(0).get("n"));
                v[0]=pendapatan; v[1]=hpp; v[2]=pendapatan-hpp; v[3]=peng;
                v[4]=pendapatan-hpp-peng; v[5]=ntrx; v[6]=npeng;
                box.getChildren().setAll(
                        kpiRow(pendapatan,hpp,pendapatan-hpp,peng,pendapatan-hpp-peng),
                        chartsRow(perDay,pendapatan,hpp),
                        detailRow(pendapatan,hpp,peng,pendapatan-hpp-peng,ntrx,npeng,
                                dari.getValue().toString(),ke.getValue().toString()));
            };
            dari.valueProperty().addListener((o,ov,nv)->refresh.run());
            ke.valueProperty().addListener((o,ov,nv)->refresh.run());

            HBox toolbar=new HBox(10,dari,Ui.label("–","mut"),ke,Ui.spacer(),
                Ui.btn("Ekspor Excel (CSV)","btn-acc",()->{
                    if(dari.getValue()==null||ke.getValue()==null) return;
                    List<String[]> out=new ArrayList<>();
                    out.add(new String[]{"Laporan Keuangan — SENJA COFFEE"});
                    out.add(new String[]{"Periode",dari.getValue()+" s/d "+ke.getValue()});
                    out.add(new String[]{""});
                    out.add(new String[]{"RINGKASAN"});
                    out.add(new String[]{"Total Pendapatan",String.valueOf(v[0])});
                    out.add(new String[]{"HPP Penjualan",String.valueOf(v[1])});
                    out.add(new String[]{"Laba Kotor",String.valueOf(v[2])});
                    out.add(new String[]{"Total Pengeluaran",String.valueOf(v[3])});
                    out.add(new String[]{v[4]<0?"Rugi":"Laba Bersih",String.valueOf(v[4])});
                    out.add(new String[]{"Margin Kotor",v[0]>0?String.format("%.1f%%",v[2]*100.0/v[0]):"0%"});
                    out.add(new String[]{""});
                    out.add(new String[]{"TREN HARIAN"});
                    out.add(new String[]{"Tanggal","Pendapatan","HPP","Pengeluaran","Laba Bersih"});
                    perDay.forEach((day,a)->out.add(new String[]{day,
                        String.valueOf(a[0]),String.valueOf(a[1]),String.valueOf(a[2]),
                        String.valueOf(a[0]-a[1]-a[2])}));
                    out.add(new String[]{""});
                    out.add(new String[]{"RINCIAN PENGELUARAN"});
                    out.add(new String[]{"ID","Tanggal","Keterangan","Kategori","Jenis","Jumlah"});
                    for(var pg:Db.query("SELECT * FROM pengeluaran WHERE is_deleted=0 AND tanggal>=? AND tanggal<=? ORDER BY tanggal",
                            dari.getValue().toString(),ke.getValue().toString()))
                        out.add(new String[]{Ui.str(pg.get("kode")),Ui.str(pg.get("tanggal")),Ui.str(pg.get("keterangan")),
                            Ui.str(pg.get("kategori")),Ui.str(pg.get("jenis")),String.valueOf(Ui.lng(pg.get("jumlah")))});
                    ekspor("laporan-stok_"+LocalDate.now()+".csv",out);
                }));
            refresh.run();
            return new VBox(12,toolbar,box);
        }

        /* ---- baris KPI ---- */
        private HBox kpiRow(long pendapatan,long hpp,long labaKotor,long peng,long bersih){
            Label b=new Label(Ui.money(Math.abs(bersih)));
            b.getStyleClass().add("stat-big");
            b.setStyle("-fx-text-fill:"+(bersih<0?"-bad":"-ok")+";");
            VBox laba=new VBox(2,Ui.label(bersih<0?"RUGI BERSIH":"LABA BERSIH","stat-cap"),b,
                    Ui.label("selama periode","ph-sub"));
            laba.getStyleClass().add("stat");
            HBox row=new HBox(10,
                    mini("TOTAL PENDAPATAN",Ui.money(pendapatan)),
                    mini("HPP PENJUALAN",Ui.money(hpp)),
                    mini("PENGELUARAN",Ui.money(peng)),
                    laba);
            for(var n2:row.getChildren()) HBox.setHgrow(n2,Priority.ALWAYS);
            return row;
        }

        /* ---- tren (LineChart) + margin (donut) ---- */
        private HBox chartsRow(TreeMap<String,long[]> perDay,long pendapatan,long hpp){
            List<String> days=new ArrayList<>(perDay.keySet());
            int n=days.size();

            NumberAxis xAxis=new NumberAxis();
            NumberAxis yAxis=new NumberAxis();
            xAxis.setLowerBound(0);
            xAxis.setUpperBound(Math.max(1,n-1));
            xAxis.setTickUnit(Math.max(1,Math.round(n/10.0)));
            xAxis.setMinorTickVisible(false);
            xAxis.setTickLabelFormatter(new StringConverter<Number>(){
                @Override public String toString(Number idx){
                    int i=idx.intValue();
                    if(i<0||i>=days.size()) return "";
                    LocalDate d=LocalDate.parse(days.get(i));
                    return d.getDayOfMonth()+" "+Ui.MB[d.getMonthValue()-1];
                }
                @Override public Number fromString(String s){ return 0; }
            });
            yAxis.setTickLabelFormatter(new StringConverter<Number>(){
                @Override public String toString(Number val){
                    double x=val.doubleValue();
                    if(Math.abs(x)>=1_000_000) return String.format("%.1fjt",x/1_000_000);
                    if(Math.abs(x)>=1_000) return String.format("%.0frb",x/1_000);
                    return String.valueOf((long)x);
                }
                @Override public Number fromString(String s){ return 0; }
            });

            LineChart<Number,Number> line=new LineChart<>(xAxis,yAxis);
            line.setCreateSymbols(false);
            line.setVerticalGridLinesVisible(false);
            line.setAnimated(false);
            line.setLegendVisible(true);
            XYChart.Series<Number,Number> sPend=new XYChart.Series<>();
            sPend.setName("Pendapatan");
            XYChart.Series<Number,Number> sLaba=new XYChart.Series<>();
            sLaba.setName("Laba Bersih");
            long bestPend=0;
            for(int i=0;i<n;i++){
                long[] a=perDay.get(days.get(i));
                long pend=a[0], laba=a[0]-a[1]-a[2];
                sPend.getData().add(new XYChart.Data<>(i,pend));
                sLaba.getData().add(new XYChart.Data<>(i,laba));
                if(pend>bestPend) bestPend=pend;
            }
            line.getData().addAll(sPend,sLaba);
            line.setPrefHeight(250);
            VBox.setVgrow(line,Priority.ALWAYS);

            Label best=new Label(Ui.money(bestPend));
            best.getStyleClass().add("stat-mid");
            VBox trenCard=Ui.card(
                new HBox(10,Ui.h3("Tren Pendapatan & Laba"),Ui.spacer(),
                    new VBox(2,Ui.label("HARI TERBAIK","stat-cap"),best)),
                line);

            VBox donutCard=Ui.card(Ui.h3("Margin Bisnis"));
            donutCard.setPrefWidth(290);
            if(pendapatan<=0){
                donutCard.getChildren().add(Ui.label("Belum ada penjualan pada periode ini.","mut"));
            }else{
                long labaKotor=pendapatan-hpp;
                double margin=Math.max(0,Math.min(100,labaKotor*100.0/pendapatan));
                PieChart pie=new PieChart();
                pie.setLabelsVisible(false);
                pie.setLegendVisible(false);
                pie.setStartAngle(90);
                pie.setAnimated(false);
                pie.setPrefSize(200,200);
                pie.getData().add(new PieChart.Data("Laba kotor",Math.max(0,labaKotor)));
                pie.getData().add(new PieChart.Data("HPP",hpp));
                Circle hole=new Circle(60);
                hole.getStyleClass().add("donut-hole");
                Label pct=new Label(String.format("%.1f%%",margin));
                pct.getStyleClass().add("stat-big");
                VBox center=new VBox(pct,Ui.label("MARGIN KOTOR","stat-cap"));
                center.setAlignment(Pos.CENTER);
                StackPane donut=new StackPane(pie,hole,center);
                donut.setPrefHeight(220);
                donutCard.getChildren().addAll(donut,
                    new HBox(14,legendDot("-acc","Laba kotor "+Ui.money(Math.max(0,labaKotor))),
                                  legendDot("-line2","HPP "+Ui.money(hpp))));
            }

            HBox row=new HBox(12,trenCard,donutCard);
            HBox.setHgrow(trenCard,Priority.ALWAYS);
            return row;
        }

        /* ---- detail + rincian ---- */
        private HBox detailRow(long pendapatan,long hpp,long peng,long bersih,long ntrx,long npeng,String from,String to){
            VBox det=Ui.card(Ui.h3("Detail Keuangan"),
                drow("Pendapatan Penjualan ("+ntrx+" transaksi)",Ui.money(pendapatan)),
                drow("HPP Produk Terjual","−"+Ui.money(hpp)),
                drow("Pengeluaran ("+npeng+" entri)","−"+Ui.money(peng)),
                drow(bersih<0?"Rugi":"Laba Bersih",(bersih<0?"−":"")+Ui.money(bersih)));
            VBox rinc=Ui.card(Ui.h3("Rincian Pengeluaran"));
            var pengs=Db.query("SELECT * FROM pengeluaran WHERE is_deleted=0 AND tanggal>=? AND tanggal<=? ORDER BY tanggal",from,to);
            if(pengs.isEmpty()) rinc.getChildren().add(Ui.label("Tidak ada pengeluaran pada rentang ini.","mut"));
            for(var pg:pengs){
                Label jenisB=new Label("rutin".equals(Ui.str(pg.get("jenis")))?"Rutin":"Tak Terduga");
                jenisB.getStyleClass().addAll("bdg","bdg-w");
                rinc.getChildren().add(new HBox(10,
                    new VBox(2,Ui.label(Ui.str(pg.get("keterangan"))),
                              Ui.label(Ui.fmtD(Ui.str(pg.get("tanggal"))),"mut")),
                    Ui.spacer(),jenisB,Ui.label(Ui.money(Ui.lng(pg.get("jumlah"))))));
            }
            HBox row=new HBox(12,det,rinc);
            HBox.setHgrow(det,Priority.ALWAYS);
            HBox.setHgrow(rinc,Priority.ALWAYS);
            return row;
        }

        private HBox drow(String l,String val){
            return new HBox(10,Ui.label(l),Ui.spacer(),new Label(val));
        }
    }
}