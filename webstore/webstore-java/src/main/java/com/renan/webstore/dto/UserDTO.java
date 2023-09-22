package com.renan.webstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
    private String email;
    private String jwt;
    private Integer id;
    private boolean active;
    private Integer expiresIn;
    private String name;
    private String lastName;
    private List<String> roles;
}
