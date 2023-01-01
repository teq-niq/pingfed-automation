# code-synch

## Objective:
1. Should be self evident.   
2. A more traditional approach would be to use a common shared module and let it have a single shared class.  
That would definitely work.
There is only one drawback. Not a big drawback too.
Basically if I used the shared module approach will not be able to run "mvn clean package - P admin" under admin-api-wrapper.  
Will have to rather run "mvn clean package - P admin" under pingfed-automation.  
There is no harm in that.  
This was just a mater of personal preference.  

In any case both approaches lead to an additional module.   
 
Did want to also try  a maven based custom code generator which participates in the build without creating a custom maven plugin.  
Could have easily leveraged moustache templates for that matter and generated all kinds of code.  

That said might switch to using a common shared module later instead.  
