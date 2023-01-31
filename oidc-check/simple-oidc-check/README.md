## Setup for simple-oidc-check:

In command prompt or terminal navigate to pingfed-automation\oidc-check\simple-oidc-check folder.  
simple-oidc-check is a maven project. Its also a submodule of pingfed-automation.  
It should have been already built.  
Its a simple servlet based project.  
We are going to use this project to verify if we are able to obtain some access tokens from pingfederate.  
In command prompt or terminal after navigating to pingfed-automation\oidc-check\simple-oidc-check folder run "ant".  
On linux machines use "sudo ant".  
<img width="451" alt="simple_oidc_check" src="https://user-images.githubusercontent.com/14346578/210154307-7414149b-75b0-452e-854b-3817de1ab6a8.png">   
Build should show up like this.  
<img width="591" alt="simple_oidc_check_ant_res" src="https://user-images.githubusercontent.com/14346578/210154342-1505f797-f26b-4b6f-9b5a-5d2e4b184791.png">   
In case of difficulty edit tomcat.ver property in the build.xml file.  
##### Start Tomat
Navigate into build/apache-tomcat-${tomcat.ver}/bin folder and run startup.bat or startup.sh. 
You can also start and stop the tomcat using the ant targets as shown here.  
In command prompt or terminal after navigating to pingfed-automation\oidc-check\simple-oidc-check folder run "ant start-tomcat".   
On linux may have to do this extra step - "sudo chmod +x build/apache-tomcat-10.0.18/bin/startup.sh".  
On linux may have to do this extra step - "sudo chmod +x build/apache-tomcat-10.0.18/bin/catalina.sh".  
On linux may have to do this extra step - "sudo chmod +x build/apache-tomcat-10.0.18/bin/shutdown.sh".  
Then on linux use "sudo ant start-tomcat".   

<img width="298" alt="start_tomcat" src="https://user-images.githubusercontent.com/14346578/210154363-b207100c-81d1-46e1-aeb5-1deb86f8f965.png">   
This should result in  
<img width="773" alt="start_tomcat_result" src="https://user-images.githubusercontent.com/14346578/210154374-9e545fc6-3192-4b73-9027-aa34bdd37878.png">     

Once this tomcat has started pls visit http://localhost:8080/   
<img width="425" alt="localhost_8080" src="https://user-images.githubusercontent.com/14346578/210154396-63c1508f-f011-47c5-a1bb-ecc9cea5f052.png">   
In case you see messages of could not connect please ensure pingdirectory and pingfederate is running.    
##### Click on the "Protected...- link -(Authorization code grant flow).   
We have two configurations for essentially same oidc server.    
So in next screen it will prompt for selecting the oidc server.    
<img width="355" alt="oidcselect" src="https://user-images.githubusercontent.com/14346578/210154416-f46fc3c8-7d08-4ef6-9753-7a0f7568016d.png">   
Click on the first "Start" link. This will start and verify the authorization code flow.  
<img width="255" alt="authorization_code1" src="https://user-images.githubusercontent.com/14346578/210154455-1b3749f0-fc34-4918-a371-d856a2e7cc9a.png">    
Note: Use "password" for password
Then  
<img width="641" alt="authorization_code2" src="https://user-images.githubusercontent.com/14346578/210169644-0929666f-7265-436e-9667-0a35b82f3060.png">    
Uncheck foo scope item.   
<img width="586" alt="authorization_code3" src="https://user-images.githubusercontent.com/14346578/210169650-98df70d9-5ca6-45f4-8366-cb8462f395fd.png">    
Press Allow button.  
Next screen should be this.  
<img width="270" alt="protected" src="https://user-images.githubusercontent.com/14346578/210169662-38c982b7-b63e-45c5-a150-94af44ceb6c4.png">   
Note: As can be seen in above screen in our authorization code sample for convenience treating scope as the user roles.  Thats why it says false against foo. But true against bar.   
Also Note: Security can be applied against scope and also roles treating both as different concepts.  
Look at the console to see the access token and other details.  
This above application demonstrates the **authorisation code flow**.  
The improvment areas are :  
- Caching of jwks and the introspection.  
- use of refresh token.  
Could be done in different ways.  Steering away from it for now.   

##### Click on the "Try" link -(Client credentials grant flow).  
It should take you to another access token via client credentials grant flow.  
If all worked correctly congrats.


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
 - Note: In java spring world it is generally recommended to use spring security for implementing the Authorization code flow.  Lots of articles exist on that topic.  Also providing a springboot angular demo in this repository.  