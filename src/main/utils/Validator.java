/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.utils;

import static java.lang.Integer.getInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import main.dto.Account;
import static main.utils.Input.askToExit;
import static main.utils.Input.getString;
import static main.utils.LogMessage.errorLog;
import static main.utils.LogMessage.infoLog;

/**
 *
 * @author trann
 */
public class Validator {
    
    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter YEAR = DateTimeFormatter.ofPattern("yyyy");
    
    private static final String PASSWORD_PATTERN = "^[a-zA-Z0-9!@#$%&*+\\-_]+$";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String NAME_SPECIAL_CHAR_PATTERN = ".*[^a-zA-Z0-9_\\-#+].*";
    private static final String FULLNAME_SPECIAL_CHAR_PATTERN = "[^a-zA-Z ]";
    
    private static final Scanner scanner = new Scanner(System.in);
    
    public static String getUsername(String message, String oldData, List<Account> list) {
        String input = null;
        boolean isUnique = false;
        do {
            input = getString(message, oldData);
            if (input == null) return null;

            if (input.isEmpty()) {
                errorLog("Username must not be empty");
                if(askToExit()) return null;
                continue;
            }
            if (input.length() < 4) {
                errorLog("Username must be at least 4 character");   
                if(askToExit()) return null;
                continue;
            }
            if (input.contains(" ")) {
                errorLog("Username must not contain space");    
                if(askToExit()) return null;
                continue;
            }
            if (Character.isDigit(input.charAt(0))) {
                errorLog("Username must not begin with number"); 
                if(askToExit()) return null;
                continue;
            }
            if (input.matches(".*" + NAME_SPECIAL_CHAR_PATTERN + ".*")) {
                errorLog("Username contains special characters");
                if(askToExit()) return null;
                continue;
            } 
            
            isUnique = true;
            for(Account item : list) 
                if (item.getUsername().equals(input)) {
                    errorLog("Username has already exist");
                    if(askToExit()) return null;
                    isUnique = false;
                }
        } while (!isUnique);
        return input;
    }
    
    public static String getPassword(String message, String oldData) {
        String input = null;
        boolean pass = false;
        do {
            input = getString(message, oldData);
            if (input == null) 
                return null;
            
            if (input.isEmpty()) {
                errorLog("Password must not be empty");
                if(askToExit()) return null;
                continue;
            }
            if (input.length() < 6) {
                errorLog("Password must be at least 6 character");
                if(askToExit()) return null;
                continue;
            }
            if (input.contains(" ")) {
                errorLog("Password must not contain space");
                if(askToExit()) return null;
                continue;
            }
            if (!input.matches(PASSWORD_PATTERN)) {
                errorLog("Password contains forbidden characters");
                infoLog("!,@,#,$,%,&,*,-,_,+ are allowed");
                if(askToExit()) return null;
                continue;
            }
            confirmPassword("Confirm password", input);
            pass = true;
            
        } while (!pass); 
        return input;
    }

    public static void confirmPassword(String message, String password) {
        do {
            System.out.printf("%s: ", message);
            String input = scanner.nextLine();
            
            if (input.isEmpty()) {
                errorLog("Password must not be empty");
                if(askToExit()) return;
                continue;
            }
            if(!input.equals(password)) {
                errorLog("Password Unmatch");
                if(askToExit()) return;
            } else 
                return;
        } while (true);
    }
    
    public static String getName(String message, String oldData) {
        String input = null;
        boolean pass = false;
        do {
            input = getString(message, oldData);
            if (input == null) 
                return null;
            
            if (input.isEmpty()) {
                errorLog("Name must not be empty");
                if(askToExit()) return null;
                continue;
            }
            if (input.matches(".*" + FULLNAME_SPECIAL_CHAR_PATTERN + ".*")) {
                errorLog("Name must not have special characters");
                if(askToExit()) return null;
                continue;
            }
            pass = true;
            
        } while (!pass);

        return input.trim();
    }
    
    public static String getFullName(String message, String oldData) {
        String input = null;
        boolean pass = false;
        do {
            input = getString(message, oldData);
            if (input == null) 
                return null;
            
            if (input.isEmpty()) {
                errorLog("Full name must not be empty");
                if(askToExit()) return null;
                continue;
            }
            if (input.matches(".*\\d.*")) {
                errorLog("Full name must not contain number");
                if(askToExit()) return null;
                continue;
            }
            if (input.matches(".*" + FULLNAME_SPECIAL_CHAR_PATTERN + ".*")) {
                errorLog("Full name must not have special characters");
                if(askToExit()) return null;
                continue;
            }
            pass = true;
            
        } while (!pass);
        
        return input.trim();
    }
    
    public static String getPhoneNumber(String message, String oldData) {
        String input = null;
        boolean pass = false;
        do {
            input = getString(message, oldData);
            if (input == null) 
                return null;

            if (input.isEmpty()) {
                errorLog("Phone number must not be empty");
                if(askToExit()) return null;
                continue;
            }
            if (input.contains(" ")) {
                errorLog("Contains space");   
                if(askToExit()) return null;
                continue;
            }
            if (input.replaceAll("\\D", null).length() != 10) {
                errorLog("Phone number must be 10 digit");
                if(askToExit()) return null;
                continue;
            }
            pass = true;
        } while (!pass);

        return input.replaceAll("\\D", null);
    }

    public static String getEmail(String message, String oldData) {
        String input = null;
        boolean pass = false;
        do {
            input = getString(message, oldData);
            if (input == null) 
                return null;

            if (input.isEmpty()) {
                errorLog("Email must not be empty");
                if(askToExit()) return null;
                continue;
            }
            if (input.contains(" ")) {
                errorLog("Contains space");      
                if(askToExit()) return null;
                continue;
            }
            if (!input.matches(EMAIL_PATTERN)) {
                errorLog("Email format is wrong");
                if(askToExit()) return null;
                continue;
            }
            pass = true;
        } while (!pass);

        return input;
    }
    
    public static LocalDate getDate(String message, LocalDate oldData) {
        String input = null;
        do {
            infoLog("Date must be in format dd/MM/yyyy");
            input = getString(message, oldData == null ? null : oldData.toString());
            if (input == null) 
                return null;
            
            if (input.isEmpty()) {
                errorLog("Date must not be empty");
                if(askToExit()) return null;
            }
            if (!isValidDate(input)) {
                errorLog("Date must be right format dd/MM/yyyy");
                if(askToExit()) return null;
            }
            
        } while (!isValidDate(input));

        return LocalDate.parse(input, DATE);
    }
    
    public static LocalTime getTime(LocalTime oldData) {
        int hours, minutes, seconds;

        while (true) {
            try {
                hours = getInteger("Enter hours (0-23)", Integer.MIN_VALUE);
                if (hours == Integer.MIN_VALUE) 
                    return oldData;
                
                minutes = getInteger("Enter minutes (0-59)", Integer.MIN_VALUE);
                if (minutes == Integer.MIN_VALUE) 
                    return oldData;
                
                seconds = getInteger("Enter seconds (0-59)", Integer.MIN_VALUE);
                if (seconds == Integer.MIN_VALUE) 
                    return oldData;

                if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59 || seconds < 0 || seconds > 59) {
                    throw new IllegalArgumentException("Invalid time. Please try again.");
                }

                return LocalTime.of(hours, minutes, seconds);
            } catch (IllegalArgumentException e) {
                errorLog("Invalid input. Please enter valid numbers for hours, minutes, and seconds.");
            }
        }
    }
    
    public static LocalDateTime getDateTime(LocalDateTime oldData) {
        LocalDate date = getDate("Enter date", oldData.toLocalDate());
        LocalTime time = getTime(oldData.toLocalTime());
 
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
