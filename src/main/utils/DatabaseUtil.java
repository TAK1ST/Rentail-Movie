package main.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import static main.utils.Utility.errorLog;

public class DatabaseUtil {

<<<<<<< HEAD
    private static final String URL = "jdbc:mysql://mysql:3307/movierentalsystemdb";
    private static final String USER = "root";
    private static final String PASSWORD = "1";

=======
>>>>>>> ab5b0abf2aeffc2a2ad4bbfcca42519554fd3984
    public static Connection getConnection() throws SQLException {
        Map<String, String> envVariables = EnvReader.readEnvFile();

        String dbUrl = envVariables.get("DB_URL");
        String dbUser = envVariables.get("DB_USER");
        String dbPassword = envVariables.get("DB_PASSWORD");
        
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
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
            errorLog("Connect Fail!");
        }
        return connection;
    }

}
