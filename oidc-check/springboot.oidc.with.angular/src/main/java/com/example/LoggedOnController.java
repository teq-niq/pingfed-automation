package com.example;



import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
	public ResponseEntity<UserInfo> getLoggedOnUser() throws IOException {
		
		Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();
		UserInfo data= new UserInfo();
		System.out.println("gett authentication="+authentication.getClass().getName());
		if(authentication.isAuthenticated())
		{
			org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken o=(OAuth2AuthenticationToken) authentication;
			OAuth2AuthorizedClient client =
					oAuth2AuthorizedClientService.loadAuthorizedClient(
				            o.getAuthorizedClientRegistrationId(),
				            o.getName());
			OAuth2AccessToken accessTokenObj = client.getAccessToken();
			String tokenValue = accessTokenObj.getTokenValue();
			System.out.println("tokenValue="+tokenValue);
			System.out.println("accessTokenObj.class="+accessTokenObj.getClass().getName());
			Set<String> scopes = accessTokenObj.getScopes();
			System.out.println("scopes="+scopes);
			
			OAuth2User principal = o.getPrincipal();
			data.setUsername( principal.getName());
			
			if(principal instanceof OidcUser)
			{
				OidcUser oidcUser=(OidcUser) principal;
				data.setUsername( oidcUser.getGivenName());
				
				System.out.println("claims="+oidcUser.getClaims());
				Collection<? extends GrantedAuthority> authorities = oidcUser.getAuthorities();
				for (GrantedAuthority grantedAuthority : authorities) {
					data.add(grantedAuthority.getAuthority());
					}
			}
			
			data.setAuthenticateStatus(true);
			return new ResponseEntity<UserInfo>(data, HttpStatus.OK);
		}
		else
		{
			data.setAuthenticateStatus(false);
			return new ResponseEntity<UserInfo>(data, HttpStatus.FORBIDDEN);
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
		String url = scheme+"://"+serverName+":"+serverPort;
		return url;
	}

}
