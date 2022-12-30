package com.example.util;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;


public class ClientBuilder {
	
	public static CloseableHttpClient buildClient() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException
	{
		TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
	    SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
	    
	    
	    PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
	    	      .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create().setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
	    	              //.setSslContext(SSLContexts.createSystemDefault())
	    	    		  .setSslContext(sslContext)
	    	              .setTlsVersions(TLS.V_1_3, TLS.V_1_2)
	    	              .build())
	    	      .setDefaultSocketConfig(SocketConfig.custom()
	    	              .setSoTimeout(Timeout.ofSeconds(5))
	    	              .build())
	    	      .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.STRICT)
	    	      .setConnPoolPolicy(PoolReusePolicy.LIFO)
	    	      .setConnectionTimeToLive(TimeValue.ofMinutes(1L))
	    	      .build();

	    
	    HttpClientBuilder builder = HttpClients.custom()//.setSSLSocketFactory(sslsf)
	    	      .setConnectionManager(connectionManager);
	    
	    CloseableHttpClient client = HttpClients.custom()
	    	      .setConnectionManager(connectionManager)
	    	      .setDefaultRequestConfig(RequestConfig.custom()
	    	              .setConnectTimeout(Timeout.ofSeconds(5))
	    	              .setResponseTimeout(Timeout.ofSeconds(5))
	    	              .setCookieSpec(StandardCookieSpec.STRICT)
	    	              .build())
	    	      .build();
	    return client;
	}

}
