package com.example.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

import com.example.config.CurrentSettings;
import com.example.config.Settings;
import com.example.constants.Urls;
import com.example.util.AccessTokenVerifier;
import com.example.util.ClientBuilder;
import com.example.util.ObjectMapperHolder;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@WebServlet(urlPatterns = Urls.ClientCredentialsGrantFlowDemoPath, name = "ClientCredentialsGrantFlowDemo")
public class ClientCredentialsDemo extends HttpServlet {
	private final static Logger logger = Logger.getLogger(ClientCredentialsDemo.class.getName());
	private static final long serialVersionUID = 1L;
	private static final String STATE_KEY = "state";
	private static final String NONCE_KEY = "nonce";
	
	private static final Settings[] settingsArr=CurrentSettings.clientCredentialsDefaultSettings;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClientCredentialsDemo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String selection = req.getParameter("selection");
		int selected=0;
		if(selection!=null)
		{
			selected=Integer.parseInt(selection);
		}
		Settings settings=settingsArr[selected];
		if(settings.getSetupException()!=null)
		{
			throw new ServletException("Did not load settings properly", settings.getSetupException());
		}
		try {
			getInternal(resp, settings);
		} catch (Exception e) {
			throw new ServletException("Problem", e);
		}

	}

	private void getInternal(HttpServletResponse resp, Settings settings) throws Exception {

		logger.log(Level.FINE, "using settings="+settings);

		HttpPost request = new HttpPost(settings.getTokenEndpoint());
		logger.log(Level.FINE, "ep="+settings.getTokenEndpoint());
		String auth = settings.getClientId() + ":" + settings.getClientSecret();
		logger.log(Level.FINE, "auth="+auth);
		byte[] encodedAuth = Base64.encodeBase64(
		  auth.getBytes(StandardCharsets.ISO_8859_1));
		String authHeader = "Basic " + new String(encodedAuth);
		logger.log(Level.FINE, "authHeader(basicEncoded)=["+authHeader+"]");
		request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);

		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("grant_type", "client_credentials"));
	    String atmId = settings.getAtmId();
		if(atmId!=null)
		{
			params.add(new BasicNameValuePair("access_token_manager_id", atmId));
			logger.log(Level.FINE, "atmId="+atmId);
			
		}

	    params.add(new BasicNameValuePair("scope", "email openid"));

	    request.setEntity(new UrlEncodedFormEntity(params));
	    ClientBuilder.buildClient();
	    
	    CloseableHttpClient client = ClientBuilder.buildClient();

	
	    client.execute(request, response -> {
	    	int statusCode = response.getCode();
			logger.log(Level.FINE, "statusCode="+statusCode);
			HttpEntity entity = response.getEntity();
			String string = EntityUtils.toString(entity);
			logger.log(Level.FINE, string);
			ObjectMapper mapper= ObjectMapperHolder.mapper;
			Map<String, String> readValue = mapper.readValue(string, Map.class);
			logger.log(Level.FINE, "readValue="+readValue);
			
			String accessToken=readValue.get("access_token");
		

			try {
				boolean verified = new AccessTokenVerifier().verifyAccessTokenUsingJwks(settings.getJwksUriEndpoint(), accessToken);
				logger.log(Level.FINE, "verified="+verified);
				PrintWriter out = resp.getWriter();
				out.println("accessToken="+accessToken);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
	    	return null;
	    });
		

		
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		
	}

}
