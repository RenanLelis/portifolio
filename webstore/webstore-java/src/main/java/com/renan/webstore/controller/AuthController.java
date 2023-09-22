package com.renan.webstore.controller;

import com.renan.webstore.business.AuthService;
import com.renan.webstore.controller.form.auth.*;
import com.renan.webstore.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/public/auth/")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping(value = "/login")
    public UserDTO login(@RequestBody LoginForm form){
        return authService.login(form);
    }
    
    @PostMapping(value = "/recoverpassword")
    public void recoverPassword(@RequestBody RecoverPasswordForm form){
        authService.recoverPassword(form);
    }
    
    @PostMapping(value = "/passwordreset")
    public UserDTO resetPasswordByCode(@RequestBody PasswordResetForm form) {
        return authService.resetPasswordByCode(form);
    }
    
    @PostMapping(value = "/api/public/auth/userregistration")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerNewUser(@RequestBody UserRegistrationForm form){
        authService.registerUser(form);
    }
    
    @PostMapping(value = "/useractivation")
    public UserDTO activateUser(@RequestBody UserActivationForm form){
        return authService.activateUser(form);
    }
    
    @PostMapping(value = "/useractivationrequest")
    public void requestUserActivation(@RequestBody RequestUserActivationForm form){
        authService.requestUserActivation(form);
    }
    
}

