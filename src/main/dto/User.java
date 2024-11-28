package main.dto;

import main.base.Model;

public class User extends Model {
    
    public static enum Role {    
        NONE(0),
        ADMIN(1),
        USER(2);
        
        private final int value;

        Role(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
        
        public static Role fromValue(int value) {
            for (Role item : Role.values()) {
                if (item.value == value) {
                    return item;
                }
            }
            throw new IllegalArgumentException("Invalid value: " + value);
        }
    }
    
    private String username;
    private String password;
    private int role;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;

    public User(String id, String username, String password, int role, String fullName, String address, String phoneNumber, String email) {
        super(id);
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    
    public User(String id, String username, String password, Role role, String fullName, String address, String phoneNumber, String email) {
        super(id);
        this.username = username;
        this.password = password;
        this.role = role.getValue();
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
        return String.format("User: %s, %s, %s, %d, %s, %s, %s, %s.", 
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

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
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
