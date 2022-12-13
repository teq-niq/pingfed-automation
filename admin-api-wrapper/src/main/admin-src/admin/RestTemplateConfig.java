package admin;

import org.apache.http.Header;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;

import com.example.pingfedadmin.invoker.CustomInstantDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.threetenbp.ThreeTenModule;


import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

//import com.rrr.pingfedadmin.UsernamePasswordCredentials;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) throws NoSuchAlgorithmException, KeyManagementException, FileNotFoundException, IOException {

        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };  
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom()); 
        
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        Properties apiProps = PropertiesUtil.loadProps(new File("pingfed.api.properties"));
        //PingFederate
        //x-xsrf-header
        credentialsProvider.setCredentials(AuthScope.ANY, 
                        new UsernamePasswordCredentials(apiProps.getProperty("username"), apiProps.getProperty("password")));
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setDefaultCredentialsProvider(credentialsProvider)
                .setDefaultHeaders(defaultHeaders())
                .build();  
        
      

    
        HttpComponentsClientHttpRequestFactory customRequestFactory = new HttpComponentsClientHttpRequestFactory();
        customRequestFactory.setHttpClient(httpClient);
        
        RestTemplate restTemplate = builder.errorHandler(new MyResponseErrorHandler() ).
        		requestFactory(() -> customRequestFactory).build();
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        System.out.println("messageConverters="+messageConverters);
        MappingJackson2HttpMessageConverter found=null;
        for (HttpMessageConverter<?> httpMessageConverter : messageConverters) {
			if(httpMessageConverter instanceof MappingJackson2HttpMessageConverter)
			{
				MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter=(MappingJackson2HttpMessageConverter) httpMessageConverter;
				ObjectMapper objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
				if(objectMapper!=null)
				{
					ThreeTenModule module = new ThreeTenModule();
					 module.addDeserializer(OffsetDateTime.class, CustomInstantDeserializer.OFFSET_DATE_TIME);
					 objectMapper.registerModule(module);
					    System.out.println("REGISERED");
					    found=mappingJackson2HttpMessageConverter;
				}
			}
		}
        if(found!=null)
        {
        	messageConverters.remove(found);
        	messageConverters.add(0, found);
        	restTemplate.setMessageConverters(messageConverters);
        	System.out.println("reset");
        }
        return restTemplate;  
    }

	private Collection<? extends Header> defaultHeaders() {
		List<Header> h= new ArrayList<>();
		h.add(new BasicHeader("x-xsrf-header", "PingFederate"));
		return h;
	}
}
