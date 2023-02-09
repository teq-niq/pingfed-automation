package com.example;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse.Builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthorizationCodeTokenResponseClientForMyPingFed implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>{
	@Autowired
	ObjectMapper objectMapper;
	private DefaultAuthorizationCodeTokenResponseClient delegate= new DefaultAuthorizationCodeTokenResponseClient();
	@Override
	public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
		OAuth2AccessTokenResponse tokenResponse = delegate.getTokenResponse(authorizationGrantRequest);
		OAuth2AccessTokenResponse ret=tokenResponse;
		OAuth2AccessToken accessToken = tokenResponse.getAccessToken();
		String tokenValue = accessToken.getTokenValue();
		String[] split = tokenValue.split(Pattern.quote("."));
		if(split.length==3)
		{
			String payLoad=split[1];
			byte[] decodedBytes = java.util.Base64.getUrlDecoder().decode(payLoad);
			String decodedString = new String(decodedBytes);
			Map<String, Object> readValue;
			try {
				readValue = objectMapper.readValue(decodedString, Map.class);
				if(readValue!=null)
				{
					String scope = (String) readValue.get("scope");
					if(scope!=null)
					{
						String[] scopeAsArray = scope.split(" ");
					
						Set<String> scopesAsSet= new HashSet<>();
						for (String string : scopeAsArray) {
							scopesAsSet.add(string);
						}
						
						ret = buildAgain(tokenResponse, scopesAsSet);
					}		
				}
			

			} catch (JsonProcessingException e) {
				throw new IllegalArgumentException("Unprocessable Json", e);
			}
			
		}
		else if(split.length==5)
		{
			throw new IllegalArgumentException("Not attempted for JWE");
		}
		else
		{
			throw new IllegalArgumentException("Unexpected");
		}
		
		return ret;
	}
	private OAuth2AccessTokenResponse buildAgain(OAuth2AccessTokenResponse tokenResponse,
			Set<String> scopesAsSet) {
		Builder builder =null;
		OAuth2AccessTokenResponse ret=tokenResponse;
		OAuth2AccessToken accessToken = tokenResponse.getAccessToken();
		Map<String, Object> additionalParameters = tokenResponse.getAdditionalParameters();
		String tokenValue =null;
		String refreshTokenValue=null;
		TokenType tokenType=null;
		if(accessToken!=null)
		{
			tokenValue = accessToken.getTokenValue();
			tokenType = accessToken.getTokenType();
		}
		OAuth2RefreshToken refreshToken = tokenResponse.getRefreshToken();
		if(refreshToken!=null)
		{
			refreshTokenValue = refreshToken.getTokenValue();
		}
		if(tokenValue!=null)
		{
			//we must have a token
			builder = OAuth2AccessTokenResponse.withToken(tokenValue);
			if(refreshTokenValue!=null)
			{
				builder=builder.refreshToken(refreshTokenValue);
			}
			if(tokenType!=null)
			{
				builder=builder.tokenType(tokenType);		
			}
			if(scopesAsSet!=null)
			{
				builder=builder.scopes(scopesAsSet);
			}
			if(additionalParameters!=null)
			{
				builder=builder.additionalParameters(additionalParameters);
			}
		
			
			
		}
		if(builder!=null)
		{
			ret=builder.build();
		}
		
		return ret;		
				
	}

}
