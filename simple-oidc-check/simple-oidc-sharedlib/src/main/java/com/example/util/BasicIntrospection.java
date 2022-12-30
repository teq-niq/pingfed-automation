package com.example.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.utils.Base64;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import com.example.config.Settings;
import com.example.misc.beans.Response;

public class BasicIntrospection {
	private final static Logger logger = Logger.getLogger(BasicIntrospection.class.getName());
public static Response introspect(Settings settings, String accessToken) throws Exception, IOException {
		
		HttpPost request = new HttpPost(settings.getIntrospectionEndpoint());
		String auth = settings.getClientId() + ":" + settings.getClientSecret();
		logger.log(Level.FINE, "auth="+auth);
		byte[] encodedAuth = Base64.encodeBase64(
		  auth.getBytes(StandardCharsets.ISO_8859_1));
		String authHeader = "Basic " + new String(encodedAuth);
		logger.log(Level.FINE, "authHeader(basicEncoded)=["+authHeader+"]");
		request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("token", accessToken));
	    params.add(new BasicNameValuePair("token_type_hint", "access_token"));
	    request.setEntity(new UrlEncodedFormEntity(params));
		 CloseableHttpClient client1 = ClientBuilder.buildClient();
		return  client1.execute(request, userInfoResponse -> {
			 int intrspectionResponseStatusCode = userInfoResponse.getCode();
				logger.log(Level.FINE, "intrspectionResponseStatusCode="+intrspectionResponseStatusCode);
				HttpEntity introspectionResponseEntity = userInfoResponse.getEntity();
				String intrspsepctionResponseAsString = EntityUtils.toString(introspectionResponseEntity);
				logger.log(Level.FINE, "intrspsepctionResponseAsString="+intrspsepctionResponseAsString);
				
				
				
				
			return new Response(intrspectionResponseStatusCode, intrspsepctionResponseAsString); 
		 });
		

		
	}

}
