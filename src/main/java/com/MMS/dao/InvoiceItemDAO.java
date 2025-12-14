package com.MMS.dao;

import com.MMS.db.DBConnection;
import com.MMS.model.InvoiceItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceItemDAO {

    public void add(Connection conn, InvoiceItem it) throws SQLException {

        String sql = """
            INSERT INTO invoice_items
            (invoice_id, product_id, quantity, price, line_total)
            VALUES (?, ?, ?, ?, ?)
        """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, it.getInvoiceId());
        ps.setInt(2, it.getProductId());
        ps.setInt(3, it.getQuantity());
        ps.setDouble(4, it.getPrice());
        ps.setDouble(5, it.getLineTotal());
        ps.executeUpdate();
    }
    
    public List<InvoiceItem> getByInvoiceId(int invoiceId)
            throws SQLException {

        List<InvoiceItem> list = new ArrayList<>();
        Connection c = DBConnection.getConnection();

        PreparedStatement ps =
                c.prepareStatement(
                        "SELECT * FROM invoice_items WHERE invoice_id=?"
                );
        ps.setInt(1, invoiceId);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            InvoiceItem it = new InvoiceItem();
            it.setId(rs.getInt("id"));
            it.setInvoiceId(rs.getInt("invoice_id"));
            it.setProductId(rs.getInt("product_id"));
            it.setQuantity(rs.getInt("quantity"));
            it.setPrice(rs.getDouble("price"));
            it.setLineTotal(rs.getDouble("line_total"));
            list.add(it);
        }

        c.close();
        return list;
    }
}