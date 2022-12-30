package admin.apiwrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.example.pingfedadmin.api.*;
import com.example.pingfedadmin.model.*;
import com.example.pingfedadmin.model.Client.GrantTypesEnum;

import admin.apiwrapper.core.Core;

public class ClientCreator extends BaseCreator{
	public ClientCreator(Core core) {
		super(core);
		
	}
	
	public  void createClient(String clientId, String clientName, 
			String clientSecret, String atmId2, 
			Boolean restrictToDefaultAccessTokenManager, String oidcPolicyId, String redirectUrlsPipeSeperated,
			GrantTypesEnum... grantTypes
			) {
		core.clearTransformers();
		String[] redirectUrls= {};
		if(redirectUrlsPipeSeperated!=null )
		{
			redirectUrlsPipeSeperated=redirectUrlsPipeSeperated.trim();
			if(redirectUrlsPipeSeperated.length()>0)
			{
				redirectUrls=redirectUrlsPipeSeperated.split(Pattern.quote("|"));
				for (int i = 0; i < redirectUrls.length; i++) {
					redirectUrls[i]=redirectUrls[i].trim();
				}
			}
			
		}
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
		
		 
		 client.setGrantTypes(Arrays.asList(grantTypes));
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
