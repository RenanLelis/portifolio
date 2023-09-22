package com.renan.webstore.business.impl;

import com.renan.webstore.business.ValidatorService;
import com.renan.webstore.model.AppUser;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidatorServiceImpl implements ValidatorService {
    
    public boolean isMail(String str) {
        if (str != null && !str.isBlank()) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(str);
            return matcher.matches();
        }
        return false;
    }
    
    public boolean isValidDate(String dateStr) {
        DateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean isValidPassword(String password) {
        boolean valid = password != null && !password.isBlank();
        String formattedPassword = valid ? password.trim() : "";
        valid = valid && formattedPassword.length() >= 6 && formattedPassword.length() <= 20;
        if (valid) {
            char ch;
            boolean capitalFlag = false;
            boolean lowerCaseFlag = false;
            boolean numberFlag = false;
            for(int i=0;i < formattedPassword.length();i++) {
                ch = formattedPassword.charAt(i);
                if( Character.isDigit(ch)) {
                    numberFlag = true;
                }
                else if (Character.isUpperCase(ch)) {
                    capitalFlag = true;
                } else if (Character.isLowerCase(ch)) {
                    lowerCaseFlag = true;
                }
                if(numberFlag && capitalFlag && lowerCaseFlag)
                    return true;
            }
        }
        return false;
    }
    
    public boolean isValidNewPasswordCode(String newPasswordCode) {
        return newPasswordCode != null && !newPasswordCode.isBlank() &&
                newPasswordCode.trim().length() == AppUser.LENGTH_NEW_PASSWORD_CODE;
    }
    
    public boolean isValidActivationCode(String activationCode) {
        return activationCode != null && !activationCode.isBlank() &&
                activationCode.trim().length() == AppUser.LENGTH_ACTIVATION_CODE;
    }
    
    
    
}

