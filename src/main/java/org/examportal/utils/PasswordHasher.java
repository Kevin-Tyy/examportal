package org.examportal.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {
    // Method to hash a password using SHA-256
    public static String hashPassword(String password) {
        try {
            // Generate a random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            // Create MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Apply salt to the password
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());

            // Encode hashed password and salt as Base64 string
            String encodedHash = Base64.getEncoder().encodeToString(hashedPassword);
            String encodedSalt = Base64.getEncoder().encodeToString(salt);

            // Concatenate hashed password and salt for storage
            return encodedHash + ":" + encodedSalt;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // Handle the error appropriately in your application
        }
    }

    // Method to verify a hashed password against a plaintext password
    public static boolean verifyPassword(String plaintextPassword, String hashedPasswordWithSalt) {
        try {
            // Split hashed password and salt
            String[] parts = hashedPasswordWithSalt.split(":");
            String encodedHash = parts[0];
            String encodedSalt = parts[1];

            // Decode salt
            byte[] salt = Base64.getDecoder().decode(encodedSalt);

            // Create MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Apply salt to the password
            md.update(salt);
            byte[] hashedPassword = md.digest(plaintextPassword.getBytes());

            // Encode hashed password and salt as Base64 string
            String encodedHashToCompare = Base64.getEncoder().encodeToString(hashedPassword);

            // Compare the encoded hashed passwords
            return encodedHashToCompare.equals(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false; // Handle the error appropriately in your application
        }
    }
}
