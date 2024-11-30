/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author trann
 */
public class RetryHandler {
    private static final int MAX_ATTEMPTS = 3;

    public static boolean executeWithRetry(RetryableTask task, String taskName) {
        int attempts = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (attempts < MAX_ATTEMPTS) {
            try {
                if (task.execute()) {
                    return true;
                }
                System.out.println("Task '" + taskName + "' failed. Try again.");
            } catch (Exception e) {
                System.out.println("Error during '" + taskName + "': " + e.getMessage());
            }
            
            attempts++;
            if (attempts < MAX_ATTEMPTS) {
                System.out.println("Retrying... (" + attempts + "/" + MAX_ATTEMPTS + ")");
            } else {
                System.out.println("Maximum attempts reached.");
                try {
                    System.out.print("Do you want to exit? (yes/no): ");
                    String exitResponse = reader.readLine().trim().toLowerCase();
                    if (exitResponse.equals("yes")) {
                        System.out.println("Exiting task '" + taskName + "'.");
                        return false;
                    } else {
                        attempts = 0;
                    }
                } catch (IOException ioException) {
                    System.out.println("Error reading input: " + ioException.getMessage());
                }
            }
        }
        return false;
    }
}

@FunctionalInterface
interface RetryableTask {
    boolean execute() throws Exception;
}

