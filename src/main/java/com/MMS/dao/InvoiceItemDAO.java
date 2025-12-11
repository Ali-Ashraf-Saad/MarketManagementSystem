package com.MMS.dao;

import com.MMS.model.InvoiceItem;

import java.sql.*;

public class InvoiceItemDAO {

    public void add(Connection conn, InvoiceItem it) throws SQLException {
        String sql = "INSERT INTO invoice_items(invoice_id,product_id,quantity,price,line_total) VALUES (?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, it.getInvoiceId());
        ps.setInt(2, it.getProductId());
        ps.setInt(3, it.getQuantity());
        ps.setDouble(4, it.getPrice());
        ps.setDouble(5, it.getQuantity() * it.getPrice());
        ps.executeUpdate();
    }
}