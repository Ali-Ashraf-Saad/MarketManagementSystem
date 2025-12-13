package com.MMS.ui;

import com.MMS.MainApp;
import javafx.fxml.FXML;

public class CreateInvoiceController {

    @FXML
    void back() {
        MainApp.getScreenManager().show("start");
    }
}
