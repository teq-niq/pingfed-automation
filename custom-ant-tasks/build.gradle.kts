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
        extracted1()
    }
}
 extracted1();


fun Build_gradle.extracted1() {
    val x1: RegularFile = layout.buildDirectory.file("libs/custom-ant-tasks.jar").get()
    val f1: File = x1.asFile
    println("f1.exists()=" + f1.exists())
    if (f1.exists()) {
        ant.lifecycleLogLevel = AntBuilder.AntMessagePriority.INFO

        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            ant.importBuild("../win/build.xml") { antTaskName ->
                "ping-${antTaskName}".toString()
            }

        } else {
            ant.importBuild("../linux/build.xml")

        }

        tasks.forEach {

            if (it.name.startsWith("ping-")) {

                it.group = "Ping"
            }
        }

    }
}