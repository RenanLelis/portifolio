package com.renan.todo.controller.auth;

import com.renan.todo.business.BusinessException;
import com.renan.todo.business.UserService;
import com.renan.todo.controller.auth.form.*;
import com.renan.todo.dto.ErrorDTO;
import com.renan.todo.dto.UserDTO;
import com.renan.todo.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller for authentication, forget password and register operations
 */
@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * User login on the system
     *
     * @param form - form with email and password
     * @return - the response entity (http response)
     */
    @CrossOrigin
    @PostMapping(path = "/api/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(@RequestBody LoginForm form) {
        if (form.getEmail() == null || form.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
        }
        try {
            UserDTO user = userService.login(form.getEmail().trim(), form.getPassword().trim());
            return ResponseEntity.ok(user);
        } catch (BusinessException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * Post for password recover
     *
     * @param form - form with the input data
     * @return - the ResponseEntity
     */
    @CrossOrigin
    @PostMapping(path = "/api/auth/recoverpassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity recoverPassword(@RequestBody ForgotPasswordForm form) {
        if (form.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
        }
        try {
            userService.recoverPassword(form.getEmail().trim());
            return ResponseEntity.ok().build();
        } catch (BusinessException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * Post mapping for reset password
     *
     * @param form - input data, email and newPasswordCode
     * @return - the response entity
     */
    @CrossOrigin
    @PostMapping(path = "/api/auth/passwordreset", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity resetPassword(@RequestBody ResetPasswordForm form) {
        if (form.getEmail() == null || form.getPassword() == null || form.getNewPasswordCode() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
        }
        try {
            UserDTO user = userService.resetPasswordByCode(form.getEmail().trim(), form.getPassword().trim(), form.getNewPasswordCode().trim());
            return ResponseEntity.ok(user);
        } catch (BusinessException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * Register a new user on the database
     *
     * @param form - input data
     * @return - the new user
     */
    @CrossOrigin
    @PostMapping(path = "/api/auth/userregistration", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registerNewUser(@RequestBody NewUserForm form) {
        if (form.getEmail() == null || form.getPassword() == null || form.getUserName() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
        }
        try {
            UserDTO user = userService.registerUser(form.getEmail().trim(), form.getPassword().trim(), form.getUserName(), form.getLastName());
            return ResponseEntity.ok(user);
        } catch (BusinessException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * Activate the user using the activation code sent
     *
     * @param form - input data, email and activation code
     * @return - the user activated and logged in
     */
    @CrossOrigin
    @PostMapping(path = "/api/auth/useractivation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity activateUser(@RequestBody ActivationUserForm form) {
        if (form.getEmail() == null || form.getActivationCode() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
        }
        try {
            UserDTO user = userService.activateUser(form.getEmail().trim(), form.getActivationCode().trim());
            return ResponseEntity.ok(user);
        } catch (BusinessException e) {
            return e.getResponseEntity();
        }
    }

}
