package com.renan.webstore.dto.mapper;

import com.renan.webstore.business.JwtService;
import com.renan.webstore.dto.UserDTO;
import com.renan.webstore.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class UserDTOMapper implements Function<AppUser, UserDTO> {
    
    private final JwtService jwtService;
    
    @Override
    public UserDTO apply(AppUser user) {
        List<String> roles = getRoles(user.getRoles());
        return new UserDTO(
                user.getEmail(),
                jwtService.generateJWT(user.getId(), user.isActive(), user.getEmail(), user.getFirstName(), user.getLastName(), roles),
                user.getId(),
                user.isActive(),
                jwtService.TIMEOUT,
                user.getFirstName(),
                user.getLastName(),
                roles
        );
    }
    
    List<String> getRoles(String userRoles){
        return Arrays.stream(userRoles.split(",")).toList();
    }
    
}
