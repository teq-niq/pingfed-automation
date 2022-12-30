package com.example.oidc.principal.impl.accesstoken;

import java.util.Arrays;

import com.example.oidc.principal.accesstoken.AccessTokenPayload;

public class AccessTokenPayloadImpl implements AccessTokenPayload{
	private String[] scopes;
	private String client_id;
	private String userId;
	private Long nbf;
	private Long iat;
	private Long exp;
	@Override
	public String[] getScopes() {
		return scopes;
	}
	public void setScopes(String scope) {
		if(scope!=null)
		{
			this.scopes = scope.split(" ");
		}
		
	}
	@Override
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	@Override
	public Long getNbf() {
		return nbf;
	}
	public void setNbf(Long nbf) {
		this.nbf = nbf;
	}
	@Override
	public Long getIat() {
		return iat;
	}
	public void setIat(Long iat) {
		this.iat = iat;
	}
	@Override
	public Long getExp() {
		return exp;
	}
	public void setExp(Long exp) {
		this.exp = exp;
	}
	@Override
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "AccessTokenPayload [scopes=" + Arrays.toString(scopes) + ", client_id=" + client_id + ", userId="
				+ userId + ", nbf=" + nbf + ", iat=" + iat + ", exp=" + exp + "]";
	}
}
