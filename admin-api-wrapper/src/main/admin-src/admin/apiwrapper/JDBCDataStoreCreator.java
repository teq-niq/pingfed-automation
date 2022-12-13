package admin.apiwrapper;

import java.util.ArrayList;
import java.util.List;

import com.example.pingfedadmin.api.*;
import com.example.pingfedadmin.model.*;

import com.example.pingfedadmin.model.DataStore.TypeEnum;

import admin.TransformBean;
import admin.apiwrapper.core.Core;

public class JDBCDataStoreCreator extends BaseCreator{
	public JDBCDataStoreCreator(Core core) {
		super(core);
		
	}
	
	public  DataStore createJdbcDataStore(String id, String name, String url, String username, String password) {
		core.setRequestTransformBeans(new TransformBean("type",type->TypeEnum.JDBC.name()));
		core.setResponseTransformBeans(new TransformBean("type",type->type.charAt(0)+type.substring(1).toLowerCase()+"DataStore"));
		DataStoresApi api= new DataStoresApi(core.getApiClient());
		JdbcDataStore ds= new JdbcDataStore();
		ds.setId(id);
		ds.setName(name);
		ds.setType(TypeEnum.JDBC);
		ds.setMaskAttributeValues(false);
		ds.setConnectionUrl(url);
		ds.setDriverClass("com.mysql.cj.jdbc.Driver");
		ds.setUserName(username);
		ds.setPassword(password);
		ds.setValidateConnectionSql("SELECT 1 from dual");
		ds.setAllowMultiValueAttributes(true);
		List<JdbcTagConfig> tagConfigs= new ArrayList<>();
//		JdbcTagConfig c1= new JdbcTagConfig();
//		c1.setConnectionUrl(url);
//		c1.setDefaultSource(true);
//		tagConfigs.add(c1);
		ds.setConnectionUrlTags(tagConfigs);
		
		ds.setMinPoolSize(10);
		ds.setMaxPoolSize(100);
		ds.setBlockingTimeout(5000);
		ds.setIdleTimeout(5);
		
		
		return api.createDataStore(ds, false);
	}

	

}
