# PingFed Automation Demo  


<img width="255" alt="authorization_code1" src="https://user-images.githubusercontent.com/14346578/210153449-078d9f02-642f-4006-bd1c-f727e80ab9a2.png">  

## Objectives:
1. Speed up getting started on PingFederate.  
1.1 Setup a non clustered pingfederate instance with minimal manual effort.  
1.2 Post initial configuration setup automate configuration of pingfederate using a java admin api wrapper.
2. A general discussion on swagger in this context.
As far as setup is concerned this is only a way to quickly get started on using pingfederate. Customize as needed.  
## Prerequisites 
- Java JDK 11
- JAVA_HOME environment variable should be correctly setup.
- Java, Maven and Ant should be configured in Path environment variable.
- A running mysql with root user credentials to enable creation of a user,  schema and tables in the mysql for use by pingfederate.
- Ensure ports 9999, 9031, 8080 are available and not in use before proceeding.

## Steps to follow
#### Setup
[Setup Steps](Setup.md)
#### Swagger discussion
[Swagger Notes](SwaggerNotes.md)
