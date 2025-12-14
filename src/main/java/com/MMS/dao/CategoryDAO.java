package com.MMS.dao;

import com.MMS.db.DBConnection;
import com.MMS.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    public List<Category> getAll() throws SQLException {
        List<Category> list = new ArrayList<>();
        Connection c = DBConnection.getConnection();
        ResultSet rs = c.createStatement().executeQuery("SELECT * FROM categories");
        while(rs.next()) list.add(new Category(rs.getInt("id"),rs.getString("name")));
        c.close(); return list;
    }
    public void add(String name) throws SQLException {
    Connection c = DBConnection.getConnection();
    PreparedStatement ps =
            c.prepareStatement("INSERT INTO categories(name) VALUES(?)");
    ps.setString(1, name);
    ps.executeUpdate();
    c.close();
}
}