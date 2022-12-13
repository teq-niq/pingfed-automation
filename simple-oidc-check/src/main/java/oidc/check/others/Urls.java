package oidc.check.others;

import jakarta.servlet.http.HttpServletRequest;

public class Urls {
	public static final String RedirectPath="/oidc-hello";
	public static final String StartPath="/start";
	public static final String ClientCredentialsGrantFlowDemoPath="/ccgfd";

	
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
		
		String redirectUrl=protocol+"://localhost"+port+Urls.RedirectPath;
		return redirectUrl;
	}

}
