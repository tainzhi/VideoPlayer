import com.tainzhi.android.buildsrc.Libs

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdkVersion(Libs.Configs.compileSdkVersion)
    buildToolsVersion(Libs.Configs.buildToolsVersion)

    defaultConfig {
        minSdkVersion(Libs.Configs.minSdkVersion)
        targetSdkVersion(Libs.Configs.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner ="androidx.test.runner.AndroidJUnitRunner"

        externalNativeBuild {
            cmake {
                cppFlags("")
                // abiFilters("arm64-v8a", "armeabi-v7a", "x86")
                abiFilters("arm64-v8a", "armeabi-v7a")
            }
        }
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
    externalNativeBuild {
        cmake {
            // path  = file("src/main/jni/CMakeLists.txt")
            path  = file("CMakeLists.txt")
        }
    }

    // ndkVersion = "20.1.5948944"

    // sourceSets.main {
    //     jniLibs.srcDir 'libs'
    // }
}

dependencies {
    implementation(Libs.Kotlin.stdlib)
    testImplementation(Libs.junit)

}