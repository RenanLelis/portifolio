package com.renan.todo.controller;

import com.renan.todo.controller.form.*;
import com.renan.todo.dto.ErrorDTO;
import com.renan.todo.dto.UserDTO;
import com.renan.todo.service.AuthService;
import com.renan.todo.service.BusinessException;
import com.renan.todo.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller for authentication, forget password and register operations
 */
@RestController()
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * User login on the system
     *
     * @param form - user email and password
     *
     * @return - the user object with a signed token
     */
    @PostMapping(value = "/api/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO login(@RequestBody LoginForm form) {
        return authService.login(
                form.getEmail() != null ? form.getEmail().trim().toLowerCase() : null,
                form.getPassword() != null ? form.getPassword().trim() : null);
    }

    /**
     * Generate a code to recover password and send to e-mail
     *
     * @param form - form data with user email
     */
    @PostMapping(value = "/api/auth/recoverpassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void recoverPassword(@RequestBody RecoverPasswordForm form) {
        authService.recoverPassword(form.getEmail() != null ? form.getEmail().trim().toLowerCase() : null);
    }

    /**
     * Reset the password with the code send to e-mail
     *
     * @param form - form data with user email, new password and the code sent by
     *             email
     *
     * @return - Response Entity
     */
    @PostMapping(value = "/api/auth/passwordreset", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO passwordReset(@RequestBody PasswordResetForm form) {
        return authService.resetPasswordByCode(
                form.getEmail() != null ? form.getEmail().trim().toLowerCase() : null,
                form.getPassword() != null ? form.getPassword().trim() : null,
                form.getNewPasswordCode() != null ? form.getNewPasswordCode().trim() : null);
    }

    /**
     * Register a new user on the database
     *
     * @param form - form data with user first name, last name, email and password
     */
    @PostMapping(value = "/api/auth/userregistration", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void registerNewUser(@RequestBody UserRegistrationForm form) {
        authService.registerUser(
                form.getEmail() != null ? form.getEmail().trim().toLowerCase() : null,
                form.getPassword() != null ? form.getPassword().trim() : null,
                form.getFirstName() != null ? form.getFirstName().trim() : null,
                form.getLastName() != null ? form.getLastName().trim() : null);
    }

    /**
     * activate user on the system
     *
     * @param form - form data with user email, and activation code
     *
     * @return - UserDTO - with user data and JWT Token
     */
    @PostMapping(value = "/api/auth/useractivation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO activateUser(@RequestBody UserActivationForm form) {
        return authService.activateUser(
                form.getEmail() != null ? form.getEmail().trim().toLowerCase() : null,
                form.getActivationCode() != null ? form.getActivationCode().trim() : null);
    }

    /**
     * request for user activation on the system
     *
     * @param form - form data with user email, and activation code
     */
    @PostMapping(value = "/api/auth/useractivationrequest", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void requestUserActivation(@RequestBody RequestUserActivationForm form) {
        authService.requestUserActivation(form.getEmail() != null ? form.getEmail().trim().toLowerCase() : null);
    }

    /**
     * Handle de business exceptions and return the response entity with the status code
     *
     * @param e - the exception
     *
     * @return the response entity with the status code and error dto
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDTO> handleBusinessExceptions(BusinessException e) {
        return e.getResponseEntity();
    }

    /**
     * Handle exceptions and return the response entity with the status code
     *
     * @param e - the exception
     *
     * @return the response entity with the status code and error dto
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleExceptions(Exception e) {
        e.printStackTrace();
        if (e instanceof BusinessException) {
            return ((BusinessException) e).getResponseEntity();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDTO(MessageUtil.getErrorMessage()));
    }

}