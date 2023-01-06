package com.example.oidc.principal;

public interface TokenResponse {
	public String getAccess_token() ;
	public String getId_token();
	public String getToken_type();
	public long getExpires_in();
	public String getRaw();
	String[] getScopes();
	
	
}
