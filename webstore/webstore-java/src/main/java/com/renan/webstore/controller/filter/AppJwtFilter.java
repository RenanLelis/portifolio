package com.renan.webstore.controller.filter;

import com.google.gson.Gson;
import com.renan.webstore.business.JwtService;
import com.renan.webstore.dto.UserDTO;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@WebFilter(urlPatterns = { "/api/store/*"})
@RequiredArgsConstructor
public class AppJwtFilter implements Filter {
    
    private final JwtService jwtService;
    
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest  = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (httpRequest.getMethod().equals("OPTIONS")) {
            chain.doFilter(httpRequest, httpResponse);
        } else {
            String jwt    = httpRequest.getHeader(JwtService.AUTH);
            Claims claims = jwtService.validateJWT(jwt);
            if (claims != null) {
                try {
                    Gson gson    = new Gson();
                    UserDTO userDTO = new UserDTO();
                    userDTO.setId((String) claims.get(jwtService.CLAIMS_ID_USER));
                    userDTO.setEmail((String) claims.get(jwtService.CLAIMS_EMAIL));
                    userDTO.setActive((Boolean) claims.get(jwtService.CLAIMS_STATUS));
                    userDTO.setExpiresIn(jwtService.TIMEOUT);
                    userDTO.setRoles((List<String>) claims.get(jwtService.CLAIMS_ROLES));
                    userDTO.setName((String) claims.get(jwtService.CLAIMS_FIRST_NAME));
                    userDTO.setLastName((String) claims.get(jwtService.CLAIMS_LAST_NAME));
                    userDTO.setJwt(jwtService.refreshJWT(jwt));
                    httpResponse.setHeader(JwtService.AUTH, gson.toJson(userDTO));
                } catch (Exception e) {
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                }
                chain.doFilter(httpRequest, httpResponse);
            } else {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }
    
    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
