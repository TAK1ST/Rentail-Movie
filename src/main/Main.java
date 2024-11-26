
package main;

import java.io.IOException;
import main.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;
import main.controllers.Managers;
import main.models.Users;


public class Main {
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

    public static void main(String[] args) throws SQLException, IOException {
        connect();
        Managers.initAM();
        Managers.getAM().addActor();
        Managers.getAM().display(Managers.getAM().getList(), "Actor Managerment");
        
    }
}
