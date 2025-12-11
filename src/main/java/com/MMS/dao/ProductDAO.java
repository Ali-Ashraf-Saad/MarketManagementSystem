package com.MMS.dao;

import com.MMS.db.DBConnection;
import com.MMS.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public List<Product> getAll() throws SQLException {
        List<Product> list = new ArrayList<>();
        Connection c = DBConnection.getConnection();
        ResultSet rs = c.createStatement().executeQuery("SELECT * FROM products");
        while(rs.next()){
            list.add(new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getInt("category_id")
            ));
        }
        c.close(); return list;
    }

    public Product getById(int id) throws SQLException {
        Connection c = DBConnection.getConnection();
        PreparedStatement ps = c.prepareStatement("SELECT * FROM products WHERE id=?");
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            Product p = new Product(
                    rs.getInt("id"),rs.getString("name"),
                    rs.getDouble("price"),rs.getInt("quantity"),
                    rs.getInt("category_id"));
            c.close(); return p;
        }
        c.close(); return null;
    }
}