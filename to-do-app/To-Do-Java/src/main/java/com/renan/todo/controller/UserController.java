package com.renan.todo.controller;

import com.renan.todo.controller.filter.AuthFilter;
import com.renan.todo.controller.form.UserPasswordForm;
import com.renan.todo.controller.form.UserProfileForm;
import com.renan.todo.dto.ErrorDTO;
import com.renan.todo.service.BusinessException;
import com.renan.todo.service.JwtService;
import com.renan.todo.service.UserService;
import com.renan.todo.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The controller for user profile and password update
 */
@RestController()
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    /**
     * Update information about the user
     *
     * @param jwt  - the auth token from the user
     * @param form - User profile information
     */
    @PutMapping(value = "/api/user/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateUser(@RequestHeader(AuthFilter.AUTH) String jwt, @RequestBody UserProfileForm form) {
        Integer userID = jwtService.getIdUser(jwt);
        userService.updateUser(userID,
                form.getFirstName() != null ? form.getFirstName().trim() : "",
                form.getLastName() != null ? form.getLastName().trim() : null);
    }

    /**
     * Update user password
     *
     * @param jwt  - the auth token from the user
     * @param form - User new password form
     */
    @PutMapping(value = "/api/user/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateUserPassword(@RequestHeader(AuthFilter.AUTH) String jwt
            , @RequestBody UserPasswordForm form) {
        Integer userID = jwtService.getIdUser(jwt);
        userService.updateUserPassword(
                userID,
                form.getPassword() != null ? form.getPassword().trim() : null);
    }

}