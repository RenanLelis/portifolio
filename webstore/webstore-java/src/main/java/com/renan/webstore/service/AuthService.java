package com.renan.webstore.service;

import com.renan.webstore.dto.UserDto;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService, AuthenticationProvider {
    
    UserDto login(String email, String password) throws BusinessException;
    
    void recoverPassword(String email) throws BusinessException;
    
    UserDto resetPasswordByCode(String email, String newPassword, String newPasswordCode) throws BusinessException;
    
    void registerUser(String email, String password, String name, String lastName) throws BusinessException;
    
    UserDto activateUser(String email, String activationCode) throws BusinessException;
    
    void requestUserActivation(String email) throws BusinessException;
    
}
