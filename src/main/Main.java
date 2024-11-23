
package main;

import main.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;


public class Main {
    public static Connection connect() {
        Connection connection = null;
        try {
            // Kết nối với cơ sở dữ liệu
            connection = DatabaseUtil.getConnection();
            if (connection != null) {
                System.out.println("Kết nối tới cơ sở dữ liệu thành công!");

            }
        } catch (SQLException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            System.out.println("Kết nối thất bại!");
        }
        return connection;
    }

    public static void main(String[] args) throws SQLException {
        connect();
    }
}
