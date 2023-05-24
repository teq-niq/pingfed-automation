

plugins {
   

    id("pingfed.automation.java-war-conventions")




}



dependencies {
    providedCompile("jakarta.servlet:jakarta.servlet-api:6.0.0")
    providedCompile("jakarta.servlet.jsp:jakarta.servlet.jsp-api:3.1.0")

    providedCompile(project(":oidc-check:simple-oidc-check:simple-oidc-sharedlib"))


}




