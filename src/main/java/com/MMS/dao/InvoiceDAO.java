package com.MMS.dao;

import com.MMS.model.Invoice;
import java.sql.*;

public class InvoiceDAO {

    public int add(Connection conn, Invoice inv) throws SQLException {
        String sql = "INSERT INTO invoices(invoice_number,customer_name,cashier_name,total,status) VALUES (?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, inv.getInvoiceNumber());
        ps.setString(2, inv.getCustomerName());
        ps.setString(3, inv.getCashierName());
        ps.setDouble(4, 0.0);
        ps.setString(5, "OPEN");
        ps.executeUpdate();

        ResultSet keys = ps.getGeneratedKeys();
        if(keys.next()) return keys.getInt(1);
        return -1;
    }
}