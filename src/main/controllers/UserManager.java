
package main.controllers;

import main.base.ListManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import main.dao.AccountDAO;
import main.constants.Constants;
import main.dto.User;
import main.dto.User.Role;
import main.utils.IDGenerator;
import static main.utils.Input.getString;
import static main.utils.Input.yesOrNo;
import static main.utils.PassEncryptor.encryptPassword;
import main.utils.Validator;

/**
 *
 * @author trann
 */
public class UserManager extends ListManager<User> {
      
    public UserManager() throws IOException {
        super(User.className());
        list = AccountDAO.getAllUser();
        setAdmin();
    }
    
    private void setAdmin() throws IOException {
        if(!list.isEmpty())
            for (User item : list) 
                if (item.getRole() == 1)
                    return;
        
        list.add(new User(
                Constants.DEFAULT_ADMIN_ID, 
                "admin", 
                "1", 
                Role.ADMIN,  
                null, 
                null, 
                null, 
                null));
        AccountDAO.addUserToDB(list.getLast());
    }
    
    public boolean registorUser() {
        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "U");
        String username = Validator.getUsername("Enter username", false, list);
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
        
        list.add(new User(
                id, 
                username, 
                password, 
                Role.USER, 
                fullName, 
                address, 
                phoneNumber, 
                email));
        AccountDAO.addUserToDB(list.getLast());
        return true;
    }
public void displayUsers(List<User> users, String title) {
    System.out.println(title);
     System.out.println("|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");
    if (users.isEmpty()) {
        System.out.println("No users available.");
        return;
    }

    System.out.printf("|%-15s | %-20s | %-20s | %-10s | %-20s | %-20s | %-15s | %-20s |\n", 
            "User ID", "Username","Password", "Role", "Full Name", "Address", "Phone Number", "Email");
     System.out.println("|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");
    for (User user : users) {
        String role = user.getRole() == 1 ? "Admin" : "User";  // Chuyển đổi số vai trò thành tên vai trò
         System.out.printf("|%-15s | %-20s | %-20s | %-10s | %-20s | %-20s | %-15s | %-20s |\n", 
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                role,
                user.getFullName() != null ? user.getFullName() : "N/A",
                user.getAddress() != null ? user.getAddress() : "N/A",
                user.getPhoneNumber() != null ? user.getPhoneNumber() : "N/A",
                user.getEmail() != null ? user.getEmail() : "N/A"); 
    }
System.out.println("|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");
}


    public boolean addUser(Role registorRole) throws IOException {   
        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "U");
        list.add(new User(
                id, 
                Validator.getUsername("Enter username", false, list), 
                Validator.getPassword("Enter password", false), 
                (registorRole == Role.ADMIN) ? Validator.getRole("Choose a role", false): registorRole,  
                getString("Enter full name", false), 
                getString("Enter your address", false), 
                Validator.getPhoneNumber("Enter your phone number", false), 
                Validator.getEmail("Enter your email", false)));
        AccountDAO.addUserToDB(list.getLast());
        return true;
    }

    public boolean updateUser(String userID) {
        if (checkEmpty(list)) return false;

        User foundUser = null;
        if (userID.isEmpty()) {
            foundUser = (User)getById("Enter user's id");
        } else {
            foundUser = (User)searchById(userID);
        }        
        if (checkNull(foundUser)) return false;

        String newUsername = Validator.getUsername("Enter new username", true, list);
        String newPassword = Validator.getPassword("Enter new password", true);
        
        Role newRole = Role.NONE;
        if (foundUser.getRole() == Role.ADMIN.getValue())
            newRole = Validator.getRole("Enter new role", true);
        
        String newFullName = getString("Enter full name", true);
        String newAddress = getString("Enter your address", true); 
        String newPhoneNumber = Validator.getPhoneNumber("Enter your phone number", true);
        String newEmail = Validator.getEmail("Enter your email", true);

        if (!newUsername.isEmpty()) foundUser.setUsername(newUsername);
        if (!newPassword.isEmpty()) foundUser.setPassword(encryptPassword(newPassword));
        if (newRole != Role.NONE) foundUser.setRole(newRole.getValue());
        if (!newFullName.isEmpty()) foundUser.setFullName(newFullName);
        if (!newAddress.isEmpty()) foundUser.setAddress(newAddress);
        if (!newPhoneNumber.isEmpty()) foundUser.setPhoneNumber(newPhoneNumber);
        if (!newEmail.isEmpty()) foundUser.setEmail(newEmail);  

        AccountDAO.updateUserFromDB(foundUser);
        return true;
    }

    public boolean deleteUser() throws IOException { 
        if (checkEmpty(list)) return false;

        User foundUser = (User)getById("Enter user's id");
        if (checkNull(foundUser)) return false;

        list.remove(foundUser);
        AccountDAO.deleteUserFromDB(foundUser.getId());
        return true;
    }

    public void searchUser() {
        display(getUserBy("Enter any user's propety"), "Search Results");
    }

    public List<User> getUserBy(String message) {
        return searchBy(getString(message, false));
    }
    
    @Override
    public List<User> searchBy(String propety) {
        List<User> result = new ArrayList<>();
        for (User item : list) 
            if (item.getId().equals(propety)
                    || String.valueOf(item.getRole()).equals(propety)
                    || (item.getUsername()      != null && item.getUsername().equals(propety)) 
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
    
}