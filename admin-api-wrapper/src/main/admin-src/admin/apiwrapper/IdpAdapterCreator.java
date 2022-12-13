package admin.apiwrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.pingfedadmin.api.*;
import com.example.pingfedadmin.model.*;

import admin.apiwrapper.core.Core;
import admin.apiwrapper.util.CreatorUtil;

public class IdpAdapterCreator extends BaseCreator{
	public IdpAdapterCreator(Core core) {
		super(core);
		
	}
	
	public  IdpAdapter createIdpAdapter( String passwordValidatorId, String formAdapterId,
			String[] extendedAttributes, String[] pseudonymnAttributes, String uniqueUserKeyAttribute) {
		core.clearTransformers();
		
		IdpadaptersApi idpadaptersApi = new IdpadaptersApi(core.getApiClient());

		IdpAdapter idpAdapter = new IdpAdapter();
		idpAdapter.setName(formAdapterId);
		idpAdapter.setId(formAdapterId);
		ResourceLink pluginDescriptorRef = new ResourceLink();
		pluginDescriptorRef.setId("com.pingidentity.adapters.htmlform.idp.HtmlFormIdpAuthnAdapter");
		idpAdapter.setPluginDescriptorRef(pluginDescriptorRef);
		//idpAdapter.se
		
		PluginConfiguration configuration = new PluginConfiguration();
		
		
		ConfigTable tablesItem=new ConfigTable(); 
		tablesItem.setName("Credential Validators");
		ConfigRow rowsItem= new ConfigRow();
		rowsItem.defaultRow(false);
		ConfigField fieldsItem= new ConfigField();
		fieldsItem.setName("Password Credential Validator Instance");
		fieldsItem.setValue(passwordValidatorId);
		rowsItem.addFieldsItem(fieldsItem);
		tablesItem.addRowsItem(rowsItem);
		configuration.addTablesItem(tablesItem);
		
		IdpAdapterAttributeContract attributeContract = new IdpAdapterAttributeContract();
		IdpAdapterAttribute addedCoreAttribute = CreatorUtil.addCoreAttribute(attributeContract, "username");
		for (String pseudonymnAttribute : pseudonymnAttributes) {
		if(pseudonymnAttribute.equals("username"))
		{
			addedCoreAttribute.setPseudonym(true);
		}
		}
		CreatorUtil.addCoreAttribute(attributeContract, "policy.action");
		
		
		for (String extendedAttribute : extendedAttributes) {
			IdpAdapterAttribute addedExtendedAttribute = CreatorUtil.addExtendedAttribute(attributeContract, extendedAttribute);
			for (String pseudonymnAttribute : pseudonymnAttributes) {
				if(pseudonymnAttribute.equals(extendedAttribute))
				{
					addedExtendedAttribute.setPseudonym(true);
				}
			}
		}
		attributeContract.setMaskOgnlValues(false);
		attributeContract.setUniqueUserKeyAttribute(uniqueUserKeyAttribute);;
		idpAdapter.setAttributeContract(attributeContract);
		IdpAdapterContractMapping attributeMapping=new IdpAdapterContractMapping();
		IssuanceCriteria issuanceCriteria= new IssuanceCriteria();
		issuanceCriteria.setConditionalCriteria(new ArrayList());
		issuanceCriteria.setExpressionCriteria(new ArrayList());
		attributeMapping.setIssuanceCriteria(issuanceCriteria);
		attributeMapping.setAttributeSources(new ArrayList<AttributeSource>());
		Map<String, AttributeFulfillmentValue> attributeContractFulfillment=new HashMap<>();
		CreatorUtil.addAttributeMappingTypeAdapter(attributeContractFulfillment, "givenName");
		CreatorUtil.addAttributeMappingTypeAdapter(attributeContractFulfillment, "mail");
		CreatorUtil.addAttributeMappingTypeAdapter(attributeContractFulfillment, "sn");
		CreatorUtil.addAttributeMappingTypeAdapter(attributeContractFulfillment, "uid");
		attributeMapping.setAttributeContractFulfillment(attributeContractFulfillment);
		idpAdapter.setAttributeMapping(attributeMapping);
		idpAdapter.setConfiguration(configuration);
		
		IdpAdapter createdIdpAdapter = idpadaptersApi.createIdpAdapter(idpAdapter, false);

		return createdIdpAdapter;
	}
	
	

}
