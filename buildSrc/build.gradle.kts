
plugins {
    // Support convention plugins written in Kotlin. Convention plugins are build scripts in 'src/main' that automatically become available as plugins in the main build.
    `kotlin-dsl`
    `java`
}

repositories {
    // Use the plugin portal to apply community plugins in convention plugins.
    gradlePluginPortal()
}


dependencies {
  implementation("org.springframework.boot:spring-boot-gradle-plugin:2.7.3")
    implementation("io.swagger:swagger-codegen:2.4.28")
}