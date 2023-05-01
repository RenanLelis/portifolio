package com.renan.todo.service;

/**
 * Service for general utilities
 */
public interface UtilService {

//    static final String DATE_FORMAT = "dd/MM/yyyy";
    String DATE_FORMAT = "yyyy-MM-dd";
    Integer LENGTH_STRING_RANDOM = 6;
    String HASH_ALGORITHM = "SHA-256";

    /**
     * Check if the string has email format
     *
     * @param str - the email string
     *
     * @return - true if is valid, false if invalid
     */
    boolean isMail(String str);

    /**
     * Check if the string is empty
     *
     * @param str - the string to check
     *
     * @return - true if is empty
     */
    boolean isEmptyString(String str);

    /**
     * Check if the string is not empty
     *
     * @param str - the string to check
     *
     * @return - true if is not empty
     */
    boolean isNotEmptyString(String str);

    /**
     * Generate random string with fixed size
     *
     * @return - the random generate string
     */
    String generateRandomString();

    /**
     * Generate a random string on the specified size
     *
     * @param length - length of the string
     *
     * @return - the random generate string
     */
    String generateRandomString(Integer length);


    /**
     * Check if the string is in a valid date format
     *
     * @param dateStr - string with date
     *
     * @return - true if is valid
     */
    boolean isValidDate(String dateStr);

    /**
     * Generate a byte[] with the String's hash
     *
     * @param text - text
     * @return - a byte[] with the String's hash
     */
    byte[] generateHash(String text);

    /**
     * Generate a String with the text's hash
     *
     * @param text - text
     * @return - a String with the text's hash
     */
    String generateHashString(String text);


}