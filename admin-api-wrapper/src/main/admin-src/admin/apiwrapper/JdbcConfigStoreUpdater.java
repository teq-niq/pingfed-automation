package admin.apiwrapper;

import com.example.pingfedadmin.api.*;
import com.example.pingfedadmin.model.*;

import admin.apiwrapper.core.Core;

public class JdbcConfigStoreUpdater extends BaseCreator{
	public JdbcConfigStoreUpdater(Core core) {
		super(core);
		
	}
	
	public  void updateClientManagerJdbcConfigStore(String settingValue) {
		core.clearTransformers();
		ConfigStoreApi api= new ConfigStoreApi(core.getApiClient());
		ConfigStoreSetting setting= new ConfigStoreSetting();
		setting.setId("PingFederateDSJNDIName");
		setting.setType(ConfigStoreSetting.TypeEnum.STRING);
		setting.setStringValue(settingValue);
		api.updateSetting("org.sourceid.oauth20.domain.ClientManagerJdbcImpl", "PingFederateDSJNDIName", setting);
	}

}
