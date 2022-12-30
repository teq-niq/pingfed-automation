package com.example.util;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.config.CurrentSettings;
import com.example.config.Settings;
import com.example.constants.Constants;
import com.example.constants.Urls;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RedirectToOidc {
	private final static Logger logger = Logger.getLogger(RedirectToOidc.class.getName());
	private static final Settings[] settingsArr=CurrentSettings.authorizationCodeDefaultSettings;
	public static void redirectToOidc(HttpServletRequest req, HttpServletResponse resp, Settings settings)
			throws IOException {
		
		String requestURI = req.getRequestURI();
		String queryString = req.getQueryString();
		String originalRequestURI=requestURI;
		if(queryString!=null)
		{
			originalRequestURI=originalRequestURI+"?"+queryString;
		}
		System.out.println("originalRequestURI="+originalRequestURI);
		String redirectUrl = Urls.getRedirectrUrl(req);
		String state = addStateCookie(resp, Constants.STATE_KEY, settings, originalRequestURI);
		String nonce = addCookie(resp, Constants.NONCE_KEY, settings);
		
		String loginHint = settings.getLoginHint();
		String atmId = settings.getAtmId();
		String url=settings.getAuthorizationEndpoint()+"?redirect_uri="+
		Encoder.encode(redirectUrl)
		+"&response_type=code&client_id="+settings.getClientId()
		+"&scope="+Encoder.encode("openid email roles")
		+"&state="+Encoder.encode(state)
		+"&nonce="+Encoder.encode(nonce);
		if(loginHint!=null)
		{
			url+="&login_hint="+Encoder.encode(loginHint)	;
		}
		if(atmId!=null)
		{
			url+="&access_token_manager_id="+Encoder.encode(atmId)	;
		}
		
		
		
		logger.log(Level.FINE, "url="+url);
		
		//String url="https://accounts.google.com/o/oauth2/v2/auth?redirect_uri=http%3A%2F%2Flocalhost:8080%2Foidc-hello&prompt=consent&response_type=code&client_id=503443466006-65bvcaa25n1ba1lqqd4u72eeg1bspl9p.apps.googleusercontent.com&scope=openid&access_type=offline";
		resp.sendRedirect(url);
	}

	
	
	private static String addCookie(HttpServletResponse resp, String cookieKey, Settings settings) {
		if(cookieKey.equals(Constants.STATE_KEY))
		{
			throw new IllegalArgumentException("cookie key cannot be "+Constants.STATE_KEY);
		}
		String val=UUID.randomUUID().toString();
		
		Cookie stateCookie = new Cookie(cookieKey, val);
		stateCookie.setMaxAge(60*2);
		stateCookie.setHttpOnly(true);
		resp.addCookie(stateCookie);
		return val;
	}
	private static String addStateCookie(HttpServletResponse resp, String cookieKey, Settings settings, String originalRequestURI) {
		String val=UUID.randomUUID().toString();
		if(cookieKey.equals(Constants.STATE_KEY))
		{
			if(settingsArr.length!=1)
			{
				val=settings.getIndex()+Constants.STATE_SEPERATOR+val;
			}
			
		}
		else
		{
			throw new IllegalArgumentException("cookie key must be only "+Constants.STATE_KEY);
		}
		val=val+Constants.STATE_SEPERATOR+originalRequestURI;
		Cookie stateCookie = new Cookie(cookieKey, val);
		stateCookie.setMaxAge(60*2);
		stateCookie.setHttpOnly(true);
		resp.addCookie(stateCookie);
		return val;
	}

}
