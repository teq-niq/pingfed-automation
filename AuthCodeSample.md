# Authorization Code Flow Sample  

## About:
 - Its a simple sample demonstration of Authorization code flow.  
 - It uses tomcat security and a custom realm underneath.  
 - It does not rely on HttpSessions but the JEE container security.  
 - For convenience treating scope as the user roles.  
 - Security can be applied against scope and also roles treating both as different concepts.  
 - The improvment areas are : 
	- Caching of jwks and the introspection.  
	- use of refresh token.  
 	- Could be done in different ways.  Steering away from it for now.  
 - Similar implementations should be possible for rest apis also using same logic. 
 - This implementation sample should also be compatible with other OIDC providers. Not doing anything specific to ping federate in this sample.  
 - Keep in mind this is is a working implementation to help demonstrate and understand Authorization code flow. Also used it to test the pingfederate setup.    
 - Could not find another similar tomcat based demo except - https://github.com/boylesoftware/tomcat-oidcauth. Unlike this project we are not using HttpSessions in our demo.  
 - Similar implementations should be possible for application containers other than tomcat using same logic. Could even implement same approach in spring if needed. 
 - Note: In java spring world it is generally recommended to use spring security for implementing the Authorization code flow.  Lots of articles exist on that topic.  
 
## Screenshots:
See in  [Setup Steps](Setup.md#start-tomat) for the sample application screen shots at the end of the setup steps.  
