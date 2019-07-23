package com.eg.model;

public class AuthToken {

    private String token;
    private String username;
    private String role;

    public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public AuthToken(){

    }

    public AuthToken(String token, String username, String role){
        this.token = token;
        this.username = username;
        this.role = role;
    }

    public AuthToken(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
