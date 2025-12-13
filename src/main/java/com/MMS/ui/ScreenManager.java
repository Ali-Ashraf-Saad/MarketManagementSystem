package com.MMS.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;

public class ScreenManager {

    private final StackPane root;
    private final Map<String, Parent> screens = new HashMap<>();

    public ScreenManager(StackPane root) {
        this.root = root;
    }

    public void load(String name, String fxml) {
        try {
            Parent view = FXMLLoader.load(
                    getClass().getResource("/fxml/" + fxml)
            );
            screens.put(name, view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show(String name) {
        root.getChildren().setAll(screens.get(name));
    }
}
