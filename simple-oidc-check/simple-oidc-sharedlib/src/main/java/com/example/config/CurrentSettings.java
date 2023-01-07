package com.example.config;

import com.example.config.constants.AutomationSharedConstants;

/*
 * imagine its loaded from config file
 */
public class CurrentSettings {
	
	private final static  Settings localAuthorizatioonCode1=new Settings("https://"+AutomationSharedConstants.HOSTNAME+":9031/.well-known/openid-configuration", 
			AutomationSharedConstants.AuthCodeClientId, 
			AutomationSharedConstants.AuthCodeClientSecret,
			"user.2").introspect().lenientNonceOnMissingId().scopes("openid", "email", "foo", "bar");
			//will prompt userid
			//will use intrspection endpoint
			//if id token is not permitted will not fail login
	
	private final static  Settings localAuthorizatioonCode2=new Settings("https://"+AutomationSharedConstants.HOSTNAME+":9031/.well-known/openid-configuration", 
			AutomationSharedConstants.AuthCodeClientId, 
			AutomationSharedConstants.AuthCodeClientSecret,
			null).scopes("openid", "email", "foo", "bar");
			//no prompting of userId. wont introspect. 
			//if id token is not permitted will fail login because nonce cant be verified
	
	private final static  Settings googleAuthorizationCodeSettings=new Settings("https://accounts.google.com/.well-known/openid-configuration", 
			"replace.with.actual.google.client.id", //replace
			"replace.with.actual.google.client.secret", //replace
			null).lenientNonceOnMissingId().opaqueAccessToken().scopes("openid", "email", "profile", 
					"https://www.googleapis.com/auth/cloud-billing.readonly")
			.scopeTranslator((String input)->{
				String ret=null;
				if(input!=null)
				{
					String prefix="https://www.googleapis.com/auth/userinfo.";
					if(input.startsWith(prefix))
					{
						ret=input.substring(prefix.length());
					}
					else
					{
						ret=input;
					}
				}
				return ret;
			});

	
	private final static  Settings localClientCredentials=new Settings("https://"+AutomationSharedConstants.HOSTNAME+":9031/.well-known/openid-configuration", 
	"manual2", 
	"secret");
	
	
	
	public final static Settings [] authorizationCodeDefaultSettings =new Settings[] 
	{
			localAuthorizatioonCode1, localAuthorizatioonCode2
			// uncomment below googleAuthorizationCodeSettings after configuring its clientId and clientSecret in the Settings object.
			//, googleAuthorizationCodeSettings
	};
	public final static Settings[]  clientCredentialsDefaultSettings = new Settings[] {
			
			localClientCredentials
			};


	

	/*
	 * Have this index() called from any one servlets init
	 */
	public static void index()
	{
		for (int i = 0; i < authorizationCodeDefaultSettings.length; i++) 
		{
			Settings settings=authorizationCodeDefaultSettings[i];
			settings.setIndex(i);
			
		}
		for (int i = 0; i < clientCredentialsDefaultSettings.length; i++) 
		{
			Settings settings=clientCredentialsDefaultSettings[i];
			settings.setIndex(i);
			
		}
		
	}

	

}
