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
import main.models.User;
import main.models.User.Role;
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;
import static main.utils.Input.rolesListing;
import static main.utils.Log.errorLog;

/**
 *
 * @author trann
 */
public class Validator {
    
    private static final String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PHONE_PATTERN = "^\\d{10}$";
    private static final long MAX_PRODUCT_PRICE = 100000000;
    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    private static final Scanner scanner = new Scanner(System.in);
    
    public static String getUsername(String message, boolean enterToPass, List<User> list) {
        String input = "";
        boolean isUnique;
        do {
            isUnique = true;
            System.out.print(message + ": ");
            input = scanner.nextLine();   
            if (input.isEmpty() && enterToPass) 
                return "";

            if (input.isEmpty()) 
                errorLog("Username must not be empty");
   
            if (input.length() < 4) 
                errorLog("Accountname must be at least 4 character");         
 
            for(User item : list) 
                if (item.getUsername().equals(input)) {
                    errorLog("Accountname has already exist");
                    isUnique = false;
                }
        } while (input.length() < 4 || !isUnique);
        return input;
    }

    public static Role getRole(String message, boolean enterToPass) {
        Role[] listRole = Role.values();
        rolesListing(message);
        int input = getInteger("Choose an option: ", 0, listRole.length - 1, enterToPass);

        if (input <= -1) 
            return Role.NONE;
        else 
            return listRole[input];
    }
    
    public static String getPassword(String message, boolean enterToPass) {
        String input = "";
        do {
            System.out.print(message + ": ");
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
                errorLog("Password must contain no space");
                continue;
            }
           
            confirmPassword("Confirm password: ", input);

        } while (input.length() < 6 || input.contains(" ")); 
        return input;
    }

    public static void confirmPassword(String message, String password) {
        String confirm;
        do {
            System.out.print(message + ": ");
            confirm = scanner.nextLine();

            if(!confirm.equals(password)) 
                errorLog("Password Unmatch");
            
        } while (!confirm.equals(password));
    }
    
    public static String getName(String message, boolean enterToPass) {
        String input = "";
        do {
            System.out.print(message + ": ");
            input = scanner.nextLine(); 
            if (input.isEmpty() && enterToPass) 
               return "";
            
            if (input.isEmpty()) 
                errorLog("Name must not be empty");

            if (!Validator.isValidName(input)) 
                errorLog("Name must not have special characters");
            
        } while (input.isEmpty() || !Validator.isValidName(input));

        return input;
    }
          
    public static LocalDate getDate(String message, boolean enterToPass) {
        String input = "";
        do {
            System.out.print(message + ": ");
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
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter valid numbers for hours, minutes, and seconds.");
            }
        }
    }
    
    public static LocalDateTime getDateTime() {
        LocalDate date = getDate("Enter date", false);
        LocalTime time = getTime();
 
        return date.atTime(time);
    }
    
    public static String getPhoneNumber(String message, boolean enterToPass) {
        String input = "";
        do {
            input = getString(message, enterToPass);
            if (input.isEmpty() && enterToPass) 
                return "";

            if (input.isEmpty()) 
                errorLog("Phone number must not be empty");

            if (!Validator.isValidPhoneNumber(input)) 
                errorLog("Phone number must be 10 digit");

        } while (!Validator.isValidPhoneNumber(input));

        return input;
    }

    public static String getEmail(String message, boolean enterToPass) {
        String input = "";
        do {
            input = getString(message, enterToPass);
            if (input.isEmpty() && enterToPass) 
                return "";

            if (input.isEmpty()) 
                errorLog("Email must not be empty");

            if (!Validator.isValidEmail(input)) 
                errorLog("Email must has format ...@gmail.com");

        } while (!Validator.isValidEmail(input));

        return input;
    }
    
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
    
    public static boolean isValidName(String name) {
        return name.matches("[a-zA-Z0-9 ]+");
    }
    
    public static boolean isValidProductPrice(double price) {
        return price >= 0 && price <= MAX_PRODUCT_PRICE;
    }
    
    public static boolean isValidUsername(String username) {
        return username != null && username.length() >= 5 && !username.contains(" ");
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6 && !password.contains(" ");
    }

    public static boolean isPasswordConfirmed(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && Pattern.matches(PHONE_PATTERN, phoneNumber);
    }

    public static boolean isValidEmail(String email) {
        return email != null && Pattern.matches(EMAIL_PATTERN, email);
    }

}
