/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author trann
 */
public class Utility {
    
    public static boolean isDateInRange(LocalDate startDate, LocalDate endDate, LocalDate targetDate) {
        return (targetDate.isEqual(startDate) || targetDate.isAfter(startDate)) &&
               (targetDate.isEqual(endDate) || targetDate.isBefore(endDate));
    }
    
    public static boolean isDateTimeInRange(LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime targetDateTime) {
        return (targetDateTime.isEqual(startDateTime) || targetDateTime.isAfter(startDateTime)) &&
               (targetDateTime.isEqual(endDateTime) || targetDateTime.isBefore(endDateTime));
    }
    
    public static long extractNumber(String str) {
        String number = str.replaceAll("\\D+", "");
        return Long.parseLong(number);
    }
    
    public static int getRandomNumber(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }
    
    public static void errorLog(String message) {
        System.out.printf("[ERROR] %s.\n", message);
    }
    
    public static LocalDate toDate(String date){
        return LocalDate.parse(date, Validator.DATE);
    }
    
    public static int toInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            errorLog("Can not convert to Number");
            return 0;
        }
    }
    
}
