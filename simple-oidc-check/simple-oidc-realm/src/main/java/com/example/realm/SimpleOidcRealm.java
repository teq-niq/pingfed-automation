package com.example.realm;

import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.catalina.realm.RealmBase;

import com.example.config.CurrentSettings;
import com.example.config.Settings;
import com.example.config.constants.SharedConstants;
import com.example.oidc.principal.impl.OidcPrincipalImpl;
import com.example.util.ObjectMapperHolder;
import com.example.util.TokensProcessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SimpleOidcRealm extends RealmBase{
	private final static Logger logger = Logger.getLogger(SimpleOidcRealm.class.getName());

	private static final Settings[] settingsArr=loadSettings();
	private ObjectMapper objectMapper= ObjectMapperHolder.mapper;
	private static Settings[] loadSettings() {
		logger.log(Level.SEVERE, "Test Severe message In Realm");
		logger.log(Level.INFO, "Test Info message In Realm");
		logger.log(Level.FINE, "Test Fine message In Realm");
		logger.log(Level.FINEST, "Test Finest message In Realm");
		CurrentSettings.index();
		return CurrentSettings.authorizationCodeDefaultSettings;
	}
	@Override
	protected String getPassword(String username) {
		throw new RuntimeException("not used");
		//return null;
	}

	@Override
	protected Principal getPrincipal(String username) {
		
		throw new RuntimeException("not used");
	}
	
	/*
	 * this is what we will leverage
	 */
	@Override
    public Principal authenticate(String username, String credentials) {
		try {
			OidcPrincipalImpl principal = lastLeg(credentials);
			if(principal!=null)
			{
				principal=principal.buildDerived();
			}
			return principal;

			
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Got Problem treated as reason not to authenticate", e);
			return null;
		}
		

        
    }
	
	private OidcPrincipalImpl lastLeg( String string)
			throws Exception {
		
		 ObjectNode readValue = objectMapper.readValue(string, ObjectNode.class);
		 int settingsIndex=readValue.get(SharedConstants.LAST_LEG_SETTINGS_INDEX).asInt();
		 String nonceValueFromCookie=readValue.get(SharedConstants.LAST_LEG_NONCE_KEY).asText();
		 readValue.remove(SharedConstants.LAST_LEG_SETTINGS_INDEX);
		 readValue.remove(SharedConstants.LAST_LEG_NONCE_KEY);
		 //readValue is now in original condition without any mixing
			logger.log(Level.FINE, "readValue="+readValue);
		Settings settings = settingsArr[settingsIndex];
		
		String rawAccessData = objectMapper.writeValueAsString(readValue);
		OidcPrincipalImpl principal = TokensProcessor.fullVerifyAndBuildPrincipal(nonceValueFromCookie, settings, rawAccessData);
		
		
		
		
		return principal;
	}
	
	private String getObjNodeFieldasText(ObjectNode objecNode, String fieldName) {
		String ret=null;
		if(objecNode!=null)
		{
			JsonNode jsonNode = objecNode.get(fieldName);
			if(jsonNode!=null)
			{
				ret=jsonNode.asText();
			}
		}
		
		return ret;
	}
	
	
	
	
	

}
