/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.services;

import base.ListManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import main.CRUD.UserCRUD;
import main.models.Users;
import main.models.Users.Role;
import main.utils.IDGenerator;
import main.utils.Menu;
import main.utils.Menu.MenuAction;
import main.utils.Menu.MenuOption;
import static main.utils.Menu.showSuccess;
import static main.utils.Utility.Console.getString;
import static main.utils.Utility.Console.yesOrNo;
import main.utils.Validator;

/**
 *
 * @author trann
 */
public class UserServices extends ListManager<Users> {
    
    private static final String DISPLAY_TITLE = "List of User";
      
    public UserServices() throws IOException {
        super(Users.className());
        UserCRUD.getAllUser();
        if (list.isEmpty()) 
            setDefaultUsers();
    }
    
    private void setDefaultUsers() throws IOException {
        list.add(new Users(
                IDGenerator.generateID("", "U"), 
                "admin", 
                "1", 
                Role.ADMIN,  
                null, 
                null, 
                null, 
                null));
    }
      
    public void adminMenu() throws IOException {
        Menu.showManagerMenu(
            "User Managment",
            null,
            new MenuOption[]{
                new MenuOption("Add User", () -> showSuccess(addUser(Role.ADMIN))),
                new MenuOption("Delete User", () -> showSuccess(deleteUser())),
                new MenuOption("Update User", () -> showSuccess(updateUser())),
                new MenuOption("Search User", () -> searchUser()),
                new MenuOption("Display Users", () -> display(list, DISPLAY_TITLE)),
                new MenuOption("Back", () -> { /* Exit action */ })
            },
            new MenuAction[] { () -> Menu.getSaveMessage(isNotSaved) },
            true
        );
    }
    
    public boolean registorUser() {
        String id = !list.isEmpty() ? IDGenerator.generateID(list.getLast().getId(), "U") : "U00000";
        String username = Validator.getUsername("Enter username: ", false, list);
        String password = Validator.getPassword("Enter password: ", false);
        
        String fullName, address, phoneNumber, email;
        if (yesOrNo("Fill in all infomation? (y/n): ")) {
            fullName = getString("Enter full name: ", false);
            address = getString("Enter your address: ", false);
            phoneNumber = Validator.getPhoneNumber("Enter your phone number: ", false);
            email = Validator.getEmail("Enter your email: ", false);
        } else {
            fullName = address = phoneNumber = email = null;
        }
        
        list.add(new Users(
                id, 
                username, 
                password, 
                Role.USER, 
                fullName, 
                address, 
                phoneNumber, 
                email));
        UserCRUD.addUserToDB(list.getLast());
        return true;
    }

    public boolean addUser(Role registorRole) throws IOException {
        
        String id = !list.isEmpty() ? IDGenerator.generateID(list.getLast().getId(), "U") : "U00000";
        list.add(new Users(
                id, 
                Validator.getUsername("Enter username: ", false, list), 
                Validator.getPassword("Enter password: ", false), 
                (registorRole == Role.ADMIN) ? Validator.getRole("Choose a role: ", false): registorRole,  
                getString("Enter full name: ", false), 
                getString("Enter your address: ", false), 
                Validator.getPhoneNumber("Enter your phone number: ", false), 
                Validator.getEmail("Enter your email: ", false)));
        UserCRUD.addUserToDB(list.getLast());
        return true;
    }

    public boolean updateUser() {
        if (checkEmpty(list)) return false;

        Users foundUser = (Users)getById("Enter user's id to update: ");
        if (checkNull(foundUser)) return false;

        String newUsername = Validator.getUsername("Enter new username: ", true, list);
        String newPassword = Validator.getPassword("Enter new password: ", true);
        Role newRole = Validator.getRole("Enter new role: ", true);
        String newFullName = getString("Enter full name: ", true);
        String newAddress = getString("Enter your address: ", true); 
        String newPhoneNumber = Validator.getPhoneNumber("Enter your phone number: ", true);
        String newEmail = Validator.getEmail("Enter your email: ", true);

        if (!newUsername.isEmpty()) foundUser.setUsername(newUsername);
        if (!newPassword.isEmpty()) foundUser.setPassword(newPassword);
        if (newRole != Role.NONE) foundUser.setRole(newRole.getValue());
        if (!newFullName.isEmpty()) foundUser.setFullName(newFullName);
        if (!newAddress.isEmpty()) foundUser.setAddress(newAddress);
        if (!newPhoneNumber.isEmpty()) foundUser.setPhoneNumber(newPhoneNumber);
        if (!newEmail.isEmpty()) foundUser.setEmail(newEmail);  

        UserCRUD.updateUserFromDB(foundUser);
        return true;
    }

    public boolean deleteUser() throws IOException { 
        if (checkEmpty(list)) return false;

        Users foundUser = (Users)getById("Enter user's id to delete: ");
        if (checkNull(foundUser)) return false;

        list.remove(foundUser);
        UserCRUD.deleteUserFromDB(foundUser.getId());
        return true;
    }

    public void searchUser() {
        if (checkEmpty(list)) return;

        display(getUserBy("Enter any user's propety to seach: "), DISPLAY_TITLE);
    }

    public void display(List<Users> list, String title) {
        if (checkEmpty(list)) return;
        if (!title.isBlank()) Menu.showTitle(title);
        
        list.forEach((item) -> System.out.println(item));
    }

    public List<Users> getUserBy(String message) {
        return searchBy(getString(message, false));
    }
    
    @Override
    public List<Users> searchBy(String propety) {
        List<Users> result = new ArrayList<>();
        for (Users item : list) 
            if (item.getUsername().equals(propety) 
                    || String.valueOf(item.getRole()).equals(propety)
                    || item.getFullName().equals(propety)
                    || item.getPhoneNumber().equals(propety)
                    || item.getEmail().equals(propety)
                    || item.getAddress().equals(propety)
            ) result.add(item);
        
        return result;
    }
    
}