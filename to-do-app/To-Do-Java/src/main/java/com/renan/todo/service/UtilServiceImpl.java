package com.renan.todo.service;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UtilServiceImpl implements UtilService {

    /**
     * Check if the string has email format
     *
     * @param str - the email string
     *
     * @return - true if is valid, false if invalid
     */
    public boolean isMail(String str) {
        if (this.isNotEmptyString(str)) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(str);
            return matcher.matches();
        }
        return false;
    }

    /**
     * Check if the string is empty
     *
     * @param str - the string to check
     *
     * @return - true if is empty
     */
    public boolean isEmptyString(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * Check if the string is not empty
     *
     * @param str - the string to check
     *
     * @return - true if is not empty
     */
    public boolean isNotEmptyString(String str) {
        return !this.isEmptyString(str);
    }

    /**
     * Generate random string with fixed size
     *
     * @return - the random generate string
     */
    public String generateRandomString() {
        return generateRandomString(LENGTH_STRING_RANDOM);
    }

    /**
     * Generate a random string on the specified size
     *
     * @param length - length of the string
     *
     * @return - the random generate string
     */
    public String generateRandomString(Integer length) {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        Random random = new Random();
        StringBuilder retorno = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int r = random.nextInt(chars.length - 1);
            retorno.append(chars[r]);
        }
        return retorno.toString();
    }


    /**
     * Check if the string is in a valid date format
     *
     * @param dateStr - string with date
     *
     * @return - true if is valid
     */
    public boolean isValidDate(String dateStr) {
        System.out.println(dateStr);
        DateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            System.out.println("OK");
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Generate a byte[] with the String's hash
     *
     * @param text - text
     * @return - a byte[] with the String's hash
     */
    public byte[] generateHash(String text) {
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
    public String generateHashString(String text) {
        return stringHex(Objects.requireNonNull(generateHash(text)));
    }

    /**
     * Convert a byte array to string
     * @param bytes - byte array to convert to string
     * @return - the string
     */
    private String stringHex(byte[] bytes) {
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