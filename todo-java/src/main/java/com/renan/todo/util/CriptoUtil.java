package com.renan.todo.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * Utilities class for hash e encryption operations
 *
 * @author renan
 */
public class CriptoUtil {

    public static final String HASH_ALGORITHM = "SHA-256";

    /**
     * Generate a byte[] with the String's hash
     *
     * @param text - text
     * @return - a byte[] with the String's hash
     */
    public static byte[] generateHash(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(text.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * Generate a String with the text's hash
     *
     * @param text - text
     * @return - a String with the text's hash
     */
    public static String geraHashString(String text) {
        return stringHexa(Objects.requireNonNull(generateHash(text)));
    }

    private static String stringHexa(byte[] bytes) {
        StringBuilder s = new StringBuilder();
        for (byte aByte : bytes) {
            int highPart = ((aByte >> 4) & 0xf) << 4;
            int lowPart = aByte & 0xf;
            if (highPart == 0)
                s.append('0');
            s.append(Integer.toHexString(highPart | lowPart));
        }
        return s.toString();
    }

}
