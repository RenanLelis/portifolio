package com.renan.todo.controller.form;

/**
 * Form for request a user activation
 */
public class RequestUserActivationForm {

	private String email;

	public RequestUserActivationForm() {
		super();
	}

	public RequestUserActivationForm(String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
