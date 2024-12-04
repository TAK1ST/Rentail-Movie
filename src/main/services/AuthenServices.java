package main.services;

import main.constants.account.AccRole;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getPFM;
import main.dao.AccountDAO;
import main.dto.Account;
import main.dto.Profile;
import static main.utils.Input.getString;
import static main.utils.Input.yesOrNo;
import static main.utils.LogMessage.errorLog;
import static main.utils.LogMessage.successLog;
import main.utils.Menu;
import static main.utils.Validator.getPassword;

public class AuthenServices {

    public static Account login() {
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
                    if (forgetPassword(item.getId())) {
                        successLog("Change password complete");
                        return login();
                    }
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
                checkCreate = checkCreate && registorAccount();
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
    
    public static boolean registorAccount() {
        Account account = getACM().getInputs(new boolean[] {true, true, true, false}, null);
        if (getACM().checkNull(account)) return false;
        
        getACM().getList().add(account);
        if (AccountDAO.addAccountToDB(account)) {
            if (account.getRole() == AccRole.ADMIN) return true;
            return registorProfile(account.getId());
        }
        return false;
    }
    
    public static boolean registorProfile(String accountID) {
        if (yesOrNo("Fill in all infomation?")) {
            if (getPFM().add(new Profile(accountID))) {
                errorLog("Cannot registor account info");
                return false;
            }
        }
        return true;
    }
    
    
}
