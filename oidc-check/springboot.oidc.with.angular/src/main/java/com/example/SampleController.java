package com.example;



import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class SampleController {
	
	

	@RequestMapping(path = "/foo", method = RequestMethod.GET)
	 @PreAuthorize("hasAuthority('SCOPE_foo')")
	public ResponseEntity<Map<String, Object>> foo() {
		
		Map<String, Object> data= new HashMap<>();
		data.put("foodata", "example");
		return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
		
		
	}
	@RequestMapping(path = "/bar", method = RequestMethod.GET)
	 @PreAuthorize("hasAuthority('SCOPE_bar')")
	public ResponseEntity<Map<String, Object>> bar() {
		
		Map<String, Object> data= new HashMap<>();
		data.put("bardata", "example");
		return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
		
		
	}
	
	@RequestMapping(path = "/profile", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Map<String, Object>> profile(OAuth2AuthenticationToken authentication) {
		Map<String, Object> profile=new HashMap<>();
		Collection<GrantedAuthority> authorities = authentication.getAuthorities();
		Set<String> grantedAuthorityNames=new HashSet<>();
		for (GrantedAuthority grantedAuthority : authorities) {
			grantedAuthorityNames.add(grantedAuthority.getAuthority());
		}
		profile.put("grantedAuthorityNames", grantedAuthorityNames);
	
		Map<String, Object> attributes = authentication.getPrincipal().getAttributes();
		Set<String> keySet = attributes.keySet();
		for (String key : keySet) {
			profile.put(key, attributes.get(key));
		}
		
		return new ResponseEntity<Map<String, Object>>(profile, HttpStatus.OK);
		
		
	}
	
	

}
