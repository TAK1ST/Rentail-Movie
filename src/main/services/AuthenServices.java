package main.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import main.config.Database;
import main.constants.IDPrefix;
import main.constants.account.AccRole;
import main.constants.account.AccStatus;
import main.controllers.Managers;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getPFM;
import main.dto.Account;
import main.dto.Profile;
import static main.utils.Input.getString;
import static main.utils.Input.yesOrNo;
import static main.utils.LogMessage.errorLog;
import static main.utils.LogMessage.successLog;
import main.utils.Menu;
import static main.utils.Utility.getEnumValue;
import static main.utils.Validator.getEmail;
import static main.utils.Validator.getPassword;
import static main.utils.Validator.getUsername;

public class AuthenServices {
    
    public static void init() {
        Managers.initACM();
        Managers.initPFM();
    }

    public static Account loginPannel() {
        Menu.showHeader("Login");

        String input = getString("Enter username or email");
        if (input == null) return null;
        
        String password = getString("Enter password");
        if (password == null) return null;

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

    public static Account registorPannel() {
        boolean checkCreate = true;

        Menu.showHeader("Register");
        String[] options = {"Register new account", "Back"};
        Menu.showOptions(options, 1);
        
        int input = Menu.getChoice("Enter choice", options.length);
        switch (input) {
            case 1:
                checkCreate = checkCreate && registorAccount(AccRole.CUSTOMER);
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

    public static boolean forgetPassword(String accountID) {
        Account account = (Account) getACM().getById(accountID);
        if (getACM().checkNull(account)) 
            return errorLog("Account not found", false);
        
        if (yesOrNo("Forgot password") && updatePassword(account))
            return successLog("Success change password", true);
        else 
            return false;
    }
    
    public static boolean updatePassword(Account account) {
        if (getACM().checkNull(account)) return false;
        
        String password = getPassword("Enter password", account.getPassword());
        if (password == null) return false;
        
        account.setPassword(password);
        return updatePasswordInDB(account.getId(), password);
    }
    
    public static boolean registorAccount(AccRole role) {

        String username = getUsername("Enter username", getACM().getList());
        if (username == null) return false;
        
        String password = getPassword("Enter password");
        if (password == null) return false;

        String email = getEmail("Enter email");
        if (email == null) return false;
        
        if (role == AccRole.ADMIN) {
            role = (AccRole) getEnumValue("Choose a role", AccRole.class);
            if (role == null) return false;
        } 
        
        String id = getACM().createID(IDPrefix.ACCOUNT_PREFIX);
        
        Account account = new Account(
                id,
                username,
                password,
                email,
                role,
                AccStatus.OFFLINE,
                LocalDate.now(),
                null,
                LocalDate.now(),
                100
        );
        if (getACM().add(account)) {
            return registorProfile(id);
        }
        return false;
    }
    
    private static boolean registorProfile(String accountID) {
        if (yesOrNo("Fill in all infomation?")) {
            if (!getPFM().addProfile(accountID)) {
                errorLog("Cannot registor account info");
                return false;
            }
        }
        getPFM().add(new Profile(accountID));
        return true;
    }
    
    public static boolean updatePasswordInDB(String accountID, String newPassword) {
        String sql = "UPDATE Accounts SET password = ? WHERE account_id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setString(2, accountID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateUsernameInDB(String accountID, String username) {
        String sql = "UPDATE Accounts SET username = ? WHERE account_id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, accountID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
}
