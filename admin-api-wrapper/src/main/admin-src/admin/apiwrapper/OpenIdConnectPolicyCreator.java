package admin.apiwrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.example.pingfedadmin.api.*;
import com.example.pingfedadmin.model.*;

import com.example.pingfedadmin.model.LdapAttributeSource.SearchScopeEnum;


import admin.TransformBean;
import admin.apiwrapper.core.Core;
import admin.apiwrapper.util.CreatorUtil;

public class OpenIdConnectPolicyCreator extends BaseCreator{
	public OpenIdConnectPolicyCreator(Core core) {
		super(core);
		
	}
	
	public  OpenIdConnectPolicy createOidcPolicy( String atmId, 
			String policyId, String policyName, boolean includeSHashInIdToken, 
			boolean includeSriInIdToken, boolean includeUserInfoInIdToken, int idTokenLifetime,
			Triplet<String, Boolean, Boolean> extendedAttributeDatas[], 
			com.example.pingfedadmin.model.AttributeSource.TypeEnum dsType,
			String dsId,
			String dsAttributeSourceId, String description, 
			com.example.pingfedadmin.model.SourceTypeIdKey.TypeEnum dsAttributeType,
			Pair<String, String>[] attributeContractFulfillmentDatas,
			Pair<String, String[]>[] scopesToAttributes, boolean includeCoreAttributeInIdToken, boolean includeCoreAttributeInInUserInfo, String ldapSearchFilter, String customAttributeSourceFilter) {
		core.setRequestTransformBeans(new TransformBean("attributeMapping/attributeSources/*/type",type->AttributeSource.TypeEnum.LDAP.name()));
		core.setResponseTransformBeans(new TransformBean("attributeMapping/attributeSources/*/type",type->type.charAt(0)+type.substring(1).toLowerCase()+"AttributeSource"));
		OauthopenIdConnectApi api= new OauthopenIdConnectApi(core.getApiClient());
		 OpenIdConnectPolicy body= new OpenIdConnectPolicy();
		 body.setId(policyId);
		 body.setName(policyName);
		 ResourceLink accessTokenManagerRef = new ResourceLink();
		 accessTokenManagerRef.setId(atmId);
		 body.setAccessTokenManagerRef(accessTokenManagerRef);
		 OpenIdConnectAttributeContract attributeContract= new OpenIdConnectAttributeContract();
		 List<OpenIdConnectAttribute> coreAttributes= new ArrayList<>();
		 addAttribute(coreAttributes, "sub", includeCoreAttributeInIdToken, includeCoreAttributeInInUserInfo);
		 attributeContract.setCoreAttributes(coreAttributes);
		 List<OpenIdConnectAttribute> extendedAttributes= new ArrayList<>();

		 for (Triplet<String, Boolean, Boolean> extendedAttributeData : extendedAttributeDatas) {
			 Boolean includeInIdTokenBool = extendedAttributeData.getValue1();
			 boolean includeInIdToken=includeInIdTokenBool!=null?includeInIdTokenBool.booleanValue():false;
			 Boolean includeInUserInfoBool=extendedAttributeData.getValue2();
			 boolean includeInUserInfo=includeInUserInfoBool!=null?includeInUserInfoBool.booleanValue():false;
			 addAttribute(extendedAttributes, extendedAttributeData.getValue0(), includeInIdToken, includeInUserInfo);
		}

		 attributeContract.setExtendedAttributes(extendedAttributes);
		 body.setAttributeContract(attributeContract);
		 AttributeMapping attributeMapping= new AttributeMapping();
		 List<AttributeSource> attributeSources= new ArrayList<>();
		 AttributeSource attributeSource= dsType==AttributeSource.TypeEnum.LDAP?new LdapAttributeSource():new CustomAttributeSource();
		 attributeSource.setId(dsAttributeSourceId);
		 attributeSource.setDescription(description);
		 attributeSource.setType(dsType);
		
		 ResourceLink dataStoreRef = new ResourceLink();
		 dataStoreRef.setId(dsId);
		 attributeSource.setDataStoreRef(dataStoreRef);
		 if(attributeSource instanceof LdapAttributeSource)
		 {
			 LdapAttributeSource ldapAttributeSource=(LdapAttributeSource) attributeSource;
			 ldapAttributeSource.setBaseDn("ou=People,dc=example,dc=com");
			 ldapAttributeSource.setSearchScope(SearchScopeEnum.SUBTREE);
			 ldapAttributeSource.setSearchFilter(ldapSearchFilter);
			 ldapAttributeSource.setSearchAttributes(Arrays.asList("Subject DN",
					 "givenName", "sn", "mail", "uid"));
		 }
		 if(attributeSource instanceof CustomAttributeSource)
		 {
			 CustomAttributeSource cattributeSource=(CustomAttributeSource) attributeSource;
			 FieldEntry filterFieldsItem= new FieldEntry();
			 filterFieldsItem.setName("Resource Path");
			 filterFieldsItem.setValue(customAttributeSourceFilter);
			 cattributeSource.addFilterFieldsItem(filterFieldsItem);
		 }
		 
		 attributeSources.add(attributeSource);

		 Map<String, AttributeFulfillmentValue> attributeContractFulfillment= new HashMap<>();
		 for (Pair<String, String> attributeContractFulfillmentData : attributeContractFulfillmentDatas) {
			 CreatorUtil.addAttributeMapping(attributeContractFulfillment,
					 attributeContractFulfillmentData.getValue0(), attributeContractFulfillmentData.getValue1(), dsAttributeType, dsAttributeSourceId);
			
		}

		 
		 attributeMapping.setAttributeContractFulfillment(attributeContractFulfillment);
		 attributeMapping.setAttributeSources(attributeSources);
		 body.setAttributeMapping(attributeMapping);
		 Map<String, ParameterValues> scopeAttributeMappings=new HashMap<>();

		
		 for (Pair<String, String[]> scopeToAttribute : scopesToAttributes) {
			 ParameterValues parameterValues= new ParameterValues();
			 String scopeName = scopeToAttribute.getValue0();
				for (String attributeName : scopeToAttribute.getValue1()) {
					 parameterValues.addValuesItem(attributeName);
				}
				scopeAttributeMappings.put(scopeName, parameterValues);
			}
		 
	
		 body.setScopeAttributeMappings(scopeAttributeMappings);
		 body.setIdTokenLifetime(idTokenLifetime);
		 //
		 body.setIncludeSHashInIdToken(includeSHashInIdToken);
		 body.setIncludeSriInIdToken(includeSriInIdToken);
		 body.setIncludeUserInfoInIdToken(includeUserInfoInIdToken);
		 //
		 OpenIdConnectPolicy createPolicy2 = api.createPolicy(body, false);
		return createPolicy2;
	}
	
	private static void addAttribute(List<OpenIdConnectAttribute> coreAttributes, String attributeName, boolean includeInIdToken, boolean includeInUserInfo) {
		OpenIdConnectAttribute openIdConnectAttribute= new OpenIdConnectAttribute();
		 openIdConnectAttribute.setName(attributeName);
		 openIdConnectAttribute.setMultiValued(false);
		 openIdConnectAttribute.setIncludeInIdToken(includeInIdToken);
		 openIdConnectAttribute.setIncludeInUserInfo(includeInUserInfo);
		 coreAttributes.add(openIdConnectAttribute);
	}


}
