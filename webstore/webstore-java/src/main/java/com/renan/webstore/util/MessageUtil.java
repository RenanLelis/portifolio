package com.renan.webstore.util;

public class MessageUtil {
    
    private static final String MSE01 = "MSE01"; // An error ocurred on the operation.
    private static final String MSE02 = "MSE02"; // Error on values properties.
    private static final String MSE03 = "MSE03"; // Invalid Token.
    private static final String MSE04 = "MSE04"; // Invalid User or Password.
    private static final String MSE05 = "MSE05"; // E-mail already exists.
    private static final String MSE06 = "MSE06"; // E-mail not found.
    private static final String MSE07 = "MSE07"; // User not activated.
    private static final String MSE08 = "MSE08"; // User does not have the required privileges.
    
    public static String getErrorMessage() { return MSE01; }
    
    public static String getErrorMessageInputValues() {
        return MSE02;
    }
    
    public static String getErrorMessageToken() {
        return MSE03;
    }
    
    public static String getErrorMessageLogin() {
        return MSE04;
    }
    
    public static String getErrorMessageEmailAlreadyExists() {
        return MSE05;
    }
    
    public static String getErrorMessageUserNotFound() {
        return MSE06;
    }
    
    public static String getErrorMessageUserNotActive() {
        return MSE07;
    }
    
    public static String getErrorMessageDontHavePrivilege() { return MSE08; } // User does not have the required privileges.
}
