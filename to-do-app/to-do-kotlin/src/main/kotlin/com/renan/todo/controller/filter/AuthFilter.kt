package com.renan.todo.controller.filter

import com.google.gson.Gson
import com.renan.todo.dto.UserDTO
import com.renan.todo.service.JwtService
import com.renan.todo.util.CLAIMS_EMAIL
import com.renan.todo.util.CLAIMS_ID_STATUS
import com.renan.todo.util.CLAIMS_ID_USER
import com.renan.todo.util.TIMEOUT
import io.jsonwebtoken.Claims
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.annotation.WebFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException

const val AUTH = "AUTHORIZATION"

/**
 * Filter some endpoints checking if the user is authenticated
 */
@WebFilter(urlPatterns = ["/api/user/*", "/api/taskList/*", "/api/task/*", "/api/tasks/*"])
class AuthFilter(val jwtService: JwtService) : Filter {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse
        if (httpRequest.method == "OPTIONS") {
            chain.doFilter(httpRequest, httpResponse)
        } else {
            val jwt = httpRequest.getHeader(AUTH)
            val claims = validateToken(jwt)
            if (claims != null) {
                try {
                    val gson = Gson()
                    val userDTO = UserDTO()
                    userDTO.id = (claims[CLAIMS_ID_USER] as Int?)!!
                    userDTO.email = (claims[CLAIMS_EMAIL] as String?)!!
                    userDTO.status = (claims[CLAIMS_ID_STATUS] as Int?)!!
                    userDTO.expiresIn = TIMEOUT
                    userDTO.jwt = jwtService.refreshJWT(jwt) ?: ""
                    httpResponse.setHeader(AUTH, gson.toJson(userDTO))
                } catch (e: Exception) {
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.message)
                }
                chain.doFilter(httpRequest, httpResponse)
            } else {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED)
            }
        }
    }

    /**
     * Validate the jwt token
     */
    private fun validateToken(jwt: String): Claims? {
        val claims: Claims?
        return try {
            claims = jwtService.validateJWT(jwt)
            if (claims != null && claims.containsKey(CLAIMS_ID_USER) && claims.containsKey(CLAIMS_ID_STATUS)) {
                claims
            } else null
        } catch (e: Exception) {
            null
        }
    }

}