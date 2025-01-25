package admin.apiwrapper;

import com.example.pingfedadmin.api.*;
import com.example.pingfedadmin.model.*;

import admin.apiwrapper.core.Core;

public class JwtAtmCreator extends BaseCreator{
	public JwtAtmCreator(Core core) {
		super(core);
		
	}
	
	public  AccessTokenManager createJWTATM(String id, String name, int tokenLifeTimeInMinutes, int sequenceNo, String... attributeNames) {
		core.clearTransformers();
		OauthaccessTokenManagersApi api= new OauthaccessTokenManagersApi(core.getApiClient());
		 AccessTokenManager manager= new AccessTokenManager();
		 manager.setId(id);
		 manager.setName(name);
		 manager.sequenceNumber(sequenceNo);
		 ResourceLink pluginDescriptorRef = new ResourceLink();
			pluginDescriptorRef.setId("com.pingidentity.pf.access.token.management.plugins.JwtBearerAccessTokenManagementPlugin");
		 manager.setPluginDescriptorRef(pluginDescriptorRef);
		 
		 AccessTokenAttributeContract attributeContract= new AccessTokenAttributeContract();
		for (String attributeName : attributeNames) {
			AccessTokenAttribute extendedAttributesItem= new AccessTokenAttribute();
			 extendedAttributesItem.setName(attributeName);
			 extendedAttributesItem.setMultiValued(false);
			 attributeContract.addExtendedAttributesItem(extendedAttributesItem);
			
		}
		  
		 PluginConfiguration configuration= new PluginConfiguration();
		 addPluginConfigurationField(configuration, "Token Lifetime", String.valueOf(tokenLifeTimeInMinutes));
		 addPluginConfigurationField(configuration, "Use Centralized Signing Key", "true");
		 addPluginConfigurationField(configuration, "JWS Algorithm", "RS256");
		 
		 manager.setConfiguration(configuration);
		
		 manager.setAttributeContract(attributeContract);
		 AccessTokenManager createTokenManager = api.createTokenManager(manager);
		return createTokenManager;
	}
	
	private static void addPluginConfigurationField(PluginConfiguration configuration, String name, String value) {
		ConfigField fieldsItem= new ConfigField();
		 fieldsItem.setName(name);
		 fieldsItem.setValue(value);
		 configuration.addFieldsItem(fieldsItem);
	}

}