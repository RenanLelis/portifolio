package com.renan.todo.dto;

/**
 * User data to return as login operations
 */
public class UserDTO {

	private String  email;
	private String  jwt;
	private Integer id;
	private Integer status;
	private Integer expiresIn;
	private String  name;
	private String  lastName;

	public UserDTO() {
		super();
	}

	public UserDTO(String email, String jwt, Integer id, Integer status, Integer expiresIn, String name,
	        String lastName) {
		super();
		this.email     = email;
		this.jwt       = jwt;
		this.id        = id;
		this.status    = status;
		this.expiresIn = expiresIn;
		this.name      = name;
		this.lastName  = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
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
