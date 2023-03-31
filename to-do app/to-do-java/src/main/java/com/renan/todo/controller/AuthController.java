package com.renan.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.renan.todo.business.AuthService;
import com.renan.todo.business.BusinessException;
import com.renan.todo.controller.form.LoginForm;
import com.renan.todo.controller.form.PasswordResetForm;
import com.renan.todo.controller.form.RecoverPasswordForm;
import com.renan.todo.controller.form.RequestUserActivationForm;
import com.renan.todo.controller.form.UserActivationForm;
import com.renan.todo.controller.form.UserRegistrationForm;
import com.renan.todo.dto.ErrorDTO;
import com.renan.todo.dto.UserDTO;
import com.renan.todo.util.MessageUtil;

/**
 * The controller for authentication, forget password and register operations
 */
@RestController()
@SuppressWarnings("rawtypes")
public class AuthController {

	@Autowired
	private AuthService authService;

	/**
	 * User login on the system
	 *
	 * @param form - user email and password
	 * @return - the user object with a signed token
	 */
	@PostMapping(value = "/api/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity login(@RequestBody LoginForm form) {
		if (form.getEmail() == null || form.getPassword() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			        .body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
		}
		try {
			UserDTO user = authService.login(form.getEmail().trim().toLowerCase(), form.getPassword().trim());
			return ResponseEntity.ok(user);
		} catch (BusinessException e) {
			return e.getResponseEntity();
		}
	}

	/**
	 * Generate a code to recover password and send to e-mail
	 *
	 * @param form - form data with user email
	 * @return - Response Entity
	 */
	@PostMapping(value = "/api/auth/recoverpassword", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity recoverPassword(@RequestBody RecoverPasswordForm form) {
		if (form.getEmail() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			        .body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
		}
		try {
			authService.recoverPassword(form.getEmail().trim().toLowerCase());
			return ResponseEntity.ok().build();
		} catch (BusinessException e) {
			return e.getResponseEntity();
		}
	}

	/**
	 * Reset the password with the code send to e-mail
	 *
	 * @param form - form data with user email, new password and the code sent by
	 *             email
	 * @return - Response Entity
	 */
	@PostMapping(value = "/api/auth/passwordreset", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity passwordReset(@RequestBody PasswordResetForm form) {
		if (form.getEmail() == null || form.getPassword() == null || form.getNewPasswordCode() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			        .body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
		}
		try {
			UserDTO user = authService.resetPasswordByCode(form.getEmail().trim().toLowerCase(),
			        form.getPassword().trim(), form.getNewPasswordCode().trim());
			return ResponseEntity.ok(user);
		} catch (BusinessException e) {
			return e.getResponseEntity();
		}
	}

	/**
	 * Register a new user on the database
	 *
	 * @param form - form data with user first name, last name, email and password
	 * @return - Response Entity
	 */
	@CrossOrigin
	@PostMapping(value = "/api/auth/userregistration", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity registerNewUser(@RequestBody UserRegistrationForm form) {
		if (form.getEmail() == null || form.getPassword() == null || form.getFirstName() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			        .body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
		}
		try {
			authService.registerUser(form.getEmail().trim().toLowerCase(), form.getPassword().trim(),
			        form.getFirstName().trim(), form.getLastName() != null ? form.getLastName().trim() : null);
			return ResponseEntity.ok().build();
		} catch (BusinessException e) {
			return e.getResponseEntity();
		}
	}

	/**
	 * activate user on the system
	 *
	 * @param form - form data with user email, and activation code
	 * @return - Response Entity
	 */
	@PostMapping(value = "/api/auth/useractivation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity activateUser(@RequestBody UserActivationForm form) {
		if (form.getEmail() == null || form.getActivationCode() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			        .body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
		}
		try {
			UserDTO user = authService.activateUser(form.getEmail().trim().toLowerCase(),
			        form.getActivationCode().trim());
			return ResponseEntity.ok(user);
		} catch (BusinessException e) {
			return e.getResponseEntity();
		}
	}

	/**
	 * request for user activation on the system
	 *
	 * @param form - form data with user email, and activation code
	 * @return - Response Entity
	 */
	@PostMapping(value = "/api/auth/useractivationrequest", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity requestUserActivation(@RequestBody RequestUserActivationForm form) {
		if (form.getEmail() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			        .body(new ErrorDTO(MessageUtil.getErrorMessageInputValues()));
		}
		try {
			authService.requestUserActivation(form.getEmail().trim().toLowerCase());
			return ResponseEntity.ok().build();
		} catch (BusinessException e) {
			return e.getResponseEntity();
		}
	}

}
