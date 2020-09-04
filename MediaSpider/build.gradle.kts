import com.tainzhi.android.buildsrc.Libs

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(Libs.Version.compileSdkVersion)
    buildToolsVersion(Libs.Version.buildToolsVersion)

    defaultConfig {
        minSdkVersion(Libs.Version.minSdkVersion)
        targetSdkVersion(Libs.Version.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner ="androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    //
    // testOptions {
    //     unitTests {
    //         includeAndroidResources = true
    //     }
    // }
}

dependencies {
    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.jsoup)
    implementation(Libs.Google.gson)
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.volley)

    ///////////////////////////////////////////////////////////////////////////
    // 以下是 unit test 依赖
    ///////////////////////////////////////////////////////////////////////////
    testImplementation(Libs.junit)
    testImplementation(Libs.Mockito.core)
    testImplementation(Libs.Koin.test)
    testImplementation(Libs.Coroutines.test)
    testImplementation(Libs.AndroidX.Test.core)
    testImplementation(Libs.AndroidX.archCoreTesting)
    testImplementation(Libs.Google.truth)
    testImplementation(Libs.robolectric)
    testImplementation(Libs.AndroidX.Test.Ext.junit)

    ///////////////////////////////////////////////////////////////////////////
    // 以下是 android test 依赖
    ///////////////////////////////////////////////////////////////////////////
    // Koin for Unit test and instrumented test
    androidTestImplementation(Libs.Koin.test)
    androidTestImplementation(Libs.Coroutines.test)

    androidTestImplementation(Libs.AndroidX.archCoreTesting)
    // Core Library
    androidTestImplementation(Libs.AndroidX.Test.core)
    // Required for instrumen(ed tests, AndroidJUnitRunner and JUnit Rules
    androidTestImplementation(Libs.AndroidX.Test.runner)
    androidTestImplementation(Libs.AndroidX.Test.rules)
    androidTestImplementation(Libs.AndroidX.Test.Ext.junit)
    // assertion
    androidTestImplementation(Libs.AndroidX.Test.Ext.truth)
    androidTestImplementation(Libs.Google.truth)
    // espresso
    androidTestImplementation(Libs.AndroidX.Test.Espresso.core)
    androidTestImplementation(Libs.AndroidX.Test.Espresso.contrib)
    androidTestImplementation(Libs.AndroidX.Test.Espresso.intents)
    androidTestImplementation(Libs.AndroidX.Work.workTesting)

}