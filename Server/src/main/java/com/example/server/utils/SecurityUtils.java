package com.example.server.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class SecurityUtils {

    /**
     * This method generates a secure password using SHA-256 password hashing with salt.
     *
     * @param passwordToHash The password to be hashed.
     * @param salt The salt to be added to the password before hashing.
     * @return The hashed version of the password.
     */
    public static String generateSecurePassword(String passwordToHash, byte[] salt) {
        String hashedPassword = null;
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Add password bytes to digest
            md.update(salt);

            // Get the hash's bytes
            byte[] hashedBytes = md.digest(passwordToHash.getBytes());

            md.reset();
            // Convert bytes to hexadecimal format
            hashedPassword = toHex(hashedBytes);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }

    /**
     * This method generates a random salt value for password hashing implementation.
     *
     * @return The generated salt value.
     * @throws NoSuchAlgorithmException If the specified algorithm is not available.
     * @throws NoSuchProviderException  If the specified provider is not available.
     */
    public static byte[] generateSalt() throws NoSuchAlgorithmException, NoSuchProviderException {
        // Always use a SecureRandom generator
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");

        // Create an array for salt
        byte[] salt = new byte[16];

        // Generate a random salt
        secureRandom.nextBytes(salt);

        // Return the generated salt
        return salt;
    }

    public static byte[] fromHex(String hex){
        byte[] binary = new byte[hex.length() /2];
        for (int i =0; i<binary.length; i++){
            binary[i] = (byte) Integer.parseInt(hex.substring(2*i, 2*i+2),16);
        }
        return binary;
    }
    public static String toHex(byte[] array){
        BigInteger bi = new BigInteger(1,array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();

        if(paddingLength > 0){
            return String.format("%0"+paddingLength+"d",0) + hex;
        }else {
            return hex;
        }
    }
}
