plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
}
apply(from = "../publish_jitpack_aar_jar.gradle")

dependencies {
    implementation(libs.annotation)
    implementation(libs.retrofit2)
    implementation(libs.okhttp3)
    implementation(libs.kotlinx.serialization.json)
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

