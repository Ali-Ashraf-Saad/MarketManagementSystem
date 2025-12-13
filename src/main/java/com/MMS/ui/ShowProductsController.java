package com.MMS.ui;

import com.MMS.MainApp;
import javafx.fxml.FXML;

public class ShowProductsController {

    @FXML
    void back() {
        MainApp.getScreenManager().show("start");
    }
}
