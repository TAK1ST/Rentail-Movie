
package main.models;

import base.Model;

public class Users extends Model {
    private String username;
    private String passwordHash;
    private String email;
    private int role;
    private Subscription subscription;

    public Users(String userId, String username, String passwordHash, String email, int role, Subscription subscription) {
        super(userId);
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
        this.subscription = subscription;
    }

    public int getUserId() {
        return super.getId();
    }

    public void setUserId(int userId) {this.setId(userId); }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public Object[] getDatabaseValues() {
        return new Object[]
                {
                        getId(),
                        username,
                        passwordHash,
                        email,
                        role,
                        subscription
                };
    }
}
