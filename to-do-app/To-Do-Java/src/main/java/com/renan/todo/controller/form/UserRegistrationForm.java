package com.renan.todo.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Form for new user registration
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationForm {

    private String email;
    private String password;
    private String firstName;
    private String lastName;

}