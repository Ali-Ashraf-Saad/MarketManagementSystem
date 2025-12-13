package com.MMS.ui;

import com.MMS.MainApp;
import javafx.fxml.FXML;

public class InvoiceDetailsController {

    @FXML
    void back() {
        MainApp.getScreenManager().show("showInvoices");
    }
}
