package com.renan.todo.util;

/**
 * Utilitie class for Business Message
 */
public class MessageUtil {

    private static final String MSE01 = "MSE01"; // An error ocurred on the operation.
    private static final String MSE02 = "MSE02"; // Error on values properties.
    private static final String MSE03 = "MSE03"; // Invalid Token.
    private static final String MSE04 = "MSE04"; // Invalid User or Password.
    private static final String MSE05 = "MSE05"; // E-mail already exists.
    private static final String MSE06 = "MSE06"; // E-mail not found.
    private static final String MSE07 = "MSE07"; // User not activated.
    private static final String MSE08 = "MSE08"; // Cannot delete only list of the user

    /**
     * return a error message about the input values
     *
     * @return - return a generic error message
     */
    public static String getErrorMessage() {
        return MSE01;
    }

    /**
     * return a error message about the input values
     *
     * @return - code for error message
     */
    public static String getErrorMessageInputValues() {
        return MSE02;
    }

    /**
     * return an error message about the invalid jwt token
     *
     * @return - code for error message
     */
    public static String getErrorMessageToken() {
        return MSE03;
    }

    /**
     * return an error message about invalid user or password
     *
     * @return - code for error message
     */
    public static String getErrorMessageLogin() {
        return MSE04;
    }

    /**
     * return an error message about a email that already exists
     *
     * @return - code for error message
     */
    public static String getErrorMessageEmailAlreadyExists() {
        return MSE05;
    }

    /**
     * return an error message about an email not found on database
     *
     * @return - code for error message
     */
    public static String getErrorMessageUserNotFound() {
        return MSE06;
    }

    /**
     * return an error message about user not active on the system
     *
     * @return - code for error message
     */
    public static String getErrorMessageUserNotActive() {
        return MSE07;
    }

    /**
     * return an error message saying that the user cannot delete it's only list
     *
     * @return - code for error message
     */
    public static String getErrorMessageDeleteOnlyListOfUser() {
        return MSE08;
    }

}