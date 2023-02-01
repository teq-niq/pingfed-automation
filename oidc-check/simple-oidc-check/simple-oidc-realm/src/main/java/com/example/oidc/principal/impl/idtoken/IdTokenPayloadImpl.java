package com.example.oidc.principal.impl.idtoken;

import com.example.oidc.principal.idtoken.IdTokenPayload;

public class IdTokenPayloadImpl implements IdTokenPayload{
	private String sub;
	private String aud;
	private String jti;
	private String iss;
	private Long iat;
	private Long exp;
	private Long auth_time;
	private String given_name;
	private String family_name;
	private String email;
	private String nonce;
	@Override
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	@Override
	public String getAud() {
		return aud;
	}
	public void setAud(String aud) {
		this.aud = aud;
	}
	@Override
	public String getJti() {
		return jti;
	}
	public void setJti(String jti) {
		this.jti = jti;
	}
	@Override
	public String getIss() {
		return iss;
	}
	public void setIss(String iss) {
		this.iss = iss;
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
	public Long getAuth_time() {
		return auth_time;
	}
	public void setAuth_time(Long auth_time) {
		this.auth_time = auth_time;
	}
	@Override
	public String getGiven_name() {
		return given_name;
	}
	public void setGiven_name(String given_name) {
		this.given_name = given_name;
	}
	@Override
	public String getFamily_name() {
		return family_name;
	}
	public void setFamily_name(String family_name) {
		this.family_name = family_name;
	}
	@Override
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	@Override
	public String toString() {
		return "IdTokenPayload [sub=" + sub + ", aud=" + aud + ", jti=" + jti + ", iss=" + iss + ", iat=" + iat
				+ ", exp=" + exp + ", auth_time=" + auth_time + ", given_name=" + given_name + ", family_name="
				+ family_name + ", email=" + email + ", nonce=" + nonce + "]";
	}
	
	
}
