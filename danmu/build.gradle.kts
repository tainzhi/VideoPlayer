import com.tainzhi.android.buildsrc.Libs

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
}

android {
    compileSdkVersion(Libs.Configs.compileSdkVersion)
    buildToolsVersion(Libs.Configs.buildToolsVersion)

    defaultConfig {
        minSdkVersion(Libs.Configs.minSdkVersion)
        targetSdkVersion(Libs.Configs.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
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