package com.renan.todo.controller.form;

/**
 * Form for password recover operations
 */
public class RecoverPasswordForm {

	private String email;

	public RecoverPasswordForm() {
		super();
	}

	public RecoverPasswordForm(String email) {
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
