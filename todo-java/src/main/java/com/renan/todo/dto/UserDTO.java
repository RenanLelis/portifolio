package com.renan.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User DTO - with de token
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String email;
    private String jwt;
    private Integer id;
    private Integer status;
    private Integer expiresIn;
    private String name;
    private String lastName;

}
