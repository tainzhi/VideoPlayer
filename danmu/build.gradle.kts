import com.tainzhi.android.buildsrc.Libs

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.tainzhi.android.danmu"
    compileSdk = 30
    buildToolsVersion = "30.0.2"

    defaultConfig {
        minSdk = 30
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    api(Libs.Kotlin.stdlib)
    api(Libs.AndroidX.appcompat)
    api(Libs.AndroidX.coreKtx)
    api(Libs.Glide.glide)
}