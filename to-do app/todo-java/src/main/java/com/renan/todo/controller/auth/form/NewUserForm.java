package com.renan.todo.controller.auth.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * form for new user registration
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserForm {
    private String userName;
    private String lastName;
    private String email;
    private String password;
}
