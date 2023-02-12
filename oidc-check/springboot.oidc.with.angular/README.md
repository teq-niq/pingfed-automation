# Spring boot angular authorization code sample.  


## Addditional Prerequisite:

### Add ssl certificate to the trusted store.   
Before proceeding lets perform an additional prerequisite.
Visit https://localhost:9999/  


<img width="633" alt="pingfedlanding" src="https://user-images.githubusercontent.com/14346578/215850920-b76e66c4-6d8b-4dc7-b600-34425dd21850.png">    
Click on the "Not secure" as shown below.   
  
<img width="260" alt="certificatecheck" src="https://user-images.githubusercontent.com/14346578/215851310-4bdd61bd-0642-4a21-9195-8c8c6d4f0796.png">   
 
Proceed as shown next.

(A quick side note: Its possible to add proper ssl certificates into pingfederate and is very straightforward. Not covering that here. )        
  
<img width="251" alt="certificatecheck1" src="https://user-images.githubusercontent.com/14346578/215851488-8bcb8373-e45c-4124-82ff-e12dc9d973ab.png">   

Click on the menu item corresponding to Certificate is not valid as shown above.   
This will launch the certificate viewer.     
Showing below a partial screen of same.       
 
<img width="411" alt="certificatecheck2" src="https://user-images.githubusercontent.com/14346578/215851691-aefdbd78-b109-4fb4-b917-12b3b9d7a7a4.png">   
    
Lets visit the details tab.   
  
<img width="410" alt="certificatecheck3" src="https://user-images.githubusercontent.com/14346578/215851855-c494ccb3-e549-4e7f-9de8-d7403879394c.png">    
    
Press the "Export" button.      
This will launch File save as dialog for the crt file.  
 
<img width="701" alt="certificatecheck4" src="https://user-images.githubusercontent.com/14346578/215852002-0c7a6cef-2b1b-4816-9b13-9a802a02afc3.png">    
Save the file at a suitable location.  
In command prompt or terminal visit same location.
Execute the below command:   

keytool -import -trustcacerts -file localhost.crt -alias localpingfed -keystore %JAVA_HOME%/lib/security/cacerts -storepass changeit    

<img width="482" alt="keytool" src="https://user-images.githubusercontent.com/14346578/215852636-ac70c56a-c95d-4982-a9b4-df84a556f9e4.PNG">   
 
When prompted enter yes   

<img width="209" alt="keytool1" src="https://user-images.githubusercontent.com/14346578/215853229-49eab6ef-1042-46e0-8be2-2e3c6966ba11.PNG">   

On linux might need to use sudo and $JAVA_HOME when using keytool.   
## Demo:

springboot.oidc.with.angular is a maven project. Its also a submodule of pingfed-automation\oidc-check.  
It should have been already built.  However we need to build it properly.
In command prompt or terminal navigate to pingfed-automation\oidc-check\springboot.oidc.with.angular folder.  
Run the below command.   

mvn clean package -P npmbuild    

<img width="498" alt="build_springboot_angular" src="https://user-images.githubusercontent.com/14346578/216162830-b1f3550f-3b10-424e-9425-5a549fc9d88d.png">    

This should give a result as shown below.    
<img width="633" alt="build_springboot_angular1" src="https://user-images.githubusercontent.com/14346578/216163155-8e6f9533-1f6d-4e7a-9085-9cb5207188aa.png">    

   
Its a simple spring boot based project. Using angular for front end.  
We are going to use this project to verify if we are able to obtain some access tokens from pingfederate. 
With command prompt at pingfed-automation\oidc-check\springboot.oidc.with.angular lets run the following    

java -jar target\springboot.oidc.with.angular.jar   

<img width="600" alt="springboot-angular1" src="https://user-images.githubusercontent.com/14346578/215853492-68e73a43-e678-49df-ad64-0db087ea32fd.png">   

Showing below how it launches.    

<img width="944" alt="springboot-angular2" src="https://user-images.githubusercontent.com/14346578/215853660-008b3c4b-9e44-4ce0-987a-983d33b03d65.png">      

Vist http://localhost:8081    

<img width="680" alt="springboot-angular3" src="https://user-images.githubusercontent.com/14346578/218252358-2fd48d84-edfd-4eda-ba63-c9ced69afd6a.png">   

Click Login     

<img width="680" alt="springboot-angular4" src="https://user-images.githubusercontent.com/14346578/215853956-792477d2-0ab6-4cc8-86a7-50860caf0a07.png">   

Enter user.0 and password for the username and password.   

<img width="584" alt="springboot-angular5" src="https://user-images.githubusercontent.com/14346578/215854179-4f5dc139-b53b-4790-8848-2136f557ae9d.png">

Uncheck foo now. Press Allow.  

<img width="584" alt="uncheck foo" src="https://user-images.githubusercontent.com/14346578/218252517-b9dccf3f-b2ad-4737-bb69-5fc6ec586740.png">

Login should be successful.   
 
<img width="584" alt="springboot-angular6" src="https://user-images.githubusercontent.com/14346578/218252642-0e02d6c5-7469-4666-9eaa-5da3a1d7a593.png">    

The second link is a Protected route and appears only when logged in.   
But both links have same functionality for this demo.  
Click the first one.  

<img width="584" alt="springboot-angular6a" src="https://user-images.githubusercontent.com/14346578/218252978-be8e013a-c38f-416a-818c-a8b60fbbf675.png">   

Now check the "Show Innaccessible" checkbox.  

<img width="584" alt="springboot-angular6b" src="https://user-images.githubusercontent.com/14346578/218253418-4337ef96-5a99-43a7-af3f-16cd66ec6d0e.png">   

The Access Foo Buton is now available even though it should not be.  
Click all the buttons.   

<img width="584" alt="springboot-angular6c" src="https://user-images.githubusercontent.com/14346578/218297445-264ba111-908d-425f-85c6-c76a4ff55a64.png">   

Notice how all the buttons work. But "Access Foo" button reported  403 and Problem.   

If you logout and are at this page you will see even less buttons.

<img width="584" alt="springboot-angular6d" src="https://user-images.githubusercontent.com/14346578/218253795-ffd0b3ba-bd80-4190-94f2-d5adb4c094dc.png">  

Again pressing the checkbox would show you all the buttons including the inaccessibble ones. 

<img width="584" alt="springboot-angular6e" src="https://user-images.githubusercontent.com/14346578/218263256-a33651ad-5e8d-4203-9a3b-48cf3527ad94.png"> 

Pressing the inaccessible ones will report 401 as shown below.   
<img width="584" alt="springboot-angular6f" src="https://user-images.githubusercontent.com/14346578/218263902-e29f2264-c5d9-4e17-88f7-ccdf64b9b47e.png"> 

Conclusion: So we have seen how the login and scopes selection affects how the users can do what on the application.   

You can also launch with application with a VM argument of -DisOn4200=true.  
If you do that during development you can keep the server running at 8081 but front end at 4200.  You could launch the application then at http://localhost:4200 when doing angular development. 


#### Additional Notes:
A spring boot angular OIDC application can be done in different ways.   
The approach taken here is the same approach used by back end web applications. 
As can be seen here the SPA angular app is not a pure SPA. Its compromising on its SPA'ness during login. 
Have deliberately chosen an approach where the front end, back end application are all in the same web app.  
The main advanatage of this approach is that the access token never leaves the back end.  
It never is made accessible to the angular javascript front end.  
This is often a most recommended highly secure and compact approach- I am attempting here the The BFF Pattern (Backend for Frontend).  
However this is not the only way.  
There are other approaches also possible. 
If needed one can also use Authorization Code with PKCE when working with Pure SPA or native applications where browser redirect is being avoided or not possible. 
Depending on the requirements choose the appropriate approach.  




## Improvements in this sample code
Logout needs to use a pingfed logout endpoint.
Note: Logout itself works.  


## Cleanup if needed:
### Remove ssl certificate from the trusted store. 
Run below command to remove certificate from the trusted store.
keytool -delete -alias localpingfed -keystore %JAVA_HOME%/lib/security/cacerts

This should remove the ssl certificate from the trust store.


  