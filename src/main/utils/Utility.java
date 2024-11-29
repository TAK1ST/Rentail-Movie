/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.utils;

import java.time.LocalDate;
import static main.utils.Input.getInteger;
import static main.utils.LogMessage.errorLog;

/**
 *
 * @author trann
 */
public class Utility {
       
    public static <E extends Enum<E>> void enumListing(String message, Class<E> enumClass) {
        if (enumClass.isEnum()) {
            E[] enumConstants = enumClass.getEnumConstants();
            System.out.printf("%s:\n", message);
            for(int index = 1; index < enumConstants.length; index++) {
                if (index % 3 == 0)
                    System.out.println();
                System.out.printf("%2d.%-25s", index, enumConstants[index]);
            }
            System.out.println("\n");
        } else {
            errorLog("The provided class is not an enum.");
        }
    }
    
    public static <E extends Enum<E>> E getEnumValue(String prompt, Class<E> enumClass, boolean enterToPass) {
        if (!enumClass.isEnum()) {
            throw new IllegalArgumentException("Provided class is not an enum");
        }

        E[] enumConstants = enumClass.getEnumConstants();
        System.out.println(prompt);
        for (int i = 0; i < enumConstants.length; i++) {
            System.out.printf("[%d] %s%n", i, enumConstants[i]);
        }
        
        int choice = getInteger("Enter choice", 0, enumConstants.length, enterToPass);
        if (choice >= 0) {
            return enumConstants[choice];
        } 
        
        return enumConstants[0];
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
    
    public static int toInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            errorLog("Can not convert to Number");
            return Integer.MIN_VALUE;
        }
    }
    
}
