package main.encription;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncryptor{

    // Method to encrypt a password using SHA-256 algorithm
    public static String encryptPassword(String password) //This method takes a plain-text password as input
    {
        try {
            // Get an instance of the SHA-256 algorithm
            MessageDigest md = MessageDigest.getInstance("SHA-256");    //create an instance of the SHA-256 algorithm using

            // Compute the hash of the password bytes
            byte[] hash = md.digest(password.getBytes());   //The hash is represented as an array of bytes

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));  //converts the byte array to a hexadecimal string representation.
            }

            // Return the hexadecimal string representation of the hash
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Throw a runtime exception if the SHA-256 algorithm is not available
            throw new RuntimeException("Error encrypting password", e);
        }
    }
}
