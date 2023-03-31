package com.renan.todo.controller.form;

/**
 * Form for password reset operations
 */
public class PasswordResetForm {

	private String email;
	private String password;
	private String newPasswordCode;

	public PasswordResetForm() {
		super();
	}

	public PasswordResetForm(String email, String password, String newPasswordCode) {
		super();
		this.email           = email;
		this.password        = password;
		this.newPasswordCode = newPasswordCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPasswordCode() {
		return newPasswordCode;
	}

	public void setNewPasswordCode(String newPasswordCode) {
		this.newPasswordCode = newPasswordCode;
	}

}
