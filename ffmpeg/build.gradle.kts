import com.tainzhi.android.buildsrc.Libs

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tainzhi.android.ffmpeg"
    compileSdk = 30
    buildToolsVersion = "30.0.2"

    defaultConfig {
        minSdk = 30
        testInstrumentationRunner ="androidx.test.runner.AndroidJUnitRunner"

        externalNativeBuild {
            cmake {
                cppFlags("")
                // abiFilters("arm64-v8a", "armeabi-v7a", "x86")
                abiFilters("arm64-v8a")
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
    ndkVersion = "21.0.6113669"

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
repositories {
    mavenCentral()
}