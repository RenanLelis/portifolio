package com.renan.todo.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Form for login operations
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {
    private String email;
    private String password;
}