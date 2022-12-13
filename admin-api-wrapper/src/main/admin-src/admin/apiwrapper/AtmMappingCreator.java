package admin.apiwrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.pingfedadmin.api.*;
import com.example.pingfedadmin.model.*;
import com.example.pingfedadmin.model.AccessTokenMappingContext.TypeEnum;

import admin.apiwrapper.core.Core;
import admin.apiwrapper.util.CreatorUtil;
import admin.beans.AccessTokenMappingAttribute;

public class AtmMappingCreator extends BaseCreator{
	public AtmMappingCreator(Core core) {
		super(core);
		
	}

	public  AccessTokenMapping createTokenMappings(String id, TypeEnum contextType, String contextRefId, String atmRefId,
			AccessTokenMappingAttribute... attributes) {
		core.clearTransformers();
		OauthaccessTokenMappingsApi api= new OauthaccessTokenMappingsApi(core.getApiClient());
		
		 AccessTokenMapping  mapping=new AccessTokenMapping ();
		 mapping.setId(id);
		 AccessTokenMappingContext context= new AccessTokenMappingContext();
		 context.setType(contextType);
		 if(contextRefId!=null)
		 {
			 ResourceLink contextRef = new ResourceLink();
			 contextRef.setId(contextRefId);
			 context.setContextRef(contextRef);
		 }

		 mapping.setContext(context);
		 ResourceLink accessTokenManagerRef = new ResourceLink();
		 accessTokenManagerRef.setId(atmRefId);
		 mapping.setAccessTokenManagerRef(accessTokenManagerRef);
		 mapping.setAttributeSources(new ArrayList<AttributeSource>());
		 Map<String, AttributeFulfillmentValue> attributeContractFulfillment=new HashMap<>();
		 for (AccessTokenMappingAttribute attribute : attributes) {
			 CreatorUtil.addAttributeMapping(attributeContractFulfillment, attribute);
		}
		 
		 mapping.setAttributeContractFulfillment(attributeContractFulfillment);
		 AccessTokenMapping createMapping = api.createMapping(mapping, false);
		return createMapping;
	}

}
