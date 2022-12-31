package com.example.config;



/*
 * imagine its loaded from config file
 */
public class CurrentSettings {
	
	private final static  Settings localAuthorizatioonCode1=new Settings("https://localhost:9031/.well-known/openid-configuration", 
			AutomationSharedConstants.AuthCodeClientId, 
			AutomationSharedConstants.AuthCodeClientSecret,
			"user.2").introspect();
	private final static  Settings localAuthorizatioonCode2=new Settings("https://localhost:9031/.well-known/openid-configuration", 
			AutomationSharedConstants.AuthCodeClientId, 
			AutomationSharedConstants.AuthCodeClientSecret,
			null);


	
	private final static  Settings localClientCredentials=new Settings("https://localhost:9031/.well-known/openid-configuration", 
	"manual2", 
	"secret");
	
	
	
	public final static Settings [] authorizationCodeDefaultSettings =new Settings[] {
			localAuthorizatioonCode1, localAuthorizatioonCode2};
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
