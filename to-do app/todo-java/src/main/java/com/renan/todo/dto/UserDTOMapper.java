package com.renan.todo.dto;

import com.renan.todo.entities.User;
import com.renan.todo.util.JwtUtil;

public class UserDTOMapper {

    /**
     * Convert an user to an userDTO, generating the JWT
     *
     * @param user - object to convert to DTO
     * @return - thw dto
     */
    public static UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getEmail(),
                JwtUtil.generateJWT(user.getId(), user.getUserStatus(), user.getEmail()),
                user.getId(),
                user.getUserStatus(),
                JwtUtil.TIMEOUT,
                user.getUserName(),
                user.getLastName()
        );
    }

}
