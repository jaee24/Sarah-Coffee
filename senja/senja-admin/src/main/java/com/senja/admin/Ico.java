package com.senja.admin;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;

/** Ikon vektor monokrom (gaya garis, viewBox 24x24) — warna via CSS class .is/.isf */
public final class Ico {
    private Ico() {}

    public static Node get(String name){ return get(name,0.8); }

    public static Node get(String name,double scale){
        Group g=new Group();
        for(String d:paths(name)){
            if(d==null) continue;
            if(d.startsWith("C ")){ // "C cx cy r [f]" — f = titik solid
                String[] p=d.trim().split(" ");
                Circle c=new Circle(num(p[1]),num(p[2]),num(p[3]));
                c.getStyleClass().add(p.length>4?"isf":"is");
                g.getChildren().add(c);
            }else{
                SVGPath s=new SVGPath();
                s.setContent(d);
                s.getStyleClass().add("is");
                g.getChildren().add(s);
            }
        }
        g.setScaleX(scale); g.setScaleY(scale);
        return g;
    }

    private static double num(String s){ return Double.parseDouble(s); }

    private static String[] paths(String n){
        return switch(n){
            case "home" -> new String[]{"M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z","M9 22V12h6v10"};
            case "users" -> new String[]{"M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2","C 9 7 4","M23 21v-2a4 4 0 0 0-3-3.87","M16 3.13a4 4 0 0 1 0 7.75"};
            case "clipboard" -> new String[]{"M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2","M9 2h6a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H9a1 1 0 0 1-1-1V3a1 1 0 0 1 1-1z"};
            case "clock" -> new String[]{"C 12 12 10","M12 6v6l4 2"};
            case "coffee" -> new String[]{"M18 8h1a4 4 0 0 1 0 8h-1","M2 8h14v9a4 4 0 0 1-4 4H6a4 4 0 0 1-4-4z","M6 1v3M10 1v3M14 1v3"};
            case "tag" -> new String[]{"M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z","M7 7h0.01"};
            case "package" -> new String[]{"M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z","M3.27 6.96L12 12.01l8.73-5.05","M12 22.08V12"};
            case "truck" -> new String[]{"M1 3h14v12H1z","M16 8h4l3 3v5h-7V8z","C 5.5 18.5 2.5","C 18.5 18.5 2.5"};
            case "cart" -> new String[]{"M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6","C 9 21 1 f","C 20 21 1 f"};
            case "receipt" -> new String[]{"M4 2v20l2-1 2 1 2-1 2 1 2-1 2 1 2-1 2 1V2l-2 1-2-1-2 1-2-1-2 1-2-1-2 1z","M8 7h8M8 11h8M8 15h5"};
            case "wallet" -> new String[]{"M21 12V7H5a2 2 0 0 1 0-4h14v4","M3 5v14a2 2 0 0 0 2 2h16v-5","M18 12a2 2 0 0 0 0 4h4v-4z"};
            case "trending" -> new String[]{"M23 6l-9.5 9.5-5-5L1 18","M17 6h6v6"};
            case "layers" -> new String[]{"M12 2L2 7l10 5 10-5z","M2 17l10 5 10-5","M2 12l10 5 10-5"};
            case "calendar" -> new String[]{"M5 4h14a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2z","M16 2v4M8 2v4M3 10h18"};
            case "chart" -> new String[]{"M18 20V10M12 20V4M6 20v-6"};
            case "sliders" -> new String[]{"M4 21v-7M4 10V3M12 21v-9M12 8V3M20 21v-5M20 12V3","M1 14h6M9 8h6M17 16h6"};
            default -> new String[]{"C 12 12 8"};
        };
    }
}