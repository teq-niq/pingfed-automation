package com.example.oidc.principal.impl.idtoken;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.oidc.principal.idtoken.IdTokenData;
import com.example.oidc.principal.idtoken.IdTokenHeader;
import com.example.oidc.principal.idtoken.IdTokenPayload;
import com.example.util.JsonUtils;
import com.example.util.JwtAccessTokenUtil;
import com.example.util.ObjectMapperHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class IdTokenDataImpl implements IdTokenData{
	private final static Logger logger = Logger.getLogger(IdTokenDataImpl.class.getName());
	
	private IdTokenHeader header;
	private IdTokenPayload payload;
	private String raw;
	private String decodedHeader;
	private String decodedPayload;
	
	public IdTokenDataImpl() {
		header= new IdTokenHeaderImpl();
		payload= new IdTokenPayloadImpl();
	}

	@Override
	public String getRaw() {
		return raw;
	}
	
	public void setRaw(String raw) {
		this.raw = raw;
		IdTokenHeaderImpl headerImpl = this.getHeaderImpl();
		IdTokenPayloadImpl payloadImpl = this.getPayloadImpl();
		try {
			this.decodedHeader = JwtAccessTokenUtil.getTokenDecodedHeader(raw);
			ObjectNode decodedHedaerAsObjectNode = ObjectMapperHolder.mapper.readValue(this.decodedHeader, ObjectNode.class);
			headerImpl.setKid(JsonUtils.getTextFieldFromObjectNode(decodedHedaerAsObjectNode, "kid"));
			headerImpl.setAlg(JsonUtils.getTextFieldFromObjectNode(decodedHedaerAsObjectNode, "alg"));
			this.decodedPayload = JwtAccessTokenUtil.getTokenDecodedPayload(raw);
			ObjectNode decodedPayloadAsObjectNode = ObjectMapperHolder.mapper.readValue(this.decodedPayload, ObjectNode.class);
			payloadImpl.setSub(JsonUtils.getTextFieldFromObjectNode(decodedPayloadAsObjectNode, "sub"));
			payloadImpl.setAud(JsonUtils.getTextFieldFromObjectNode(decodedPayloadAsObjectNode, "aud"));
			payloadImpl.setJti(JsonUtils.getTextFieldFromObjectNode(decodedPayloadAsObjectNode, "jti"));
			payloadImpl.setIss(JsonUtils.getTextFieldFromObjectNode(decodedPayloadAsObjectNode, "iss"));
			payloadImpl.setIat(JsonUtils.getLongFieldFromObjectNode(decodedPayloadAsObjectNode, "iat"));
			payloadImpl.setExp(JsonUtils.getLongFieldFromObjectNode(decodedPayloadAsObjectNode, "exp"));
			payloadImpl.setAuth_time(JsonUtils.getLongFieldFromObjectNode(decodedPayloadAsObjectNode, "auth_time"));
			payloadImpl.setGiven_name(JsonUtils.getTextFieldFromObjectNode(decodedPayloadAsObjectNode, "given_name"));
			payloadImpl.setFamily_name(JsonUtils.getTextFieldFromObjectNode(decodedPayloadAsObjectNode, "family_name"));
			payloadImpl.setEmail(JsonUtils.getTextFieldFromObjectNode(decodedPayloadAsObjectNode, "email"));
			payloadImpl.setNonce(JsonUtils.getTextFieldFromObjectNode(decodedPayloadAsObjectNode, "nonce"));
		} catch (JsonProcessingException e) {
			logger.log(Level.SEVERE, "Unexpected Problem", e);
		}
		
	}
	@Override
	public IdTokenHeader getHeader() {
		return header;
	}
	@Override
	public IdTokenPayload getPayload() {
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
	
	public IdTokenHeaderImpl getHeaderImpl() {
		return (IdTokenHeaderImpl) header;
	}
	
	public IdTokenPayloadImpl getPayloadImpl() {
		return (IdTokenPayloadImpl) payload;
	}

	@Override
	public String toString() {
		return "IdTokenDataImpl [header=" + header + ", payload=" + payload + ", raw=" + raw + ", decodedHeader="
				+ decodedHeader + ", decodedPayload=" + decodedPayload + "]";
	}

	

}
