package com.MMS.ui;

import com.MMS.MainApp;
import com.MMS.dao.*;
import com.MMS.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AddCategoryController {

    @FXML private TextField categoryNameField;
    @FXML private TextField productNameField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private ComboBox<Category> categoryBox;

    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final ProductDAO productDAO = new ProductDAO();

    @FXML
    public void initialize() {
        try {
            categoryBox.getItems().addAll(categoryDAO.getAll());
        } catch (Exception ignored) {}
    }

    @FXML
    void saveCategory() {
        try {
            if (!categoryNameField.getText().isEmpty())
                categoryDAO.add(categoryNameField.getText());

            if (categoryBox.getValue() != null) {
                Product p = new Product();
                p.setName(productNameField.getText());
                p.setPrice(Double.parseDouble(priceField.getText()));
                p.setQuantity(Integer.parseInt(quantityField.getText()));
                p.setCategoryId(categoryBox.getValue().getId());
                productDAO.add(p);
            }
            MainApp.getScreenManager().show("start");
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error saving").show();
        }
    }

    @FXML
    void back() {
        MainApp.getScreenManager().show("start");
    }
}