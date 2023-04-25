package com.renan.todo.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Form for password reset operations
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetForm {
    private String email;
    private String password;
    private String newPasswordCode;
}