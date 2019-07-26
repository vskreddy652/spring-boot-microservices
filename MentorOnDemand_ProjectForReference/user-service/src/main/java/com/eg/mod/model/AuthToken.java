package com.eg.mod.model;

public class AuthToken {

	private Long id;
	private String userName;
	private String token;
	private String role;

	public AuthToken(Long id, String userName, String role, String token) {
		this.id = id;
		this.userName = userName;
		this.role = role;
		this.token = token;
	}

	public AuthToken(String token) {
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
