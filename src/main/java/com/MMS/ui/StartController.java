package com.MMS.ui;

import com.MMS.MainApp;
import javafx.fxml.FXML;

public class StartController {

    @FXML
    void createInvoice() {
        MainApp.getScreenManager().show("createInvoice");
    }

    @FXML
    void showProducts() {
        MainApp.getScreenManager().show("showProducts");
    }

    @FXML
    void showInvoices() {
        MainApp.getScreenManager().show("showInvoices");
    }

    @FXML
    void addCategory() {
        MainApp.getScreenManager().show("addCategory");
    }

    @FXML
    void editProduct() {
        MainApp.getScreenManager().show("editProduct");
    }
}
