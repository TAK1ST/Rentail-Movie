package main.controllers;

import main.base.ListManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import main.dao.AccountDAO;
import main.constants.Constants;
import main.dto.Account;
import main.dto.Account.Role;
import main.dto.Account.Status;
import main.utils.IDGenerator;
import static main.utils.Input.getString;
import static main.utils.Input.yesOrNo;
import static main.utils.PassEncryptor.encryptPassword;
import main.utils.Validator;

/**
 *
 * @author trann
 */
public class AccountManager extends ListManager<Account> {

    public AccountManager() throws IOException {
        super(Account.className());
        list = AccountDAO.getAllAccount();
        setAdmin();
    }

    private void setAdmin() throws IOException {
        if (!list.isEmpty()) {
            for (Account item : list) {
                if (item.getRole() == 1) {
                    return;
                }
            }
        }

        list.add(new Account(
                Constants.DEFAULT_ADMIN_ID,
                "admin@gmail.com",
                "1",
                null,
                Role.ADMIN,
                Status.ONLINE));
        AccountDAO.addAccountToDB(list.getLast());
    }

    public boolean registorAccount() {
        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "U");
        String username = Validator.getAccountName("Enter username", false, list);
        String password = Validator.getPassword("Enter password", false);

        String fullName, address, phoneNumber, email;
        if (yesOrNo("Fill in all infomation?")) {
            fullName = getString("Enter full name", false);
            address = getString("Enter your address", false);
            phoneNumber = Validator.getPhoneNumber("Enter your phone number", false);
            email = Validator.getEmail("Enter your email", false);
        } else {
            fullName = address = phoneNumber = email = null;
        }

        list.add(new Account(
                id,
                username,
                password,
                email,
                Role.USER,
                Status.NONE));
        AccountDAO.addAccountToDB(list.getLast());
        return true;
    }

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
        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "U");
        
        list.add(new Account(
                id,
                Validator.getAccountName("Enter username", false, list),
                Validator.getPassword("Enter password", false),
                Validator.getEmail("Enter your email", false),
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

        Role newRole = Role.NONE;
        if (foundAccount.getRole() == Role.ADMIN.getValue()) {
            newRole = Validator.getRole("Enter new role", true);
        }
        String newEmail = Validator.getEmail("Enter your email", true);

        if (!newAccountname.isEmpty()) {
            foundAccount.setAccountName(newAccountname);
        }
        if (!newPassword.isEmpty()) {
            foundAccount.setPassword(encryptPassword(newPassword));
        }
        if (newRole != Role.NONE) {
            foundAccount.setRole(newRole.getValue());
        }
        if (!newEmail.isEmpty()) {
            foundAccount.setEmail(newEmail);
        }

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
                    || (item.getAccountName() != null && item.getAccountName().equals(propety))
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

}
