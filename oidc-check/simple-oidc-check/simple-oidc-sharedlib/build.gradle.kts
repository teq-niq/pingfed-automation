

plugins {
   

    id("pingfed.automation.java-library-conventions")




}



dependencies {
    api("org.apache.httpcomponents.client5:httpclient5:5.2")
    api("org.apache.httpcomponents.core5:httpcore5:5.2")
    api("com.fasterxml.jackson.core:jackson-databind:2.14.1")
    api("com.fasterxml.jackson.core:jackson-annotations:2.14.1")
    api(project(":automation-shared-lib"))


}




