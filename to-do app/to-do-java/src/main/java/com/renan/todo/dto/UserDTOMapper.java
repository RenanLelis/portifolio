package com.renan.todo.dto;

import com.renan.todo.model.User;
import com.renan.todo.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * Mapper to create a UserDTO from a User Object
 */
@Service
public class UserDTOMapper implements Function<User, UserDTO> {

    /**
     * Convert an user to an userDTO, generating the JWT
     *
     * @param user - object to convert to DTO
     * @return - thw dto
     */
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getEmail(),
                JwtUtil.generateJWT(user.getId(), user.getUserStatus(), user.getEmail(), user.getFirstName(), user.getLastName()),
                user.getId(),
                user.getUserStatus(),
                JwtUtil.TIMEOUT,
                user.getFirstName(),
                user.getLastName()
        );
    }
}
