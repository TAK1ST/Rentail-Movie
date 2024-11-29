package main.dto;

import main.base.Model;

public class Account extends Model {

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
    
    public static enum Status {
        NONE(0),
        BANNED(1),
        OFFLINE(2),
        ONLINE(3);

        private final int value;

        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Status fromValue(int value) {
            for (Status item : Status.values()) {
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
    private String email;
    private int status;

    //Constructors
    public Account(String id, String email, String password, String username,int role, int status) {
        super(id);
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.status = status;
    }

    public Account(String id, String username, String password, String email,Role role, Status status) {
        super(id);
        this.username = username;
        this.password = password;
        this.role = role.getValue();
        this.email = email;
        this.status = status.getValue();
    }

    public Account(Account other) {
        super(other.getId());
        this.username = other.username;
        this.password = other.password;
        this.role = other.role;
        this.email = other.email;
        this.status = other.status;
    }

    //Methods
    @Override
    public String toString() {
        return String.format("Account: %s, %s, %s, %s, %s, %s.",
                super.getId(),
                username,
                password,
                role,
                email,
                status);
    }

    @Override
    public Object[] getDatabaseValues() {
        return new Object[]{
            super.getId(),
            username,
            password,
            role,
            email,
            status
        };
    }

    public static String className() {
        return "Account";
    }

    public String getAccountName() {
        return username;
    }

    public void setAccountName(String username) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
