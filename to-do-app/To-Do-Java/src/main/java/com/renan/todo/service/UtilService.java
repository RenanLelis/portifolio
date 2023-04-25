package com.renan.todo.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Random;

/**
 * Service for general utilities
 */
public interface UtilService {

    public static final String DATE_FORMAT = "MM/dd/yyyy";
    public static final Integer LENGTH_STRING_RANDOM = 6;
    public static final String HASH_ALGORITHM = "SHA-256";

    /**
     * Check if the string has email format
     *
     * @param str - the email string
     *
     * @return - true if is valid, false if invalid
     */
    public boolean isMail(String str);

    /**
     * Check if the string is empty
     *
     * @param str - the string to check
     *
     * @return - true if is empty
     */
    public boolean isEmptyString(String str);

    /**
     * Check if the string is not empty
     *
     * @param str - the string to check
     *
     * @return - true if is not empty
     */
    public boolean isNotEmptyString(String str);

    /**
     * Generate random string with fixed size
     *
     * @return - the random generate string
     */
    public String generateRandomString();

    /**
     * Generate a random string on the specified size
     *
     * @param length - length of the string
     *
     * @return - the random generate string
     */
    public String generateRandomString(Integer length);


    /**
     * Check if the string is in a valid date format
     *
     * @param dateStr - string with date
     *
     * @return - true if is valid
     */
    public boolean isValidDate(String dateStr);

    /**
     * Generate a byte[] with the String's hash
     *
     * @param text - text
     * @return - a byte[] with the String's hash
     */
    public byte[] generateHash(String text);

    /**
     * Generate a String with the text's hash
     *
     * @param text - text
     * @return - a String with the text's hash
     */
    public String generateHashString(String text);


}