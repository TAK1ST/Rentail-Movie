/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.models;

import base.Model;

/**
 *
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
    private String status;

    public Account(String id, String username, String password, Role role, String status) {
        super(id);
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
    }
    
    public Account(Account other) {
        super(other.getId());
        this.username = other.username;
        this.password = other.password;
        this.role = other.role;
        this.status = other.status;
    }
    
    @Override
    public String toString() {
        return String.format("Account: %s, %s, %s, %s, %s.", super.getId(), username, password, role, status);
    }
    
    @Override
    public Object[] getDatabaseValues() {
        return new Object[]
                {
                        super.getId(),
                        username,
                        password,
                        role,
                        status,
                };
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
