package com.senja.admin.page;

import javafx.scene.Node;

public interface Page {
    String id();
    String title();
    String subtitle();
    Node view();
}