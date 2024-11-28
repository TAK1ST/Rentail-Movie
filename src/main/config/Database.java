package main.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import static main.utils.Log.errorLog;

public class Database {

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
            connection = getConnection();
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
