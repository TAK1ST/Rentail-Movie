package main.dto;

import java.time.LocalDate;
import main.base.Model;
import main.constants.account.AccRole;
import main.constants.account.AccStatus;
import static main.utils.Utility.formatDate;
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
    }

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
        String[] attr = getAttributes();
        int count = 0;
        return String.format(
                "\n[%s]:\n"
                + "%s: %s,\n"
                + "%s: %s,\n"
                + "%s: %s,\n"
                + "%s: %s,\n"
                + "%s: %s,\n"
                + "%s: %s,\n"
                + "%s: %s,\n"
                + "%s: %s,\n"
                + "%s: %s,\n"
                + "%s: %d.",
                className(),
                attr[count++], super.getId(),
                attr[count++], username,
                attr[count++], password,
                attr[count++], email,
                attr[count++], role,
                attr[count++], status,
                attr[count++], formatDate(createAt, Validator.DATE),
                attr[count++], formatDate(updateAt, Validator.DATE),
                attr[count++], formatDate(onlineAt, Validator.DATE),
                attr[count++], creability
        );
    }
    
    public static String className() {
        return "Account";
    }
    
    public static String[] getAttributes() {
        return new String[] {
            "Id", 
            "Username", 
            "Password", 
            "Role", 
            "Email", 
            "Status", 
            "Created at", 
            "Updated at", 
            "Online at", 
            "Creability"};
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
