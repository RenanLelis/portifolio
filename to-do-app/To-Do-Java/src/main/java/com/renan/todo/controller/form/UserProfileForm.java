package com.renan.todo.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Form for user profile update
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileForm {

    private String firstName;
    private String lastName;

}