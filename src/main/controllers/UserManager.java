
package main.controllers;

import main.base.ListManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import main.dao.AccountDAO;
import main.constants.Constants;
import main.constants.AccRole;
import main.dto.Account;
import main.utils.IDGenerator;
import static main.utils.Input.getString;
import static main.utils.Input.yesOrNo;
import static main.utils.PassEncryptor.encryptPassword;
import main.utils.Validator;
import static main.utils.Validator.getFullName;

/**
 *
 * @author trann
 */
public class UserManager extends ListManager<Account> {
      
    public UserManager() throws IOException {
        super(Account.className());
        list = AccountDAO.getAllAccount();
        setAdmin();
    }
    
    private void setAdmin() throws IOException {
        if(!list.isEmpty())
            for (Account item : list) 
                if (item.getRole() == AccRole.ADMIN)
                    return;
        
        list.add(new Account(
                Constants.DEFAULT_ADMIN_ID, 
                "admin", 
                "1", 
                null, 
                AccRole.ADMIN,  
                null));
        AccountDAO.addAccountToDB(list.getLast());
    }
    
    public boolean registorCustomer() {
        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "U");
        String username = Validator.getAccountName("Enter username", false, list);
        String password = Validator.getPassword("Enter password", false);
        
        String fullName, address, phoneNumber, email;
        if (yesOrNo("Fill in all infomation?")) {
            fullName = getFullName("Enter full name", false);
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
                AccRole.CUSTOMER, 
                fullName, 
                address, 
                phoneNumber, 
                email));
        AccountDAO.addAccountToDB(list.getLast());
        return true;
    }

    public boolean addAccount(AccRole registorRole) throws IOException {   
        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "U");
        list.add(new Account(
                id, 
                Validator.getAccountName("Enter username", false, list), 
                Validator.getPassword("Enter password", false), 
                (registorRole == AccRole.ADMIN) ? Validator.getRole("Choose a role", false): registorRole,  
                getFullName("Enter full name", false), 
                getString("Enter your address", false), 
                Validator.getPhoneNumber("Enter your phone number", false), 
                Validator.getEmail("Enter your email", false)));
        AccountDAO.addAccountToDB(list.getLast());
        return true;
    }

    public boolean updateAccount(String userID) {
        if (checkEmpty(list)) return false;

        Account foundAccount = null;
        if (userID.isEmpty()) {
            foundAccount = (Account)getById("Enter user's id");
        } else {
            foundAccount = (Account)searchById(userID);
        }        
        if (checkNull(foundAccount)) return false;

        String newAccountName = Validator.getAccountName("Enter new username", true, list);
        String newPassword = Validator.getPassword("Enter new password", true);
        
        AccRole newRole = AccRole.NONE;
        if (foundAccount.getRole() == AccRole.ADMIN)
            newRole = Validator.getRole("Enter new role", true);
        
        String newFullName = getString("Enter full name", true);
        String newAddress = getString("Enter your address", true); 
        String newPhoneNumber = Validator.getPhoneNumber("Enter your phone number", true);
        String newEmail = Validator.getEmail("Enter your email", true);

        if (!newAccountName.isEmpty()) foundAccount.setAccountName(newAccountName);
        if (!newPassword.isEmpty()) foundAccount.setPassword(encryptPassword(newPassword));
        if (newRole != AccRole.NONE) foundAccount.setRole(newRole);
        if (!newFullName.isEmpty()) foundAccount.setFullName(newFullName);
        if (!newAddress.isEmpty()) foundAccount.setAddress(newAddress);
        if (!newPhoneNumber.isEmpty()) foundAccount.setPhoneNumber(newPhoneNumber);
        if (!newEmail.isEmpty()) foundAccount.setEmail(newEmail);  

        AccountDAO.updateAccountFromDB(foundAccount);
        return true;
    }

    public boolean deleteAccount() throws IOException { 
        if (checkEmpty(list)) return false;

        Account foundAccount = (Account)getById("Enter user's id");
        if (checkNull(foundAccount)) return false;

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
        for (Account item : list) 
            if (item.getId().equals(propety)
                    || String.valueOf(item.getRole()).equals(propety)
                    || (item.getAccountName()      != null && item.getAccountName().equals(propety)) 
                    || (item.getFullName()      != null && item.getFullName().equalsIgnoreCase(propety))
                    || (item.getPhoneNumber()   != null && item.getPhoneNumber().equals(propety))
                    || (item.getEmail()         != null && item.getEmail().equals(propety))
                    || (item.getAddress()       != null && item.getAddress().trim().toLowerCase().contains(propety))
            ) result.add(item);
        
        return result;
    }
    
    public void showMyProfile(String userID) {
        display(searchById(userID), "My Profile");
    }
    
    @Override
    public void display(List<Account> users, String title) {
        if (checkEmpty(list)) return;
        
        System.out.println(title);
        System.out.println("|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");
        System.out.printf("|%-15s | %-20s | %-20s | %-10s | %-20s | %-20s | %-15s | %-20s |\n", 
                "Account ID", "AccountName","Password", "Role", "Full Name", "Address", "Phone Number", "Email");
        System.out.println("|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");
        for (Account user : users) {
            String role = user.getRole() == AccRole.ADMIN ? "Admin" : "Account";
             System.out.printf("|%-15s | %-20s | %-20s | %-10s | %-20s | %-20s | %-15s | %-20s |\n", 
                    user.getId(),
                    user.getAccountName(),
                    user.getPassword(),
                    role,
                    user.getFullName() != null ? user.getFullName() : "N/A",
                    user.getAddress() != null ? user.getAddress() : "N/A",
                    user.getPhoneNumber() != null ? user.getPhoneNumber() : "N/A",
                    user.getEmail() != null ? user.getEmail() : "N/A"); 
        }
        System.out.println("|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");
    }
    
}