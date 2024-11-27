/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.utils;

import constants.Constants;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class IDGenerator {
    
    public static synchronized String generateIDByTime(String typeCode) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter year = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = now.format(year);
        return String.format("%s%s", typeCode, timestamp);
    }
    
    public static String generateID(String lastestID, String prefix) {
        int lastNumberID = lastestID.isEmpty() ? 0 : Integer.parseInt(lastestID.substring(prefix.length()));  
        int idNumberLength = (Constants.ID_LENGTH - prefix.length());
        return String.format("%s%0" + idNumberLength + "d", prefix, ++lastNumberID);
    }   
}
