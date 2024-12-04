/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.utils;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import main.constants.account.AccRole;
import main.constants.IDPrefix;
import static main.utils.LogMessage.errorLog;


public class IDGenerator {
    
    public static final int ID_LENGTH = 8;
    public static final int CODE_LENGTH = 8;
    public static final String DEFAULT_ADMIN_ID = String.format("%s%0"+ (ID_LENGTH - IDPrefix.ACTOR_PREFIX.length()) + "d", IDPrefix.ADMIN_PREFIX, 0);
    
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    
    public static synchronized String generateIDByTime(String typeCode) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter year = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = now.format(year);
        return String.format("%s%s", typeCode, timestamp);
    }
    
    public static String generateID(String lastestID, String prefix) {
        int lastNumberID = lastestID.isEmpty() ? 0 : Integer.parseInt(lastestID.substring(prefix.length()));  
        int idNumberLength = (ID_LENGTH - prefix.length());
        return String.format("%s%0" + idNumberLength + "d", prefix, ++lastNumberID);
    }   
    
    public static String generateAccID(String lastestID, AccRole role) {
        String prefix;
        switch(role) {
            case ADMIN: prefix = IDPrefix.ACTOR_PREFIX;
                break;
            case STAFF: prefix = IDPrefix.STAFF_PREFIX;
                break;
            case CUSTOMER: prefix = IDPrefix.CUSTOMER_PREFIX;
                break;    
            case PREMIUM: prefix = IDPrefix.PREMIUM_PREFIX;
                break;
            default:
                errorLog("Role can not be NONE");
                return "";
        }
        
        return generateID(lastestID, prefix);
    }  
    
    public static String generateDiscountCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }
        return code.toString();
    }
    
}
