package com.renan.webstore.business;

public interface ValidatorService {
    String DATE_FORMAT = "yyyy-MM-dd";
    boolean isMail(String str);
    boolean isValidDate(String dateStr);
    boolean isValidPassword(String password);
    boolean isValidNewPasswordCode(String newPasswordCode);
    boolean isValidActivationCode(String activationCode);
}
