package com.renan.todo.controller.auth.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Form for user activation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivationUserForm {

    private String email;
    private String activationCode;

}
