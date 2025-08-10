plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
}
apply(from = "../publish_jitpack_aar_jar.gradle")

dependencies {
    api(project(":annotation"))
    implementation(libs.symbol.processing.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
    implementation(libs.okhttp3.logging.interceptor)
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