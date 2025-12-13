package com.MMS.ui;

import com.MMS.MainApp;
import javafx.fxml.FXML;

public class ShowInvoicesController {

    @FXML
    void back() {
        MainApp.getScreenManager().show("start");
    }

    @FXML
    void openInvoiceDetails() {
        MainApp.getScreenManager().show("invoiceDetails");
    }
}
