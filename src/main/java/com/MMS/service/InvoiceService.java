package com.MMS.service;

import com.MMS.dao.*;
import com.MMS.db.DBConnection;
import com.MMS.model.*;

import java.sql.Connection;
import java.sql.SQLException;

public class InvoiceService {

    private InvoiceDAO invoiceDAO = new InvoiceDAO();
    private InvoiceItemDAO itemDAO = new InvoiceItemDAO();
    private ProductDAO productDAO = new ProductDAO();

    public void createInvoice(Invoice inv) throws SQLException {
        Connection conn = DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            int invoiceId = invoiceDAO.add(conn, inv);
            inv.setId(invoiceId);

            for (InvoiceItem it : inv.getItems()) {
                Product p = productDAO.getById(it.getProductId());
                if (p.getQuantity() < it.getQuantity())
                    throw new SQLException("Not enough stock");
                it.setInvoiceId(invoiceId);
                itemDAO.add(conn, it);
            }

            conn.commit();
        } catch (Exception ex) {
            conn.rollback();
            throw ex;
        }
        conn.close();
    }
}