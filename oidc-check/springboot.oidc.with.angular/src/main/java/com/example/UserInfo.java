package com.example;

public class UserInfo {
	
	private boolean authenticateStatus;
	
	private String username;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean isAuthenticateStatus() {
		return authenticateStatus;
	}
	public void setAuthenticateStatus(boolean authenticateStatus) {
		this.authenticateStatus = authenticateStatus;
	}

}
