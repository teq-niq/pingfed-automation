//import org.apache.tools.ant.filters.*;

plugins {
   

    id("pingfed.automation.java-library-conventions")




}



dependencies {
    compileOnly("org.apache.tomcat:tomcat-catalina:10.1.4")
    api(project(":oidc-check:simple-oidc-check:simple-oidc-sharedlib"))


}


tasks.jar{

    archiveFileName.set("original-"+archiveFileName.get());
}

val fatJar = task("fatJar", type = Jar::class) {
    dependsOn(":oidc-check:simple-oidc-check:simple-oidc-sharedlib:jar", ":automation-shared-lib:jar")
    archiveFileName.set(project.name+".jar");
duplicatesStrategy=DuplicatesStrategy.EXCLUDE;
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory()) it else zipTree(it) })
    with(tasks["jar"] as CopySpec)
}

tasks {
    "build" {
        dependsOn(fatJar)
    }
}



tasks.processResources{

        filter {it.replace("\${project.artifactId}", project.name);

        }



}



