package com.example.oidc.principal.idtoken;

public interface IdTokenData {

	public String getRaw();

	IdTokenHeader getHeader();

	IdTokenPayload getPayload();

	String getDecodedHeader();

	String getDecodedPayload(); 
	

}
