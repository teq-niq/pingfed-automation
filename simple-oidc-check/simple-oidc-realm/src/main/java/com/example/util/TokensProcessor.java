package com.example.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import com.example.config.Settings;
import com.example.misc.beans.Response;
import com.example.oidc.principal.impl.OidcPrincipalImpl;
import com.fasterxml.jackson.databind.JsonNode;

public class TokensProcessor {
	private final static Logger logger = Logger.getLogger(TokensProcessor.class.getName());
	public static OidcPrincipalImpl fullVerifyAndBuildPrincipal(String nonceValueFromCookie, Settings settings,
			String rawAccessData) {
		OidcPrincipalImpl principal= new OidcPrincipalImpl();
		principal.getTokenResponseImpl().setRaw(rawAccessData);
		
		 String jwksUriEndpoint = settings.getJwksUriEndpoint();
		
		
		String accessToken=principal.getTokenResponse().getAccess_token();
		logger.log(Level.FINE, "accessToken="+accessToken);
		boolean verifiedIdToken=false;
		boolean verifiedAccessToken=false;
		
		if(accessToken!=null)
		{
			principal.getAccessTokenDataImpl().setRaw(accessToken);
			try {
				verifiedAccessToken = new AccessTokenVerifier().verifyAccessTokenUsingJwks(jwksUriEndpoint, accessToken);
			} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
				principal=null;
				logger.log(Level.SEVERE, "problem", e);
			}
		}
		else
		{
			//treat as verified- no we must always have access token
			verifiedAccessToken=false;
		}
		
		

		String idToken = principal.getTokenResponse().getId_token();
		logger.log(Level.FINE, "idToken="+idToken);
		if(idToken!=null)
		{
			principal.getIdTokenDataImpl().setRaw(idToken);
			try {
				verifiedIdToken = new AccessTokenVerifier().verifyAccessTokenUsingJwks(jwksUriEndpoint, idToken);
			} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
				principal=null;
				logger.log(Level.SEVERE, "problem", e);
			}
		}
		else
		{
			//treat as verified
			verifiedIdToken=true;
		}
			
		logger.log(Level.FINE, "verifiedAccessToken="+verifiedAccessToken+",verifiedIdToken="+verifiedIdToken);	
			
			 principal.setSettingsIndex(settings.getIndex());
			
			try {
				
				
				
				if(verifiedAccessToken && verifiedIdToken)
				{

				
					if(idToken!=null)
					{
						if(nonceValueFromCookie!=null)
						{
							boolean nonceMatched = nonceMatched(nonceValueFromCookie, idToken);
							logger.log(Level.FINE, "nonceMatched="+nonceMatched);
							if(!nonceMatched)
							{
								//log also
								principal=null;
							}
							
						}
						
							
						Response userInfoResponse = buildUserInfo(settings, accessToken);
						if(userInfoResponse.getStatusCode()==200)
						{
							principal.getUserInfoImpl().setRaw(userInfoResponse.getResponse());
						}
						else
						{
							//being defensive
							//log also
							principal=null;
						}
							
						
						
						
						
					}
					else
					{
						if(!settings.isLenientNonceOnMissingId())
						{
							//did not get id token
							//user might not have agreed to share id token
							logger.info("Did not get id token. Could not verify nonce. Choosing to stop login based on configured settings.");
							principal=null;
						}
						
					}
					
					
					if(settings.useIntrospection())
					{
						Response introspectionResponse = BasicIntrospection.introspect(settings, accessToken);
						if(introspectionResponse.getStatusCode()==200)
						{
							principal.getIntrospectionResponseImpl().setRaw(introspectionResponse.getResponse());
							if(!principal.getIntrospectionResponse().isActive())
							{
								//log also
								principal=null;
							}
						}
						else
						{
							//being defensive
							//log also
							principal=null;
						}
					}
					
					
					
					
				}
				else
				{
					//log also
					principal=null;
				}
				
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
		return principal;
	}
	

	
	
	private static boolean nonceMatched(String nonceValueFromCookie, String idToken) throws IOException {
		boolean nonceMatched=false;
		String[] idTokenSections = idToken.split("\\.");
		for (int i = 0; i < idTokenSections.length; i++) 
		{
			String idTokenSection = idTokenSections[i];
			if(i!=(idTokenSections.length-1))
			{
				byte[] decode = java.util.Base64.getDecoder().decode(idTokenSection);
				String decoded=new String(decode);
				logger.log(Level.FINE, "i="+i+",decode="+decoded);
				if(i==(idTokenSections.length-2))
				{
					JsonNode node=ObjectMapperHolder.mapper.readTree(decode);
					JsonNode nonceNode = node.get("nonce");
					if(nonceNode!=null)
					{
						String nonceFromIdfToken = nonceNode.asText();
						logger.log(Level.FINE, "nonceFromIdfToken="+nonceFromIdfToken);
						if(nonceFromIdfToken.equals(nonceValueFromCookie))
						{
							nonceMatched=true;
						}
					}
				}
			}
			else
			{
				logger.log(Level.FINE, "i="+i+",idTokenSection="+idTokenSection);
			}
			
			
		}
		return nonceMatched;
	}

	
	

	
	

	
	



	private static Response buildUserInfo(Settings settings, String accessToken) throws Exception, IOException {
		HttpGet get = new HttpGet(settings.getUserinfoEndpoint());
		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		
		 CloseableHttpClient client1 = ClientBuilder.buildClient();
		return  client1.execute(get, userInfoResponse -> {
			 int userinfoResponseStatusCode = userInfoResponse.getCode();
				logger.log(Level.FINE, "userinfoResponseStatusCode="+userinfoResponseStatusCode);
				HttpEntity userInfoResponseEntity = userInfoResponse.getEntity();
				String userInfoAsString = EntityUtils.toString(userInfoResponseEntity);
				logger.log(Level.FINE, "userInfoAsString="+userInfoAsString);
				
				
				
				
			return new Response(userinfoResponseStatusCode, userInfoAsString); 
		 });
		

		
	}

}
