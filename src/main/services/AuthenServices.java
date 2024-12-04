package main.services;

import static main.controllers.Managers.getACM;
import main.dto.Account;
import static main.utils.Input.getString;
import static main.utils.Input.yesOrNo;
import static main.utils.LogMessage.errorLog;
import static main.utils.LogMessage.successLog;
import main.utils.Menu;
import static main.utils.Validator.getPassword;

public class AuthenServices {

    public static Account login() {
        Menu.showHeader("Login");
        String input = getString("Enter username or email", false);
        if (input.isEmpty()) {
            return null;
        }

        String password = getString("Enter password", false);
        if (password.isEmpty()) {
            return null;
        }

        for (Account item : getACM().getList()) {
            if (input.equals(item.getUsername()) || input.equals(item.getEmail())) {
                if (password.equals(item.getPassword())) {
                    return new Account(item);
                } else {
                    errorLog("Wrong username/email or password");
                    forgetPassword(item.getId());
                }

            }
        }
        return null;
    }

    public static Account loginTest(String input, String password) {
        Menu.showHeader("Login");
        if (input.isEmpty()) {
            return null;
        }

        if (password.isEmpty()) {
            return null;
        }

        for (Account item : getACM().getList()) {
            if (input.equals(item.getUsername()) || input.equals(item.getEmail())) {
                if (password.equals(item.getPassword())) {
                    return new Account(item);
                } else {
                    errorLog("Wrong username/email or password");
                    forgetPassword(item.getId());
                }

            }
        }
        return null;
    }

    public static Account registor() {
        boolean checkCreate = true;

        Menu.showHeader("Register");
        String[] options = {"Register new account", "Back"};
        Menu.showOptions(options, 1);
        int input = Menu.getChoice("Enter choice", options.length);
        switch (input) {
            case 1:
                checkCreate = checkCreate && getACM().registorAccount();
                break;
            default:
                return null;
        }

        if (!checkCreate) {
            errorLog("Cannot register account");
            return null;
        } else {
            successLog("Registration successful!");
        }

        return getACM().getList().getLast();
    }

    public static void forgetPassword(String accountID) {
        if (yesOrNo("Forgot password")) {
            String newPassword = getPassword("Enter new password", false);
            if (newPassword.isEmpty()) {
                return;
            }
            getACM().updatePassword(accountID, newPassword);
        }
    }
}
