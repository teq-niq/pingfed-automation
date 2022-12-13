package admin.apiwrapper;

import com.example.pingfedadmin.api.*;
import com.example.pingfedadmin.model.*;

import admin.apiwrapper.core.Core;

public class ScopesCreator extends BaseCreator {
	public ScopesCreator(Core core) {
		super(core);

	}

	public void addScopes(String... scopes) {
		core.clearTransformers();
		OauthauthServerSettingsApi api = new OauthauthServerSettingsApi(core.getApiClient());
		for (String scope : scopes) {
			addScope(api, scope, scope);

		}

	}

	private static void addScope(OauthauthServerSettingsApi api, String name, String desc) {
		ScopeEntry body = new ScopeEntry();
		body.setName(name);
		body.setDescription(desc);
		body.setDynamic(false);
		api.addCommonScope(body);
	}

}
