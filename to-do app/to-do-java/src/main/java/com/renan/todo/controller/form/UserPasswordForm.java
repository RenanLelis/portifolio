package com.renan.todo.controller.form;

/**
 * Form for user password update
 */
public class UserPasswordForm {

	private String password;

	public UserPasswordForm() {
		super();
	}

	public UserPasswordForm(String password) {
		super();
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
