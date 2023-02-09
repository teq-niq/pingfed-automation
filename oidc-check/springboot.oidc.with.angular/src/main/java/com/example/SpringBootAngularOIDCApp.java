package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SpringBootAngularOIDCApp {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAngularOIDCApp.class, args);
	}
	
	
}
