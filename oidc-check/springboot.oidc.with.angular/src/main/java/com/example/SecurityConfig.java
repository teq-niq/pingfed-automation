package com.example;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
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
		corsConfiguration
				.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT", "OPTIONS", "PATCH", "DELETE"));
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setExposedHeaders(List.of("Authorization"));

		OAuth2LoginConfigurer<HttpSecurity> oauth2Login = http.cors().configurationSource(request -> corsConfiguration)
				.and().

				// http.
				csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and().authorizeHttpRequests()
				.requestMatchers("/tologin", "/getLoggedOnUser", "/secured").authenticated().and().oauth2Client().and()
				.oauth2Login();

		oauth2Login.

				tokenEndpoint().accessTokenResponseClient(this.accessTokenResponseClient());

		oauth2Login

				.and().authorizeHttpRequests().anyRequest().permitAll();

		return http.build();
	}
	@Bean
	 OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
	
		return new AuthorizationCodeTokenResponseClientForMyPingFed();
	}

}
