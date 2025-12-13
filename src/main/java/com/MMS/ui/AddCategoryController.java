package com.MMS.ui;

import com.MMS.MainApp;
import javafx.fxml.FXML;

public class AddCategoryController {

    @FXML
    void back() {
        MainApp.getScreenManager().show("start");
    }

    @FXML
    void saveCategory() {
        // save category later
    }
}
