package com.example.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.StatusLine;

import com.example.util.ClientBuilder;
import com.example.util.ObjectMapperHolder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;

public class Settings {
	private final static Logger logger = Logger.getLogger(Settings.class.getName());
	private final String clientId;
	private final String clientSecret;
	private final String loginHint;
	private  String atmId;
	private final String tokenEndpoint;
	private final String userinfoEndpoint;
	private final String authorizationEndpoint;
	private final String jwksUriEndpoint;
	private final String introspectionEndpoint;
	private boolean introspect=false;
	private boolean lenientNonceOnMissingId=false;
	private boolean opaqueAccessToken=false;
	
	
	public boolean isOpaqueAccessToken() {
		return opaqueAccessToken;
	}

	public boolean isLenientNonceOnMissingId() {
		return lenientNonceOnMissingId;
	}
	private int index;
	public Settings(String wellKnown,       String clientId, String clientSecret)  {
		this(wellKnown, clientId, clientSecret, null);
		
	}
	
	public Settings(String wellKnown, String clientId, String clientSecret, String loginHint) {
		super();
		this.wellKnown = wellKnown;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.loginHint=loginHint;
		this.endPoints= new HashMap<>();
		try {
			setup();
			this.setupException=null;
			
		} catch (Exception e) {
			this.setupException=e;
			
		}
		this.tokenEndpoint=this.endPoints.get("token_endpoint");
		this.userinfoEndpoint=this.endPoints.get("userinfo_endpoint");
		this.authorizationEndpoint=this.endPoints.get("authorization_endpoint");
		this.jwksUriEndpoint=endPoints.get("jwks_uri");
		this.introspectionEndpoint=endPoints.get("introspection_endpoint");
		
	}
	private void setup() throws Exception
	{
		HttpGet request = new HttpGet(this.wellKnown);
		CloseableHttpClient client = ClientBuilder.buildClient();
		//HttpClient client = HttpClientBuilder.create().build();
		//HttpResponse response1 = client.execute(request);
		
		Object responseData = client.execute(request, response -> {
			  int statusCode = response.getCode();
			  if (response.getCode() >= 300) {
			      throw new ClientProtocolException(new StatusLine(response).toString());
			  }
			  final HttpEntity responseEntity = response.getEntity();
			  if (responseEntity == null) {
			      return null;
			  }
			 //EntityUtils.
//			  try (InputStream inputStream = responseEntity.getContent()) {
//			      return null;// objectMapper.readTree(inputStream);
//			  }
			  
			  
			
				logger.log(Level.FINE, "statusCode="+statusCode);
				HttpEntity entity = response.getEntity();
				String string = EntityUtils.toString(entity);
				ObjectMapper mapper= ObjectMapperHolder.mapper;
				JsonNode input = mapper.readValue(string, JsonNode.class);
				Iterator<String> fieldNames = input.fieldNames();
				while(fieldNames.hasNext())
				{
					String fieldName = fieldNames.next();
					JsonNode jsonNode = input.get(fieldName);
					if(jsonNode instanceof TextNode)
					{
						TextNode textNode=(TextNode) jsonNode;
						this.endPoints.put(fieldName, textNode.asText());
					}
					
				}
				return null;
			});
		
		
		
		logger.log(Level.FINE, "endPoints="+endPoints);
		
		
	}
	private final String wellKnown;
	public String getWellKnown() {
		return wellKnown;
	}
	public String getClientId() {
		return clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public Map<String, String> getEndPoints() {
		return endPoints;
	}
	
	public String getAtmId() {
		return atmId;
	}
	
	public Settings atmId(String atmId)
	{
		this.atmId=atmId;
		return this;
	}
	
	Settings opaqueAccessToken()
	{
		this.opaqueAccessToken=true;
		return this;
	}
	
	Settings opaqueAccessToken(boolean flag)
	{
		this.opaqueAccessToken=flag;
		return this;
	}
	
	Settings lenientNonceOnMissingId()
	{
		this.lenientNonceOnMissingId=true;
		return this;
	}
	Settings lenientNonceOnMissingId(boolean flag)
	{
		this.lenientNonceOnMissingId=flag;
		return this;
	}
	public Settings introspect()
	{
		this.introspect=true;
		return this;
	}
	public Settings introspect(boolean flag)
	{
		this.introspect=flag;
		return this;
	}

	public String getLoginHint() {
		return loginHint;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	public String getJwksUriEndpoint() {
		return jwksUriEndpoint;
	}

	public String getAuthorizationEndpoint() {
		return authorizationEndpoint;
	}
	public String getUserinfoEndpoint() {
		return userinfoEndpoint;
	}
	public String getTokenEndpoint() {
		return tokenEndpoint;
	}
	private final Map<String, String> endPoints;
	private  Exception setupException;
	public Exception getSetupException() {
		return setupException;
	}

	@Override
	public String toString() {
		return "Settings [wellKnown=" + wellKnown + ", clientId=" + clientId + ", clientSecret=" + clientSecret
				+ ", loginHint=" + loginHint + ", atmId=" + atmId + ", tokenEndpoint=" + tokenEndpoint
				+ ", userinfoEndpoint=" + userinfoEndpoint + ", authorizationEndpoint=" + authorizationEndpoint
				+ ", jwksUriEndpoint=" + jwksUriEndpoint
				+ ", endPoints=" + endPoints + ", setupException=" + setupException + "]";
	}

	public String getIntrospectionEndpoint() {
		return introspectionEndpoint;
	}

	public boolean useIntrospection() {
		return introspect;
	}

}
