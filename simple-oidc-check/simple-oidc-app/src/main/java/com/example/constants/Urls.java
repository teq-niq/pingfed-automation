package com.example.constants;

import com.example.config.AutomationSharedConstants;

import jakarta.servlet.http.HttpServletRequest;

public class Urls {
	public static final String RedirectPath="/oidc-hello";
	public static final String StartPath="/start";
	public static final String ClientCredentialsGrantFlowDemoPath="/ccgfd";
	public static final String ProtectedPath="/protected";
	public static final String Logout="/logout";
	public static final String WelcomePath="/";

	
	public static String getRedirectrUrl(HttpServletRequest req) {
		int localPort = req.getLocalPort();
		
		boolean secure = req.isSecure();
		String port=":"+localPort;
		if(localPort==443 && secure)
		{
			port="";
		}
		if(localPort==80 && (!secure))
		{
			port="";
		}
		String protocol = secure?"https":"http";
		
		String redirectUrl=protocol+"://"+AutomationSharedConstants.HOSTNAME+port+Urls.RedirectPath;
		return redirectUrl;
	}

}
