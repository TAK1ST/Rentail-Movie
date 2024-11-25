
package src.main;

import main.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;


public class Main {
    public static Connection connect() throws ClassNotFoundException {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Kết nối với cơ sở dữ liệu
            connection = DatabaseUtil.getConnection();
            if (connection != null) {
                System.out.println("Connect Database Sucessfully!");

            }
        } catch (SQLException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            System.out.println("Connect fail!");
        }
        return connection;
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        connect();
    }
}
