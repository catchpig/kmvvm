plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}
apply(from = "../common.gradle")

android {
    namespace = "com.catchpig.utils"
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.core.ktx)
    api(libs.logger)
    api(libs.gson)
    compileOnly(libs.material)
    compileOnly(libs.rxandroid)
    compileOnly(libs.rxjava)

    api(project(":annotation"))
}

apply(from = "../publish_jitpack_aar_jar.gradle")
