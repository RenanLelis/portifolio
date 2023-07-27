package com.renan.webstore.security;

import com.renan.webstore.model.Authority;
import com.renan.webstore.service.AppErrorType;
import com.renan.webstore.service.BusinessException;
import com.renan.webstore.util.MessageUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        Authentication authentication = getAuthentication(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String jwt) {
        try {
            Claims claims = jwtService.validateJWT(jwt);
            List<String> roles = (List<String>) claims.get(JwtService.CLAIMS_ROLES);
            String username = (String) claims.get(JwtService.CLAIMS_EMAIL);
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (String role: roles) {
                authorities.add(new Authority(role));
            }
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new BusinessException(
                    MessageUtil.getErrorMessageToken(),
                    BusinessException.BUSINESS_MESSAGE,
                    AppErrorType.NOT_ALLOWED
            );
        }
    }

}
