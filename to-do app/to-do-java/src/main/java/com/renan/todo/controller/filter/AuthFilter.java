package com.renan.todo.controller.filter;

import java.io.IOException;

import com.google.gson.Gson;
import com.renan.todo.dto.UserDTO;
import com.renan.todo.util.JwtUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter some endpoints checking if the user is authenticated
 */
@WebFilter(urlPatterns = { "/api/user/*", "/api/taskList/*", "/api/task/*", "/api/tasks/*" })
public class AuthFilter implements Filter {

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
					Gson    gson    = new Gson();
					UserDTO userDTO = new UserDTO();
					userDTO.setId((Integer) claims.get(JwtUtil.CLAIMS_ID_USER));
					userDTO.setEmail((String) claims.get(JwtUtil.CLAIMS_EMAIL));
					userDTO.setStatus((Integer) claims.get(JwtUtil.CLAIMS_ID_STATUS));
					userDTO.setExpiresIn(JwtUtil.TIMEOUT);
					userDTO.setJwt(JwtUtil.refreshJWT(jwt));
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
			claims = JwtUtil.validateJWT(jwt);
			if (claims.containsKey(JwtUtil.CLAIMS_ID_USER) && claims.containsKey(JwtUtil.CLAIMS_ID_STATUS)) {
				return claims;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
}
