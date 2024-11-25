/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controllers;

import base.Manager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.models.User;
import main.models.User.Role;
import main.utils.DatabaseUtil;
import main.utils.Menu;
import main.utils.Menu.MenuAction;
import main.utils.Menu.MenuOption;
import static main.utils.Menu.showSuccess;
import main.utils.Utility;
import static main.utils.Utility.Console.getString;
import main.utils.Validator;

/**
 *
 * @author trann
 */
public class UserManager extends Manager<User> {
    
    private static final String DISPLAY_TITLE = "List of User";
      
    public UserManager() throws IOException {
        super(User.className());
        getAllUser();
        if (list.isEmpty()) 
            setDefaultUsers();
    }
    
    private void setDefaultUsers() throws IOException {
        list.add(new User("U00000", "admin", "1", Role.ADMIN, "None", null, null, null, null));
    }
      
    public void managerMenu() throws IOException {
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

    public boolean addUser(Role registorRole) throws IOException {
        
        String id = !list.isEmpty() ? Utility.generateID(list.getLast().getId(), "U") : "U00000";
        list.add(new User(
                id, 
                Validator.getUsername("Enter username: ", false, list), 
                Validator.getPassword("Enter password: ", false), 
                (registorRole == Role.ADMIN) ? Validator.getRole("Choose a role: ", false): registorRole, 
                getString("Enter status: ", false), 
                getString("Enter full name: ", false), 
                getString("Enter your address: ", false), 
                Validator.getPhoneNumber("Enter your phone number: ", false), 
                Validator.getEmail("Enter your email: ", false)));
        addUserToDB(list.getLast());
        return true;
    }

    public boolean updateUser() {
        if (checkEmpty(list)) return false;

        User foundUser = (User)getById("Enter user's id to update: ");
        if (checkNull(foundUser)) return false;

        String newUsername = Validator.getUsername("Enter new username: ", true, list);
        String newPassword = Validator.getPassword("Enter new password: ", true);
        Role newRole = Validator.getRole("Enter new role: ", true);
        String newStatus = getString("Enter status: ", true);
        String newFullName = getString("Enter full name: ", true);
        String newAddress = getString("Enter your address: ", true); 
        String newPhoneNumber = Validator.getPhoneNumber("Enter your phone number: ", true);
        String newEmail = Validator.getEmail("Enter your email: ", true);

        if (!newUsername.isEmpty()) foundUser.setUsername(newUsername);
        if (!newPassword.isEmpty()) foundUser.setPassword(newPassword);
        if (newRole != Role.NONE) foundUser.setRole(newRole.getValue());
        if (!newStatus.isEmpty()) foundUser.setStatus(newStatus);
        if (!newFullName.isEmpty()) foundUser.setFullName(newFullName);
        if (!newAddress.isEmpty()) foundUser.setAddress(newAddress);
        if (!newPhoneNumber.isEmpty()) foundUser.setPhoneNumber(newPhoneNumber);
        if (!newEmail.isEmpty()) foundUser.setEmail(newEmail);  

        updateUserFromDB(foundUser);
        return true;
    }

    public boolean deleteUser() throws IOException { 
        if (checkEmpty(list)) return false;

        User foundUser = (User)getById("Enter user's id to delete: ");
        if (checkNull(foundUser)) return false;

        list.remove(foundUser);
        deleteUserFromDB(foundUser.getId());
        return true;
    }

    public void searchUser() {
        if (checkEmpty(list)) return;

        display(getUserBy("Enter any user's propety to seach: "), DISPLAY_TITLE);
    }

    public void display(List<User> list, String title) {
        if (checkEmpty(list)) return;
        if (!title.isBlank()) Menu.showTitle(title);
        
        list.forEach((item) -> System.out.println(item));
    }

    public List<User> getUserBy(String message) {
        return searchBy(getString(message, false));
    }
    
    @Override
    public List<User> searchBy(String propety) {
        List<User> result = new ArrayList<>();
        for (User item : list) 
            if (item.getUsername().equals(propety) 
                    || item.getStatus().equals(propety)
                    || String.valueOf(item.getRole()).equals(propety)
                    || item.getFullName().equals(propety)
                    || item.getPhoneNumber().equals(propety)
                    || item.getEmail().equals(propety)
                    || item.getAddress().equals(propety)
            ) result.add(item);
        
        return result;
    }
    
        public boolean addUserToDB(User user) {
        String sql = "INSERT INTO User (userId, username, password, role, status, fullname, address, phoneNumber, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getId());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, user.getRole());
            preparedStatement.setString(5, user.getStatus());
            preparedStatement.setString(6, user.getFullName());
            preparedStatement.setString(7, user.getAddress());
            preparedStatement.setString(8, user.getPhoneNumber());
            preparedStatement.setString(9, user.getEmail());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateUserFromDB(User user) {
        String sql = "UPDATE User SET username = ?, password = ?, role = ?, status = ?, fullname = ?, address = ?, phoneNumber = ?, email = ? WHERE userId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setInt(3, user.getRole());
            preparedStatement.setString(4, user.getStatus());
            preparedStatement.setString(5, user.getFullName());
            preparedStatement.setString(6, user.getAddress());
            preparedStatement.setString(7, user.getPhoneNumber());
            preparedStatement.setString(8, user.getEmail());
            preparedStatement.setString(9, user.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteUserFromDB(String userID) {
        String sql = "DELETE FROM User WHERE userId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, userID);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void getAllUser() {
        String sql = "SELECT * FROM User";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                User user = new User(
                    resultSet.getString("userId"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getInt("role"),
                    resultSet.getString("status"),
                    resultSet.getString("fullname"),
                    resultSet.getString("address"),
                    resultSet.getString("phoneNumber"),
                    resultSet.getString("email")
                );
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}