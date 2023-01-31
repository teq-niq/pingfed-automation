package com.example;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@RestController
public class LogoutController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    

    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
			.getContextHolderStrategy();
    
    @RequestMapping(path = "/logoutuser", method = RequestMethod.GET, consumes=MediaType.ALL_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request, HttpServletResponse httpServletResponse,
                     Authentication authentication) throws ServletException {
    	System.out.println("loggingout");
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
				
			}
		
		SecurityContext context = this.securityContextHolderStrategy.getContext();
		this.securityContextHolderStrategy.clearContext();

			context.setAuthentication(null);
		
			request.logout();
			System.out.println("loggedout");
			Map<String, String> ret= new HashMap<>();
			ret.put("loggedout", "done");
			//TODO
			//must also use the OIDC logout endpoint
			return new ResponseEntity<Map<String, String>>(ret,HttpStatusCode.valueOf(200));
       
    }

  
  
}
