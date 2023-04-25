package com.renan.todo.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Form for request a user activation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestUserActivationForm {
    private String email;
}