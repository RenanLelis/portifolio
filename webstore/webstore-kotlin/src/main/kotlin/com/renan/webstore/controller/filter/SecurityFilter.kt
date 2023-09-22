package com.renan.webstore.controller.filter

import com.renan.webstore.business.*
import com.renan.webstore.model.AppUser
import com.renan.webstore.model.STATUS_ACTIVE
import com.renan.webstore.model.STATUS_INACTIVE
import com.renan.webstore.model.buildRole
import io.jsonwebtoken.Claims
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class SecurityFilter(
    val jwtService: JwtService
) : OncePerRequestFilter()  {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        var authorization = request.getHeader("Authorization")
        if (authorization != null) {
            authorization = authorization.replace("Bearer ", "")
            val claims: Claims = jwtService.validateJWT(authorization)

            val idUser = claims[CLAIMS_ID_USER] as Int
            val active = claims[CLAIMS_STATUS] as Boolean
            val email = claims[CLAIMS_EMAIL] as String
            val firstName = claims[CLAIMS_FIRST_NAME] as String
            val lastName = claims[CLAIMS_LAST_NAME] as String?
            val roles = claims[CLAIMS_ROLES] as List<String>

            val user = AppUser(
                id = idUser,
                firstName = firstName,
                lastName = lastName,
                email = email,
                userPassword = "",
                activationCode = null,
                newPasswordCode = null,
                userStatus = if (active) STATUS_ACTIVE else STATUS_INACTIVE,
                roles = buildRole(roles)
            )

            val authentication = UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }

}