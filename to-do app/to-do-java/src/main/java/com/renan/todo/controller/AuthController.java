package com.renan.todo.controller;

import com.renan.todo.business.AppErrorType;
import com.renan.todo.business.AuthService;
import com.renan.todo.business.BusinessException;
import com.renan.todo.controller.form.*;
import com.renan.todo.dto.ErrorDTO;
import com.renan.todo.dto.UserDTO;
import com.renan.todo.util.MessageUtil;
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
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * User login on the system
     *
     * @param form - user email and password
     *
     * @return - the user object with a signed token
     */
    @PostMapping(value = "/api/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO login(@RequestBody LoginForm form) {
        if (form.getEmail() == null || form.getPassword() == null) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        UserDTO user = authService.login(form.getEmail().trim().toLowerCase(), form.getPassword().trim());
        return user;
    }

    /**
     * Generate a code to recover password and send to e-mail
     *
     * @param form - form data with user email
     *
     * @return - Response Entity
     */
    @PostMapping(value = "/api/auth/recoverpassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void recoverPassword(@RequestBody RecoverPasswordForm form) {
        if (form.getEmail() == null) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        authService.recoverPassword(form.getEmail().trim().toLowerCase());
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
        if (form.getEmail() == null || form.getPassword() == null || form.getNewPasswordCode() == null) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        UserDTO user = authService.resetPasswordByCode(form.getEmail().trim().toLowerCase(),
                form.getPassword().trim(), form.getNewPasswordCode().trim());
        return user;
    }

    /**
     * Register a new user on the database
     *
     * @param form - form data with user first name, last name, email and password
     */
    @PostMapping(value = "/api/auth/userregistration", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void registerNewUser(@RequestBody UserRegistrationForm form) {
        if (form.getEmail() == null || form.getPassword() == null || form.getFirstName() == null) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        authService.registerUser(form.getEmail().trim().toLowerCase(), form.getPassword().trim(),
                form.getFirstName().trim(), form.getLastName() != null ? form.getLastName().trim() : null);
    }

    /**
     * activate user on the system
     *
     * @param form - form data with user email, and activation code
     *
     * @return - Response Entity
     */
    @PostMapping(value = "/api/auth/useractivation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO activateUser(@RequestBody UserActivationForm form) {
        if (form.getEmail() == null || form.getActivationCode() == null) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        UserDTO user = authService.activateUser(form.getEmail().trim().toLowerCase(),
                form.getActivationCode().trim());
        return user;
    }

    /**
     * request for user activation on the system
     *
     * @param form - form data with user email, and activation code
     *
     * @return - Response Entity
     */
    @PostMapping(value = "/api/auth/useractivationrequest", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void requestUserActivation(@RequestBody RequestUserActivationForm form) {
        if (form.getEmail() == null) {
            throw new BusinessException(MessageUtil.getErrorMessageInputValues(),
                    BusinessException.BUSINESS_MESSAGE, AppErrorType.INVALID_INPUT);
        }
        authService.requestUserActivation(form.getEmail().trim().toLowerCase());
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