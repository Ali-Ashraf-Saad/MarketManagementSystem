package com.MMS.ui;

import com.MMS.MainApp;
import com.MMS.dao.InvoiceItemDAO;
import com.MMS.model.InvoiceItem;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class InvoiceDetailsController {

    @FXML
    private TableView<InvoiceItem> invoiceTable;

    private static int invoiceId;

    private final InvoiceItemDAO itemDAO = new InvoiceItemDAO();

    public static void setInvoiceId(int id) {
        invoiceId = id;
    }

    @FXML
    public void initialize() {
        setupTable();
        loadItems();
    }

    private void setupTable() {
        TableColumn<InvoiceItem, Number> pidCol =
                new TableColumn<>("Product ID");
        pidCol.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getProductId()));

        TableColumn<InvoiceItem, Number> qtyCol =
                new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getQuantity()));

        TableColumn<InvoiceItem, Number> priceCol =
                new TableColumn<>("Price");
        priceCol.setCellValueFactory(c ->
                new SimpleDoubleProperty(c.getValue().getPrice()));

        TableColumn<InvoiceItem, Number> totalCol =
                new TableColumn<>("Total");
        totalCol.setCellValueFactory(c ->
                new SimpleDoubleProperty(c.getValue().getLineTotal()));

        invoiceTable.getColumns().setAll(pidCol, qtyCol, priceCol, totalCol);
    }

    private void loadItems() {
        try {
            invoiceTable.setItems(
                    FXCollections.observableArrayList(
                            itemDAO.getByInvoiceId(invoiceId)
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void back() {
        MainApp.getScreenManager().show("showInvoices");
    }
}