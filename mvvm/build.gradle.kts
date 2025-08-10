plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.plugin.serialization)
}

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
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
    api(libs.retrofit2.adapter.rxjava3)
    api(libs.retrofit2.converter.gson)
    implementation(libs.rxandroid)
    implementation(libs.rxjava)
    api(libs.loadingview)
    api(libs.immersionbar)
    api(libs.immersionbar.ktx)
    compileOnly(libs.smartrefreshlayout)
}

apply(from = "../publish_jitpack_aar_jar.gradle")
