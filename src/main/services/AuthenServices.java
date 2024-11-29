package main.services;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static main.config.Database.getConnection;
import static main.controllers.Managers.getACM;
import main.dto.Account;
import static main.utils.Input.getString;
import static main.utils.LogMessage.errorLog;
import main.utils.Menu;
import main.constants.AccRole;
import main.constants.AccStatus;
import static main.utils.PassEncryptor.validatePassword;

public class AuthenServices {

    public static Account login() throws SQLException {
        Account account = null;

        Menu.showTitle("Login");
        String input = getString("Enter username or email", false);
        String password = getString("Enter password", false);

        // connect database 
        Connection connection = getConnection();

        try {
            String query = "SELECT * FROM accounts WHERE username = ? OR email = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, input);  
            stmt.setString(2, input);  
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                if (validatePassword(password,storedPassword)) {
                    String id = rs.getString("id");
                    String username = rs.getString("username");
                    String email = rs.getString("email");
                    AccRole role = AccRole.valueOf(rs.getString("role"));
                    AccStatus status = AccStatus.valueOf(rs.getString("status"));
                    account = new Account(id, username, storedPassword, email, role, status);
                } else {
                    errorLog("Wrong username or password");
                }
            } else {
                errorLog("User not found");
            }
        } catch (SQLException e) {
            errorLog("Database error: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();  
                }
            } catch (SQLException e) {
                errorLog("Error closing database connection: " + e.getMessage());
            }
        }

        return account;
    }

    public static Account registor() throws IOException {
        boolean checkCreate = true;

        Menu.showTitle("Register");
        String[] options = {"Register new account", "Back"};
        Menu.showOptions(options, 1);
        int input = Menu.getChoice("Enter choice", options.length);
        switch (input) {
            case 1:
                checkCreate = checkCreate && getACM().registorAccount();
                break;
            case 2:
                return null;
        }

        if (!checkCreate) {
            errorLog("Cannot register account");
            return null;
        } else {
            System.out.println("Registration successful!");
        }

        return getACM().getList().getLast();
    }
}
