

plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}

rootProject.name = "pingfed-automation"
include("verify-downloads", "custom-ant-tasks", "automation-shared-lib", "admin-api-wrapper", ":oidc-check:simple-oidc-check:simple-oidc-sharedlib",
        ":oidc-check:simple-oidc-check:simple-oidc-app",
        ":oidc-check:simple-oidc-check:simple-oidc-realm",
        ":oidc-check:springboot.oidc.with.angular:front-end",
        ":oidc-check:springboot.oidc.with.angular")



