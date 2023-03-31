package com.renan.todo.controller.form;

/**
 * Form for user profile update
 */
public class UserProfileForm {

	private String name;
	private String lastName;

	public UserProfileForm() {
		super();
	}

	public UserProfileForm(String name, String lastName) {
		super();
		this.name     = name;
		this.lastName = lastName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
