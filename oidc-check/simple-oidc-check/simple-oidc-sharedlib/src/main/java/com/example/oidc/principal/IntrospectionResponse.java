package com.example.oidc.principal;

public interface IntrospectionResponse {

	String getRaw();

	String getUserid();

	Long getNbf();

	String[] getScopes();

	boolean isActive();

	String getToken_type();

	Long getExp();

	Long getIat();

	String getClient_id();
	
	

}
