package com.renan.webstore.controller.form.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegistrationForm {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
