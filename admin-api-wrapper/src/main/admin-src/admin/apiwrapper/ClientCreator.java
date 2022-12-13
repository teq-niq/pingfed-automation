package admin.apiwrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.pingfedadmin.api.*;
import com.example.pingfedadmin.model.*;
import com.example.pingfedadmin.model.Client.GrantTypesEnum;

import admin.apiwrapper.core.Core;

public class ClientCreator extends BaseCreator{
	public ClientCreator(Core core) {
		super(core);
		
	}
	
	public  void createClient(String clientId, String clientName, 
			String clientSecret, String atmId2, boolean clientCredentials, boolean authorizationCode,
			Boolean restrictToDefaultAccessTokenManager, String oidcPolicyId, String... redirectUrls) {
		core.clearTransformers();
		OauthclientsApi api= new OauthclientsApi(core.getApiClient());
		 Client client= new Client();
		 client.setClientId(clientId);
		 client.setName(clientName);
		 if(redirectUrls!=null && redirectUrls.length>0)
		 {
			 client.setRedirectUris(Arrays.asList(redirectUrls));
		 }
		 
		 ClientAuth clientAuth=new ClientAuth();
		 clientAuth.setSecret(clientSecret);
		 clientAuth.setType(ClientAuth.TypeEnum.SECRET);
		 client.setClientAuth(clientAuth);
		 List<GrantTypesEnum> grantTypes= new ArrayList<>();
		 if(clientCredentials)
		 {
			 grantTypes.add(GrantTypesEnum.CLIENT_CREDENTIALS);
		 }
		 if(authorizationCode)
		 {
			 grantTypes.add(GrantTypesEnum.AUTHORIZATION_CODE);
		 }
		 client.setGrantTypes(grantTypes);
		 if(atmId2!=null)
		 {
			 ResourceLink accessTokenManagerRef = new ResourceLink();
			 accessTokenManagerRef.setId(atmId2);
			 client.setDefaultAccessTokenManagerRef(accessTokenManagerRef);
			
		 }
		 if(restrictToDefaultAccessTokenManager!=null)
		 {
			 client.setRestrictToDefaultAccessTokenManager(restrictToDefaultAccessTokenManager);
		 }
		 ClientOIDCPolicy oidcPolicy= new ClientOIDCPolicy();
		 ResourceLink policyGroup = new ResourceLink();
			policyGroup.setId(oidcPolicyId);
		
		 oidcPolicy.setPolicyGroup(policyGroup);
		 client.setOidcPolicy(oidcPolicy);
		
		 
		 api.createClient(client);
	}


}
