package com.example.oidc.principal.impl;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import com.example.oidc.principal.IntrospectionResponse;
import com.example.oidc.principal.OidcPrincipal;
import com.example.oidc.principal.TokenResponse;
import com.example.oidc.principal.UserInfo;
import com.example.oidc.principal.accesstoken.AccessTokenData;
import com.example.oidc.principal.idtoken.IdTokenData;
import com.example.oidc.principal.impl.accesstoken.AccessTokenDataImpl;
import com.example.oidc.principal.impl.idtoken.IdTokenDataImpl;
import com.example.util.ObjectMapperHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.catalina.realm.GenericPrincipal;

public class OidcPrincipalImpl extends GenericPrincipal implements OidcPrincipal, Principal {
	

	public OidcPrincipalImpl(String name, TokenResponse tokenResponse, AccessTokenData accessTokenData,
			IdTokenData idTokenData, UserInfo userInfo, IntrospectionResponse introspectionResponse, 
			String userId, String[] roles, int settingsIndex) {
		super(name, Arrays.asList(roles));
		this.tokenResponse = tokenResponse;
		this.accessTokenData = accessTokenData;
		this.idTokenData = idTokenData;
		this.userInfo = userInfo;
		this.introspectionResponse = introspectionResponse;
		this.name = name;
		this.userId = userId;
		this.roles = roles;
		this.settingsIndex = settingsIndex;
	}
	
	public OidcPrincipalImpl()
	{
		super(null, null);
		tokenResponse=new TokenResponseImpl();
		accessTokenData= new AccessTokenDataImpl();
		idTokenData=new IdTokenDataImpl();
		userInfo= new UserInfoImpl();
		introspectionResponse = new IntrospectionResponseImpl();
	}

	
	@Override
	public TokenResponse getTokenResponse() {
		return tokenResponse;
	}
	@Override
	public IdTokenData getIdTokenData() {
		return idTokenData;
	}
	@Override
	public UserInfo getUserInfo() {
		return userInfo;
	}

	public TokenResponseImpl getTokenResponseImpl() {
		return (TokenResponseImpl) tokenResponse;
	}
	public IdTokenDataImpl getIdTokenDataImpl() {
		return (IdTokenDataImpl) idTokenData;
	}
	public UserInfoImpl getUserInfoImpl() {
		return (UserInfoImpl) userInfo;
	}
	private TokenResponse tokenResponse;
	
	private AccessTokenData accessTokenData;
	private IdTokenData idTokenData;
	private UserInfo userInfo;
	private IntrospectionResponse introspectionResponse;
	private String name;
	private String userId;
	private String[] roles;
	private int settingsIndex;
	
	
	
	
	
	

	@Override
	public String getName() {
		
		return this.name;
	}

	@Override
	public String[] getRoles() {
		
		return this.roles;
	}
	

	@Override
	public boolean hasRole(String role) {
		 if ("*".equals(role)) { // Special 2.4 role meaning everyone
	            return true;
	        }
		boolean hasRole=false;
		for (String checkWith : roles) {
			if(role.equals(checkWith))
			{
				hasRole=true;
				break;
			}
		}
		return hasRole;
	}
	public void setTokenResponse(TokenResponse acessTokenData) {
		this.tokenResponse = acessTokenData;
	}
	public void setIdTokenData(IdTokenData idTokenData) {
		this.idTokenData = idTokenData;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	
	/*
	 * Customise
	 */
	public OidcPrincipalImpl buildDerived() {
		if(this.userInfo.getGiven_name()!=null && this.userInfo.getGiven_name().length()>0)
		{
			this.name=this.userInfo.getGiven_name();
		}
		else if(this.idTokenData.getPayload().getGiven_name()!=null &&this.idTokenData.getPayload().getGiven_name().length()>0)
		{
			this.name=this.idTokenData.getPayload().getGiven_name();
		}
	
		this.roles=this.accessTokenData.getPayload().getScopes();
		
		this.userId=this.accessTokenData.getPayload().getUserId();
		if(this.userId==null)
		{
			this.userId=this.getIntrospectionResponse().getUserid();
		}
		return new  OidcPrincipalImpl(this.name, this.tokenResponse, this.accessTokenData,
				this.idTokenData, this.userInfo, this.introspectionResponse, 
				this.userId, this.roles, this.settingsIndex);
		
	}
	@Override
	public AccessTokenData getAccessTokenData() {
		return accessTokenData;
	}
	public AccessTokenDataImpl getAccessTokenDataImpl() {
		return (AccessTokenDataImpl) accessTokenData;
	}
	public void setAccessTokenData(AccessTokenData accessTokenData) {
		this.accessTokenData = accessTokenData;
	}
	
	@Override
	public String toJsonString()
	{
		try {
			return ObjectMapperHolder.mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public String getUserId() {
		return userId;
	}
	@Override
	public IntrospectionResponse getIntrospectionResponse() {
		return introspectionResponse;
	}
	public IntrospectionResponseImpl getIntrospectionResponseImpl() {
		return (IntrospectionResponseImpl) introspectionResponse;
	}
	@Override
	public int getSettingsIndex() {
		return settingsIndex;
	}
	public void setSettingsIndex(int settingsIndex) {
		this.settingsIndex = settingsIndex;
	}

	@Override
	public String toString() {
		return "OidcPrincipal [tokenResponse=" + tokenResponse + ", accessTokenData=" + accessTokenData
				+ ", idTokenData=" + idTokenData + ", userInfo=" + userInfo + ", introspectionResponse="
				+ introspectionResponse + ", name=" + name + ", userId=" + userId + ", roles=" + Arrays.toString(roles)
				+ ", settingsIndex=" + settingsIndex + "]";
	}
	
	

}
