package main.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PassEncryptor {

    public static String encryptPassword(String password) {
        try {
            // Get an instance of the SHA-256 algorithm
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Compute the hash of the password bytes
            byte[] hash = md.digest(password.getBytes());

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));  // Convert the byte array to a hexadecimal string representation.
            }

            // Return the hexadecimal string representation of the hash
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error encrypting password", e);
        }
    }

    public static boolean validatePassword(String enteredPassword, String storedHash) {
        // Encrypt the entered password
        String hashedEnteredPassword = encryptPassword(enteredPassword);
        
        // Check if the hashed entered password matches the stored hash
        return hashedEnteredPassword.equals(storedHash);
    }

}
