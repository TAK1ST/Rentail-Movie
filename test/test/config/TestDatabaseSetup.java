/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import main.constants.AccRole;
import main.constants.AccStatus;

public class TestDatabaseSetup {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/movierentalsystemdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1";
    
    public static Connection getTestConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    public static void setupTestDatabase() {
        try (Connection conn = getTestConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute ("INSERT INTO Accounts ("
                + "AD000001,"
                + "Kiet"
                + "1"
                + "Kiet@gmail.com"
                + AccRole.ADMIN
                + AccStatus.ONLINE
                + null
                + null
                + null
                );
//            // Create test tables
//            stmt.execute("CREATE DATABASE IF NOT EXISTS movierentalsystemdb");
//            stmt.execute("USE movierentalsystemdb");
//            
//            // Copy your table creation SQL here, but add _test suffix
//            // Example:
//            stmt.execute("CREATE TABLE IF NOT EXISTS Movies LIKE movierentalsystemdb.Movies");
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to setup test database", e);
        }
    }
    
    public static void cleanTestDatabase() {
        try (Connection conn = getTestConnection();
             Statement stmt = conn.createStatement()) {
            
            // Clean up test data
            stmt.execute("TRUNCATE TABLE Movies");
            // Add other tables as needed
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clean test database", e);
        }
    }
}
