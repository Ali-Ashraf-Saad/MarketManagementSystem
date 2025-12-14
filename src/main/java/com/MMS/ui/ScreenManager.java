package com.MMS.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;

public class ScreenManager {

    private final StackPane root;
    private final Map<String, Node> screens = new HashMap<>();

    public ScreenManager(StackPane root) {
        this.root = root;
    }

    public void load(String name, String fxml) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/" + fxml));
            Node screen = loader.load();
            screens.put(name, screen);
        } catch (Exception e) {
            System.err.println("Failed to load: " + fxml);
            e.printStackTrace();
        }
    }

    public void show(String name) {
        Node screen = screens.get(name);
        if (screen == null) {
            System.err.println("Screen not found: " + name);
            return;
        }
        root.getChildren().setAll(screen);
    }
}