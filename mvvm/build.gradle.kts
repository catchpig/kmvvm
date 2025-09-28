plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.plugin.serialization)
}
apply(from = "../publish_aar_jar.gradle")
apply(from = "../common.gradle")

android {
    namespace = "com.catchpig.mvvm"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    kotlin {
        jvmToolchain(libs.versions.javaVersion.get().toInt())
    }
    buildFeatures {
        viewBinding = true
    }
}
dependencies {
    implementation(libs.appcompat)
    implementation(libs.core.ktx)
    implementation(libs.startup.runtime)
    api(libs.lifecycle.viewmodel.ktx)
    api(libs.lifecycle.runtime.ktx)
    api(libs.recyclerview)
    api(libs.kotlinx.serialization.json)

    api(project(":annotation"))
    api(project(":utils"))

    api(libs.okhttp3)
    implementation(libs.okhttp3.logging.interceptor)

    compileOnly(libs.retrofit2)
    compileOnly(libs.retrofit2.adapter.rxjava3)
    api(libs.retrofit2.converter.gson)
    compileOnly(libs.rxandroid)
    compileOnly(libs.rxjava)
    api(libs.loadingview)
    api(libs.immersionbar)
    api(libs.immersionbar.ktx)
    compileOnly(libs.smartrefreshlayout)
}
