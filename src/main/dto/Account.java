package main.dto;

import java.time.LocalDate;
import main.base.Model;
import main.constants.AccRole;
import main.constants.AccStatus;
import main.utils.Validator;

public class Account extends Model {
    
    private String username;
    private String password;
    private String email;
    private AccRole role;
    private AccStatus status;
    private LocalDate createDate;
    private LocalDate updateDate;

    public Account(
            String id, 
            String username, 
            String password, 
            String email, 
            AccRole role, 
            AccStatus status, 
            LocalDate createDate, 
            LocalDate updateDate) 
    {
        super(id);
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.status = status;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public Account(Account other) {
        super(other.getId());
        this.username = other.username;
        this.password = other.password;
        this.email = other.email;
        this.role = other.role;
        this.status = other.status;
        this.createDate = other.createDate;
        this.updateDate = other.updateDate;
    }

    @Override
    public String toString() {
        return String.format("Account: %s, %s, %s, %s, %s, %s, %s, %s.",
                super.getId(),
                username,
                password,
                email,
                role,
                status,
                createDate.format(Validator.DATE),
                updateDate.format(Validator.DATE)
        );
    }

    public static String className() {
        return "Account";
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

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }
    
}
