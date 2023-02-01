package com.example.oidc.principal.impl.idtoken;

import com.example.oidc.principal.idtoken.IdTokenHeader;

public class IdTokenHeaderImpl implements IdTokenHeader{
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
		return "IdTokenHeader [alg=" + alg + ", kid=" + kid + "]";
	}

}
