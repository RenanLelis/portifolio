package com.renan.todo.dto;

import com.renan.todo.model.User;
import com.renan.todo.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * Mapper to create a UserDTO from a User Object
 */
@Service
@RequiredArgsConstructor
public class UserDTOMapper implements Function<User, UserDTO> {

    private final JwtService jwtService;

    /**
     * Convert an user to an userDTO, generating the JWT
     *
     * @param user - object to convert to DTO
     *
     * @return - thw dto
     */
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getEmail(),
                jwtService.generateJWT(user.getId(), user.getUserStatus(), user.getEmail(), user.getFirstName(), user.getLastName()),
                user.getId(),
                user.getUserStatus(),
                jwtService.TIMEOUT,
                user.getFirstName(),
                user.getLastName()
        );
    }
}