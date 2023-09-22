package com.renan.webstore.controller.form.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetForm {
    private String email;
    private String password;
    private String newPasswordCode;
}
