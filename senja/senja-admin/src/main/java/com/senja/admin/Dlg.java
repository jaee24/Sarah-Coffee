package com.senja.admin;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public final class Dlg {
    private Dlg() {}

    @FunctionalInterface
    public interface Body { Node build(Stage dlg); }

    public static void modal(Stage owner,String title,double width,Body body){
        Stage d=new Stage();
        d.initOwner(owner); d.initModality(Modality.APPLICATION_MODAL); d.initStyle(StageStyle.TRANSPARENT);
        VBox root=new VBox(10);
        root.getStyleClass().add("dlg");
        root.setEffect(new DropShadow(16,Color.rgb(0,0,0,0.35)));
        Label h=new Label(title); h.getStyleClass().addAll("fr","h3");

        /* body dalam ScrollPane — maksimal setinggi layar dikurangi margin */
        ScrollPane sp=new ScrollPane(body.build(d));
        sp.setFitToWidth(true);
        sp.setPrefWidth(width-42);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.getStyleClass().add("scr");
        double maxH=Screen.getPrimary().getVisualBounds().getHeight()-170;
        sp.setMaxHeight(maxH);
        sp.setMinHeight(0);

        root.getChildren().addAll(h,new Separator(),sp);
        root.setPrefWidth(width);
        Scene sc=new Scene(root);
        sc.setFill(Color.TRANSPARENT);
        sc.getStylesheets().add(AdminApp.cssUrl());
        AdminApp.applyTheme(sc.getRoot());
        d.setScene(sc);
        d.centerOnScreen();
        d.showAndWait();
    }

    public static void confirm(Stage owner,String title,String msg,Runnable yes){
        modal(owner,title,420,dlg->{
            Label m=new Label(msg); m.setWrapText(true); m.getStyleClass().add("ph-sub");
            HBox btns=new HBox(10,Ui.spacer(),
                Ui.btn("Batal","btn-line",dlg::close),
                Ui.btn("Ya, lanjutkan","btn-danger",()->{ dlg.close(); yes.run(); }));
            btns.setAlignment(Pos.CENTER_RIGHT);
            return new VBox(8,m,btns);
        });
    }
}