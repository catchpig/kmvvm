plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}
android {
    namespace = "com.catchpig.download"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    api(project(":utils"))

    implementation(libs.appcompat)
    implementation(libs.core.ktx)
    api(libs.okhttp3)
    implementation(libs.okhttp3.logging.interceptor)
    compileOnly(libs.retrofit2)
    api(libs.retrofit2.adapter.rxjava3)
    api(libs.retrofit2.converter.gson)
    implementation(libs.rxandroid)
    implementation(libs.rxjava)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

apply(from = "../publish_jitpack_aar_jar.gradle")