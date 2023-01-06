package com.example.servlet;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import com.example.config.CurrentSettings;
import com.example.config.Settings;
import com.example.config.constants.SharedConstants;
import com.example.constants.Constants;
import com.example.constants.Urls;
import com.example.misc.beans.Response;
import com.example.oidc.principal.OidcPrincipal;
import com.example.util.ClientBuilder;
import com.example.util.ObjectMapperHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@WebServlet(urlPatterns = {Urls.RedirectPath}, name = "AuthorizationCodeFlowCallBack", loadOnStartup=1)
public class AuthorizationCodeFlowCallBack extends HttpServlet {
	private final static Logger logger = Logger.getLogger(AuthorizationCodeFlowCallBack.class.getName());
	private static final long serialVersionUID = 1L;
	private static final String STATE_KEY = "state";
	private static final String NONCE_KEY = "nonce";
	
	private static final Settings[] settingsArr=CurrentSettings.authorizationCodeDefaultSettings;
	private ObjectMapper objectMapper= ObjectMapperHolder.mapper;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthorizationCodeFlowCallBack() {
        super();

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		try {
			getInternal(req, resp);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "unexpected", e);
			throw new ServletException("unexpected", e);
		}
	}

	private void getInternal(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		debugRequest(req);
		String stateValueFromCookie = getValueFromCookie(req.getCookies(), STATE_KEY);
		String nonceValueFromCookie = getValueFromCookie(req.getCookies(), NONCE_KEY);
		String redirectUrl = Urls.getRedirectrUrl(req);
		String code = req.getParameter("code");
		String state=req.getParameter(STATE_KEY);
		logger.log(Level.FINE, "state="+state+",stateValueFromCookie="+stateValueFromCookie+",nonceValueFromCookie="+nonceValueFromCookie);
		
		boolean stateMatched=false;
		if(state!=null&&stateValueFromCookie!=null)
		{
			if(state.equals(stateValueFromCookie))
			{
				stateMatched=true;
			}
		}
		if(stateMatched)
		{
			
			Response tokenResponseMixed = buildTokenResponse(state, stateValueFromCookie, nonceValueFromCookie, redirectUrl, code);
		    if(tokenResponseMixed.getStatusCode()==200)
		    {
		    	/*
			     * By this time authorization code flow is almost done.
			     * User is already authenticated at OIDC.
			     * creds contains access token data, id token data.
			     * But we cant trust anything even at this stage.
			     * We must first validate the access token signing.
			     * We must also validate the nonce
			     * We must also obtain the user information
			     * If there are no issues in the reaiing we treat the user as fully authenticated
			     * and create the principal object using our custom real.
			     */
				boolean loginSucess=false;
		    	try
				{
					req.login(null, tokenResponseMixed.getResponse());
					loginSucess=true;
				}
				catch(ServletException e)
				{
					logger.log(Level.INFO, "login failed", e);
				}
			    //if the realm vaerified the reamining steps of the authorzation code flow we will now have a principal
				if(loginSucess)
				{
					Principal authenticatedPrincipal = req.getUserPrincipal();
					if(authenticatedPrincipal!=null)
					{
						if(authenticatedPrincipal instanceof OidcPrincipal)
						{
							OidcPrincipal oidcPrincipal=(OidcPrincipal) authenticatedPrincipal;
							logger.log(Level.FINE, "authenticated oidc principal="+oidcPrincipal.toString());
						}
						String originalRequestUri = extractOriginalRequestUriFromState(stateValueFromCookie);
						if(originalRequestUri==null)
						{
							originalRequestUri=req.getContextPath() + Urls.WelcomePath;
						}
						resp.sendRedirect(originalRequestUri);
					}
					else
					{
						resp.setStatus(401);
						resp.getWriter().println("Forbidden");
					}
				}
				else
				{
					resp.setStatus(401);
					resp.getWriter().println("Forbidden. Login Failed");
				}
				
		    }
		    else
		    {
		    	resp.setStatus(401);
				resp.getWriter().println("Forbidden");
		    }
		}
		else
		{
			resp.setStatus(401);
			resp.getWriter().println("Forbidden");
		}
		
		
		
	}

	private String extractOriginalRequestUriFromState(String stateValueFromCookie) {
		int lastIndex = stateValueFromCookie.lastIndexOf(Constants.STATE_SEPERATOR);
		if(lastIndex==-1)
		{
			throw new AssertionError("Unexpected problem");
		}
		int indexToUse=lastIndex+Constants.STATE_SEPERATOR.length();
		String ret=stateValueFromCookie.substring(indexToUse);
		if(ret.startsWith("/start?selection="))
		{
			int inRetLastIndex=ret.lastIndexOf("&orig=");
			if(inRetLastIndex==-1)
			{
				ret=null;
			}
			else
			{
				int retIndexToUse=inRetLastIndex+"&orig=".length();
				ret=URLDecoder.decode(ret.substring(retIndexToUse));
			}
		}
		System.out.println("OriginalRequestUriFromState="+ret);
		return ret;
	}

	private void debugRequest(HttpServletRequest req) {
		
		Enumeration<String> parameterNames = req.getParameterNames();
		while(parameterNames.hasMoreElements())
		{
			String parametername = parameterNames.nextElement();
			String[] parameterValues = req.getParameterValues(parametername);
			String val=parameterValues.length>1?Arrays.toString(parameterValues):parameterValues[0];
			logger.log(Level.FINE, "["+parametername+"="+val+"]");
		}
		
		logger.log(Level.FINE, "---------");
		Enumeration<String> headerNames = req.getHeaderNames();
		while(headerNames.hasMoreElements())
		{
			String headerName = headerNames.nextElement();
			Enumeration<String> headers = req.getHeaders(headerName);
			List<String> headerList=new ArrayList<>();
			while(headers.hasMoreElements())
			{
				String header = headers.nextElement();
				headerList.add(header);
			}
			String val=headerList.size()>1?headerList.toString():headerList.get(0);
			logger.log(Level.FINE, "<"+headerName+"="+val+">");
			
		}
		logger.log(Level.FINE, "---------");
		Cookie[] cookies = req.getCookies();
		for (Cookie cookie : cookies) {
			logger.log(Level.FINE, "cookie:"+cookie.getName()+"="+cookie.getValue());
		}
	}

	private Response buildTokenResponse(String state, String stateValueFromCookie, String nonceValueFromCookie, String redirectUrl,
			String code) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
		
		Settings settings = extractSettings(stateValueFromCookie);
		
		logger.log(Level.FINE, "posting to "+settings.getTokenEndpoint());
		HttpPost request = new HttpPost(settings.getTokenEndpoint());
		String auth = settings.getClientId() + ":" + settings.getClientSecret();
		byte[] encodedAuth = Base64.getEncoder().encode(
		  auth.getBytes(StandardCharsets.ISO_8859_1));
		
		
		String authHeader = "Basic " + new String(encodedAuth);
		request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
		
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("grant_type", "authorization_code"));
	    params.add(new BasicNameValuePair("code", code));
	    params.add(new BasicNameValuePair("redirect_uri", redirectUrl));

	    request.setEntity(new UrlEncodedFormEntity(params));
	    
	    CloseableHttpClient client = ClientBuilder.buildClient();
	   
	    logger.log(Level.FINE, "time before="+System.currentTimeMillis());
	    Response credsResponse=getCreds(request, client);
	    if(credsResponse.getStatusCode()==200)
	    {
	    	ObjectNode readValue = objectMapper.readValue(credsResponse.getResponse(), ObjectNode.class);
		    readValue.put(SharedConstants.LAST_LEG_NONCE_KEY, nonceValueFromCookie);
		    readValue.put(SharedConstants.LAST_LEG_SETTINGS_INDEX, settings.getIndex());
		    String mixed = objectMapper.writeValueAsString(readValue);
		    credsResponse= new Response(200, mixed);
	    }
	    
	    
	    return credsResponse;
	}
	private String getValueFromCookie(Cookie[] cookies, String key) {
		String stateValueFromCookie =null;
		if(cookies!=null)
		{
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals(key))
				{
					stateValueFromCookie=cookie.getValue();
				}
			}
		}
		return stateValueFromCookie;
	}
	
	private Settings extractSettings(String stateValueFromCookie) {
		Settings settings = null;
		if(settingsArr.length==1)
		{
			settings = settingsArr[0];
		}
		else
		{
			int index = stateValueFromCookie.indexOf(Constants.STATE_SEPERATOR);
			if(index==-1)
			{
				throw new IllegalArgumentException("state did not contain our "+Constants.STATE_SEPERATOR);
			}
			String settingIndexStr=stateValueFromCookie.substring(0, index);
			logger.log(Level.FINE, "settingIndexStr="+settingIndexStr);
			//add some more checks here later
			int settingIndex=Integer.parseInt(settingIndexStr);
			settings = settingsArr[settingIndex];
		}
			

		return settings;
	}


	private Response getCreds(HttpPost request, CloseableHttpClient client) throws IOException {
		return client.execute(request, response -> {
			logger.log(Level.FINE, "time after="+System.currentTimeMillis());
	    	int statusCode = response.getCode();
			logger.log(Level.FINE, "statusCode="+statusCode);
			HttpEntity entity = response.getEntity();
			//
			String string = EntityUtils.toString(entity);
			logger.log(Level.FINE, string);
			return new Response(statusCode, string);
	    });
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		
		CurrentSettings.index();
		logger.log(Level.SEVERE, "Test Severe message");
		logger.log(Level.INFO, "Test Info message");
		logger.log(Level.FINE, "Test Fine message");
		logger.log(Level.FINEST, "Test Finest message");
	}


	
	

	


	
	
	


	

	

}
