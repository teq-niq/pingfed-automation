package admin.apiwrapper;

import com.example.pingfedadmin.api.*;
import com.example.pingfedadmin.model.*;

import admin.apiwrapper.core.Core;

public class PasswordCredentialValidatorCreator  extends BaseCreator{
	public PasswordCredentialValidatorCreator(Core core) {
		super(core);
		
	}
	
	public  PasswordCredentialValidator createPasswordCredentialValidator(
			String ldapdsId, String id, String name, String filter) {
		core.clearTransformers();
		
		PasswordCredentialValidatorsApi api= new PasswordCredentialValidatorsApi(core.getApiClient());
		PasswordCredentialValidator pcv= new PasswordCredentialValidator();
		pcv.setName(name);
		pcv.setId(id);
		ResourceLink pluginDescriptorRef = new ResourceLink();
		pcv.setPluginDescriptorRef(pluginDescriptorRef);
		pluginDescriptorRef.setId("org.sourceid.saml20.domain.LDAPUsernamePasswordCredentialValidator");
		ConfigField  field= new ConfigField ();
		field.setName("LDAP Datastore");
		field.setValue(ldapdsId);
		PluginConfiguration configuration = new PluginConfiguration();
		configuration.addFieldsItem(field);
		 field= new ConfigField ();
			field.setName("Search Base");
			field.setValue("ou=People,dc=example,dc=com");
			configuration.addFieldsItem(field);
			 field= new ConfigField ();
				field.setName("Search Filter");
				field.setValue(filter);
				configuration.addFieldsItem(field);
				field= new ConfigField ();
				field.setName("Trim Username Spaces For Search");
				field.setValue("true");
				configuration.addFieldsItem(field);
				field= new ConfigField ();
				field.setName("Enable PingDirectory Detailed Password Policy Requirement Messaging");
				field.setValue("true");
				configuration.addFieldsItem(field);
				pcv.setConfiguration(configuration);
		PasswordCredentialValidator createPasswordCredentialValidator = api.createPasswordCredentialValidator(pcv);
		
		return createPasswordCredentialValidator;
	}

}
