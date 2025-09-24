plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
}
apply(from = "../publish_jitpack_aar_jar.gradle")
apply(from = "../common.gradle")

dependencies {
    implementation(libs.annotation)
    implementation(libs.retrofit2)
    implementation(libs.okhttp3)
    implementation(libs.kotlinx.serialization.json)
}
