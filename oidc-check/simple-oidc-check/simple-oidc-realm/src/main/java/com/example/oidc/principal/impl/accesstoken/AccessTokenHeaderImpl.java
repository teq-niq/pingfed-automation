package com.example.oidc.principal.impl.accesstoken;

import com.example.oidc.principal.accesstoken.AccessTokenHeader;

public class AccessTokenHeaderImpl implements  AccessTokenHeader{
	private String alg;
	private String kid;
	@Override
	public String getAlg() {
		return alg;
	}
	public void setAlg(String alg) {
		this.alg = alg;
	}
	@Override
	public String getKid() {
		return kid;
	}
	public void setKid(String kid) {
		this.kid = kid;
	}
	@Override
	public String toString() {
		return "AccessTokenHeader [alg=" + alg + ", kid=" + kid + "]";
	}
	

}
