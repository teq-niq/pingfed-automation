plugins {
   

    id("pingfed.automation.java-spring-library-conventions")
}

val buildProfile: String? by project
println("buildProfile="+buildProfile)
//apply(from = "profile-${buildProfile ?: "default"}.gradle.kts")


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.swagger:swagger-annotations:1.6.6")
    implementation("org.threeten:threetenbp:1.6.1")
    implementation("com.github.joschi.jackson:jackson-datatype-threetenbp:2.6.4")
    implementation("org.apache.httpcomponents:httpclient")
    implementation("org.javatuples:javatuples:1.2")
    implementation("com.github.joschi.jackson:jackson-datatype-threetenbp:2.6.4")

}

if(buildProfile.equals("admin"))
{

}
