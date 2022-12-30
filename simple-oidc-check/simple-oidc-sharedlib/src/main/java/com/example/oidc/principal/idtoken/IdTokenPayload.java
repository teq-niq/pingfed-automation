package com.example.oidc.principal.idtoken;

public interface IdTokenPayload {

	String getSub();

	String getAud();

	String getJti();

	String getIss();

	Long getIat();

	Long getExp();

	Long getAuth_time();

	String getGiven_name();

	String getFamily_name();

	String getEmail();

	String getNonce();

}
