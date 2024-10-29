package model;

import base.Model;

/**
 * @author trann
 */
public class Account extends Model {

    public static enum Role {
        NONE,
        ADMIN,
        CUSTOMER,
        SELLER,
    }

    private String username;
    private String password;
    private Role role;
    private boolean haveInfo;
    private boolean banned;

    public Account(String id, String username, String password, Role role, boolean haveInfo, boolean banned) {
        super(id);
        this.username = username;
        this.password = password;
        this.role = role;
        this.haveInfo = haveInfo;
        this.banned = banned;
    }

    public Account(Account other) {
        super(other.getId());
        this.username = other.username;
        this.password = other.password;
        this.role = other.role;
        this.haveInfo = other.haveInfo;
        this.banned = other.banned;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %b, %b", super.getId(), username, password, role, haveInfo, banned);
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isHaveInfo() {
        return haveInfo;
    }

    public void setHaveInfo(boolean haveInfo) {
        this.haveInfo = haveInfo;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setIsBanned(boolean banned) {
        this.banned = banned;
    }
    @Override
    public Object[] getDatabaseValues() {
        return new Object[]{
                getId(),
                username,
                password,
                role != null ? role.name() : Role.NONE.name(), // Lưu tên của enum Role
                haveInfo,
                banned
        };
    }
}
