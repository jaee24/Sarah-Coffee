package com.senja.pos;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public final class Toast {
    private static VBox host;
    private Toast() {}
    static void init(VBox h){ host=h; }
    public static void ok(String m){ show(m,false); }
    public static void bad(String m){ show(m,true); }

    public static void show(String msg,boolean bad){
        if(host==null) return;
        if(!Platform.isFxApplicationThread()){ Platform.runLater(()->show(msg,bad)); return; }
        Label l=new Label(msg);
        l.getStyleClass().add("toast-lbl"); l.setWrapText(true); l.setMaxWidth(340);
        StackPane b=new StackPane(l);
        b.getStyleClass().addAll("toast",bad?"bad":"ok");
        host.getChildren().add(b);
        FadeTransition in=new FadeTransition(Duration.millis(180),b);
        in.setFromValue(0); in.setToValue(1); in.play();
        PauseTransition p=new PauseTransition(Duration.seconds(2.8));
        p.setOnFinished(e->{
            FadeTransition out=new FadeTransition(Duration.millis(220),b);
            out.setFromValue(1); out.setToValue(0);
            out.setOnFinished(e2->host.getChildren().remove(b));
            out.play();
        });
        p.play();
    }
}