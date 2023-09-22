package com.renan.webstore.dto.mapper

import com.renan.webstore.business.JwtService
import com.renan.webstore.business.TIMEOUT
import com.renan.webstore.dto.UserDTO
import com.renan.webstore.model.AppUser
import com.renan.webstore.model.STATUS_ACTIVE
import com.renan.webstore.model.buildRoles
import org.springframework.stereotype.Component
import java.util.function.Function

@Component
class UserDTOMapper(
    val jwtService: JwtService
) : Function<AppUser, UserDTO> {

    override fun apply(user: AppUser): UserDTO {

        return UserDTO(
            email = user.email,
            jwt = jwtService.generateJWT(
                idUser = user.id!!,
                active = user.userStatus == STATUS_ACTIVE,
                email = user.email,
                firstName = user.firstName,
                lastName = user.lastName,
                roles = buildRoles(user.roles)
            ),
            id = user.id,
            active = user.userStatus == STATUS_ACTIVE,
            expiresIn = TIMEOUT,
            name = user.firstName,
            lastName = user.lastName,
            roles = buildRoles(user.roles)
        )
    }

}