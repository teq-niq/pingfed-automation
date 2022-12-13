package oidc.check.others;

public class CurrentSettings {
	private final static  Settings localAuthorizatioonCode1=new Settings("https://localhost:9031/.well-known/openid-configuration", 
			"manual1", 
			"secret",
			"user.2");

	
	private final static  Settings localClientCredentials=new Settings("https://localhost:9031/.well-known/openid-configuration", 
	"manual2", 
	"secret");
	
	
	
	public final static Settings [] authorizationCodeDefaultSettings =new Settings[] {
			localAuthorizatioonCode1};
	public final static Settings[]  clientCredentialsDefaultSettings = new Settings[] {
			
			localClientCredentials
			};


	

	

	

}
