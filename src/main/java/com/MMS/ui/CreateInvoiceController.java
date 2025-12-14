package com.MMS.ui;

import com.MMS.MainApp;
import com.MMS.dao.CategoryDAO;
import com.MMS.dao.ProductDAO;
import com.MMS.model.*;
import com.MMS.service.InvoiceService;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CreateInvoiceController {

    @FXML private TableView<Product> productTable;
    @FXML private TableView<InvoiceItem> invoiceTable;
    @FXML private TextField quantityField;
    @FXML private ComboBox<Category> categoryBox;
    @FXML private Label totalLabel;

    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final InvoiceService invoiceService = new InvoiceService();

    private final Invoice invoice = new Invoice();
    private ObservableList<Product> allProducts;

    @FXML
    public void initialize() {
        setupProductTable();
        setupInvoiceTable();
        loadCategories();
        loadProducts();
        updateTotal();
    }

    private void setupProductTable() {
        productTable.getColumns().setAll(
                col("ID", p -> p.getId()),
                col("Name", Product::getName),
                col("Price", Product::getPrice),
                col("Stock", Product::getQuantity)
        );
    }

    private void setupInvoiceTable() {
        invoiceTable.getColumns().setAll(
                col("Product", InvoiceItem::getProductId),
                col("Qty", InvoiceItem::getQuantity),
                col("Price", InvoiceItem::getPrice),
                col("Total", InvoiceItem::getLineTotal)
        );
    }

    private <T> TableColumn<T, ?> col(String title, javafx.util.Callback<T, ?> c) {
        TableColumn<T, Object> col = new TableColumn<>(title);
        col.setCellValueFactory(x -> new SimpleObjectProperty<>(c.call(x.getValue())));
        return col;
    }

    private void loadProducts() {
        try {
            allProducts = FXCollections.observableArrayList(productDAO.getAll());
            productTable.setItems(allProducts);
        } catch (Exception e) {
            showError("Failed to load products");
        }
    }

    private void loadCategories() {
        try {
            categoryBox.setItems(
                    FXCollections.observableArrayList(categoryDAO.getAll())
            );
        } catch (Exception e) {
            showError("Failed to load categories");
        }
    }

    @FXML
    void filterProducts() {
        Category c = categoryBox.getValue();
        if (c == null) {
            productTable.setItems(allProducts);
            return;
        }
        productTable.setItems(
                allProducts.filtered(p -> p.getCategoryId() == c.getId())
        );
    }

    @FXML
    void addToInvoice() {
        Product p = productTable.getSelectionModel().getSelectedItem();
        if (p == null) return;

        int qty = Integer.parseInt(quantityField.getText());
        InvoiceItem it = new InvoiceItem();
        it.setProductId(p.getId());
        it.setQuantity(qty);
        it.setPrice(p.getPrice());
        it.setLineTotal(qty * p.getPrice());

        invoice.getItems().add(it);
        invoiceTable.getItems().add(it);
        updateTotal();
        quantityField.clear();
    }

    private void updateTotal() {
        double total = invoice.getItems()
                .stream().mapToDouble(InvoiceItem::getLineTotal).sum();
        totalLabel.setText(total + " EGP");
    }

    @FXML
    void finishInvoice() {
        try {
            invoice.setInvoiceNumber("INV-" + System.currentTimeMillis());
            invoice.setCashierName("Admin");
            invoiceService.createInvoice(invoice);
            MainApp.getScreenManager().show("start");
        } catch (Exception e) {
            showError("Failed to save invoice");
        }
    }

    @FXML
    void back() {
        MainApp.getScreenManager().show("start");
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}