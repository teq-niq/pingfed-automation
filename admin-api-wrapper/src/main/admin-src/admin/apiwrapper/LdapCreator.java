package admin.apiwrapper;

import java.util.ArrayList;
import java.util.List;

import com.example.pingfedadmin.api.*;
import com.example.pingfedadmin.model.*;
import com.example.pingfedadmin.model.DataStore.TypeEnum;
import com.example.pingfedadmin.model.LdapDataStore.LdapTypeEnum;

import admin.TransformBean;
import admin.apiwrapper.core.Core;




public class LdapCreator extends BaseCreator {
	
	public LdapCreator(Core core) {
		super(core);
		
	}
	
	public  DataStore createLdap(String id, String name, String hostName, String userDn, String password) {
		DataStoresApi dataStoresApi= new DataStoresApi(core.getApiClient());
		core.setRequestTransformBeans(new TransformBean("type",type->TypeEnum.LDAP.name()));
		core.setResponseTransformBeans(new TransformBean("type",type->type.charAt(0)+type.substring(1).toLowerCase()+"DataStore"));
		
		LdapDataStore ldapDataStore = new LdapDataStore();
		List<String> hostNames = addStringToNewList(hostName);
		ldapDataStore.setHostnames(hostNames);
		ldapDataStore.setType(TypeEnum.LDAP);
		ldapDataStore.setId(id);
		ldapDataStore.setName(name);
		ldapDataStore.setLdapType(LdapTypeEnum.PING_DIRECTORY);
		ldapDataStore.setUserDN(userDn);
		ldapDataStore.setPassword(password);
		DataStore createdDataStore = dataStoresApi.createDataStore(ldapDataStore, false);
		return createdDataStore;
	}

	
	private  List<String> addStringToNewList(String string) {
		List<String> hostNames= new ArrayList<>();
		hostNames.add(string);
		return hostNames;
	}


	

}
