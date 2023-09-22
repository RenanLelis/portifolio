package com.renan.webstore.business;

import java.util.List;

public interface MailService {
    
    void sendEmail(String destination, String subject, String message) throws BusinessException;
    
    void sendEmail(List<String> destinationList, String subject, String message) throws BusinessException;
    
    void sendNewPasswordEmail(String destination, String newPasswordCode) throws BusinessException;
    
    void sendActivationEmail(String destination, String activationCode) throws BusinessException;
    
}
