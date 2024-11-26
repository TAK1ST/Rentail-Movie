package main.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/movierentalsystemdb";
    private static final String USER = "root";
    private static final String PASSWORD = "1";

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
            // Kết nối với cơ sở dữ liệu
            connection = DatabaseUtil.getConnection();
            if (connection != null) {
                System.out.println("Connect Databases sucessfully!");

            }
        } catch (SQLException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            System.out.println("Connect Fail!");
        }
        return connection;
    }

}
