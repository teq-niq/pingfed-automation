package admin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.pingfedadmin.api.*;
import com.example.pingfedadmin.invoker.ApiClient;
import com.example.pingfedadmin.model.*;
import com.example.pingfedadmin.model.Client.GrantTypesEnum;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import admin.apiwrapper.AtmMappingCreator;
import admin.apiwrapper.ClientCreator;
import admin.apiwrapper.IdpAdapterCreator;
import admin.apiwrapper.IdpAdapterMappingCreator;
import admin.apiwrapper.JDBCDataStoreCreator;
import admin.apiwrapper.JdbcConfigStoreUpdater;
import admin.apiwrapper.JwtAtmCreator;
import admin.apiwrapper.LdapCreator;
import admin.apiwrapper.OpenIdConnectPolicyCreator;
import admin.apiwrapper.PasswordCredentialValidatorCreator;
import admin.apiwrapper.ScopesCreator;
import admin.apiwrapper.core.Core;
import admin.apiwrapper.util.CreatorUtil;
import admin.beans.AccessTokenMappingAttribute;
import admin.synched.SynchedAutomationSharedConstants;



public class Setup implements ISetup{
	
	
	private ApiClient apiClient;
	private MyClientHttpRequestInterceptor interceptor;
	private Core core;
	public void setResponseTransformBeans(TransformBean... transformBeans) {
		this.interceptor.setResponseTransformBeans(transformBeans);
	}
	public void setRequestTransformBeans(TransformBean... transformBeans) {
		this.interceptor.setRequestTransformBeans(transformBeans);
	}

	Setup() throws KeyManagementException, NoSuchAlgorithmException
	{
		

	}
	public void prepare(String alternateBasePath) throws NoSuchAlgorithmException, KeyManagementException, FileNotFoundException, IOException {
		RestTemplate restTemplate = new RestTemplateConfig().restTemplate(new RestTemplateBuilder());
		   interceptor = new MyClientHttpRequestInterceptor();
	        restTemplate.getInterceptors().add(interceptor);
		apiClient = new ApiClient(restTemplate);
		core= new Core(apiClient, interceptor);
		System.out.println("originalBasePath="+apiClient.getBasePath());
		if(alternateBasePath!=null)
		{
			apiClient.setBasePath(alternateBasePath);//if needing to cyhange
		}
		
		System.out.println("finalBasePath="+apiClient.getBasePath());
	}
	
	
	public  void setup() throws NoSuchAlgorithmException, KeyManagementException, FileNotFoundException, IOException {
		String ldapDsId="MyLDAP";
		String formAdapterid="HTMLFormAdapter";
		String passwordValidatorId="PasswordValidator";
		 String atmId1="testingATM1";
		 String policyId1="testingpolicy1";
		 String ldapAttributeSourceId="mypingfedldapds";
		 String atmId2="testingATM2";
		Properties mySqlProps = PropertiesUtil.loadProps(new File("../mysql.properties"));
		this.setupDb(mySqlProps);

		new LdapCreator(core).createLdap(ldapDsId, "MyLdap", "localhost", "cn=Directory Manager", "manager");
		
		PasswordCredentialValidator passwordCredentialValidator = new PasswordCredentialValidatorCreator(core).createPasswordCredentialValidator(
				ldapDsId, passwordValidatorId, passwordValidatorId, "uid=${username}");
		
		 
		  IdpAdapter idpAdapter1 = new IdpAdapterCreator(core).createIdpAdapter( passwordValidatorId, formAdapterid,
				 new String[] {"givenName", "mail", "sn", "uid"}, new String[]{"uid"}, "uid");
		
		  IdpAdapterMapping createdIdpAdapterMapping = new IdpAdapterMappingCreator(core).createIdpAdapterGrantMapping(formAdapterid, "username");
		
		 new JwtAtmCreator(core).createJWTATM( atmId1, "jwtatm1", 120, 1, SynchedAutomationSharedConstants.AtmOauth_PersistentGrantUserKeyAttrName, "iat", "nbf");
		 
		 new AtmMappingCreator(core).createTokenMappings( "jwtatm1mapping", AccessTokenMappingContext.TypeEnum.IDP_ADAPTER, formAdapterid, atmId1,
				 new AccessTokenMappingAttribute(null, SynchedAutomationSharedConstants.AtmOauth_PersistentGrantUserKeyAttrName, SourceTypeIdKey.TypeEnum.OAUTH_PERSISTENT_GRANT, "USER_KEY"),
				 new AccessTokenMappingAttribute(null, "iat", SourceTypeIdKey.TypeEnum.EXPRESSION, "#iat=@org.jose4j.jwt.NumericDate@now().getValue()"),
				 new AccessTokenMappingAttribute(null, "nbf", SourceTypeIdKey.TypeEnum.EXPRESSION, "#nbf = @org.jose4j.jwt.NumericDate@now(), #nbf.addSeconds(10), #nbf = #nbf.getValue()")
				 
				);
		 
		 new JwtAtmCreator(core).createJWTATM(atmId2, "jwtatm2", 5, 2, "iss", "sub", "aud", "nbf", "iat");
		 new AtmMappingCreator(core).createTokenMappings("jwtatm2mapping", AccessTokenMappingContext.TypeEnum.CLIENT_CREDENTIALS, null, atmId2,
				 new AccessTokenMappingAttribute(null, "iss", SourceTypeIdKey.TypeEnum.EXPRESSION, "#value = #this.get(\"context.HttpRequest\").getObjectValue().getRequestURL().toString(), #length = #value.length(), #length = #length-16, #iss = #value.substring(0, #length)"),
				 new AccessTokenMappingAttribute(null, "sub", SourceTypeIdKey.TypeEnum.TEXT, "6a481348-42a1-49d7-8361-f76ebd23634b"),
				 new AccessTokenMappingAttribute(null, "aud", SourceTypeIdKey.TypeEnum.TEXT, "https://apiauthete.ssa.gov/mga/sps/oauth/oauth20/token"),
				 new AccessTokenMappingAttribute(null, "nbf", SourceTypeIdKey.TypeEnum.EXPRESSION, "#nbf = @org.jose4j.jwt.NumericDate@now(), #nbf.addSeconds(10), #nbf = #nbf.getValue()"),
				 new AccessTokenMappingAttribute(null, "iat", SourceTypeIdKey.TypeEnum.EXPRESSION, "#iat=@org.jose4j.jwt.NumericDate@now().getValue()")
				 
						 
						 		 
				 );
		
		 
		 new ScopesCreator(core).addScopes( "email", "roles");
		 
		 new ClientCreator(core).createClient( SynchedAutomationSharedConstants.AuthCodeClientId, SynchedAutomationSharedConstants.AuthCodeClientId, SynchedAutomationSharedConstants.AuthCodeClientSecret, atmId1, 
				  true, null, "http://localhost:8080/oidc-hello", GrantTypesEnum.AUTHORIZATION_CODE,
				 GrantTypesEnum.ACCESS_TOKEN_VALIDATION);
		 new ClientCreator(core).createClient( "manual2", "manual2", "secret", atmId2, 
				  true, null, "", GrantTypesEnum.CLIENT_CREDENTIALS);
			 
		 Pair<String, String[]>[] scopesToAttributes=new Pair[] {
				 Pair.with("email", new String[] {"email", "family_name", "given_name"})
		 };
			
		 new OpenIdConnectPolicyCreator(core).createOidcPolicy( atmId1, policyId1, 
				 policyId1, false, false, false, 5,
				 new Triplet [] {
						 Triplet.with("email", true, true),
						 Triplet.with("family_name", true, true),
						 Triplet.with("given_name", true, true)}, 
				 AttributeSource.TypeEnum.LDAP, ldapDsId, ldapAttributeSourceId, "my pingfed ldap ds", 
				 SourceTypeIdKey.TypeEnum.LDAP_DATA_STORE,
				 new Pair[] {
						 Pair.with("sub", "Subject DN"),
						 Pair.with("email", "mail"),
						 Pair.with("family_name", "sn"),
						 Pair.with("given_name", "givenName")
				 },
				 scopesToAttributes, true, true, "uid=${"+SynchedAutomationSharedConstants.AtmOauth_PersistentGrantUserKeyAttrName+"}", 
				 "/users?uid=${"+SynchedAutomationSharedConstants.AtmOauth_PersistentGrantUserKeyAttrName+"}");
	
	}
	
	
		
	
	
	private void setupDb(Properties mysqlProps) {
		String userName=mysqlProps.getProperty("mysql.u");
		String password=mysqlProps.getProperty("mysql.upwd");
		String schema=mysqlProps.getProperty("mysql.schema");
		String host=mysqlProps.getProperty("mysql.host");
		String port=mysqlProps.getProperty("mysql.port");
		String url="jdbc:mysql://"+host+":"+port+"/"+schema+"?autoReconnect=true";
		String jdbcDsId="JDBC-MYSQL";
		String jdbcDsName="mysqljdbcName";
		new JDBCDataStoreCreator(core).createJdbcDataStore(jdbcDsId, jdbcDsName, url, userName, password);
		
		new JdbcConfigStoreUpdater(core).updateClientManagerJdbcConfigStore(jdbcDsId);
		
	}


	

	

	}
