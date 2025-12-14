package com.MMS.service;

import com.MMS.dao.*;
import com.MMS.db.DBConnection;
import com.MMS.model.*;

import java.sql.Connection;
import java.sql.SQLException;

public class InvoiceService {

    private final InvoiceDAO invoiceDAO = new InvoiceDAO();
    private final InvoiceItemDAO itemDAO = new InvoiceItemDAO();
    private final ProductDAO productDAO = new ProductDAO();

    public void createInvoice(Invoice inv) throws SQLException {

        Connection conn = DBConnection.getConnection();

        try {
            conn.setAutoCommit(false);

            // Calculate total
            double total = 0;
            for (InvoiceItem it : inv.getItems()) {
                total += it.getLineTotal();
            }
            inv.setTotal(total);

            // Insert invoice
            int invoiceId = invoiceDAO.add(conn, inv);
            inv.setId(invoiceId);

            //Insert items + update stock
            for (InvoiceItem it : inv.getItems()) {

                Product p = productDAO.getById(it.getProductId());

                if (p.getQuantity() < it.getQuantity())
                    throw new SQLException("Not enough stock");

                // Insert invoice item
                it.setInvoiceId(invoiceId);
                itemDAO.add(conn, it);

                // Update product stock
                int newQty = p.getQuantity() - it.getQuantity();
                productDAO.updateQuantity(conn, p.getId(), newQty);
            }

            conn.commit();

        } catch (Exception ex) {
            conn.rollback();
            throw ex;
        }

        conn.close();
    }
}