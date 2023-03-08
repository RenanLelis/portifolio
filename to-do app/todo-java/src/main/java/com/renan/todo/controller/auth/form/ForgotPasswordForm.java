package com.renan.todo.controller.auth.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * form for recover password operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordForm {
    private String email;
}
