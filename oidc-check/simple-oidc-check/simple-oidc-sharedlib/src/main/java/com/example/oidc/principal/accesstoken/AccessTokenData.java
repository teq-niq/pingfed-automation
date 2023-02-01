package com.example.oidc.principal.accesstoken;

public interface AccessTokenData {
	public String getRaw() ;

	AccessTokenHeader getHeader();

	AccessTokenPayload getPayload();

	String getDecodedHeader();

	String getDecodedPayload();
}
