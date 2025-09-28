plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
}
apply(from = "../publish_aar_jar.gradle")
apply(from = "../common.gradle")

kotlin {
    jvmToolchain(libs.versions.javaVersion.get().toInt())
}

dependencies {
    api(project(":annotation"))
    implementation(libs.symbol.processing.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
    implementation(libs.okhttp3.logging.interceptor)
}