package main.utils;

import java.util.Arrays;

public class LogMessage {
    
    private static int flagNum = 1;
    
    public static boolean errorLog(String message, boolean returnValue) {
        System.out.printf("[ERROR] %s.\n", message);
        return returnValue;
    }
    
    public static void errorLog(String message) {
        System.out.printf("[ERROR] %s.\n", message);
    }
    
    public static boolean successLog(String message, boolean returnValue) {
        System.out.printf("[SUCCESS] %s.\n", message);
        return returnValue;
    }
    
    public static void successLog(String message) {
        System.out.printf("[SUCCESS] %s.\n", message);
    }
    
    public static boolean infoLog(String message, boolean returnValue) {
        System.out.printf("[INFO] %s.\n", message);
        return returnValue;
    }
    
    public static void infoLog(String message) {
        System.out.printf("[INFO] %s.\n", message);
    }
    
    public static boolean debugLog(boolean returnValue) {
        System.out.println("[FLAG]");
        return returnValue;
    }
    
    public static void debugLog() {
        System.out.printf("[FLAG %d]\n", flagNum++);
    }
    
    public static void valueLog(Object item) {
        System.out.println("-> " + item.toString());
    }
    
    public static void valueLog(Object... items) {
        System.out.println("-> " +Arrays.toString(items));
    }
    
}
