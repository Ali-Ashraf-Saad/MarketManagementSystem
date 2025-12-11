package com.MMS;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
        Scene sc = new Scene(fxml.load(), 900, 600);
        stage.setTitle("Market System");
        stage.setScene(sc);
        stage.show();
    }

    public static void main(String[] args) { launch(); }
}