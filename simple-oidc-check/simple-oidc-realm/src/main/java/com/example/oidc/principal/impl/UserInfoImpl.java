package com.example.oidc.principal.impl;


import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.oidc.principal.UserInfo;
import com.example.util.JsonUtils;
import com.example.util.ObjectMapperHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class UserInfoImpl implements UserInfo{
	private final static Logger logger = Logger.getLogger(UserInfoImpl.class.getName());
	private String raw;
	
	private String sub;

	private String given_name;
	private String family_name;
	private String email;
	@Override
	public String getRaw() {
		return raw;
	}
	@Override
	public String getSub() {
		return sub;
	}
	@Override
	public String getGiven_name() {
		return given_name;
	}
	@Override
	public String getFamily_name() {
		return family_name;
	}
	@Override
	public String getEmail() {
		return email;
	}
	public void setRaw(String raw) {
		this.raw = raw;
		try {
			ObjectNode userInfoObjectNode = ObjectMapperHolder.mapper.readValue(raw, ObjectNode.class);
			this.setSub(JsonUtils.getTextFieldFromObjectNode(userInfoObjectNode, "sub"));
			this.setGiven_name(JsonUtils.getTextFieldFromObjectNode(userInfoObjectNode, "given_name"));
			this.setFamily_name(JsonUtils.getTextFieldFromObjectNode(userInfoObjectNode, "family_name"));
			this.setEmail(JsonUtils.getTextFieldFromObjectNode(userInfoObjectNode, "email"));
		} catch (JsonProcessingException e) {
			logger.log(Level.SEVERE, "Unexpected Problem", e);
		}
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	public void setGiven_name(String given_name) {
		this.given_name = given_name;
	}
	public void setFamily_name(String family_name) {
		this.family_name = family_name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "UserInfoImpl [raw=" + raw + ", sub=" + sub + ", given_name=" + given_name + ", family_name="
				+ family_name + ", email=" + email + "]";
	}
	


}
