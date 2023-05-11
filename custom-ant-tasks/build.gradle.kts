import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    id("pingfed.automation.java-library-conventions")
    `kotlin-dsl`
}

dependencies {
    implementation("org.apache.ant:ant")
   
}



tasks.jar{
    doLast{
        setupAntTasks()
    }
}
setupAntTasks();


fun setupAntTasks() {
    val x1: RegularFile = layout.buildDirectory.file("libs/custom-ant-tasks.jar").get()
    val f1: File = x1.asFile

    if (f1.exists()) {
        ant.lifecycleLogLevel = AntBuilder.AntMessagePriority.INFO

        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            ant.importBuild("../win/build.xml"  ) { antTaskName ->
                "ping-${antTaskName}".toString()
            }

        } else {
            ant.importBuild("../linux/build.xml"){ antTaskName ->
                "ping-${antTaskName}".toString()
            }

        }

       tasks.forEach {

            if (it.name.startsWith("ping-")) {

                it.group = "Ping"

            }
        }

    }
}