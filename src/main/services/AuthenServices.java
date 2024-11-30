package main.services;

import java.io.IOException;
import java.sql.SQLException;
import static main.controllers.Managers.getACM;
import main.dto.Account;
import static main.utils.Input.getString;
import static main.utils.LogMessage.errorLog;
import main.utils.Menu;

public class AuthenServices {

    public static Account login() throws SQLException {
        
        Menu.showTitle("Login");
        String input = getString("Enter username or email", false);
        String password = getString("Enter password", false);

        for (Account item : getACM().getList()) {
            if (input.equals(item.getUsername()) || input.equals(item.getEmail())) {
                if (password.equals(item.getPassword())) {
                    return new Account(item);
                }
            }
        }
        errorLog("Wrong username/email or password");
        return null;
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
