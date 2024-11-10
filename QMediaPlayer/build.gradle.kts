import com.tainzhi.android.buildsrc.Libs

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tainzhi.qmediaplayer"
    splits {
        abi {
            isEnable = true
            reset()
            // include("x86", "x86_64", "armeabi-v8a", "armeabi") 会安装4个apk
            include("armeabi-v8a")
            isUniversalApk = false
        }
    }

    compileSdk = 30
    buildToolsVersion = "30.0.2"

    defaultConfig {
        minSdk = 30
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
    implementation(project(":ffmpeg"))

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