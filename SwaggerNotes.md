# Choice of maven plugin version
used below maven plugin because swagger.json file specified "swagger": "2.0"  
&lt;groupId&gt;io.swagger&lt;/groupId&gt;  
&lt;artifactId&gt;swagger-codegen-maven-plugin&lt;/artifactId&gt;  
&lt;version&gt;2.4.28&lt;/version&gt;  

# discriminator problem
As an example, if we look at the swagger definition for DataStore we see that discriminator is the "type" property.

To the back end api -lets say one - which is expecting a DataStore or its derived child classes as request body one should be able to post a LdapDataStore by specifying the type.
And same also should work in same way when returning a datastore in response.

https://swagger.io/specification/v2/ says the following against discriminator.

discriminator- 
Adds support for polymorphism. The discriminator is the schema property name that is used to differentiate between other schema that inherit this schema. The property name used MUST be defined at this schema and it MUST be in the required property list. When used, the value MUST be the name of this schema or any schema that inherits it.

In case of datastore type however allows only one of "LDAP", "PING_ONE_LDAP_GATEWAY", "JDBC", "CUSTOM".

None of these is the actual name of the schema eg
LdapDataStore, PingOneLdapGatewayDataStore, JdbcDataStore, CustomDataStore.

While it may look intuitive to humans that JDBC must mean JdbcDataStore this will require additional mapping. That mapping concept however came only in version 3 of open api.
https://swagger.io/specification/

So the type "enum" should have been actually listing  "LdapDataStore", "PingOneLdapGatewayDataStore", "JdbcDataStore", "CustomDataStore" than the "LDAP", "PING_ONE_LDAP_GATEWAY", "JDBC", "CUSTOM" it does currently.

Is the other solution in using version 3 of open api?  
Maybe.  
But version three also has issues of other kinds when dealing with object inheritance and polymorhism. see more here- https://github.com/swagger-api/swagger-core/issues/3312   

Other than these points I did notice some other minor issues in the swagger definitions. Not listing them here for brevity.

In spite of these issues the back end can actually work which is a good thing.  
Especially if you are using postman and swagger-ui most of the times.  

The issue is more when you want to use the code-generation approach and the swagger json/yaml has some problems like the one we noted.  



# Solution for above issues:

The solution is to be careful about the yaml/json. If needed correct it manually.  Or else use a transformer like shown here in this case.  
To overcome these challenges while using the code generated java wrapper I also applied some json transformation via the rest template interceptor.  

This enabled me to successfuly administrate the pingfederate instance using java code.

