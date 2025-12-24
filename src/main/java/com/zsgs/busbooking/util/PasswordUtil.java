package com.zsgs.busbooking.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PasswordUtil {

    private static final int ITERATIONS = 65536; // Number of iterations
    private static final int KEY_LENGTH = 256; // Key length in bits
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    // Generate a random salt
    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    // Hash password with salt
    private static byte[] hashPassword(String password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                ITERATIONS,
                KEY_LENGTH
        );

        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        return factory.generateSecret(spec).getEncoded();
    }


    public static String hashPassword(String password) {
        try {
            byte[] salt = generateSalt();
            byte[] hash = hashPassword(password, salt);

            // Store as salt:hash
            return Base64.getEncoder().encodeToString(salt) + ":" +
                    Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // Verify password
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            // Split stored hash into salt and hash
            String[] parts = storedHash.split(":");
            if (parts.length != 2) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] hash = Base64.getDecoder().decode(parts[1]);

            // Hash the provided password with the same salt
            byte[] testHash = hashPassword(password, salt);

            // Compare hashes
            return slowEquals(hash, testHash);

        } catch (Exception e) {
            return false;
        }
    }

    // Constant-time comparison to prevent timing attacks
    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}