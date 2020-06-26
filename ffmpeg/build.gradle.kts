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

        // externalNativeBuild {
        //     cmake {
        //         cppFlags("")
        //     }
        // }
        ndk {
            // abiFilters("armeabi-v7a")  // 指定要ndk需要兼容的架构(这样其他依赖包里mips,x86,armeabi,arm-v8之类的so会被过滤掉)
            abiFilters("arm64-v8a")  // 指定要ndk需要兼容的架构(这样其他依赖包里mips,x86,armeabi,arm-v8之类的so会被过滤掉)
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
}

dependencies {
    implementation(Libs.Kotlin.stdlib)
    testImplementation(Libs.junit)

}