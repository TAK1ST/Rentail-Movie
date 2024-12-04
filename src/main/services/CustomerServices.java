/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.services;

import static main.controllers.Managers.getACM;
import main.dto.Account;

/**
 *
 * @author trann
 */
public class CustomerServices {
    
    public static void showMyProfile(Account account) {
        getACM().show(account, "My Profile");
    }
    
}
