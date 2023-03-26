# PingFed Automation Setup steps
## Prerequisites 
- Java JDK 17
- JAVA_HOME environment variable should be correctly setup.
- Java, Maven and Ant should be configured in Path environment variable.
- A running mysql with root user credentials to enable creation of a user,  schema and tables in the mysql for use by pingfederate.
- Ensure ports 9999, 9031, 8080, 8081 are available and not in use before proceeding.  


## The steps
Clone the project from - here to a suitable folder in your machine.  
git clone -b main https://github.com/teq-niq/pingfed-automation.git  
Read pingfed-automation\downloads\downloadnotes.txt.   
<ins>Download the files as mentioned here into **"pingfed-automation\downloads"** folder.</ins>    
In command prompt/terminal navigate to pingfed-automation folder.  
Run “mvn clean package” in command prompt.  
<img width="420" alt="build" src="https://user-images.githubusercontent.com/14346578/210153578-7bab1333-c9c4-441c-841f-3636dcab39b4.png">  
Wait for it to finish successfully.  
<img width="447" alt="buildresult" src="https://user-images.githubusercontent.com/14346578/210246321-280ce33f-fa8a-4448-8826-3ef2efbb6475.png">    
In command prompt/terminal navigate to "pingfed-automation/verify-downloads" folder.  
Run "verifydownloads.bat" or "verifydownloads.sh".
Might need to use "sudo chmod +x verifydownloads.sh" on linux.   
Might need to use "sudo ./verifydownloads.sh" on linux.  
I got this output.  
<img width="666" alt="verifydownloads" src="https://user-images.githubusercontent.com/14346578/210153720-536f2a35-603e-4d78-902e-d632e81bd244.png">   
In command prompt/terminal navigate to 
"pingfed-automation/win/" or "pingfed-automation/linux"  

```diff
! Impotant Note: Before proceeding ensure that mysql is running and reachable.  
! pingfed-automation/mysql.properties file entries should match the expectations.  
+ Edit pingfed-automation\mysql.properties as needed.  
```
**Impotant Note:**
*Before proceeding ensure that mysql is running and reachable.   
pingfed-automation/mysql.properties file entries should match the expectations.  
Edit pingfed-automation\mysql.properties as needed.*   

Run “ant”.   
<ins>On linux might need to use  “sudo ant”.</ins>   
In linux sometimes sudo ant will report "sudo: ant: command not found".
If so please add below line in your .bashrc and source it.  
- alias sudo='sudo env PATH=$PATH $@'   
After adding above line run below.    
- source ~/.bashrc
   
<img width="369" alt="setup" src="https://user-images.githubusercontent.com/14346578/210153762-a663526e-9900-436c-a07b-e9686014f10c.png">    

Result should look like this:   
<img width="588" alt="setuprun-part1" src="https://user-images.githubusercontent.com/14346578/210153791-e6e8662f-7c01-4c9d-a463-35c7a918beeb.png">  
<img width="604" alt="setuprun-part2" src="https://user-images.githubusercontent.com/14346578/210153798-08e1a56d-8665-4da0-b979-9a07d718113c.png">  
That should setup pinfederate.
#### Start PingDirectory
Run "ant start-ds". On linux use "sudo ant start-ds".     
<img width="412" alt="start_ds" src="https://user-images.githubusercontent.com/14346578/210153869-5818bb83-99a6-4303-b7be-681b0aefbaa6.png">  
Result should look like this:  
<img width="567" alt="start_ds_result" src="https://user-images.githubusercontent.com/14346578/210153897-455b13eb-9616-400b-ab68-6e7342014b28.png">  
Note: Via ant just starting the Ping directory. 
PingDirectory can also be started by launching: start-server.bat or start-server.sh found in bin/bat folder of the Ping Directory.
#### Start Ping Federate
Run “ant start-pingfed”. On linux use "sudo ant start-pingfed".    
<img width="447" alt="start_pingfed" src="https://user-images.githubusercontent.com/14346578/210153941-6f95a083-fcef-49a1-ba47-553bc1f2501f.png">  
Result should look like this:  
<img width="567" alt="start_pingfed_result" src="https://user-images.githubusercontent.com/14346578/210153987-0c61dd67-3979-4893-8dea-cd6da4f9e2be.png">  
Note: Via ant just starting the Ping Federate. Also capturing the process id in case of windows.  
Ping Federate can also be started by launching: run.bat or run.sh found in bin folder of the Ping Federate.  
#### Use Ping Federate Admin Console first time
I am using chrome browser. Should possibly work well in other browsers too.  
Visit https://localhost:9999/  
<img width="331" alt="chrome-step1" src="https://user-images.githubusercontent.com/14346578/210154004-f23aa91a-5d27-489b-bd44-6723c2abdfdd.png">   
You might get a message "Your connection is not private" as shown above.
If so press Advanced button   
<img width="304" alt="chrome-step2" src="https://user-images.githubusercontent.com/14346578/210154017-619af26b-0aaf-47bf-aca3-0bd60220f4a0.png">   
Click on the proceed to localhost link.  
Note: Its possible to configure SSL better. Not covering that here.  
We should be seeing this.  
<img width="238" alt="chrome-step3" src="https://user-images.githubusercontent.com/14346578/210154036-12a351e2-8268-4dca-ba99-dae23be55404.png">   
Check the checkbox and press Next button.  
<img width="202" alt="chrome-step4" src="https://user-images.githubusercontent.com/14346578/210154054-3e9fac8e-a11a-4623-98b3-196113481494.png">   
I left the base url at "https://localhost:9031" for now. It can be modified later too. Press Next.  
<img width="210" alt="chrome-step5" src="https://user-images.githubusercontent.com/14346578/210154087-50000fc4-a05b-436f-ab7a-8ba048026cb3.png">   
Do nothing. Just press Next.  For now avoid the check box encouraging you to connect to a PingOne Account.   
<img width="191" alt="chrome-step6" src="https://user-images.githubusercontent.com/14346578/210154114-2e68c868-ef0a-4613-aa85-49eef5eaa1fa.png">    
Press the choose file button. Navigate to the ping federate license file in pingfed-automation/downloads.  
Select it.  
<img width="187" alt="chrome-step7" src="https://user-images.githubusercontent.com/14346578/210154133-98b39ca4-943b-42c3-8418-1d7fb8916192.png">    
Press Next.  
<img width="195" alt="chrome-step8" src="https://user-images.githubusercontent.com/14346578/210154154-0e835e6a-410b-4dc9-9801-840c7b695b07.png">   

```diff
! Impotant Note: Retain the default. For password I fed "Admin@123" without the quotes.  
+ Ensure this matches with pingfed-automation\admin-api-wrapper\pingfed.api.properties file contents.  
```

**Impotant Note:**
*Retain the default. For password I fed "Admin@123" without the quotes.  
Ensure this matches with pingfed-automation\admin-api-wrapper\pingfed.api.properties file contents.*  

**Note:** Its possible to create additional users for use with pingfed  api.
However keeping it simple.  
Prss Next.  
<img width="191" alt="chrome-step9" src="https://user-images.githubusercontent.com/14346578/210154177-d42d869e-7b19-4977-a850-d9d6cf2f4c57.png">   
Do nothing. Press Finish.   

<img width="360" alt="chrome-step10" src="https://user-images.githubusercontent.com/14346578/210154194-10dba6f6-92a5-4ce7-aaa8-3db94e8257e5.png">   

#### swagger.json

This immediate next step has already been done if you are on version pingfederate-11.2.3.  
If your pingfederate version is higher do please update the file content here by following below 2 steps.  
 
- Visit  https://localhost:9999/pf-admin-api/v1/swagger.json
- Copy its contents into the file- pingfed-automation\admin-api-wrapper\swagger-json\swagger.json.   
  
#### Swagger Code generation
In command prompt/terminal visit folder - "pingfed-automation".
Run "mvn clean package -P admin".    
<img width="259" alt="codegen" src="https://user-images.githubusercontent.com/14346578/210247771-ee8abbce-3689-4c2f-9533-96f49850192f.png">       
Result should look like this:   
<img width="447" alt="buildresult" src="https://user-images.githubusercontent.com/14346578/210246321-280ce33f-fa8a-4448-8826-3ef2efbb6475.png">    
This time because we used the admin profile during maven build there has also been some code generation.   
Now navigate to "pingfed-automation\admin-api-wrapper" in command prompt.  
Run "java -jar target/admin-api-wrapper.jar admin.Main".  
<img width="697" alt="automated_pingfed_config" src="https://user-images.githubusercontent.com/14346578/210154285-6e0b6552-7a5f-4e3f-b35a-7e3d76e7be78.png">    
Output should look like this:   
<img width="681" alt="automated_pingfed_config_result" src="https://user-images.githubusercontent.com/14346578/210248610-0e0ce7c5-044e-4642-9a71-0351ced971cd.png">   
If you understand pingfederate configuration details - visit https://localhost:9999/ and verify the results in the admin console.  
Now lets quickly proceed and verify if this was done correctly or not.  
#### Verify the automated configuration
There are two simple example projects which can be used to verify that the automated pingfed configuration worked.   
These examples can be easily run on a desktop machine using localhost.
They are listed here:    
- pingfed-automation\oidc-check\simple-oidc-check and  
- pingfed-automation\oidc-check\springboot.oidc.with.angular

simple-oidc-check - is a roll your own example code project where the demo is done without using any library/framework.   

springboot.oidc.with.angular - here the demo is done using a spring boot angular code authorization code example.  

For simple-oidc-check please see- [simple-oidc-check](oidc-check/simple-oidc-check/README.md)  
For springboot.oidc.with.angular please see- [springboot-angular-oidc-check](oidc-check/springboot.oidc.with.angular/README.md) 


#### Stop Ping Federate
Run “ant stop-pingfed”  
<img width="445" alt="stop_pingfed" src="https://user-images.githubusercontent.com/14346578/210154520-4fc91e8f-e101-4c87-aa32-da37620068f6.png">   
Result should look like this:  
<img width="559" alt="stop_pingfed_result" src="https://user-images.githubusercontent.com/14346578/210154537-1dbc438a-e924-439c-a747-dee800748120.png">    
Note: Via ant just stopping the Ping Federate by killing the process.  
Relying on the process id noted earlier in case of windows. In case of other environments just detecting processes that are running PingFederate and stopping them.    

#### Stop PingDirectory
Run “ant stop-ds”  
<img width="420" alt="stop_ds" src="https://user-images.githubusercontent.com/14346578/210154564-2cc58c7c-ec94-4a5d-8aec-4b992a91ffcc.png">   
Result should look like this:  

<img width="460" alt="stop_ds_result" src="https://user-images.githubusercontent.com/14346578/210154578-346c747a-ecb8-486c-b511-05383ba42804.png">   
Note: Via ant just stopping the Ping directory. 
PingDirectory can also be stopped by launching: stop-server.bat or stop-server.sh found in bin/bat folder of the Ping Directory.


#### Undo the Setup If and when needed
Run “ant clean”  
<img width="395" alt="undosetup" src="https://user-images.githubusercontent.com/14346578/210154619-18f6954c-14d5-430a-ad50-0bef0b54dda0.png">   
Result should be like this.  
<img width="538" alt="undosetupresult" src="https://user-images.githubusercontent.com/14346578/210154640-4aec73e8-c85b-4c2c-ac3e-f0059855319b.png">   
Note: Before running "ant clean" ensure that pingfederate and pingdirectory are stopped.
Also Note: Can again setup by running "ant".


#### Trouble shooting
In linux sometimes sudo ant will report "sudo: ant: command not found".
If so please add below line in your .bashrc and source it.
- alias sudo='sudo env PATH=$PATH $@'   
After adding above line run below.    
- source ~/.bashrc    

I did something wrong. How do I start again?   
- Run ant stop-pingfed if its running.   
- Run ant stop-ds if its running   
- Run ant clean.   
- Worst case scenario restart the machine.   
- Run ant clean   
- After ant clean assuming pingfed-automation\downloads folder has the needed files. Start again with "ant setup".  

