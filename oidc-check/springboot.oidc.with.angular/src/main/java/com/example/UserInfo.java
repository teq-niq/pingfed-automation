package com.example;

import java.util.HashSet;
import java.util.Set;

public class UserInfo {
	
	private boolean authenticateStatus;
	
	private String username;
	private Set<String> authorities= new HashSet<>();
	public Set<String> getAuthorities() {
		return authorities;
	}
	void add(String authority)
	{
		this.authorities.add(authority);
	}
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
