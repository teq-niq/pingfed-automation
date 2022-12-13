package admin.apiwrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.pingfedadmin.api.*;
import com.example.pingfedadmin.model.*;

import admin.apiwrapper.core.Core;
import admin.apiwrapper.util.CreatorUtil;

public class IdpAdapterMappingCreator extends BaseCreator{
	public IdpAdapterMappingCreator(Core core) {
		super(core);
		
	}
	
	public  IdpAdapterMapping createIdpAdapterGrantMapping( String formAdapterid, String userKeyField) {
		core.clearTransformers();
		OauthidpAdapterMappingsApi api= new OauthidpAdapterMappingsApi(core.getApiClient());
		 IdpAdapterMapping body = new IdpAdapterMapping();
		 body.setId(formAdapterid);
		 ResourceLink pluginDescriptorRef = new ResourceLink();
		pluginDescriptorRef.setId(formAdapterid);
			
		 body.setIdpAdapterRef(pluginDescriptorRef);
		 Map<String, AttributeFulfillmentValue> attributeContractFulfillment=new HashMap<>();
		 CreatorUtil.addAttributeMappingTypeAdapter(attributeContractFulfillment, "USER_NAME", "givenName");
		 CreatorUtil.addAttributeMappingTypeAdapter(attributeContractFulfillment, "USER_KEY", userKeyField);
		 body.setAttributeContractFulfillment(attributeContractFulfillment);
		 IssuanceCriteria issuanceCriteria= new IssuanceCriteria();
			issuanceCriteria.setConditionalCriteria(new ArrayList());
			issuanceCriteria.setExpressionCriteria(new ArrayList());
			body.setIssuanceCriteria(issuanceCriteria);
			body.setAttributeSources(new ArrayList<AttributeSource>());
		 IdpAdapterMapping createdIdpAdapterMapping = api.createIdpAdapterMapping(body, false);
		return createdIdpAdapterMapping;
	}

}
