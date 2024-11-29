/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.utils;

import java.util.Scanner;
import static main.utils.LogMessage.errorLog;

/**
 *
 * @author trann
 */
public class Input {
    
    private static final Scanner scanner = new Scanner(System.in);
    
    public static String getString(String message, boolean enterToPass) {
            String result = "";
            do {
                System.out.print(message + ": ");
                result = scanner.nextLine();
                if(result.isEmpty() && enterToPass) 
                    return "";
                
                if(result.isEmpty())
                    errorLog("Please input");
                
            } while (result.isEmpty());

            return result;
        }

    public static int getInteger(String message, int min, int max, boolean enterToPass) {
        int number;
        while (true) {
            System.out.printf("%s (%d -> %d):", message, min, max);
            String input = scanner.nextLine();
            if (input.isEmpty() && enterToPass) {
                return Integer.MIN_VALUE;
            }
            try {
                number = Integer.parseInt(input);
                if (number >= min && number <= max) {
                    return number;
                } else {
                    errorLog("Number not in range");
                }
            } catch (NumberFormatException e) {
                errorLog("Please enter an integer");
            }
        }
    }

    public static double getDouble(String message, double min, double max, boolean enterToPass) {
        double number;
        while (true) {
            System.out.print(message + ": ");
            String input = scanner.nextLine();
            if (input.isEmpty() && enterToPass) {
                return Double.MIN_VALUE;
            }
            try {
                number = Double.parseDouble(input);
                if (number >= min && number <= max) {
                    return number;
                } else {
                    errorLog("Number not in range");
                }
            } catch (NumberFormatException e) {
                errorLog("Please enter an integer");
            }
        }
    }

    public static boolean yesOrNo(String message) {
        System.out.print(message + " (Y): ");
        return scanner.nextLine().equalsIgnoreCase("y");
    }

    public static String selectInfo(String message, String[] infoLists, boolean enterToPass) {
        System.out.println("\n" + message + ": ");
        for (int index = 0; index < infoLists.length; index++) {
                if (index % 4 == 0) System.out.println();
                System.out.printf("%2d. %-25s ", index, infoLists[index]);
        }
        System.out.println("\n");
        if (!enterToPass) 
            return infoLists[getInteger("Enter an option", 0, infoLists.length - 1, enterToPass)];
        else 
            return "";
    }
    
}
