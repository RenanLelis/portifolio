package com.renan.todo.controller.auth.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * form for reset password with the generated code
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordForm {

    private String email;
    private String password;
    private String newPasswordCode;

}
