/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;

/**
 *
 * @author trann
 */
public class User extends Account {

    private String fullName;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String email;
    private String address;

    public User(String id, String username, String password, Role role, boolean haveInfo, boolean banned,
            String fullName, LocalDate dateOfBirth, String phoneNumber, String email, String address) {
        super(id, username, password, role, haveInfo, banned);
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    public User(Account other, String fullName, LocalDate dateOfBirth, String phoneNumber, String email,
            String address) {
        super(other);
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    public User(User other) {
        super(other.getId(), other.getUsername(), other.getPassword(), other.getRole(), other.isHaveInfo(),
                other.isBanned());
        this.fullName = other.fullName;
        this.dateOfBirth = other.dateOfBirth;
        this.phoneNumber = other.phoneNumber;
        this.email = other.email;
        this.address = other.address;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s, %s",
                super.getId(),
                fullName,
                dateOfBirth,
                phoneNumber,
                email,
                address);
    }

    public static String className() {
        return "User";
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
