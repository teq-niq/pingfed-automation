
plugins {
	java
	id("org.springframework.boot") version "3.0.0"
	id("io.spring.dependency-management") version "1.1.0"

}

repositories{
    mavenCentral()
}

dependencies {


    implementation("org.springframework.boot:spring-boot-starter")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5:3.1.1.RELEASE")
    implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")

    runtimeOnly(project(":oidc-check:springboot.oidc.with.angular:front-end"))

}

tasks.named("bootJar"){
    doFirst{
        println("jar invoked.begins...")
    }
    dependsOn(":oidc-check:springboot.oidc.with.angular:front-end:assembleFrontend")
    doLast{
        println("jar invoked.ends...")
    }
}



