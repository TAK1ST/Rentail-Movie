/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import main.base.ListManager;
import main.base.Model;
import main.dto.Account;
import main.dto.Actor;
import main.dto.Genre;
import main.dto.Language;
import main.dto.Movie;
import main.dto.Profile;
import static main.utils.LogMessage.errorLog;
import static main.utils.LogMessage.infoLog;
import static main.utils.Utility.toInt;

/**
 *
 * @author trann
 */
public class Input {
    
    private static final Scanner scanner = new Scanner(System.in);
    
    private static int getBackIn = 3;
    
    public static boolean askToExit() {
        if (--getBackIn == 0) {
            getBackIn = 3;
            return !yesOrNo("\nContinue");
        }
        return false;
    }
    
    public static void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    public static String getString(String message, String oldData) {
            String result = null;
            do {
                if (oldData != null && !oldData.isEmpty()) {
                    System.out.println();
                    infoLog(String.format("Press Enter to skip, default value will be: %s", oldData));
                }
                
                System.out.print(message + ": ");
                result = scanner.nextLine();
                if(result.isEmpty() && oldData != null && !oldData.isEmpty()) 
                    return oldData;
                
                if(result.isEmpty()) {
                    errorLog("Please input");
                    if(askToExit()) return null;
                }
            } while (result.isEmpty());

            return result;
        }
    
    public static String getString(String message) {
        return getString(message, null);
    }

    public static int getInteger(String message, int min, int max, int oldData) {
        int number;
        while (true) {
            if (oldData != Integer.MIN_VALUE)  {
                System.out.println();
                infoLog(String.format("Press Enter to skip, default value will be: %d", oldData));
            }
            
            if (min == Integer.MIN_VALUE || max == Integer.MAX_VALUE)
                System.out.printf("%s: ", message);
            else 
                System.out.printf("%s (%d -> %d): ", message, min, max);
            
            String input = scanner.nextLine();
            if (input.isEmpty() && oldData != Integer.MIN_VALUE) {
                return oldData;
            }
            if(input.isEmpty()) {
                errorLog("Please input");
                if(askToExit()) 
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
    
    public static int getInteger(String message, int min, int max) {
        return getInteger(message, min, max, Integer.MIN_VALUE);
    }

    public static double getDouble(String message, double min, double max, double oldData) {
        double number;
        while (true) {
            if (oldData != Double.MIN_VALUE) {
                System.out.println();
                infoLog(String.format("Press Enter to skip, default value will be: %.2f", oldData));
            }
            
            if (min == Double.MIN_VALUE || max == Double.MAX_VALUE)
                System.out.printf("%s: ", message);
            else 
                System.out.printf("%s (%.2f -> %.2f): ", message, min, max);
            
            String input = scanner.nextLine();
            if (input.isEmpty() && oldData != Double.MIN_VALUE) {
                return oldData;
            }
            if(input.isEmpty()) {
                errorLog("Please input");
                if(askToExit()) 
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
    
    public static double getDouble(String message, double min, double max) {
        return getDouble(message, min, max, Double.MIN_VALUE);
    }

    public static boolean yesOrNo(String message) {
        System.out.print(message + " (Y): ");
        return scanner.nextLine().equalsIgnoreCase("y");
    }

    public static String selectInfo(String message, String[] infoLists, String oldData) {
        if (oldData != null || !oldData.isEmpty())  {
            System.out.println();
            infoLog("Press Enter to skip");
        }
        System.out.println("\n" + message + ": ");
        for (int index = 1; index <= infoLists.length; index++) {
                if (index % 4 == 1) System.out.println();
                System.out.printf("[%02d] %-25s ", index, infoLists[index - 1]);
        }
        System.out.println("\n");
        int option = getInteger("Enter an option", 1, infoLists.length, Integer.MIN_VALUE);
        if (option == Integer.MIN_VALUE)
            return oldData;
        return infoLists[option - 1];
    }
    
    public static String selectInfo(String message, String[] infoLists) {
        return selectInfo(message, infoLists, null);
    }
    
    public static <T extends Model> String selectByNumbers(String message, ListManager<T> manager, String oldData) {
        manager.display(false);
        String temps = null;

        String input = getString(message, null);
        if (input == null) return oldData;
        
        String[] inputs = input.split(",");

        for (String item : inputs) {
            item = item.trim();
            int index = toInt(item);
            if (index > 0 && index <= manager.getList().size()) {
                temps += manager.getList().get(index).getId();
            }
        }

        return temps;
    }
    
    public static <T extends Model> String selectByNumbers(String message, ListManager<T> manager) {
        return selectByNumbers(message, manager, null);
    }
    
    public static <T extends Model> String[] returnNames(String stringList, ListManager<T> manager) {
        if (stringList.isEmpty() || stringList.isBlank())
            return new String[] {};
        
        List<T> items = manager.getList();
        String[] ids = stringList.split(",");
        
        List<String> result = new ArrayList<>();
        for (String id : ids) {
            for (T item : items) {
                if (item.getId().equals(id.trim())) {
                    if (item instanceof Genre) {
                        Genre res = (Genre) item;
                        result.add(res.getGenreName()); 
                    } 
                    else if (item instanceof Actor) {
                        Actor res = (Actor) item;
                        result.add(res.getActorName()); 
                    } 
                    else if (item instanceof Language) {
                        Language res = (Language) item;
                        result.add(res.getName()); 
                    } 
                    else if (item instanceof Account) {
                        Account res = (Account) item;
                        result.add(res.getUsername()); 
                    } 
                    else if (item instanceof Movie) {
                        Movie res = (Movie) item;
                        result.add(res.getTitle()); 
                    } 
                    else if (item instanceof Profile) {
                        Profile res = (Profile) item;
                        result.add(res.getFullName()); 
                    }
                    break; 
                }
            }
        }

        return result.toArray(new String[0]);
    }
    
    public static <T extends Model> String returnName(String string, ListManager<T> manager) {
        return String.join(", ", returnNames(string, manager));
    }

}
