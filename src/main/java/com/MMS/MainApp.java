package com.MMS;

import com.MMS.ui.ScreenManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static ScreenManager screenManager;

    public static ScreenManager getScreenManager() {
        return screenManager;
    }

    @Override
    public void start(Stage stage) {

        StackPane root = new StackPane();
        screenManager = new ScreenManager(root);

        screenManager.load("start", "StartView.fxml");
        screenManager.load("createInvoice", "CreateInvoice.fxml");
        screenManager.load("showProducts", "ShowProducts.fxml");
        screenManager.load("showInvoices", "ShowInvoices.fxml");
        screenManager.load("addCategory", "AddCategory.fxml");
        screenManager.load("editProduct", "EditProduct.fxml");
        screenManager.load("invoiceDetails", "InvoiceDetails.fxml");

        screenManager.show("start");

        Scene scene = new Scene(root, 1100, 700);
        stage.setScene(scene);
        stage.setTitle("Market Management System");
        stage.show();

        // To Link CSS Files
        scene.getStylesheets().add(
                getClass().getResource("/css/style.css").toExternalForm()
        );
    }


    public static void main(String[] args) {
        launch();
    }

}
