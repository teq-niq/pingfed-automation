package com.example.oidc.principal;

import java.security.Principal;

import com.example.oidc.principal.accesstoken.AccessTokenData;
import com.example.oidc.principal.idtoken.IdTokenData;

public interface OidcPrincipal extends Principal{
	public TokenResponse getTokenResponse(); 
	public IdTokenData getIdTokenData();
	public UserInfo getUserInfo();
	public String getName();
	public String[] getRoles();
	public boolean hasRole(String role);
	
	


	public AccessTokenData getAccessTokenData();
	public String toString();
	String toJsonString();
	String getUserId();
	IntrospectionResponse getIntrospectionResponse();
	int getSettingsIndex();
	

}
