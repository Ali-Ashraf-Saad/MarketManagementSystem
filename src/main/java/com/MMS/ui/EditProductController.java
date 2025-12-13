package com.MMS.ui;

import com.MMS.MainApp;
import javafx.fxml.FXML;

public class EditProductController {

    @FXML
    void back() {
        MainApp.getScreenManager().show("start");
    }

    @FXML
    void saveChanges() {
        // update product later
    }
}
