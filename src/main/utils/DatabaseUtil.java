package main.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/movierentalsystemdb";
    private static final String USER = "root";
    private static final String PASSWORD = "Ngoduong2209";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();
            if (connection != null) {
                System.out.println("Connect Databases sucessfully!");

            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connect Fail!");
        }
        return connection;
    }

}
