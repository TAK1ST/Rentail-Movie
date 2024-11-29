package main.dto;

import main.base.Model;
import main.constants.Role;

public class User extends Model {
    
    private String username;
    private String password;
    private Role role;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;
    
    public User(String id, String username, String password, Role role, String fullName, String address, String phoneNumber, String email) {
        super(id);
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    
    public User(User other) {
        super(other.getId());
        this.username = other.username;
        this.password = other.password;
        this.role = other.role;
        this.fullName = other.fullName;
        this.address = other.address;
        this.phoneNumber = other.phoneNumber;
        this.email = other.email;
    }
    
    @Override
    public String toString() {
        return String.format("User: %s, %s, %s, %s, %s, %s, %s, %s.", 
                super.getId(),
                username,
                password,
                role,
                fullName,
                address,
                phoneNumber,
                email);
    }
    
    @Override
    public Object[] getDatabaseValues() {
        return new Object[] {
            super.getId(),
            username,
            password,
            role,
            fullName,
            address,
            phoneNumber,
            email,
        };
    }
    
    public static String className() {
        return "User";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
