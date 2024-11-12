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
    buildToolsVersion = "32.0.0"

    defaultConfig {
        minSdk = 26
        targetSdk = 30
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

}


dependencies {
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
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