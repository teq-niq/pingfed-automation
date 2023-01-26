package com.example.oidc.principal.accesstoken;

public interface AccessTokenPayload {
	public String[] getScopes();
	public String getClient_id();
	public Long getNbf();
	public Long getIat();
	public Long getExp();
	String getUserId();

}
