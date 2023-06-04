import org.apache.tools.ant.taskdefs.condition.Os

plugins {
   

    id("pingfed.automation.java-war-conventions")
    id("org.siouan.frontend-jdk11") version "6.0.0"




}



tasks.war{
    doLast{
        setupAntTasks()
    }
}
setupAntTasks();


fun setupAntTasks() {

        ant.lifecycleLogLevel = AntBuilder.AntMessagePriority.INFO

        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            ant.importBuild("../build.xml"  ) { antTaskName ->
                "demo1-${antTaskName}".toString()
            }

        } else {
            ant.importBuild("../build.xml"){ antTaskName ->
                "demo1-${antTaskName}".toString()
            }

        }

        tasks.forEach {

            if (it.name.startsWith("demo1-")) {

                it.group = "Demo1"

            }
        }


}

dependencies {
    providedCompile("jakarta.servlet:jakarta.servlet-api:6.0.0")
    providedCompile("jakarta.servlet.jsp:jakarta.servlet.jsp-api:3.1.0")

    providedCompile(project(":oidc-check:simple-oidc-check:simple-oidc-sharedlib"))


}




