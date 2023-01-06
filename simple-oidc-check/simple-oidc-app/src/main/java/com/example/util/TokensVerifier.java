package com.example.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.config.CurrentSettings;
import com.example.config.Settings;
import com.example.misc.beans.Response;
import com.example.oidc.principal.OidcPrincipal;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TokensVerifier {
	private final static Logger logger = Logger.getLogger(TokensVerifier.class.getName());
	private static final Settings[] settingsArr=CurrentSettings.authorizationCodeDefaultSettings;
	//verifyAccessTokenUsingJwks
	//no need for nonce matchin
	//call userintrspection
	 public boolean fullVerify(OidcPrincipal oidcPrincipal)
	 {
		 boolean ret=false;
		 try {
			boolean verified = new AccessTokenVerifier().verifyAccessTokenUsingJwks(oidcPrincipal);
			if(verified)
			{
				Settings settings = settingsArr[oidcPrincipal.getSettingsIndex()];
				if(settings.useIntrospection())
				{
					Response introspectionResponse = BasicIntrospection.introspect(settings, oidcPrincipal.getAccessTokenData().getRaw());
					if(introspectionResponse.getStatusCode()==200)
					{
						
						ObjectNode introspectionResponseObj = ObjectMapperHolder.mapper.readValue(introspectionResponse.getResponse(), ObjectNode.class);
						Boolean active = JsonUtils.getBooleanFieldFromObjectNode(introspectionResponseObj, "active");
						ret=active;
					}
				}
				else
				{
					ret=true;
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Problem happened so we treat as unverified", e);
		}
		return ret;
		 
	 }

}
