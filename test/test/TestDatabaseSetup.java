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

    public static void addAccountDatabase() {
        try (Connection conn = getTestConnection(); Statement stmt = conn.createStatement()) {
            // Chỉnh sửa câu lệnh INSERT INTO
            stmt.execute("INSERT INTO Accounts (account_id, username, password, role, email, status, online_at, created_at, updated_at) "
                    + "VALUES ('PR000001', 'DuongNgo', '1','" + AccRole.PREMIUM + "'," +" 'DuongNgo@gmail.com', '" + AccStatus.ONLINE + "', NULL, NULL, NULL)");

        } catch (SQLException e) {
            throw new RuntimeException("Failed to setup test database", e);
        }
    }

}
