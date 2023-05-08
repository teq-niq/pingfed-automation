

plugins {

    id("pingfed.automation.java-common-conventions")
    `kotlin-dsl`
}



repositories {
    // Use the plugin portal to apply community plugins in convention plugins.
    gradlePluginPortal()
}

task("verify-downloads", JavaExec::class) {
    mainClass.set("VerifyDownloads")
    group="Ping"
    classpath = sourceSets["main"].runtimeClasspath
}