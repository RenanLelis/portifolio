package com.renan.webstore.controller.form.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginForm {
    private String email;
    private String password;
}
