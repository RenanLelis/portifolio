package com.renan.webstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private String email;
    private String jwt;
    private Integer id;
    private Integer status;
    private Integer expiresIn;
    private String name;
    private String lastName;
    private List<String> roles;
}
