package com.renan.webstore.business;

import com.renan.webstore.controller.form.auth.*;
import com.renan.webstore.dto.UserDTO;

public interface AuthService {
    
    UserDTO login(LoginForm form) throws BusinessException;
    
    void recoverPassword(RecoverPasswordForm form) throws BusinessException;
    
    UserDTO resetPasswordByCode(PasswordResetForm form) throws BusinessException;
    
    void registerUser(UserRegistrationForm form) throws BusinessException;
    
    UserDTO activateUser(UserActivationForm form) throws BusinessException;
    
    void requestUserActivation(RequestUserActivationForm form) throws BusinessException;
    
}
