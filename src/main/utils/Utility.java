/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static main.utils.Input.getInteger;
import static main.utils.LogMessage.errorLog;

/**
 *
 * @author trann
 */
public class Utility {
       
    public static <E extends Enum<E>> void enumListing(String message, Class<E> enumClass) {
        if (!enumClass.isEnum()) {
            errorLog("The provided class is not an enum.");
            return;
        }
        
        E[] enumConstants = enumClass.getEnumConstants();
        System.out.printf("%s:\n", message);
        for(int index = 1; index < enumConstants.length; index++) {
            if (index % 3 == 0)
                System.out.println();
            System.out.printf("[%02d] %-25s", index, enumConstants[index]);
        }
        System.out.println("\n");
    }
    
    public static <E extends Enum<E>> E getEnumValue(String message, Class<E> enumClass, E oldData) {
        enumListing(message, enumClass);
        E[] enumConstants = enumClass.getEnumConstants();
        
        int choice = getInteger("Enter choice", 1, enumConstants.length, Integer.MIN_VALUE);
        if (choice == Integer.MIN_VALUE && oldData != null) 
            return oldData;
        
        if (choice == Integer.MIN_VALUE)
            return enumConstants[0];
        
        return enumConstants[choice];
    }
    
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
    
    public static String formatDate(LocalDate date, DateTimeFormatter format) {
        if (date == null) return null;
        else 
            return date.format(format);
    }
    
    public static int toInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            errorLog("Can not convert to Number");
            return Integer.MIN_VALUE;
        }
    }
    
}
