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

public class Input {
    
    private static final Scanner scanner = new Scanner(System.in);
    
    private static final String SELECT_NUM_UNVALID_PATTERN = "[^0-9, ]";
    private static final String COMMA_UNVALID_PATTERN = "[^a-zA-Z0-9, ]";
    
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
                    infoLog("Press enter to pass");
                    System.out.printf("If enter, default value is: %s\n", oldData);
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
                infoLog("Press enter to pass");
                System.out.printf("If enter, default value is: %d\n", oldData);
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
                infoLog("Press enter to pass");
                System.out.printf("If enter, default value is: %.2f\n", oldData);
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
        System.out.println("\n" + message + ": ");
        for (int index = 1; index <= infoLists.length; index++) {
                if (index % 4 == 1) System.out.println();
                System.out.printf("[%02d] %-25s ", index, infoLists[index - 1]);
        }
        if (oldData != null && !oldData.isEmpty())  {
            System.out.println();
            infoLog("Press enter to pass");
            System.out.printf("If enter, default value is: %s\n", oldData);
        }
        int option = getInteger("Enter an option", 1, infoLists.length, Integer.MIN_VALUE);
        if (option == Integer.MIN_VALUE)
            return oldData;
        return infoLists[option - 1];
    }
    
    public static String selectInfo(String message, String[] infoLists) {
        return selectInfo(message, infoLists, null);
    }
    
    public static <T extends Model> String selectByNumbers(String message, ListManager<T> manager, String oldData) {
        manager.show();
        List<String> temps = new ArrayList<>();

        String input = oldData;
        do {
            input = getString(message, input);
            if (input == oldData)
                return oldData;
            
            if (input != null && input.matches("[a-zA-Z]+")) {
                errorLog("Input must not contain letters");
                input = null;
            }
            
            if (input != null && input.matches(".*" + SELECT_NUM_UNVALID_PATTERN + ".*")) {
                errorLog("Input must not have special characters");
                input = null; 
            }
        } while (input == null);
        
        String[] inputs = input.split(",");

        for (String item : inputs) {
            item = item.trim();
            int index = toInt(item);
            if (index >= 0 && index < manager.getList().size()) {
                String value = manager.getList().get(index).getId();
                if (!temps.contains(value))
                    temps.add(value);
                else 
                    infoLog("Removing duplicate..");
            } else {
                infoLog("Removing out of index inputs..");
            }
        }

        return String.join(", ", temps);
    }
    
    public static <T extends Model> String selectByNumbers(String message, ListManager<T> manager) {
        return selectByNumbers(message, manager, null);
    }
    
    public static <T extends Model> String[] returnIDs(String stringList, ListManager<T> manager) {
        if (stringList.isEmpty() || stringList.isBlank())
            return null;
        
        if (stringList.matches(".*" + COMMA_UNVALID_PATTERN + ".*")) {
            errorLog("Input must not have special characters");
            return null;   
        }
        
        List<T> items = manager.getList();
        String[] ids = stringList.split(",");
        
        List<String> result = new ArrayList<>();
        for (String id : ids) {
            for (T item : items) {
                if (item.getId().equals(id.trim())) {
                    if (item instanceof Genre) {
                        Genre res = (Genre) item;
                        if (!result.contains(res.getGenreName()))
                            result.add(res.getGenreName()); 
                    } 
                    else if (item instanceof Actor) {
                        Actor res = (Actor) item;
                        if (!result.contains(res.getId()))
                            result.add(res.getId()); 
                    } 
                    else if (item instanceof Language) {
                        Language res = (Language) item;
                        if (!result.contains(res.getCode()))
                            result.add(res.getCode()); 
                    } 
                    break; 
                }
            }
        }

        return result.toArray(new String[0]);
    }
    
    public static <T extends Model> String returnID(String string, ListManager<T> manager) {
        return String.join(", ", returnIDs(string, manager));
    }
    
    public static <T extends Model> String[] returnNames(String stringList, ListManager<T> manager) {
        if (stringList.isEmpty() || stringList.isBlank())
            return null;
        
        if (stringList.matches(".*" + COMMA_UNVALID_PATTERN + ".*")) {
            errorLog("Input must not have special characters");
            return null;   
        }
        
        List<T> items = manager.getList();
        String[] ids = stringList.split(",");
        
        List<String> result = new ArrayList<>();
        for (String id : ids) {
            for (T item : items) {
                if (item.getId().equals(id.trim())) {
                    if (item instanceof Genre) {
                        Genre res = (Genre) item;
                        if (!result.contains(res.getGenreName())) 
                            result.add(res.getGenreName()); 
                    } 
                    else if (item instanceof Actor) {
                        Actor res = (Actor) item;
                        if (!result.contains(res.getActorName())) 
                            result.add(res.getActorName()); 
                    } 
                    else if (item instanceof Language) {
                        Language res = (Language) item;
                        if (!result.contains(res.getName())) 
                            result.add(res.getName()); 
                    } 
                    else if (item instanceof Account) {
                        Account res = (Account) item;
                        if (!result.contains(res.getUsername())) 
                            result.add(res.getUsername()); 
                    } 
                    else if (item instanceof Movie) {
                        Movie res = (Movie) item;
                        if (!result.contains(res.getTitle())) 
                            result.add(res.getTitle()); 
                    } 
                    else if (item instanceof Profile) {
                        Profile res = (Profile) item;
                        if (!result.contains(res.getFullName())) 
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
