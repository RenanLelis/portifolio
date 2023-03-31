package com.renan.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.renan.todo.business.BusinessException;
import com.renan.todo.business.UserService;
import com.renan.todo.controller.filter.AuthFilter;
import com.renan.todo.controller.form.UserPasswordForm;
import com.renan.todo.controller.form.UserProfileForm;
import com.renan.todo.dto.ErrorDTO;
import com.renan.todo.util.JwtUtil;
import com.renan.todo.util.MessageUtil;

/**
 * The controller for user profile and password update
 */
@RestController()
@SuppressWarnings("rawtypes")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * Update information about the user
	 * 
	 * @param jwt  - the auth token from the user
	 * @param form - User profile infomration
	 * @return - Response Entity
	 */
	@PutMapping(value = "/api/user/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity updateUser(@RequestHeader(AuthFilter.AUTH) String jwt, @RequestBody UserProfileForm form) {
		try {
			if (form.getName() == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				        .body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
			}
			Integer userID = JwtUtil.getIdUser(jwt);
			userService.updateUser(userID, form.getName().trim(),
			        form.getLastName() != null ? form.getLastName().trim() : null);
			return ResponseEntity.ok().build();
		} catch (BusinessException e) {
			return e.getResponseEntity();
		}
	}

	/**
	 * Update user password
	 *
	 * @param jwt  - the auth token from the user
	 * @param form - User new password form
	 * @return - Response Entity
	 */
	@PutMapping(value = "/api/user/password", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity updateUserPassword(@RequestHeader(AuthFilter.AUTH) String jwt,
	        @RequestBody UserPasswordForm form) {
		try {
			if (form.getPassword() == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				        .body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
			}
			Integer userID = JwtUtil.getIdUser(jwt);
			userService.updateUserPassword(userID, form.getPassword().trim());
			return ResponseEntity.ok().build();
		} catch (BusinessException e) {
			return e.getResponseEntity();
		}
	}

}
