package com.renan.webstore.service;

public interface UtilService {
    
    String DATE_FORMAT = "yyyy-MM-dd";
    Integer LENGTH_STRING_RANDOM = 6;
    
    String generateRandomString();
    
    String generateRandomString(Integer length);
    
    boolean isValidDate(String dateStr);
    
}
