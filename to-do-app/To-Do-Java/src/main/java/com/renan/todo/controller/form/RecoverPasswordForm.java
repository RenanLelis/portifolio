package com.renan.todo.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Form for password recover operations
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecoverPasswordForm {
    private String email;
}