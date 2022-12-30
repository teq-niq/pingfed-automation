# PingFed Automation Setup steps
## Prerequisites 
- Java JDK 11
- JAVA_HOME environment variable should be correctly setup.
- Java, Maven and Ant should be configured in Path environment variable.
- A running mysql with root user credentials to enable creation of a user,  schema and tables in the mysql for use by pingfederate.
- Ensure ports 9999, 9031, 8080 are available and not in use before proceeding.  

## The steps
Clone the project from - here to a suitable folder in your machine.  
Read pingfed-automation\downloads\downloadnotes.txt.   
Download the files as mentioned here into "pingfed-automation\downloads" folder.  
In command prompt/terminal navigate to pingfed-automation folder.  
Run “mvn clean package” in command prompt.  
![build](images/build.png)  
Wait for it to finish successfully.  
![buildresult](images/buildresult.png)   
In command prompt/terminal navigate to "pingfed-automation/verify-downloads" folder.  
Run "verifydownloads.bat" or "verifydownloads.sh".
I got this output.
![verifydownloadresult](images/verifydownloads.png)  
In command prompt/terminal navigate to 
"pingfed-automation/win/" or "pingfed-automation/linux"  

```diff
! Impotant Note: Before proceeding ensure that mysql is running and reachable.  
! pingfed-automation/mysql.properties file entries should match the expectations.  
+ Edit pingfed-automation\mysql.properties as needed.  
```
  

Run “ant”  
![setup](images/setup.png)  
Result should look like this:  
![setuprun-part1](images/setuprun-part1.png)   
![setuprun-part2](images/setuprun-part2.png) 
That should setup pinfederate.
#### Start PingDirectory
Run “ant start-ds”  
![start ds](images/start_ds.png)   
Result should look like this:  
![start ds result](images/start_ds_result.png) 
Note: Via ant just starting the Ping directory. 
PingDirectory can also be started by launching: start-server.bat or start-server.sh found in bin/bat folder of the Ping Directory.
#### Start Ping Federate
Run “ant start-pingfed”  
![start ping fed](images/start_pingfed.png)   
Result should look like this:  
![start png fed result](images/start_pingfed_result.png) 
Note: Via ant just starting the Ping Federate. Also capturing the process id.  
Ping Federate can also be started by launching: run.bat or run.sh found in bin folder of the Ping Federate.  
#### Use Ping Federate Admin Console first time
I am using chrome browser. Should possibly work well in other browsers too.  
Visit https://localhost:9999/  
![chrome-step1](images/chrome-step1.png)  
You might get a message "Your connection is not private" as shown above.
If so press Advanced button
![chrome-step2](images/chrome-step2.png)  
Click on the proceed to localhost link.  
Note: Its possible to configure SSL. Not covering that here.  
We should be seeing this.  
![chrome-step3](images/chrome-step3.png)  
Check the checbox and press Next button.  
![chrome-step4](images/chrome-step4.png)  
I left the base url at "https://localhost:9031" for now. It can be modified later too. Press Next.  
![chrome-step5](images/chrome-step5.png)   
Do nothing. Press Next.   
![chrome-step6](images/chrome-step6.png)  
Press the choose file button. Navigate to the ping federate license file in pingfed-automation/downloads.  
Select it.  
![chrome-step7](images/chrome-step7.png)  
Press Next.  
![chrome-step8](images/chrome-step8.png)   

```diff
! Retain the default. For password I fed "Admin@123" without the quotes.  
+ Ensure this matches with pingfed-automation\admin-api-wrapper\pingfed.api.properties file contents.  
```

Note: Its possible to create additional users for use with pingfed  api.
However keeping it simple.  
Prss Next.  
![chrome-step9](images/chrome-step9.png)   
Do nothing. Press Finish.   

![chrome-step10](images/chrome-step10.png)   
#### swagger.json
Visit  https://localhost:9999/pf-admin-api/v1/swagger.json
Copy its contents into the file- pingfed-automation\admin-api-wrapper\swagger-json\swagger.json.   
This step has already been done if you are on version pingfederate-11.2.0.  
If your pingfederate version is higher do please update the file content here.  
#### Swagger Code generation
In command prompt/terminal visit folder - "pingfed-automation/admin-api-wrapper".
Run "mvn clean package -P admin".    
![codegen](images/codegen.png)  
Result should look like this:   
![codegen result](images/codegen-result.png)  
Staying in same location in command prompt Run "java -jar target/admin-api-wrapper.jar admin.Main".  
![Automated ping fed configuration](images/automated_pingfed_config.png)
Visit https://localhost:9999/ and verify the results.  
Lets quickly verify if this was done correctly or not.  
#### Verify the automated configuration
In command prompt or terminal navigate to pingfed-automation\simple-oidc-check folder.  
simple-oidc-check is a maven project. Its also a submodule of pingfed-automation.  
It should be already built.  
Its a simple servlet based project.  
We are going to use this project to verify if we are able to obtain some access tokens from pingfederate.  
In command prompt or terminal after navigating to pingfed-automation\simple-oidc-check folder run "ant".
![in simple oic check run ant](images/simple_oidc_check.png)   
Build should show up like this.  
![in simple oic check run ant result](images/simple_oidc_check_ant_res.png)  
In case of difficulty edit tomcat.ver property in the build.xml file.  
Navigate into build/apache-tomcat-${tomcat.ver}/bin folder and run startup.bat or startup.sh.  
Once this tomcat has started pls visit http://localhost:8080/   
![Localhost 8080](images/localhost_8080.png)   
In case you see messages of could not connect please ensure pingdirectory and pingfederate is running.    
- **Click on the "Protected..." link**.   
We have two configurations for essentially same oidc server.  
So in next screen it will prompt for selecting the oidc server.    
![oidc select](images/oidcselect.png)  
This will start and verify the authorization code flow.  
![Login](images/authorization_code1.png)  
Note: Use "password" for password
Then  
![Approval Request](images/authorization_code2.png)   
Next screen should be this.  
![Reached Protected](images/protected.png) 
Look at the console to see the acces token and other details.  
This above application demonstrates the **authorisation code flow**.  
The improvment areas are :  
1 Caching of jwks and the introspection.  
2 use of refresh token.  

- **Click on the "Try" link**.  
It should take you to another access token via client credentials grant flow.  
If all worked correctly congrats.

#### Stop Ping Federate
Run “ant stop-pingfed”  
![stop ping fed](images/stop_pingfed.png)   
Result should look like this:  
![stop png fed result](images/stop_pingfed_result.png) 
Note: Via ant just stopping the Ping Federate by killing the process.  
Relying on the process id noted earlier.

#### Stop PingDirectory
Run “ant stop-ds”  
![stop ds](images/stop_ds.png)   
Result should look like this:  
![stop ds result](images/stop_ds_result.png) 
Note: Via ant just stopping the Ping directory. 
PingDirectory can also be stopped by launching: stop-server.bat or stop-server.sh found in bin/bat folder of the Ping Directory.


#### Undo the Setup If and when needed
Run “ant clean”  
![undosetup](images/undosetup.png) 
Result should be like this.  
![undosetupresult](images/undosetupresult.png) 
Note: Before running ant clean ensure that pingfederate and pingdirectory are stopped.
Also Note: Can again setup by running "ant".
