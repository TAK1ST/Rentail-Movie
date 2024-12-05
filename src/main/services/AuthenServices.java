package main.services;

import java.time.LocalDate;
import main.constants.IDPrefix;
import main.constants.account.AccRole;
import main.constants.account.AccStatus;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getPFM;
import main.dao.AccountDAO;
import main.dto.Account;
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

    public static Account loginPannel() {
        Menu.showHeader("Login");

        String input = getString("Enter username or email", null);
        if (input.isEmpty()) return null;
        
        String password = getString("Enter password", null);
        if (password.isEmpty()) return null;

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
        if (yesOrNo("Forgot password")) {
            String newPassword = getPassword("Enter new password", null);
            if (newPassword == null) 
                return false;
            
            return updatePassword(accountID, newPassword);
        }
        return false;
    }
    
    public static boolean updatePassword(String accountID, String newPassword) {
        Account foundAccount = (Account) getACM().searchById(accountID);
        if (getACM().checkNull(foundAccount)) return false;
        
        foundAccount.setPassword(newPassword);
        return AccountDAO.updatePasswordInDB(accountID, newPassword);
    }
    
    public static boolean registorAccount(AccRole role) {

        String username = getUsername("Enter username", null, getACM().getList());
        if (username.isEmpty()) return false;
        
        String password = getPassword("Enter password", null);
        if (password.isEmpty()) return false;

        String email = getEmail("Enter email", null);
        if (email.isEmpty()) return false;
        
        if (role == AccRole.ADMIN) {
            role = (AccRole) getEnumValue("Choose a role", AccRole.class, role);
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
            if (getPFM().addProfile(accountID)) {
                errorLog("Cannot registor account info");
                return false;
            }
        }
        return true;
    }
    
}
