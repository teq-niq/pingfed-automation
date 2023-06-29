# PingFed Automation Demo  


<img width="255" alt="authorization_code1" src="https://user-images.githubusercontent.com/14346578/210153449-078d9f02-642f-4006-bd1c-f727e80ab9a2.png">  


## Objectives:
1. Speed up getting started on PingFederate.  
1.1 Setup a non clustered pingfederate instance with minimal manual effort.  
1.2 Post initial configuration setup automate configuration of pingfederate using a java admin api wrapper.
2. A general discussion on swagger in this context.
As far as setup is concerned this is only a way to quickly get started on using pingfederate. Customize as needed.  

## Prerequisites 
- Java JDK 17
- JAVA_HOME environment variable should be correctly setup.
- Java, Maven and Ant should be configured in Path environment variable.
- A running mysql with root user credentials to enable creation of a user,  schema and tables in the mysql for use by pingfederate.
- Ensure ports 9999, 9031, 8080, 8081 are available and not in use before proceeding.

## Steps to follow
#### Setup
[Setup Steps](Setup.md) - setup steps.  

## Other points
#### Swagger discussion
[Swagger Notes](SwaggerNotes.md)- some notes.  
#### Authorization code flow sample
There are two simple example projects which can be used to verify that the automated pingfed configuration worked.   The below sample projects are only there for convenience. The main repository is about the java wrapper over the pingfederate admin api.  
They are listed here:    
- pingfed-automation\oidc-check\simple-oidc-check and  
- pingfed-automation\oidc-check\springboot.oidc.with.angular

simple-oidc-check - is a roll your own example code project where the demo is done without using any library/framework for OIDC purpose in general.   

springboot.oidc.with.angular - here the demo is done using a spring boot angular project for an authorization code example.  

For simple-oidc-check please see- [simple-oidc-check](oidc-check/simple-oidc-check/README.md)  
For springboot.oidc.with.angular please see- [springboot-angular-oidc-check](oidc-check/springboot.oidc.with.angular/README.md) 

Note: the two demo apps are sightly different in their flow but both do validate our pingfederate setup and configuration automation.

#### Ping federate admin java wrapper - others
This is possibly the first such implementation.  
#### Versions  
[Version Notes](versions/versions.md)  
