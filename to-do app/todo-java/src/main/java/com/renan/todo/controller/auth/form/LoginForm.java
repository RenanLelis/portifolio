package com.renan.todo.controller.auth.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * form for login operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {

    private String email;
    private String password;

}
