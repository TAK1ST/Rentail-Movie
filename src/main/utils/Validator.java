/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import main.constants.AccRole;
import main.constants.ActorRank;
import main.dto.Account;
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;
import static main.utils.LogMessage.errorLog;
import static main.utils.LogMessage.infoLog;
import static main.utils.Utility.enumListing;

/**
 *
 * @author trann
 */
public class Validator {
    
    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    private static final String PASSWORD_PATTERN = "^[a-zA-Z0-9!@#$%&*+\\-_]+$";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String NAME_SPECIAL_CHAR_PATTERN = ".*[^a-zA-Z0-9_\\-#+].*";
    private static final String FULLNAME_SPECIAL_CHAR_PATTERN = "[^a-zA-Z ]";
    
    private static final Scanner scanner = new Scanner(System.in);
    
    public static String getUsername(String message, boolean enterToPass, List<Account> list) {
        String input = "";
        boolean isUnique = false;
        do {
            if (enterToPass) infoLog("Press Enter to skip");
            
            System.out.printf("%s: ", message);
            input = scanner.nextLine();   
            if (input.isEmpty() && enterToPass) 
                return "";

            if (input.isEmpty()) {
                errorLog("Username must not be empty");
                continue;
            }
            if (input.length() < 4) {
                errorLog("Username must be at least 4 character");      
                continue;
            }
            if (input.contains(" ")) {
                errorLog("Username must not contain space");      
                continue;
            }
            if (Character.isDigit(input.charAt(0))) {
                errorLog("Username must not begin with number");      
                continue;
            }
            if (input.matches(".*" + NAME_SPECIAL_CHAR_PATTERN + ".*")) {
                errorLog("Username contains special characters");
                continue;
            } 
            isUnique = true;
            for(Account item : list) 
                if (item.getUsername().equals(input)) {
                    errorLog("Username has already exist");
                    isUnique = false;
                }
        } while (!isUnique);
        return input;
    }
    
    public static String getPassword(String message, boolean enterToPass) {
        String input = "";
        boolean pass = false;
        do {
            if (enterToPass) infoLog("Press Enter to skip");
            
            System.out.printf("%s: ", message);
            input = scanner.nextLine();
            if (input.isEmpty() && enterToPass) 
                return "";
            
            if (input.isEmpty()) {
                errorLog("Password must not be empty");
                continue;
            }
            if (input.length() < 6) {
                errorLog("Password must be at least 6 character");
                continue;
            }
            if (input.contains(" ")) {
                errorLog("Password must not contain space");
                continue;
            }
            if (!input.matches(PASSWORD_PATTERN)) {
                errorLog("Password contains forbidden characters");
                infoLog("!,@,#,$,%,&,*,-,_,+ are allowed");
                continue;
            }
            confirmPassword("Confirm password", input);
            pass = true;
            
        } while (!pass); 
        return input;
    }

    public static void confirmPassword(String message, String password) {
        String confirm;
        do {
            System.out.printf("%s: ", message);
            confirm = scanner.nextLine();

            if(!confirm.equals(password)) 
                errorLog("Password Unmatch");
            
        } while (!confirm.equals(password));
    }
    
    public static String getName(String message, boolean enterToPass) {
        String input = "";
        boolean pass = false;
        do {
            if (enterToPass) infoLog("Press Enter to skip");
            
            System.out.printf("%s: ", message);
            input = scanner.nextLine(); 
            if (input.isEmpty() && enterToPass) 
               return "";
            
            if (input.isEmpty()) {
                errorLog("Name must not be empty");
                continue;
            }
            if (input.matches(".*" + FULLNAME_SPECIAL_CHAR_PATTERN + ".*")) {
                errorLog("Name must not have special characters");
                continue;
            }
            pass = true;
            
        } while (!pass);

        return input.trim();
    }
    
    public static String getFullName(String message, boolean enterToPass) {
        String input = "";
        boolean pass = false;
        do {
            if (enterToPass) infoLog("Press Enter to skip");
            
            System.out.printf("%s: ", message);
            input = scanner.nextLine(); 
            if (input.isEmpty() && enterToPass) 
               return "";
            
            if (input.isEmpty()) {
                errorLog("Full name must not be empty");
                continue;
            }
            if (input.matches(".*\\d.*")) {
                errorLog("Full name must not contain number");
                continue;
            }
            if (input.matches(".*" + FULLNAME_SPECIAL_CHAR_PATTERN + ".*")) {
                errorLog("Full name must not have special characters");
                continue;
            }
            pass = true;
            
        } while (!pass);
        
        return input.trim();
    }
    
    public static String getPhoneNumber(String message, boolean enterToPass) {
        String input = "";
        boolean pass = false;
        do {
            if (enterToPass) infoLog("Press Enter to skip");
            
            input = getString(message, enterToPass);
            if (input.isEmpty() && enterToPass) 
                return "";

            if (input.isEmpty()) {
                errorLog("Phone number must not be empty");
                continue;
            }
            if (input.contains(" ")) {
                errorLog("Contains space");      
                continue;
            }
            if (input.replaceAll("\\D", "").length() != 10) {
                errorLog("Phone number must be 10 digit");
                continue;
            }
            pass = true;
        } while (!pass);

        return input.replaceAll("\\D", "");
    }

    public static String getEmail(String message, boolean enterToPass) {
        String input = "";
        boolean pass = false;
        do {
            if (enterToPass) infoLog("Press Enter to skip");
            
            input = getString(message, enterToPass);
            if (input.isEmpty() && enterToPass) 
                return "";

            if (input.isEmpty()) {
                errorLog("Email must not be empty");
                continue;
            }
            if (input.contains(" ")) {
                errorLog("Contains space");      
                continue;
            }
            if (!input.matches(EMAIL_PATTERN)) {
                errorLog("Email format is wrong");
                continue;
            }
            pass = true;
        } while (!pass);

        return input;
    }
    
    public static LocalDate getDate(String message, boolean enterToPass) {
        String input = "";
        do {
            if (enterToPass) infoLog("Press Enter to skip");
            
            System.out.printf("%s (%s): ", message, DATE);
            input = scanner.nextLine(); 
            if (input.isEmpty() && enterToPass) 
               return null;
            
            if (input.isEmpty()) 
                errorLog("Date must not be empty");

            if (!Validator.isValidDate(input)) 
                errorLog("Date must be right format dd/MM/yyyy");
                
        } while (!Validator.isValidDate(input));

        return LocalDate.parse(input, DATE);
    }
    
    public static LocalTime getTime() {
        int hours, minutes, seconds;

        while (true) {
            try {
                System.out.print("Enter hours (0-23): ");
                hours = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter minutes (0-59): ");
                minutes = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter seconds (0-59): ");
                seconds = Integer.parseInt(scanner.nextLine());

                if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59 || seconds < 0 || seconds > 59) {
                    throw new IllegalArgumentException("Invalid time. Please try again.");
                }

                return LocalTime.of(hours, minutes, seconds);
            } catch (IllegalArgumentException e) {
                errorLog("Invalid input. Please enter valid numbers for hours, minutes, and seconds.");
            }
        }
    }
    
    public static LocalDateTime getDateTime() {
        LocalDate date = getDate("Enter date", false);
        LocalTime time = getTime();
 
        return date.atTime(time);
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static boolean isDateInRange(LocalDate startDate, LocalDate endDate, LocalDate targetDate) {
        return (targetDate.isEqual(startDate) || targetDate.isAfter(startDate)) &&
               (targetDate.isEqual(endDate) || targetDate.isBefore(endDate));
    }
    
    public static boolean isDateTimeInRange(LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime targetDateTime) {
        return (targetDateTime.isEqual(startDateTime) || targetDateTime.isAfter(startDateTime)) &&
               (targetDateTime.isEqual(endDateTime) || targetDateTime.isBefore(endDateTime));
    }
    
    public static boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, DATE);
            return true; 
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}
