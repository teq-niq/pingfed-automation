package oidc.check;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Base64;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import oidc.check.others.ClientBuilder;
import oidc.check.others.CurrentSettings;
import oidc.check.others.Settings;
import oidc.check.others.Urls;


@WebServlet(urlPatterns = Urls.RedirectPath, name = "OidcHello")
public class OidcHello extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String STATE_KEY = "state";
	private static final String NONCE_KEY = "nonce";
	
	private static final Settings[] settingsArr=CurrentSettings.authorizationCodeDefaultSettings;
	private ObjectMapper objectMapper= new ObjectMapper();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OidcHello() {
        super();

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Settings settings=(Settings) req.getSession().getAttribute("selected");
		if(settings.getSetupException()!=null)
		{
			throw new ServletException("Did not load settings properly", settings.getSetupException());
		}
		if(settings.getSetupException()!=null)
		{
			throw new ServletException("Did not load settings properly", settings.getSetupException());
		}
		try {
			getInternal(req, resp, settings);
		} catch (Exception e) {
			throw new ServletException("Problem", e);
		}
	}

	private void getInternal(HttpServletRequest req, HttpServletResponse resp, Settings settings)
			throws Exception {

		Enumeration<String> parameterNames = req.getParameterNames();
		while(parameterNames.hasMoreElements())
		{
			String parametername = parameterNames.nextElement();
			String[] parameterValues = req.getParameterValues(parametername);
			String val=parameterValues.length>1?Arrays.toString(parameterValues):parameterValues[0];
			System.out.println("["+parametername+"="+val+"]");
		}
		String state=req.getParameter(STATE_KEY);
		
		Cookie[] cookies = req.getCookies();
		for (Cookie cookie : cookies) {
			System.out.println("cookie:"+cookie.getName()+"="+cookie.getValue());
		}
		String stateValueFromCookie = getValueFromCookie(cookies, STATE_KEY);
		String nonceValueFromCookie = getValueFromCookie(cookies, NONCE_KEY);
		System.out.println("state="+state+",stateValueFromCookie="+stateValueFromCookie+",nonceValueFromCookie="+nonceValueFromCookie);
		
		if(state!=null||stateValueFromCookie!=null)
		{
			boolean stateMatched=false;
			if(state!=null&&stateValueFromCookie!=null)
			{
				if(state.equals(stateValueFromCookie))
				{
					stateMatched=true;
				}
			}
			System.out.println("stateMatched="+stateMatched);
			if(!stateMatched)
			{
				throw new IllegalArgumentException("state was not matched");
			}
			
		}
		System.out.println("---------");
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
			System.out.println("<"+headerName+"="+val+">");
			
		}
		System.out.println("---------");
		System.out.println("posting to "+settings.getTokenEndpoint());
		HttpPost request = new HttpPost(settings.getTokenEndpoint());
		String auth = settings.getClientId() + ":" + settings.getClientSecret();
		byte[] encodedAuth = Base64.getEncoder().encode(
		  auth.getBytes(StandardCharsets.ISO_8859_1));
		
		
		String authHeader = "Basic " + new String(encodedAuth);
		request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
		String redirectUrl = Urls.getRedirectrUrl(req);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("grant_type", "authorization_code"));
	    params.add(new BasicNameValuePair("code", req.getParameter("code")));
	    params.add(new BasicNameValuePair("redirect_uri", redirectUrl));

	    request.setEntity(new UrlEncodedFormEntity(params));
	    
	    CloseableHttpClient client = ClientBuilder.buildClient();

	    
	    client.execute(request, response -> {
	    	int statusCode = response.getCode();
			System.out.println("statusCode="+statusCode);
			HttpEntity entity = response.getEntity();
			String string = EntityUtils.toString(entity);
			System.out.println(string);
			ObjectMapper mapper= new ObjectMapper();
			Map<String, String> readValue = mapper.readValue(string, Map.class);
			System.out.println("readValue="+readValue);
			
			String accessToken=readValue.get("access_token");
			String idToken = readValue.get("id_token");
			System.out.println();
			System.out.println("accessToken="+accessToken);

			boolean verified;
			try {
				verified = new AccessTokenVerifier(settings.getJwksUriEndpoint()).verifyAccessToken(accessToken);
				System.out.println("verified="+verified);
				System.out.println();
				System.out.println("idToken="+idToken);
				
				if(idToken!=null && nonceValueFromCookie!=null)
				{
					boolean nonceMatched = nonceMatched(nonceValueFromCookie, idToken);
					System.out.println("nonceMatched="+nonceMatched);
					if(!nonceMatched)
					{
						throw new IllegalArgumentException("nonce not matched in id token");
					}
					
				}
				try {
					next(resp, settings, accessToken);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
			
			
	    	return null;
	    });
	    
	    
		
		
	}

	

	
	



	private void next(HttpServletResponse resp, Settings settings, String accessToken) throws Exception, IOException {
		HttpGet get = new HttpGet(settings.getUserinfoEndpoint());
		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		
		 CloseableHttpClient client1 = ClientBuilder.buildClient();
		 client1.execute(get, userInfoResponse -> {
			 int userinfoResponseStatusCode = userInfoResponse.getCode();
				System.out.println("userinfoResponseStatusCode="+userinfoResponseStatusCode);
				HttpEntity userInfoResponseEntity = userInfoResponse.getEntity();
				String userInfoAsString = EntityUtils.toString(userInfoResponseEntity);
				System.out.println("userInfoAsString="+userInfoAsString);
				resp.getWriter().println("accessToken="+accessToken);
			return null; 
		 });
		

		
	}
	
	private boolean nonceMatched(String nonceValueFromCookie, String idToken) throws IOException {
		boolean nonceMatched=false;
		String[] idTokenSections = idToken.split("\\.");
		for (int i = 0; i < idTokenSections.length; i++) 
		{
			String idTokenSection = idTokenSections[i];
			if(i!=(idTokenSections.length-1))
			{
				byte[] decode = java.util.Base64.getDecoder().decode(idTokenSection);
				String decoded=new String(decode);
				System.out.println("i="+i+",decode="+decoded);
				if(i==(idTokenSections.length-2))
				{
					JsonNode node=objectMapper.readTree(decode);
					JsonNode nonceNode = node.get("nonce");
					if(nonceNode!=null)
					{
						String nonceFromIdfToken = nonceNode.asText();
						System.out.println("nonceFromIdfToken="+nonceFromIdfToken);
						if(nonceFromIdfToken.equals(nonceValueFromCookie))
						{
							nonceMatched=true;
						}
					}
				}
			}
			else
			{
				System.out.println("i="+i+",idTokenSection="+idTokenSection);
			}
			
			
		}
		return nonceMatched;
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


	
	
	


	

	

}
