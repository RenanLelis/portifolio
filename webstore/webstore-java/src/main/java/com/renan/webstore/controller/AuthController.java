package com.renan.webstore.controller;

import com.renan.webstore.controller.form.auth.*;
import com.renan.webstore.dto.UserDto;
import com.renan.webstore.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/public/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto login(@RequestBody LoginForm form) {
        return authService.login(form.getEmail(), form.getPassword());
    }
    
    @PostMapping(value = "/recoverpassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void recoverPassword(@RequestBody RecoverPasswordForm form) {
        authService.recoverPassword(form.getEmail());
    }
    
    @PostMapping(value = "/passwordreset", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto passwordReset(@RequestBody PasswordResetForm form) {
        return authService.resetPasswordByCode(form.getEmail(), form.getPassword(), form.getNewPasswordCode());
    }
    
    @PostMapping(value = "/userregistration", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void registerNewUser(@RequestBody UserRegistrationForm form) {
        authService.registerUser(
                form.getEmail(),
                form.getPassword(),
                form.getFirstName(),
                form.getLastName()
        );
    }
    
    @PostMapping(value = "/useractivation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto activateUser(@RequestBody UserActivationForm form) {
        return authService.activateUser(
                form.getEmail(),
                form.getActivationCode()
        );
    }
    
    @PostMapping(value = "/useractivationrequest", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void requestUserActivation(@RequestBody RequestUserActivationForm form) {
        authService.requestUserActivation(form.getEmail());
    }
    
    @GetMapping(value = "/test")
    public String getTestMessageAuth() {
        return "Teste de mensagem no endpoint ";
    }
    
}
