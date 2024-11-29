package main.controllers;

import main.base.ListManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
import main.dao.AccountDAO;
import main.constants.Constants;
import main.dto.Account;
import main.dto.Account.Role;
import main.dto.Account.Status;
=======
import main.constants.AccRole;
import main.constants.AccStatus;
import main.dao.AccountDAO;
import main.constants.Constants;
import main.dto.Account;
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
import main.utils.IDGenerator;
import static main.utils.Input.getString;
import static main.utils.Input.yesOrNo;
import static main.utils.PassEncryptor.encryptPassword;
<<<<<<< HEAD
=======
import static main.utils.Utility.getEnumValue;
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
import main.utils.Validator;

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
<<<<<<< HEAD
                if (item.getRole() == 1) {
=======
                if (item.getRole() == AccRole.ADMIN) {
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
                    return;
                }
            }
        }
<<<<<<< HEAD

=======
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
        list.add(new Account(
                Constants.DEFAULT_ADMIN_ID,
                "admin",
                "1",
<<<<<<< HEAD
                null,
<<<<<<< HEAD
                Role.ADMIN,
                Status.ONLINE));
=======
=======
                "admin@gmail.com",
>>>>>>> 0e27071236bd8733c57014037059c15ad6cbef83
                AccRole.ADMIN,
                AccStatus.OFF));
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
        AccountDAO.addAccountToDB(list.getLast());
    }

    public boolean registorAccount() {
        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "U");
        String username = Validator.getAccountName("Enter username", false, list);
        String password = Validator.getPassword("Enter password", false);

<<<<<<< HEAD
        String fullName, address, phoneNumber, email;
        if (yesOrNo("Fill in all infomation?")) {
            fullName = getString("Enter full name", false);
            address = getString("Enter your address", false);
            phoneNumber = Validator.getPhoneNumber("Enter your phone number", false);
            email = Validator.getEmail("Enter your email", false);
        } else {
            fullName = address = phoneNumber = email = null;
=======
        String fullName, phoneNumber, email;
        if (yesOrNo("Fill in all infomation?")) {
            fullName = getString("Enter full name", false);
            getString("Enter your address", false);
            phoneNumber = Validator.getPhoneNumber("Enter your phone number", false);
            email = Validator.getEmail("Enter your email", false);
        } else {
            fullName = phoneNumber = email = null;
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
        }

        list.add(new Account(
                id,
                username,
                password,
                email,
<<<<<<< HEAD
                Role.USER,
                Status.NONE));
=======
                AccRole.CUSTOMER,
                AccStatus.OFF));
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
        AccountDAO.addAccountToDB(list.getLast());
        return true;
    }

<<<<<<< HEAD
<<<<<<< HEAD
    public void displayAccounts(List<Account> users, String title) {
        System.out.println(title);
        System.out.println("|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");
        if (users.isEmpty()) {
            System.out.println("No users available.");
            return;
        }

        System.out.printf("|%-15s | %-20s | %-20s | %-10s | %-20s |\n",
                "Account ID", "Accountname", "Password", "Role", "Email");
        System.out.println("|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");
        for (Account user : users) {
            String role = user.getRole() == 1 ? "Admin" : "Account";  // Chuyển đổi số vai trò thành tên vai trò
            System.out.printf("|%-15s | %-20s | %-20s | %-10s | %-20s |\n",
                    user.getId(),
                    user.getAccountName(),
                    user.getPassword(),
                    role,
                    user.getEmail() != null ? user.getEmail() : "N/A");
        }
        System.out.println("|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");
    }

    public boolean addAccount(Role registorRole, Status registorStatus) throws IOException {
=======
    public boolean addAccount(AccRole registorRole, AccStatus registorStatus) throws IOException {
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
=======
    public boolean addAccount(AccRole registorRole) throws IOException {
>>>>>>> 0e27071236bd8733c57014037059c15ad6cbef83
        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "U");
        
        list.add(new Account(
                id,
                Validator.getAccountName("Enter username", false, list),
                Validator.getPassword("Enter password", false),
                Validator.getEmail("Enter your email", false),
<<<<<<< HEAD
                (registorRole == Role.ADMIN) ? Validator.getRole("Choose a role", false) : registorRole,
                (registorStatus == Status.ONLINE) ? Validator.getStatus("Choose a status", false) : registorStatus));

        // Add account to database
        AccountDAO.addAccountToDB(list.getLast());

        return true;
    }

    public boolean updateAccount(String userID) {
        if (checkEmpty(list)) {
            return false;
        }

=======
                (registorRole == AccRole.ADMIN) ? (AccRole)getEnumValue("Choose a role", AccRole.class, false) : registorRole,
                AccStatus.OFF
        ));
        
        return AccountDAO.addAccountToDB(list.getLast());
    }

    public boolean updateAccount(String userID) {
        if (checkEmpty(list)) return false;
        
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
        Account foundAccount = null;
        if (userID.isEmpty()) {
            foundAccount = (Account) getById("Enter user's id");
        } else {
            foundAccount = (Account) searchById(userID);
        }
        if (checkNull(foundAccount)) {
            return false;
        }

        String newAccountname = Validator.getAccountName("Enter new username", true, list);
        String newPassword = Validator.getPassword("Enter new password", true);

<<<<<<< HEAD
        Role newRole = Role.NONE;
        if (foundAccount.getRole() == Role.ADMIN.getValue()) {
            newRole = Validator.getRole("Enter new role", true);
=======
        AccRole newRole = AccRole.NONE;
        if (foundAccount.getRole() == AccRole.ADMIN) {
            newRole = (AccRole)getEnumValue("Choose a role", AccRole.class, true);
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
        }
        String newEmail = Validator.getEmail("Enter your email", true);

        if (!newAccountname.isEmpty()) {
<<<<<<< HEAD
            foundAccount.setAccountName(newAccountname);
=======
            foundAccount.setUsername(newAccountname);
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
        }
        if (!newPassword.isEmpty()) {
            foundAccount.setPassword(encryptPassword(newPassword));
        }
<<<<<<< HEAD
        if (newRole != Role.NONE) {
            foundAccount.setRole(newRole.getValue());
=======
        if (newRole != AccRole.NONE) {
            foundAccount.setRole(newRole);
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
        }
        if (!newEmail.isEmpty()) {
            foundAccount.setEmail(newEmail);
        }

<<<<<<< HEAD
        AccountDAO.updateAccountFromDB(foundAccount);
        return true;
    }

    public boolean deleteAccount() throws IOException {
        if (checkEmpty(list)) {
            return false;
        }

        Account foundAccount = (Account) getById("Enter user's id");
        if (checkNull(foundAccount)) {
            return false;
        }

        list.remove(foundAccount);
        AccountDAO.deleteAccountFromDB(foundAccount.getId());
        return true;
=======
        return AccountDAO.updateAccountInDB(foundAccount);
    }

    public boolean deleteAccount() throws IOException {
        if (checkEmpty(list)) return false;
        
        Account foundAccount = (Account) getById("Enter user's id");
        if (checkNull(foundAccount)) return false;
        
        list.remove(foundAccount);
        return AccountDAO.deleteAccountFromDB(foundAccount.getId());
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
    }

    public void searchAccount() {
        display(getAccountBy("Enter any user's propety"), "Search Results");
    }

    public List<Account> getAccountBy(String message) {
        return searchBy(getString(message, false));
    }

    @Override
    public List<Account> searchBy(String propety) {
        List<Account> result = new ArrayList<>();
        for (Account item : list) {
            if (item.getId().equals(propety)
<<<<<<< HEAD
                    || (item.getAccountName() != null && item.getAccountName().equals(propety))
=======
                    || (item.getUsername() != null && item.getUsername().equals(propety))
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
                    || (item.getEmail() != null && item.getEmail().equals(propety))
                    || String.valueOf(item.getRole()).equals(propety)
                    || String.valueOf(item.getStatus()).equals(propety)) {
                result.add(item);
            }
        }
<<<<<<< HEAD

=======
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
        return result;
    }

    public void showMyProfile(String userID) {
        display(searchById(userID), "My Profile");
    }
<<<<<<< HEAD
=======
    
    @Override
    public void display(List<Account> users, String title) {
        if (checkEmpty(list)) return;
        
        System.out.println(title);
        System.out.println("|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");
        System.out.printf("|%-15s | %-20s | %-20s | %-10s | %-20s |\n",
                "Account ID", "Accountname", "Password", "Role", "Email");
        System.out.println("|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");
        for (Account user : users) {
            String role = user.getRole() == AccRole.ADMIN ? "Admin" : "Account";  // Chuyển đổi số vai trò thành tên vai trò
            System.out.printf("|%-15s | %-20s | %-20s | %-10s | %-20s |\n",
                    user.getId(),
                    user.getUsername(),
                    user.getPassword(),
                    role,
                    user.getEmail() != null ? user.getEmail() : "N/A");
        }
        System.out.println("|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");
    }
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8

}
