package com.example.oidc.principal.impl.accesstoken;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.oidc.principal.accesstoken.AccessTokenData;
import com.example.oidc.principal.accesstoken.AccessTokenHeader;
import com.example.oidc.principal.accesstoken.AccessTokenPayload;
import com.example.util.JsonUtils;
import com.example.util.JwtAccessTokenUtil;
import com.example.util.ObjectMapperHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AccessTokenDataImpl implements  AccessTokenData{
	private final static Logger logger = Logger.getLogger(AccessTokenDataImpl.class.getName());
	private AccessTokenHeader header;
	private AccessTokenPayload payload;
	public AccessTokenDataImpl()
	{
		header= new AccessTokenHeaderImpl();
		payload= new AccessTokenPayloadImpl();
	}
	private String raw;
	private String decodedHeader;
	private String decodedPayload;
	
	
	@Override
	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;

		
		try {
			this.decodedHeader = JwtAccessTokenUtil.getTokenDecodedHeader(raw);
			AccessTokenHeaderImpl headerImpl = this.getHeaderImpl();
			AccessTokenPayloadImpl payloadImpl = this.getPayloadImpl();
			ObjectNode decodedHedaerAsObjectNode = ObjectMapperHolder.mapper.readValue(this.decodedHeader, ObjectNode.class);
			headerImpl.setKid(JsonUtils.getTextFieldFromObjectNode(decodedHedaerAsObjectNode, "kid"));
			headerImpl.setAlg(JsonUtils.getTextFieldFromObjectNode(decodedHedaerAsObjectNode, "alg"));
			this.decodedPayload = JwtAccessTokenUtil.getTokenDecodedPayload(raw);
			ObjectNode decodedPayloadAsObjectNode = ObjectMapperHolder.mapper.readValue(this.decodedPayload, ObjectNode.class);
			payloadImpl.setScopes(JsonUtils.getTextFieldFromObjectNode(decodedPayloadAsObjectNode, "scope"));
			payloadImpl.setClient_id(JsonUtils.getTextFieldFromObjectNode(decodedPayloadAsObjectNode, "client_id"));
			payloadImpl.setNbf(JsonUtils.getLongFieldFromObjectNode(decodedPayloadAsObjectNode, "nbf"));
			payloadImpl.setIat(JsonUtils.getLongFieldFromObjectNode(decodedPayloadAsObjectNode, "iat"));
			payloadImpl.setExp(JsonUtils.getLongFieldFromObjectNode(decodedPayloadAsObjectNode, "exp"));
			
		} catch (JsonProcessingException e) {
			logger.log(Level.SEVERE, "Unexpected Problem", e);
		}
		
		 
	}
	@Override
	public AccessTokenHeader getHeader() {
		return header;
	}
	@Override
	public AccessTokenPayload getPayload() {
		return payload;
	}
	@Override
	public String getDecodedHeader() {
		return decodedHeader;
	}
	@Override
	public String getDecodedPayload() {
		return decodedPayload;
	}
	
	public AccessTokenHeaderImpl getHeaderImpl() {
		return (AccessTokenHeaderImpl) header;
	}

	public AccessTokenPayloadImpl getPayloadImpl() {
		return (AccessTokenPayloadImpl) payload;
	}

	@Override
	public String toString() {
		return "AccessTokenData [header=" + header + ", payload=" + payload + ", raw=" + raw + ", decodedHeader="
				+ decodedHeader + ", decodedPayload=" + decodedPayload + "]";
	}
	
	
	
	
	
	
	
}
