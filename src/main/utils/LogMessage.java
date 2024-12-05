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
    
    public static boolean errorLog(String message) {
        System.out.printf("[ERROR] %s.\n", message);
        return false;
    }
    
    public static boolean successLog(String message) {
        System.out.printf("[SUCCESS] %s.\n", message);
        return true;
    }
    
    public static void infoLog(String message) {
        System.out.printf("[INFO] %s.\n", message);
    }
    
    public static void debugLog() {
        System.out.println("[FLAG]");
    }
    
}
