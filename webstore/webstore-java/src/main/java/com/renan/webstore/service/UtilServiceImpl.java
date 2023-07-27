package com.renan.webstore.service;

import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;

@Service
public class UtilServiceImpl implements UtilService {
    
    public String generateRandomString() {
        return generateRandomString(LENGTH_STRING_RANDOM);
    }
    
    public String generateRandomString(Integer length) {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        Random random = new Random();
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int r = random.nextInt(chars.length - 1);
            value.append(chars[r]);
        }
        return value.toString();
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
    
}
