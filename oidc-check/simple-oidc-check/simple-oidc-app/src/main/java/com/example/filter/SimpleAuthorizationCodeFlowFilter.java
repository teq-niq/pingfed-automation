package com.example.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.config.CurrentSettings;
import com.example.config.Settings;
import com.example.constants.Urls;
import com.example.oidc.principal.OidcPrincipal;
import com.example.util.RedirectToOidc;
import com.example.util.TokensVerifier;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter(Urls.ProtectedPath)//configure here to match protected urls
public class SimpleAuthorizationCodeFlowFilter implements Filter{
	private final static Logger logger = Logger.getLogger(SimpleAuthorizationCodeFlowFilter.class.getName());
	
	private static final Settings[] settingsArr=CurrentSettings.authorizationCodeDefaultSettings;
	

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		if(req instanceof HttpServletRequest && resp instanceof HttpServletResponse)
		{
			HttpServletRequest request=(HttpServletRequest) req;
			HttpServletResponse response=(HttpServletResponse) resp;
			
			/*
			 * no sessions - just container security. Possible in tomcat, in other app servers and also spring. using tomcat here
			 */
			Principal userPrincipal = request.getUserPrincipal();
			if(userPrincipal!=null)
			{
				//is authenticated already
				if(userPrincipal instanceof OidcPrincipal)
				{
					logger.log(Level.FINE, "found OidcPrincipal");
					OidcPrincipal oidcPrincipal=(OidcPrincipal) userPrincipal;
					try {
						//need to cache the jwks public key- need a strategy
						boolean verified = new TokensVerifier().fullVerify(oidcPrincipal);
						if(verified)
						{
							chain.doFilter(req, resp);
						}
						else
						{
							response.setStatus(401);
							response.getWriter().println("Forbidden");
						}
					} catch (Exception e) {
						logger.log(Level.SEVERE, "problem", e);
						throw new ServletException(e);
					}
					
					
				}
				else
				{
					//we are not really expecting any other realm to kick in.
					//but if it does lets trust it
					logger.log(Level.FINE, "=>found principal of type "+userPrincipal.getClass().getName());
					chain.doFilter(req, resp);
				}
				
			}
			else if(settingsArr.length==1)
			{
				
				RedirectToOidc.redirectToOidc(request, response, settingsArr[0]);
			}
			else
			{
				//must select the setting first
				String requestURI = request.getRequestURI();
				String queryString = request.getQueryString();
				String originalRequestURI=requestURI;
				if(queryString!=null)
				{
					originalRequestURI=originalRequestURI+"?"+queryString;
				}
				originalRequestURI=URLEncoder.encode(originalRequestURI);
				response.sendRedirect("oidcselect.jsp?orig1="+originalRequestURI);
			}
		
			
		
			
			
		}
		else
		{
			//choosing not to work with non http for now
			
			resp.getWriter().println("Forbidden");
		}
		
		
		
	}


	

}
