/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.utils;

<<<<<<< HEAD
import model.Account;
import model.Account.Role;
=======
import main.models.Account;
import main.models.Account.Role;
>>>>>>> test

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class IDMaker {
    
    public static synchronized String generateAccID(Account listLastAcc, Role role) {
        String roleFormat;
        switch(role) {
            case Role.ADMIN -> roleFormat = "AD";
            case Role.CUSTOMER -> roleFormat = "CT";
            case Role.SELLER -> roleFormat = "SL";
            default -> roleFormat = "AC";
        }
        
        int lastNumberID = Integer.parseInt(listLastAcc.getId().substring(3));   
        return String.format("%s%04d", roleFormat, ++lastNumberID);
    }

    public static synchronized String generateID(String typeCode) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter year = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = now.format(year);
        return String.format("%s%s", typeCode, timestamp);
    }
}
