package com.renan.todo.controller.form;

/**
 * Form for new user registration
 */
public class UserRegistrationForm {

	private String email;
	private String password;
	private String firstName;
	private String lastName;

	public UserRegistrationForm() {
		super();
	}

	public UserRegistrationForm(String email, String password, String firstName, String lastName) {
		super();
		this.email     = email;
		this.password  = password;
		this.firstName = firstName;
		this.lastName  = lastName;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
