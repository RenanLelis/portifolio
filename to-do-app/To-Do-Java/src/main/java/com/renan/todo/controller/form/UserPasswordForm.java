package com.renan.todo.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Form for user password update
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordForm {
    private String password;
}