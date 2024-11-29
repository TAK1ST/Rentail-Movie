package main.dto;

import main.base.Model;
import main.constants.AccRole;
import main.constants.AccStatus;

public class Account extends Model {
    
    private String username;
    private String password;
    private String email;
    private AccRole role;
    private AccStatus status;

    public Account(String id, String username, String password, String email, AccRole role, AccStatus status) {
        super(id);
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.status = status;
    }

    public Account(Account other) {
        super(other.getId());
        this.username = other.username;
        this.password = other.password;
        this.email = other.email;
        this.role = other.role;
        this.status = other.status;
    }

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

    public AccRole getRole() {
        return role;
    }

    public void setRole(AccRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccStatus getStatus() {
        return status;
    }

    public void setStatus(AccStatus status) {
        this.status = status;
    }
}
