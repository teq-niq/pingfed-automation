package com.example;



import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;




@RestController
public class LoggedOnController {
	@Value("${isOn4200:false}")
	private boolean isOn4200;
	@Autowired
	private  OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

	@RequestMapping(path = "/getLoggedOnUser", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getLoggedOnUser() throws IOException {
		
		Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();
		Map<String, Object> data= new HashMap<>();
		System.out.println("gett authentication="+authentication.getClass().getName());
		if(authentication.isAuthenticated())
		{
			org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken o=(OAuth2AuthenticationToken) authentication;
			OAuth2AuthorizedClient client =
					oAuth2AuthorizedClientService.loadAuthorizedClient(
				            o.getAuthorizedClientRegistrationId(),
				            o.getName());
			OAuth2AccessToken accessTokenObj = client.getAccessToken();
			System.out.println("accessTokenObj.class="+accessTokenObj.getClass().getName());
			Set<String> scopes = accessTokenObj.getScopes();
			System.out.println("scopes="+scopes);
			
			OAuth2User principal = o.getPrincipal();

			data.put("username", principal.getName());
			if(principal instanceof OidcUser)
			{
				OidcUser oidcUser=(OidcUser) principal;
				
				data.put("username", oidcUser.getGivenName());
				System.out.println("claims="+oidcUser.getClaims());
				Collection<? extends GrantedAuthority> authorities = oidcUser.getAuthorities();
				for (GrantedAuthority grantedAuthority : authorities) {
					System.out.println("grantedAuthority="+grantedAuthority.getAuthority()+"@"+grantedAuthority.getClass().getName());
					if(grantedAuthority instanceof OidcUserAuthority)
					{
						OidcUserAuthority oua=(OidcUserAuthority) grantedAuthority;
						OidcIdToken idToken = oua.getIdToken();
					
						System.out.println("idToken.class="+idToken.getClass().getName());
						OidcUserInfo userInfo = oua.getUserInfo();
						
						System.out.println("userInfo.class="+userInfo.getClass().getName());
						
					}

				}
			}
			
			data.put("authenticateStatus", true);
			return new ResponseEntity<Map<String,Object>>(data, HttpStatusCode.valueOf(200));
		}
		else
		{
			data.put("authenticateStatus", false);
			return new ResponseEntity<Map<String,Object>>(data, HttpStatusCode.valueOf(403));
		}
		
		
		
	}
	
	@RequestMapping(path = "/tologin", method = RequestMethod.GET)
	public void tologin(@AuthenticationPrincipal OidcUser oidcUser, HttpServletRequest request, HttpServletResponse response) throws IOException {

		
	
		String serverUrl = getServerUrl(request);
		response.sendRedirect(isOn4200?"http://localhost:4200":serverUrl);
	}

	private String getServerUrl(HttpServletRequest request) {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		ServletContext servletContext = request.getServletContext();
		String servletContextName = servletContext.getServletContextName();
		String contextPath = servletContext.getContextPath();
		//System.out.println("scheme="+scheme+",serverName="+serverName+",serverPort="+serverPort+",servletContextName="+servletContextName+",contextPath="+contextPath);
		String url = scheme+"://"+serverName+":"+serverPort;
		return url;
	}

}
