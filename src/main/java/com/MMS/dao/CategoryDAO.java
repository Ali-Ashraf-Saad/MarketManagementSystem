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
}