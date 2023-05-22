

plugins {
   

    id("pingfed.automation.java-library-conventions")




}



dependencies {
    implementation("org.apache.httpcomponents.client5:httpclient5:5.2")
    implementation("org.apache.httpcomponents.core5:httpcore5:5.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.14.1")
    implementation(project(":automation-shared-lib"))


}




