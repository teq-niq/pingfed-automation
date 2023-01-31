#### Spring boot angular authorization code sample.  


#### Addditional Prerequisite:

##### Add ssl certificate to the trusted store.   
Before proceeding lets perform an additional prerequisite.
Visit https://localhost:9999/  
<img width="445" alt="pingfedlanding" src="..\..\images\pingfedlanding.png"> 
<img width="633" alt="pingfedlanding" src="https://user-images.githubusercontent.com/14346578/215850920-b76e66c4-6d8b-4dc7-b600-34425dd21850.png">    
Click on the "Not secure" as shown below.   

<img width="222" alt="certificatecheck" src="..\..\images\certificatecheck.png">   
<img width="260" alt="certificatecheck" src="https://user-images.githubusercontent.com/14346578/215851310-4bdd61bd-0642-4a21-9195-8c8c6d4f0796.png">   
Proceed as shown next        
   
<img width="222" alt="certificatecheck1" src="..\..\images\certificatecheck1.png">   
<img width="251" alt="certificatecheck1" src="https://user-images.githubusercontent.com/14346578/215851488-8bcb8373-e45c-4124-82ff-e12dc9d973ab.png">   

Click on the menu item corresponding to Certificate is not valid as shown above.   
This will launch the certificate viewer.     
Showing below a partial screen of same.       
<img width="222" alt="pingfedlanding" src="..\..\images\certificatecheck2.png">  
<img width="411" alt="certificatecheck2" src="https://user-images.githubusercontent.com/14346578/215851691-aefdbd78-b109-4fb4-b917-12b3b9d7a7a4.png">      
Lets visit the details tab.   
<img width="222" alt="pingfedlanding" src="..\..\images\certificatecheck3.png">  
<img width="410" alt="certificatecheck3" src="https://user-images.githubusercontent.com/14346578/215851855-c494ccb3-e549-4e7f-9de8-d7403879394c.png">    
    
Press the "Export" button.      
This will launch File save as dialog for the crt file.  
<img width="333" alt="pingfedlanding" src="..\..\images\certificatecheck4.png">  
<img width="701" alt="certificatecheck4" src="https://user-images.githubusercontent.com/14346578/215852002-0c7a6cef-2b1b-4816-9b13-9a802a02afc3.png">    
Save the file at a suitable location.  
In command prompt or terminal visit same location.
Execute the below command:  
keytool -import -trustcacerts -file localhost.crt -alias localpingfed -keystore %JAVA_HOME%/lib/security/cacerts -storepass changeit  
<img width="482" alt="keytool" src="https://user-images.githubusercontent.com/14346578/215852636-ac70c56a-c95d-4982-a9b4-df84a556f9e4.PNG">    
When prompted enter yes   
<img width="209" alt="keytool1" src="https://user-images.githubusercontent.com/14346578/215853229-49eab6ef-1042-46e0-8be2-2e3c6966ba11.PNG">   

#### Demo:
In command prompt or terminal navigate to pingfed-automation\oidc-check\springboot.oidc.with.angular folder.  
springboot.oidc.with.angular is a maven project. Its also a submodule of pingfed-automation\oidc-check.  
It should have been already built.  
Its a simple spring boot based project. Using angular for front end.  
We are going to use this project to verify if we are able to obtain some access tokens from pingfederate. 
With command prompt at pingfed-automation\oidc-check\springboot.oidc.with.angular lets run the following
java -jar target\springboot.oidc.with.angular.jar   
<img width="445" alt="springboot-angular launch" src="..\..\images\springboot-angular1.png">  
<img width="600" alt="springboot-angular1" src="https://user-images.githubusercontent.com/14346578/215853492-68e73a43-e678-49df-ad64-0db087ea32fd.png">   
Showing below how it launches.    
<img width="445" alt="springboot-angular launch done" src="..\..\images\springboot-angular2.png"> 
<img width="944" alt="springboot-angular2" src="https://user-images.githubusercontent.com/14346578/215853660-008b3c4b-9e44-4ce0-987a-983d33b03d65.png">      
Vist http://localhost:8081    
<img width="445" alt="springboot-angular" src="..\..\images\springboot-angular3.png">  
<img width="185" alt="springboot-angular3" src="https://user-images.githubusercontent.com/14346578/215853832-832d1288-0ce1-4fb6-b282-6cfca7b585b6.png">   
Click Login     
<img width="445" alt="springboot-angular login" src="..\..\images\springboot-angular4.png">   
<img width="680" alt="springboot-angular4" src="https://user-images.githubusercontent.com/14346578/215853956-792477d2-0ab6-4cc8-86a7-50860caf0a07.png">   
Enter user.0 and password for the username and password.   
<img width="445" alt="springboot-angular logged one" src="..\..\images\springboot-angular5.png">   
<img width="584" alt="springboot-angular5" src="https://user-images.githubusercontent.com/14346578/215854179-4f5dc139-b53b-4790-8848-2136f557ae9d.png">
Login should be successful.   
<img width="445" alt="springboot-angular logged one" src="..\..\images\springboot-angular6.png">  
<img width="182" alt="springboot-angular6" src="https://user-images.githubusercontent.com/14346578/215854388-323b4054-3992-4d96-999a-f3a9c886cad2.png">    
Visit the links and try out the application.  
#### Additional Notes:
A spring boot angular OIDC application can be done in different ways.   
The approach taken here is the same approach used by back end web applications.  
Have deliberately chosen an approach where the front end, back end application are all in the same web app.  
The main advanatage of this approach is that the access token never leaves the back end.  
It never is made accessible to the angular javascript front end.  
This is often a most recommended highly secure and compact approach.  
However this is not the only way.  
There are other approaches also possible. Depending on the requirements choose the appropriate approach.  
As can be seen here the SPA angular app is not a pure SPA. Its compromising on its SPA'ness during login.



### Improvements in this sample code
Logout needs to use a pingfed logout endpoint.
Logout itself works.  


#### Cleanup if needed:
##### Remove ssl certificate from the trusted store. 
Run below command to remove certificate from the trusted store.
keytool -delete -alias localpingfed -keystore %JAVA_HOME%/lib/security/cacerts

This should remove the ssl certificate from the trust store.

#### CORS when not logged on

If debugging in the browser one can see some CORS errors.
These could be eliminated by the following steps:  
Visit https://localhost:9999/pingfederate/app#/onAuthzServerSettings
Under Cross-Origin Resource Sharing Settings>Allowed Origin add the below:
http://localhost:8081
After Adding remember to press the save button.
Stop the PingFederate running server.  
Edit pingfed-automation\win\ping\pingfederate\pingfederate-11.2.0\server\default\data\config-store\cors-configuration.xml file.
add below entry   
&lt;con:item name="urlPatterns"&gt;/as/authorization.oauth2&lt;/con:item&gt;
Restart PingFederate

Visit   http://localhost:8081
The CORS warnings will have disappeared.

But do note this recommendation in the comments in same cors-configuration.xml file:   
Adding the OAuth authorization endpoint (/as/authorization.oauth2) to the urlPatterns field is _strongly_ discouraged due to the resulting security risks.

So best to undo these CORS related changes for pingfederate.





  