plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
}
apply(from = "../publish_aar_jar.gradle")
apply(from = "../common.gradle")

kotlin {
    jvmToolchain(libs.versions.javaVersion.get().toInt())
}

dependencies {
    implementation(libs.annotation)
    implementation(libs.retrofit2)
    implementation(libs.okhttp3)
    implementation(libs.kotlinx.serialization.json)
}
