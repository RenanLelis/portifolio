package com.renan.todo.controller.form;

/**
 * Form for new user activation
 */
public class UserActivationForm {

	private String email;
	private String activationCode;

	public UserActivationForm() {
		super();
	}

	public UserActivationForm(String email, String activationCode) {
		super();
		this.email          = email;
		this.activationCode = activationCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
}
