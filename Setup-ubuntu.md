[<img src="images/grey-win.png">](Setup-win.md)[<img src="images/white-ubuntu.png">](Setup-ubuntu.md) 

# PingFed Automation Setup steps

## Prerequisites 
- Java JDK 17  
- JAVA_HOME environment variable should be correctly setup.
- Java,  Path environment variable.   
- A running mysql with root user credentials to enable creation of a user,  schema and tables in the mysql for use by pingfederate.  
- Ensure ports 9999, 9031, 8080, 8081 are available and not in use before proceeding.  
- Internet access  
- Should be able to launch gradlew(.bat) included in the project. 


## The steps

Note:  For linux I used a ubuntu desktop I had set up on the cloud.   
Clone the project from - here to a suitable folder in your machine.  
git clone -b main https://github.com/teq-niq/pingfed-automation.git  
Read pingfed-automation\downloads\downloadnotes.txt.   
<ins>Download the files as mentioned here into **"pingfed-automation\downloads"** folder.</ins>    
In command prompt/terminal navigate to pingfed-automation folder.   
**For convenience We will run most of our commands from this location only.**  
On linux do this first.
Run sudo chmod +x ./gradlew   
![chmod on gradlew](https://github.com/teq-niq/pingfed-automation/assets/14346578/8a5409f7-433f-44b9-ac55-1573f33038b5)  

Run  "sudo ./gradlew clean build" in linux terminal.  
This might take some time on first run.  
<img width="420" alt="build" src="https://github.com/teq-niq/pingfed-automation/assets/14346578/6ad82884-161b-459c-acfb-334b5ae92d02">  
Wait for it to finish successfully.  
<img width="447" alt="buildresult" src="https://github.com/teq-niq/pingfed-automation/assets/14346578/ed43be9d-79ed-4be7-a434-2193f0ac346e">    

Run "sudo ./gradlew verify-downloads".  
![run verify downloads](https://github.com/teq-niq/pingfed-automation/assets/14346578/ad704765-f964-4b95-b6ef-93dbc5ea7af9)   

I got this output.  
<img width="666" alt="verifydownloads" src="https://github.com/teq-niq/pingfed-automation/assets/14346578/8e38acf5-1251-4d80-985b-29ad72978092">   
 

```diff
! Impotant Note: Before proceeding ensure that mysql is running and reachable.  
! pingfed-automation/mysql.properties file entries should match the expectations.  
+ Edit pingfed-automation\mysql.properties as needed.  
```
**Impotant Note:**
*Before proceeding ensure that mysql is running and reachable.   
pingfed-automation/mysql.properties file entries should match the expectations.  
Edit pingfed-automation\mysql.properties as needed.*   

Run “sudo ./gradlew ping-setup”.  
   
<img width="369" alt="setup" src="https://github.com/teq-niq/pingfed-automation/assets/14346578/698f6158-b581-45a1-b4f1-5b9fdf250518">     

Result should look like this:   

<img width="604" alt="setuprun-part2" src="https://github.com/teq-niq/pingfed-automation/assets/14346578/d3557187-5b1d-442c-8a06-4f3750ac0fe1">    
 
That should setup pinfederate.
#### Start PingDirectory
Run "sudo ./gradlew ping-start-ds".     
<img width="412" alt="start_ds" src="https://github.com/teq-niq/pingfed-automation/assets/14346578/1cc52e58-6921-4409-9753-a2ae91e26385">  
Result should look like this:  
<img width="567" alt="start_ds_result" src="https://github.com/teq-niq/pingfed-automation/assets/14346578/29c66baf-0261-4b06-8208-4c2e5576591e">  
Note: 
PingDirectory can also be started by launching: start-server.bat or start-server.sh found in bin/bat folder of the Ping Directory.
#### Start Ping Federate
Run “sudo ./gradlew ping-start-pingfed”.      
<img width="447" alt="start_pingfed" src="https://github.com/teq-niq/pingfed-automation/assets/14346578/ebee3d13-4397-4db0-b946-0ad8ab03d141">  
Result should look like this:  
<img width="567" alt="start_pingfed_result" src="https://github.com/teq-niq/pingfed-automation/assets/14346578/4b410b48-656f-40d7-a89e-e99c579edfe4">  
Note: Just starting the Ping Federate. 
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

This immediate next step has already been done if you are on version pingfederate-12.2.0.  
If your pingfederate version is higher do please update the file content here by following below 2 steps.  
 
- Visit  https://localhost:9999/pf-admin-api/v1/swagger.json
- Copy its contents into the file- pingfed-automation\admin-api-wrapper\swagger-json\swagger.json.   
  
#### Swagger Code generation
Run "sudo ./gradlew clean build :admin-api-wrapper:auto-administer-pingfed -P buildProfile=admin".    
<img width="600" alt="codegen" src="https://github.com/teq-niq/pingfed-automation/assets/14346578/99a1c2e4-ebe1-4edc-b763-6d4347b0cf7b">       
Result should look like this:   
<img width="447" alt="buildresult" src="https://github.com/teq-niq/pingfed-automation/assets/14346578/44ef108b-0e06-4bd4-9fb0-88980c66851f">    
This time because we used the admin profile during gradle build there has also been some code generation and automated pingfed configuration.     
  
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

For simple-oidc-check please see- [simple-oidc-check](oidc-check/simple-oidc-check/README_ubuntu.md)  
For springboot.oidc.with.angular please see- [springboot-angular-oidc-check](oidc-check/springboot.oidc.with.angular/README_ubuntu.md) 


#### Stop Ping Federate
Run “sudo ./gradlew ping-stop-pingfed”  
<img width="445" alt="stop_pingfed" src="https://github.com/teq-niq/pingfed-automation/assets/14346578/8db016bd-bba0-4219-a676-0cd60e899515">   
Result should look like this:  
<img width="559" alt="stop_pingfed_result" src="https://github.com/teq-niq/pingfed-automation/assets/14346578/d6a8ffe3-b178-453f-b4e3-083e3658c098">    
Note: Just stopping the Ping Federate by killing the process.  
Relying on the process id noted earlier in case of windows. In case of other environments just detecting processes that are running PingFederate and stopping them.    

#### Stop PingDirectory
Run “sudo ./gradlew ping-stop-ds”  
<img width="420" alt="stop_ds" src="https://github.com/teq-niq/pingfed-automation/assets/14346578/6adcff42-723f-4ef5-99dc-d98412bf3672">   
Result should look like this:  

<img width="460" alt="stop_ds_result" src="https://github.com/teq-niq/pingfed-automation/assets/14346578/31d9d5a0-c68d-40af-af47-6552ddfc4d97">     

Note: Just stopping the Ping directory. 
PingDirectory can also be stopped by launching: stop-server.bat or stop-server.sh found in bin/bat folder of the Ping Directory.


#### Undo the Setup If and when needed
Run “sudo ./gradlew ping-clean”  
<img width="395" alt="undosetup" src="https://github.com/teq-niq/pingfed-automation/assets/14346578/f1cc1e4f-5816-4961-8325-ef502415eb5d">   
Result should be like this.  
<img width="538" alt="undosetupresult" src="https://github.com/teq-niq/pingfed-automation/assets/14346578/73bbefc4-d0bb-4046-b383-52b35b73dfaf">   
Note: Before running "ant clean" ensure that pingfederate and pingdirectory are stopped.
Also Note: Can again setup by running "sudo ./gradlew ping-setup".


#### Trouble shooting
In linux sometimes sudo xyz will report "sudo: xyz: command not found". Replace xyz with a valid command.  
If so please add below line in your .bashrc and source it.
- alias sudo='sudo env PATH=$PATH $@'   
After adding above line run below.    
- source ~/.bashrc    

I did something wrong. How do I start again?   
- Run "sudo ./gradlew ping-stop-pingfed" if its running.   
- Run "sudo ./gradlew ping-stop-ds" if its running   
- Run "sudo ./gradlew ping-clean".   
- Worst case scenario restart the machine.   
- Run sudo ./gradlew ping-clean"   
- After "sudo ./gradlew ping-clean" assuming pingfed-automation\downloads folder has the needed files. Start again with the steps.  

