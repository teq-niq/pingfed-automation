package com.example.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JwtAccessTokenUtil {
	
	public static String getTokenDecodedHeader(String accessToken) throws JsonMappingException, JsonProcessingException {
	
		int firstDotPos = accessToken.indexOf('.');
		String header=accessToken.substring(0, firstDotPos);
		String decodedString = decodePart(header);
		
		return decodedString;
	}
public static String getTokenDecodedPayload(String accessToken) throws JsonMappingException, JsonProcessingException {
		
		int firstDotPos = accessToken.indexOf('.');
		int lastDotPos=accessToken.lastIndexOf('.');
		String payload=accessToken.substring(firstDotPos+1, lastDotPos);
		
		String decodedString = decodePart(payload);
		
		return decodedString;
	}
	private static ObjectNode decodeAsObjectNode(String jwtPart) throws JsonProcessingException, JsonMappingException {
		String decodedString = decodePart(jwtPart);
		
		 ObjectNode decodedAsObjectNode = ObjectMapperHolder.mapper.readValue(decodedString, ObjectNode.class);
		return decodedAsObjectNode;
	}
	private static String decodePart(String jwtPart) {
		byte[] decodedBytes = java.util.Base64.getUrlDecoder().decode(jwtPart);
		String decodedString = new String(decodedBytes);
		return decodedString;
	}
	

}
