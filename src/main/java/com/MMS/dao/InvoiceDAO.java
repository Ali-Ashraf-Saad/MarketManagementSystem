package com.MMS.dao;

import com.MMS.db.DBConnection;
import com.MMS.model.Invoice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {

    public int add(Connection conn, Invoice inv) throws SQLException {

        String sql = """
            INSERT INTO invoices
            (invoice_number, customer_name, cashier_name, total, status)
            VALUES (?, ?, ?, ?, ?)
        """;

        PreparedStatement ps =
                conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, inv.getInvoiceNumber());
        ps.setString(2, inv.getCustomerName());
        ps.setString(3, inv.getCashierName());
        ps.setDouble(4, inv.getTotal());
        ps.setString(5, "OPEN");

        ps.executeUpdate();

        ResultSet keys = ps.getGeneratedKeys();
        if (keys.next())
            return keys.getInt(1);

        return -1;
    }

    // ✅ FIXED: use DBConnection
    public List<Invoice> getAll() throws SQLException {

        List<Invoice> list = new ArrayList<>();
        Connection c = DBConnection.getConnection();

        ResultSet rs = c.createStatement()
                .executeQuery("SELECT * FROM invoices ORDER BY id DESC");

        while (rs.next()) {
            Invoice inv = new Invoice();
            inv.setId(rs.getInt("id"));
            inv.setInvoiceNumber(rs.getString("invoice_number"));
            inv.setCashierName(rs.getString("cashier_name"));
            inv.setTotal(rs.getDouble("total"));
            inv.setStatus(rs.getString("status"));
            list.add(inv);
        }

        c.close();
        return list;
    }
}