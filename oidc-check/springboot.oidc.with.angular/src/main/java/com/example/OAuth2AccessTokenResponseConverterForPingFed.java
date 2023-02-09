package com.example;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.core.endpoint.DefaultMapOAuth2AccessTokenResponseConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse.Builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OAuth2AccessTokenResponseConverterForPingFed implements Converter<Map<String, Object>, OAuth2AccessTokenResponse> {
	ObjectMapper objectMapper= new ObjectMapper();
	private Converter<Map<String, Object>, OAuth2AccessTokenResponse> delegate = new DefaultMapOAuth2AccessTokenResponseConverter();
	@Override
	public OAuth2AccessTokenResponse convert(Map<String, Object> source) {
		
		OAuth2AccessTokenResponse convert = delegate.convert(source);
		
		
		return buildAgain(convert, source);
		
		
	}
	
	
		
	private OAuth2AccessTokenResponse buildAgain(OAuth2AccessTokenResponse tokenResponse,
			Map<String, Object> source) {
		Integer expiresIn = (Integer) source.get("expires_in");
		Builder builder =null;
		OAuth2AccessTokenResponse ret=tokenResponse;
		OAuth2AccessToken accessToken = tokenResponse.getAccessToken();
		Map<String, Object> additionalParameters = tokenResponse.getAdditionalParameters();
		String tokenValue =null;
		String refreshTokenValue=null;
		TokenType tokenType=null;
		Set<String> scopesAsSet=null;
		if(accessToken!=null)
		{
			tokenValue = accessToken.getTokenValue();
			scopesAsSet = getScopes(accessToken);
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
			if(expiresIn!=null)
			{
				builder=builder.expiresIn(expiresIn);
			}

			if(refreshTokenValue!=null)
			{
				builder=builder.refreshToken(refreshTokenValue);
			}
			if(tokenType!=null)
			{
				builder=builder.tokenType(tokenType);		
			}
			if(scopesAsSet!=null && scopesAsSet.size()!=0)
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
	Set<String> getScopes(OAuth2AccessToken accessToken )
	{
		Set<String> scopesAsSet= new HashSet<>();
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
				} catch (JsonProcessingException e) {
					throw new IllegalArgumentException("Unprocessable Json", e);
				}
				if(readValue!=null)
				{
					String scope = (String) readValue.get("scope");
					if(scope!=null)
					{
						String[] scopeAsArray = scope.split(" ");
					
						
						for (String string : scopeAsArray) {
							scopesAsSet.add(string);
						}
						
					}
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
		return scopesAsSet;
						
	}


}
