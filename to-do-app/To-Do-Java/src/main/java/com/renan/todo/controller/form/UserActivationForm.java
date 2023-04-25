package com.renan.todo.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Form for new user activation
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivationForm {

    private String email;
    private String activationCode;

}