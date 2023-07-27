package com.renan.webstore.controller.form.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserActivationForm {
    private String email;
    private String activationCode;
}
