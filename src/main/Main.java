
package main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import main.utils.DatabaseUtil;


public class Main {

    public static void main(String[] args) throws SQLException {
        Connection conn = null;
        try {
            // Kết nối với cơ sở dữ liệu
            conn = DatabaseUtil.getConnection();
            if (conn != null) {
                System.out.println("Kết nối tới cơ sở dữ liệu thành công!");
               
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Kết nối thất bại!");
        } finally {
            // Đóng kết nối
            DatabaseUtil.closeConnection(conn);
        }
    }
    
}
