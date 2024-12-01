package main.controllers;

import main.base.ListManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.constants.AccRole;
import main.constants.AccStatus;
import main.dao.AccountDAO;
import main.constants.Constants;
import static main.controllers.Managers.getPFM;
import main.dto.Account;
import main.utils.IDGenerator;
import static main.utils.Input.getString;
import static main.utils.Input.selectInfo;
import static main.utils.Input.yesOrNo;
import static main.utils.LogMessage.errorLog;
import static main.utils.PassEncryptor.encryptPassword;
import static main.utils.Utility.getEnumValue;
import main.utils.Validator;
import static main.utils.Validator.getEmail;
import static main.utils.Validator.getPassword;
import static main.utils.Validator.getUsername;

/**
 *
 * @author trann
 */
public class AccountManager extends ListManager<Account> {

    public AccountManager() throws IOException {
        super(Account.className());
        list = AccountDAO.getAllAccounts();
        setAdmin();
    }

    private void setAdmin() throws IOException {
        if (!list.isEmpty()) {
            for (Account item : list) {
                if (item.getRole() == AccRole.ADMIN) {
                    return;
                }
            }
        }
        list.add(new Account(
                Constants.DEFAULT_ADMIN_ID,
                "admin",
                encryptPassword("1"),
                "admin@gmail.com",
                AccRole.ADMIN,
                AccStatus.OFFLINE
        ));
        AccountDAO.addAccountToDB(list.getLast()); 
    }

    public boolean registorAccount() throws IOException {
        
        String username = getUsername("Enter username", false, list);
        if (username.isEmpty()) return false;
        
        String password = getPassword("Enter password", false);
        if (password.isEmpty()) return false;
        
        String email = getEmail("Enter email", false);
        if (password.isEmpty()) return false;
        
        String id = IDGenerator.generateAccID(list.isEmpty() ? "" : list.getLast().getId(), AccRole.CUSTOMER);
        if (yesOrNo("Fill in all infomation?")) {
            if (getPFM().addProfile(id)) {
                errorLog("Cannot registor account info");
                return false;
            }
        }

        list.add(new Account(
                id,
                username,
                encryptPassword(password),
                email,
                AccRole.CUSTOMER,
                AccStatus.OFFLINE
        ));
        return AccountDAO.addAccountToDB(list.getLast());
    }

    public boolean addAccount(AccRole registorRole) throws IOException {

        String username = getUsername("Enter username", false, list);
        if (username.isEmpty()) return false;
        
        String password = getPassword("Enter password", false);
        if (password.isEmpty()) return false;
        
        String email = getEmail("Enter your email", false);
        if (email.isEmpty()) return false;
        
        AccRole role = (registorRole == AccRole.ADMIN) ? (AccRole)getEnumValue("Choose a role", AccRole.class, false) : registorRole;
        String id = IDGenerator.generateAccID(list.isEmpty() ? "" : list.getLast().getId(), role);

        list.add(new Account(
                id,
                username,
                password,
                email,
                role,
                AccStatus.OFFLINE
        ));
        if (AccountDAO.addAccountToDB(list.getLast())) {
            if (list.getLast().getRole() == AccRole.ADMIN) {
                return true;
            }

            if (!getPFM().addProfile(id)) {
                errorLog("Cannot registor info");
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean updateAccount(String userID) {
        if (checkEmpty(list)) {
            return false;
        }

        Account foundAccount;
        if (userID.isEmpty()) {
            foundAccount = (Account) getById("Enter user's id");
        } else {
            foundAccount = (Account) searchById(userID);
        }
        if (checkNull(foundAccount)) {
            return false;
        }

        String newAccountname = getUsername("Enter new username", true, list);
        String newPassword = getPassword("Enter new password", true);

        AccRole newRole = AccRole.NONE;
        if (foundAccount.getRole() == AccRole.ADMIN) {
            newRole = (AccRole) getEnumValue("Choose a role", AccRole.class, true);
        }
        String newEmail = Validator.getEmail("Enter your email", true);

        if (!newAccountname.isEmpty()) {
            foundAccount.setUsername(newAccountname);
        }
        if (!newPassword.isEmpty()) {
            foundAccount.setPassword(encryptPassword(newPassword));
        }
        if (newRole != AccRole.NONE) {
            foundAccount.setRole(newRole);
        }
        if (!newEmail.isEmpty()) {
            foundAccount.setEmail(newEmail);
        }

        return AccountDAO.updateAccountInDB(foundAccount);
    }

    public void updatePassword(String accountID, String newPassword) {
        Account foundAccount = (Account) searchById(accountID);
        if (checkNull(foundAccount)) return;
        
        foundAccount.setPassword(newPassword);
        AccountDAO.updatePasswordInDB(accountID, newPassword);
    }

    public boolean deleteAccount() throws IOException {
        if (checkEmpty(list)) return false;
      
        Account foundAccount = (Account) getById("Enter user's id");
        if (checkNull(foundAccount)) return false;
       

        list.remove(foundAccount);
        return AccountDAO.deleteAccountFromDB(foundAccount.getId());
    }

    public void searchAccount() {
        display(getAccountBy("Enter any user's propety"));
    }

    public List<Account> getAccountBy(String message) {
        return searchBy(getString(message, false));
    }

    @Override
    public List<Account> searchBy(String propety) {
        List<Account> result = new ArrayList<>();
        for (Account item : list) {
            if (item.getId().equals(propety)
                    || (item.getUsername() != null && item.getUsername().equals(propety))
                    || (item.getEmail() != null && item.getEmail().equals(propety))
                    || String.valueOf(item.getRole()).equals(propety)
                    || String.valueOf(item.getStatus()).equals(propety)) {
                result.add(item);
            }
        }
        return result;
    }

    public void showMyProfile(String userID) {
        display(searchById(userID), "My Profile");
    }
    
    public List<Account> sortBy(String propety) {
        if (checkEmpty(list)) {
            return null;
        }
        
        List<Account> result = new ArrayList<>(list);
        switch (propety) {
            case "username":
                result.sort(Comparator.comparing(Account::getUsername));
                break;
            default:
                sortById();
                break;
        }
        return result;
    }


    @Override
    public void display(List<Account> tempList) {
        if (checkEmpty(tempList)) {
            return;
        }

        int usernameLength = 0;
        int emailLength = 0;
        for (Account item : list) {
            usernameLength = Math.max(usernameLength, item.getUsername().length());
            emailLength = Math.max(emailLength, item.getEmail().length());
        }
        
        int widthLength = 8 + usernameLength + 8 + emailLength + 8 + 16;
        
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.printf("\n| %-8s | %-" + usernameLength + "s | %-8s | %-" + emailLength + "s | %-8s |\n",
                "ID", "Username", "Role", "Email" , "Status");
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        for (Account item : tempList) {
            System.out.printf("\n| %-8s | %-" + usernameLength + "s | %-8s | %-" + emailLength + "s | %-8s |",
                    item.getId(),
                    item.getUsername(),
                    item.getRole(),
                    item.getEmail(),
                    item.getStatus());
        }
        System.out.println();
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.println();
    }
    
    public void show() {
        while (yesOrNo("Sort the list?")) {
            String[] options = new String[]{"username"};
            display(sortBy(selectInfo("Sort review by", options, true)));
        } 
}
}