package com.renan.todo.util;

/**
 * Utilitie class for Business Message
 */
public class MessageUtil {

    public static final String MSE01 = "MSE01"; // An error ocurred on the operation.
    public static final String MSE02 = "MSE02"; // Error on values properties.
    public static final String MSE03 = "MSE03"; // Invalid Token.
    public static final String MSE04 = "MSE04"; // Invalid User or Password.
    public static final String MSE05 = "MSE05"; // E-mail already exists.
    public static final String MSE06 = "MSE06"; // E-mail not found.

    //getErrorMessage return a generic error message
    public static String getErrorMessage() {
        return MSE01;
    }

    //getErrorMessageInputValues return a error message about the input values
    public static String getErrorMessageInputValues() {
        return MSE02;
    }

    //getErrorMessageToken return an error message about the invalid jwt token
    public static String getErrorMessageToken() {
        return MSE03;
    }

    //getErrorMessageLogin return an error message about invalid user or password
    public static String getErrorMessageLogin() {
        return MSE04;
    }

    //getErrorMessageEmailAlreadyExists return an error message about a email that already exists
    public static String getErrorMessageEmailAlreadyExists() {
        return MSE05;
    }

    //getErrorMessageUserNotFound return an error message about an email not found on database
    public static String getErrorMessageUserNotFound() {
        return MSE06;
    }

}
