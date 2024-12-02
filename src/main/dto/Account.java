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
    private LocalDate createAt;
    private LocalDate updateAt;
    private LocalDate onlineAt;
    private int creability;
    
    public Account() {
    };

    public Account(
            String id, 
            String username, 
            String password, 
            String email, 
            AccRole role, 
            AccStatus status, 
            LocalDate createAt, 
            LocalDate updateAt,
            LocalDate onlineAt,
            int creability) 
    {
        super(id);
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.status = status;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.onlineAt = onlineAt;
        this.creability = creability;
    }

    public Account(Account other) {
        super(other.getId());
        this.username = other.username;
        this.password = other.password;
        this.email = other.email;
        this.role = other.role;
        this.status = other.status;
        this.createAt = other.createAt;
        this.updateAt = other.updateAt;
        this.onlineAt = other.onlineAt;
        this.creability = other.creability;
    }

    @Override
    public String toString() {
        return String.format("Account: %s, %s, %s, %s, %s, %s, %s, %s, %s, %d.",
                super.getId(),
                username,
                password,
                email,
                role,
                status,
                createAt.format(Validator.DATE),
                updateAt.format(Validator.DATE),
                onlineAt.format(Validator.DATE),
                creability
        );
    }

    public static String className() {
        return "Account";
    }
    
    @Override
    public String[] getSearchOptions() {
        return new String[] {"Id", "Username", "Password", "Role", "Email", "Status", "Online at", "Created at", "Updated at", "Creability"};
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

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public LocalDate getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
    }

    public LocalDate getOnlineAt() {
        return onlineAt;
    }

    public void setOnlineAt(LocalDate onlineAt) {
        this.onlineAt = onlineAt;
    }

    public int getCreability() {
        return creability;
    }

    public void setCreability(int creability) {
        this.creability = creability;
    }
    
}
