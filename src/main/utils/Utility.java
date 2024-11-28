/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import static main.utils.Log.errorLog;

/**
 *
 * @author trann
 */
public class Utility {
    
    public static long extractNumber(String str) {
        String number = str.replaceAll("\\D+", "");
        return Long.parseLong(number);
    }
    
    public static int getRandomNumber(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
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
