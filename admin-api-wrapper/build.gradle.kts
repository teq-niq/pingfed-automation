import com.example.SimpleSwaggerToJavaTask

plugins {
   

    id("pingfed.automation.java-spring-library-conventions")

    id("pingfed.automation.java-swagger2java-conventions")


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
    
    if(buildProfile.equals("admin"))
	{
		 implementation("io.swagger:swagger-codegen:2.4.28")
        implementation(project(":automation-shared-lib"))
	}

}


val x:TaskProvider<SimpleSwaggerToJavaTask> = tasks.named<SimpleSwaggerToJavaTask>("swagger2java"){
    inputSpecs.set(file("swagger-json/swagger.json"));
    target.set(layout.buildDirectory.dir("/generated/sources/swagger"));
    doLast{
        java {
            val extraSrcDir = "src/main/admin-src"
            val genSrcDir = "build/generated/sources/swagger/src/main/java"
            val mainJavaSourceSet: SourceDirectorySet = sourceSets.getByName("main").java
            mainJavaSourceSet.srcDir(genSrcDir)
            mainJavaSourceSet.srcDir(extraSrcDir)

        }
        println("configured swagger2java");
    }
}


tasks.compileJava{

       if(buildProfile.equals("admin")) {

           dependsOn(x.get())

       }

}



