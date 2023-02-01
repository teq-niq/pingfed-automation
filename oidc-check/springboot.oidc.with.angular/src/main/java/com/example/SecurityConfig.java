package com.example;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
@Configuration
@EnableWebSecurity
public class SecurityConfig {



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
 
    	 CorsConfiguration corsConfiguration = new CorsConfiguration();
         corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "X-XSRF-TOKEN"));
         corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
         corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE"));
         corsConfiguration.setAllowCredentials(true);
         corsConfiguration.setExposedHeaders(List.of("Authorization"));
         
http.cors().configurationSource(request -> corsConfiguration).and().
  	
	//http.
	csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
	.authorizeHttpRequests()
	.requestMatchers("/tologin", "/getLoggedOnUser", "/secured")
	.authenticated()
	.and().oauth2Login()
	//.authorizationEndpoint().authorizationRedirectStrategy(null)
	//.failureHandler(null)
	//.failureUrl(null)

	.and().authorizeHttpRequests()
	.anyRequest().permitAll();
       

return http.build();
    }


}
