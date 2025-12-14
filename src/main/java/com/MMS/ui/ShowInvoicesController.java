package com.MMS.ui;

import com.MMS.MainApp;
import com.MMS.dao.InvoiceDAO;
import com.MMS.model.Invoice;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ShowInvoicesController {

    @FXML
    private TableView<Invoice> invoiceTable;

    private final InvoiceDAO invoiceDAO = new InvoiceDAO();

    @FXML
    public void initialize() {
        setupTable();
        loadInvoices();
    }

    private void setupTable() {
        TableColumn<Invoice, Number> idCol =
                new TableColumn<>("ID");
        idCol.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getId()));

        TableColumn<Invoice, String> numCol =
                new TableColumn<>("Invoice No");
        numCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getInvoiceNumber()));

        TableColumn<Invoice, Number> totalCol =
                new TableColumn<>("Total");
        totalCol.setCellValueFactory(c ->
                new SimpleDoubleProperty(c.getValue().getTotal()));

        TableColumn<Invoice, String> statusCol =
                new TableColumn<>("Status");
        statusCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatus()));

        invoiceTable.getColumns().setAll(idCol, numCol, totalCol, statusCol);
    }

    private void loadInvoices() {
        try {
            invoiceTable.setItems(
                    FXCollections.observableArrayList(
                            invoiceDAO.getAll()
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void openInvoiceDetails() {
        Invoice selected =
                invoiceTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        InvoiceDetailsController.setInvoiceId(selected.getId());
        MainApp.getScreenManager().show("invoiceDetails");
    }

    @FXML
    void back() {
        MainApp.getScreenManager().show("start");
    }
}