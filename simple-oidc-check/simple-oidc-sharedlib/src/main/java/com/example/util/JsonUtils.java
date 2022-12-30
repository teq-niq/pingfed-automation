package com.example.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonUtils {
	
	public static String getTextFieldFromObjectNode(ObjectNode jwk, String fieldName) {
		String ret=null;
		if(jwk!=null)
		{
			JsonNode val = jwk.get(fieldName);
			if(val!=null)
			{
				ret=val.asText();
			}
		}
		return ret;
	}
	
	public static Long getLongFieldFromObjectNode(ObjectNode jwk, String fieldName) {
		String string = getTextFieldFromObjectNode(jwk, fieldName);
		Long ret =null;
		if(string!=null)
		{
			 ret = Long.valueOf(string);
		}
		return ret;
	}
	
	public static Boolean getBooleanFieldFromObjectNode(ObjectNode jwk, String fieldName) {
		String string = getTextFieldFromObjectNode(jwk, fieldName);
		Boolean ret =null;
		if(string!=null)
		{
			 ret = Boolean.valueOf(string);
		}
		return ret;
	}

}
