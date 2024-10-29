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
public class Customer extends User {

    private double credit;

    public Customer(String id, String username, String password, Role role, boolean haveInfo, boolean banned,
            String fullName, LocalDate dateOfBirth, String phoneNumber, String email, String address, double credit) {
        super(id, username, password, role, haveInfo, banned, fullName, dateOfBirth, phoneNumber, email, address);
        this.credit = credit;
    }

    public Customer(User other, double credit) {
        super(other);
        this.credit = credit;
    }

    @Override
    public String toString() {
        return String.format("%s, %.3f", super.getId(), credit);
    }

    public static String className() {
        return "Customer";
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

}
