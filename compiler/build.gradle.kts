plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
}
apply(from = "../publish_jitpack_aar_jar.gradle")
apply(from = "../common.gradle")

dependencies {
    api(project(":annotation"))
    implementation(libs.symbol.processing.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
    implementation(libs.okhttp3.logging.interceptor)
}