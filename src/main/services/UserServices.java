package main.services;

import base.ListManager;
import constants.Constants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import main.DAO.UserDAO;
import main.models.User;
import main.models.User.Role;
import main.utils.IDGenerator;
import main.utils.Menu;
import main.utils.Menu.MenuOption;
import static main.utils.Menu.showSuccess;
import static main.utils.PassEncryptor.encryptPassword;
import static main.utils.Utility.Console.getString;
import static main.utils.Utility.Console.yesOrNo;
import main.utils.Validator;

/**
 *
 * @author trann
 */
public class UserServices extends ListManager<User> {
      
    public UserServices() throws IOException {
        super(User.className());
        list = UserDAO.getAllUser();
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
        UserDAO.addUserToDB(list.getLast());
    }
      
    public void adminMenu() throws IOException {
        Menu.showManagerMenu(
            "User Managment",
            null,
            new MenuOption[]{
                new MenuOption("Add User", () -> showSuccess(addUser(Role.ADMIN)), true),
                new MenuOption("Delete User", () -> showSuccess(deleteUser()), true),
                new MenuOption("Update User", () -> showSuccess(updateUser("")), true),
                new MenuOption("Search User", () -> searchUser(), true),
                new MenuOption("Display Users", () -> display(list, "List of Users"), false),
                new MenuOption("Back", () -> { /* Exit action */ }, false)
            },
            null
        );
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
        UserDAO.addUserToDB(list.getLast());
        return true;
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
        UserDAO.addUserToDB(list.getLast());
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

        UserDAO.updateUserFromDB(foundUser);
        return true;
    }

    public boolean deleteUser() throws IOException { 
        if (checkEmpty(list)) return false;

        User foundUser = (User)getById("Enter user's id");
        if (checkNull(foundUser)) return false;

        list.remove(foundUser);
        UserDAO.deleteUserFromDB(foundUser.getId());
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
                    || item.getUsername().equals(propety) 
                    || String.valueOf(item.getRole()).equals(propety)
                    || item.getFullName().equals(propety)
                    || item.getPhoneNumber().equals(propety)
                    || item.getEmail().equals(propety)
                    || item.getAddress().equals(propety)
            ) result.add(item);
        
        return result;
    }
    
    public void showMyProfile(String userID) {
        display(searchById(userID), "My Profile");
    }
    
}