package com.librarymanagement;

import com.librarymanagement.config.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class TestDBConnection {
    public static void main(String[] args) {
        try (Connection connection = DBConnection.getConnection()) {
            System.out.println("Database connected successfully.");
        } catch (SQLException e) {
            System.out.println("Database connection failed.");
            System.out.println("Reason: " + e.getMessage());
        }
    }
}
