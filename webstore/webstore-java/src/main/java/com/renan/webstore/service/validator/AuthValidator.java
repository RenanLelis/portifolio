package com.renan.webstore.service.validator;

import com.renan.webstore.service.BusinessException;

public interface AuthValidator {
    
    void validateLoginInput(String email, String password) throws BusinessException;
    
    void validateNewUserInput(String email, String password, String name, String lastName) throws BusinessException;
    
    void validateResetPasswordByCodeInput(String email, String newPassword, String newPasswordCode) throws BusinessException;
    
    void validateUserActivationInput(String email, String activationCode) throws BusinessException;
    
    void validateRequestUserActivationInput(String email) throws BusinessException;
    
    void validateRecoverPasswordInput(String email) throws BusinessException;
    
    boolean isEmailValid(String email);
    
    boolean isPasswordRequirementsValid(String password);
    
    boolean isNewpasswordCodeValid(String newPasswordCode);
    
    boolean isActivationCodeValid(String activationCode);
    
}
