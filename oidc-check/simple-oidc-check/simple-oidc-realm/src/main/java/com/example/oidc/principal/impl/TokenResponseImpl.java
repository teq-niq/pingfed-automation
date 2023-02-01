package com.example.oidc.principal.impl;

import com.example.oidc.principal.TokenResponse;
import com.example.util.JsonUtils;
import com.example.util.ObjectMapperHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TokenResponseImpl implements TokenResponse{;
	private static ObjectMapper mapper= ObjectMapperHolder.mapper;
	private String access_token;
	private String id_token;
	private String token_type;
	private long expires_in;
	private String[] scopes;
	@Override
	public String[] getScopes() {
		return scopes;
	}
	private String raw;
	public void setScopes(String scopes) {
		if(scopes!=null)
		{
			this.scopes = scopes.split(" ");
		}
		else
		{
			this.scopes=null;
		}
		
	}
	@Override
	public String getAccess_token() {
		return access_token;
	}
	@Override
	public String getToken_type() {
		return token_type;
	}
	@Override
	public long getExpires_in() {
		return expires_in;
	}
	@Override
	public String getRaw() {
		return raw;
	}
	
	
	public void setRaw(String raw) {
		this.raw = raw;
		try {
			ObjectNode node = mapper.readValue(raw, ObjectNode.class);
			this.token_type=JsonUtils.getTextFieldFromObjectNode(node, "token_type");
			this.access_token=JsonUtils.getTextFieldFromObjectNode(node, "access_token");
			this.id_token=JsonUtils.getTextFieldFromObjectNode(node, "id_token");
			Long expiresIn = JsonUtils.getLongFieldFromObjectNode(node, "expires_in");
			if(expiresIn!=null)
			{
				this.expires_in=expiresIn;
			}
			this.setScopes(JsonUtils.getTextFieldFromObjectNode(node, "scope"));
		} catch (JsonProcessingException e) {
			//log warning
		}
		
	}
	
	
	@Override
	public String getId_token() {
		return id_token;
	}
	@Override
	public String toString() {
		return "TokenResponseImpl [access_token=" + access_token + ", id_token=" + id_token + ", token_type="
				+ token_type + ", expires_in=" + expires_in + ", raw=" + raw + "]";
	}
	
}
