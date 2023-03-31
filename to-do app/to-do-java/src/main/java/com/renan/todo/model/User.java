package com.renan.todo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * User Entity class
 */
@Entity
@Table(name = "USER_TODO")
public class User {

	public static final Integer STATUS_ACTIVE   = 1;
	public static final Integer STATUS_INACTIVE = 0;

	public static final Integer LENGTH_ACTIVATION_CODE   = 6;
	public static final Integer LENGTH_NEW_PASSWORD_CODE = 6;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	@Column(name = "FIRST_NAME", nullable = false)
	private String  firstName;
	@Column(name = "LAST_NAME", nullable = true)
	private String  lastName;
	@Column(name = "EMAIL", nullable = false, unique = true)
	private String  email;
	@Column(name = "USER_PASSWORD", nullable = false)
	private String  password;
	@Column(name = "ACTIVATION_CODE", nullable = true)
	private String  activationCode;
	@Column(name = "NEW_PASSWORD_CODE", nullable = true)
	private String  newPasswordCode;
	@Column(name = "USER_STATUS", nullable = false)
	private Integer userStatus;

	public User() {
		super();
	}

	public User(Integer id, String firstName, String lastName, String email, String password, String activationCode,
	        String newPasswordCode, Integer userStatus) {
		super();
		this.id              = id;
		this.firstName       = firstName;
		this.lastName        = lastName;
		this.email           = email;
		this.password        = password;
		this.activationCode  = activationCode;
		this.newPasswordCode = newPasswordCode;
		this.userStatus      = userStatus;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	public String getNewPasswordCode() {
		return newPasswordCode;
	}

	public void setNewPasswordCode(String newPasswordCode) {
		this.newPasswordCode = newPasswordCode;
	}

	public Integer getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

}
