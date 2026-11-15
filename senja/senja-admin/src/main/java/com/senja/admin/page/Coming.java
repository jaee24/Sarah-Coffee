package com.senja.admin.page;

import com.senja.admin.Ui;
import javafx.scene.Node;

public class Coming implements Page {
    private final String id,title,subtitle;
    public Coming(String id,String title,String subtitle){ this.id=id; this.title=title; this.subtitle=subtitle; }
    @Override public String id(){ return id; }
    @Override public String title(){ return title; }
    @Override public String subtitle(){ return subtitle; }
    @Override public Node view(){
        return Ui.card(Ui.label("Bagian ini dibangun pada bagian berikutnya.","ph-sub"),
                Ui.label("Struktur, database, dan sinkronisasi untuk halaman ini sudah siap — antarmukanya menyusul di bagian 2 aplikasi Admin.","mut"));
    }
}