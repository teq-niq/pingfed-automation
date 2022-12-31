package com.example.oidc.principal.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.config.AutomationSharedConstants;
import com.example.oidc.principal.IntrospectionResponse;

import com.example.util.JsonUtils;
import com.example.util.ObjectMapperHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class IntrospectionResponseImpl implements IntrospectionResponse{
	private final static Logger logger = Logger.getLogger(IntrospectionResponseImpl.class.getName());
	private String raw;
	private String userid;
	private Long nbf;
	private String[] scopes;
	private boolean active;
	private String token_type;
	private Long exp;
	private Long iat;
	private String client_id;
	@Override
	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
		try {
			ObjectNode responseObjectNode = ObjectMapperHolder.mapper.readValue(raw, ObjectNode.class);
			this.userid=JsonUtils.getTextFieldFromObjectNode(responseObjectNode, AutomationSharedConstants.AtmOauth_PersistentGrantUserKeyAttrName);
			this.nbf=JsonUtils.getLongFieldFromObjectNode(responseObjectNode, "nbf");
			this.setScopes(JsonUtils.getTextFieldFromObjectNode(responseObjectNode, "scope"));
			this.active=JsonUtils.getBooleanFieldFromObjectNode(responseObjectNode, "active");
			this.token_type=JsonUtils.getTextFieldFromObjectNode(responseObjectNode, "token_type");
			this.exp=JsonUtils.getLongFieldFromObjectNode(responseObjectNode, "exp");
			this.iat=JsonUtils.getLongFieldFromObjectNode(responseObjectNode, "iat");
			this.client_id=JsonUtils.getTextFieldFromObjectNode(responseObjectNode, "client_id");
		} catch (JsonProcessingException e) {
			logger.log(Level.SEVERE, "Unexpected Problem", e);
		}
	}

	public void setScopes(String scopes) {
		if(scopes!=null)
		{
			this.scopes = scopes.split(" ");
		}
		
	}
	@Override
	public String getUserid() {
		return userid;
	}
	@Override
	public Long getNbf() {
		return nbf;
	}
	@Override
	public String[] getScopes() {
		return scopes;
	}
	@Override
	public boolean isActive() {
		return active;
	}
	@Override
	public String getToken_type() {
		return token_type;
	}
	@Override
	public Long getExp() {
		return exp;
	}
	@Override
	public Long getIat() {
		return iat;
	}
	@Override
	public String getClient_id() {
		return client_id;
	}
}
