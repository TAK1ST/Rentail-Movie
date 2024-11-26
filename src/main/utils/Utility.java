/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import main.models.Users.Role;

/**
 *
 * @author trann
 */
public class Utility {
    private static final Scanner scanner = new Scanner(System.in);
    
    public static synchronized String generateIDByTime(String typeCode) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter year = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = now.format(year);
        return String.format("%s%s", typeCode, timestamp);
    }
    
    public static String generateID(String lastestID, String prefix) {
        int lastNumberID = Integer.parseInt(lastestID.substring(prefix.length()));   
        return String.format("%s%04d", prefix, ++lastNumberID);
    }
    
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
    
    public static class Console {
        
        public static String getString(String message, boolean enterToPass) {
            String result = "";
            do {
                System.out.print(message);
                result = scanner.nextLine();
                if(result.isEmpty() && enterToPass) 
                    return "";
                
                if(result.isEmpty())
                    errorLog("Please input");
                
            } while (result.isEmpty());

            return result;
        }

        public static int getInt(String message, int min, int max, boolean enterToPass) {
            int number;
            while (true) {
                System.out.print(message);
                String input = scanner.nextLine();
                if (input.isEmpty() && enterToPass) {
                    return -999999999;
                }
                try {
                    number = Integer.parseInt(input);
                    if (number >= min && number <= max) {
                        return number;
                    } else {
                        Utility.errorLog("Number not in range");
                    }
                } catch (NumberFormatException e) {
                    Utility.errorLog("Please enter an integer");
                }
            }
        }

        public static double getDouble(String message, double min, double max, boolean enterToPass) {
            double number;
            while (true) {
                System.out.print(message);
                String input = scanner.nextLine();
                if (input.isEmpty() && enterToPass) {
                    return -99999999f;
                }
                try {
                    number = Double.parseDouble(input);
                    if (number >= min && number <= max) {
                        return number;
                    } else {
                        Utility.errorLog("Number not in range");
                    }
                } catch (NumberFormatException e) {
                    Utility.errorLog("Please enter an integer");
                }
            }
        }

        public static boolean yesOrNo(String message) {
            String input = null;
            do {
                System.out.print("(Y/N) " + message);
                input = scanner.nextLine();

                if(!input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n")) errorLog("Wrong syntax");
            } while(!input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n"));
            return input.equalsIgnoreCase("y");
        }
     
        public static void rolesListing(String message) {
            Role list[] = Role.values();
            System.out.println(message);
            for(int index = 1; index < list.length; index++) {
                if (index % 3 == 0)
                    System.out.println();
                System.out.printf("%2d.%-25s", index, list[index]);
            }
            System.out.println("\n");
        }
        
	public static String selectInfo(String message, String[] infoLists, boolean enterToPass) {
		System.out.println("\n" + message);
		for (int index = 0; index < infoLists.length; index++) {
			if (index % 4 == 0) System.out.println();
			System.out.printf("%2d. %-25s ", index, infoLists[index]);
		}
		System.out.println("\n");
		if (!enterToPass) 
                    return infoLists[getInt("Enter an option: ", 0, infoLists.length - 1, enterToPass)];
                else 
                    return "";
	}
        
    }
}
