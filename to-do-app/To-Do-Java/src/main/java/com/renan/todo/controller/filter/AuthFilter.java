package com.renan.todo.controller.filter;

import com.google.gson.Gson;
import com.renan.todo.dto.UserDTO;
import com.renan.todo.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

/**
 * Filter some endpoints checking if the user is authenticated
 */
@WebFilter(urlPatterns = { "/api/user/*", "/api/taskList/*", "/api/task/*", "/api/tasks/*" })
@RequiredArgsConstructor
public class AuthFilter implements Filter {

    private final JwtService jwtService;
    public static final String AUTH = "AUTH";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest  httpRequest  = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (httpRequest.getMethod().equals("OPTIONS")) {
            chain.doFilter(httpRequest, httpResponse);
        } else {
            String jwt    = httpRequest.getHeader(AUTH);
            Claims claims = validateToken(jwt);
            if (claims != null) {
                try {
                    Gson gson    = new Gson();
                    UserDTO userDTO = new UserDTO();
                    userDTO.setId((Integer) claims.get(jwtService.CLAIMS_ID_USER));
                    userDTO.setEmail((String) claims.get(jwtService.CLAIMS_EMAIL));
                    userDTO.setStatus((Integer) claims.get(jwtService.CLAIMS_ID_STATUS));
                    userDTO.setExpiresIn(jwtService.TIMEOUT);
                    userDTO.setJwt(jwtService.refreshJWT(jwt));
                    httpResponse.setHeader(AUTH, gson.toJson(userDTO));
                } catch (Exception e) {
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                }
                chain.doFilter(httpRequest, httpResponse);
            } else {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }

    /**
     * Validate the jwt token
     *
     * @param jwt - the jwt token
     * @return - true if is valid
     */
    private Claims validateToken(String jwt) {
        Claims claims;
        try {
            claims = jwtService.validateJWT(jwt);
            if (claims.containsKey(jwtService.CLAIMS_ID_USER) && claims.containsKey(jwtService.CLAIMS_ID_STATUS)) {
                return claims;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}