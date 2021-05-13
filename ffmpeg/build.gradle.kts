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
    ndkVersion = "22.1.7171670"

    // ndkVersion = "20.1.5948944"

    // sourceSets.main {
    //     jniLibs.srcDir 'libs'
    // }

    packagingOptions {

        // fix:
        // More than one file was found with OS independent path 'lib/arm64-v8a/libswscale.so'.
        // If you are using jniLibs and CMake IMPORTED targets
        pickFirst("lib/arm64-v8a/libswscale.so")
        pickFirst("lib/arm64-v8a/libavcodec.so")
        pickFirst("lib/arm64-v8a/libavutil.so")
        pickFirst("lib/arm64-v8a/libpostproc.so")
        pickFirst("lib/arm64-v8a/libavformat.so")
        pickFirst("lib/arm64-v8a/libavfilter.so")
        pickFirst("lib/arm64-v8a/libswresample.so")
        pickFirst("lib/arm64-v8a/libavdevice.so")
    }
}

dependencies {
    implementation(Libs.Kotlin.stdlib)
    testImplementation(Libs.junit)

}