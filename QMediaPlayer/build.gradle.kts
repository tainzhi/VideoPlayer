import com.tainzhi.android.buildsrc.Libs

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
}

android {

    splits {
        abi {
            isEnable = true
            reset()
            // include("x86", "x86_64", "armeabi-v8a", "armeabi") 会安装4个apk
            include("armeabi-v8a")
            isUniversalApk = false
        }
    }

    compileSdkVersion(Libs.Version.compileSdkVersion)
    buildToolsVersion(Libs.Version.buildToolsVersion)

    defaultConfig {
        minSdkVersion(Libs.Version.minSdkVersion)
        targetSdkVersion(Libs.Version.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
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

}


dependencies {
    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.constraintlayout)
    implementation(project(":danmu"))

    api("tv.danmaku.ijk.media:ijkplayer-java:0.8.8")
    // Other ABIs: optional
    // 查看指令架构:
    // adb shell getprop ro.product.cpu.abi
    // api("tv.danmaku.ijk.media:ijkplayer-armv7a:0.8.8")
    // api("tv.danmaku.ijk.media:ijkplayer-armv5:0.8.8")
    api("tv.danmaku.ijk.media:ijkplayer-arm64:0.8.8")
    // api("tv.danmaku.ijk.media:ijkplayer-x86:0.8.8")
    // api("tv.danmaku.ijk.media:ijkplayer-x86_64:0.8.8")

    api("com.google.android.exoplayer:exoplayer:2.11.5")
}