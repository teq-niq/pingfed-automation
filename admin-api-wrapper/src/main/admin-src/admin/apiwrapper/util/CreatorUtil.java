package admin.apiwrapper.util;

import java.util.Map;


import com.example.pingfedadmin.model.*;
import com.example.pingfedadmin.model.SourceTypeIdKey.TypeEnum;

import admin.beans.AccessTokenMappingAttribute;

public class CreatorUtil {
	
	public static IdpAdapterAttribute addExtendedAttribute(IdpAdapterAttributeContract attributeContract, String name) {
		IdpAdapterAttribute extendedAttributesItem=new IdpAdapterAttribute();
		extendedAttributesItem.setName(name);
		extendedAttributesItem.setMasked(false);
		extendedAttributesItem.setPseudonym(false);
		attributeContract.addExtendedAttributesItem(extendedAttributesItem);
		return extendedAttributesItem;
	}
	
	public static IdpAdapterAttribute addCoreAttribute(IdpAdapterAttributeContract attributeContract, String name) {
		IdpAdapterAttribute extendedAttributesItem=new IdpAdapterAttribute();
		extendedAttributesItem.setName(name);
		extendedAttributesItem.setMasked(false);
		extendedAttributesItem.setPseudonym(false);
		attributeContract.addCoreAttributesItem(extendedAttributesItem);
		return extendedAttributesItem;
	}
	
	public static void addAttributeMappingTypeAdapter(Map<String, AttributeFulfillmentValue> attributeContractFulfillment, String val) {
		addAttributeMappingTypeAdapter(attributeContractFulfillment, val, val);
	}
	
	public static void addAttributeMappingTypeAdapter(Map<String, AttributeFulfillmentValue> attributeContractFulfillment, String key, String val) {
		addAttributeMappingWithNullId(attributeContractFulfillment, key,  TypeEnum.ADAPTER, val);
	}
	
	public static void addAttributeMappingWithNullId(Map<String, AttributeFulfillmentValue> attributeContractFulfillment,
			String key,  TypeEnum typeEnum, String val) {
		addAttributeMapping(attributeContractFulfillment, key, val, typeEnum, null);
	}
	
	public static void addAttributeMapping(Map<String, AttributeFulfillmentValue> attributeContractFulfillment,
			String key, String val, TypeEnum typeEnum, String id) {
		AttributeFulfillmentValue value=new AttributeFulfillmentValue();
		SourceTypeIdKey source=new SourceTypeIdKey();
		value.setSource(source);
		source.setType(typeEnum);
		source.setId(id);
		value.setValue(val);
		attributeContractFulfillment.put(key, value);
	}
	
	public static void addAttributeMapping(Map<String, AttributeFulfillmentValue> attributeContractFulfillment,
			AccessTokenMappingAttribute attribute) {
		addAttributeMapping(attributeContractFulfillment, attribute.getName(), attribute.getValue(), attribute.getType(), attribute.getId());
	}

}
