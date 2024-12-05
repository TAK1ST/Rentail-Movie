/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.utils;

/**
 *
 * @author trann
 */
public class LogMessage {
    
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
        System.out.println("[FLAG]");
    }
    
}
