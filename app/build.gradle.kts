plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
    id("kotlin-parcelize") // 弃用android.extensions后, 使用parcelize
    id("kotlin-kapt") // databinding 需要, google推荐compose，故databinding已经是维护状态，不再更新
    // kotlin("plugin.serialization")
    // id("androidx.navigation.safeargs")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.tainzhi.android.videoplayer"
    signingConfigs {
        getByName("debug") {
            // debug版本默认不签名
            // storeFile = file("../android.keystore")
        }

        create("release") {
            storeFile = file("../android.keystore")
            // #签名密码
            storePassword = "123456"
            // #签名别名
            keyAlias = "android"
            // #签名别名密码
            keyPassword = "tainzhi"
        }
    }

    compileSdk = 34

    defaultConfig {
        minSdk = 29
        targetSdk = 34
        applicationId = "com.tainzhi.android.videoplayer"
        // 第1，2位表示年
        // 第3，4位表示月
        // 第5，6位表示日
        // 最后两位取值01-99
        versionCode = 24112101
        versionName = "2024.11.21-1"
        flavorDimensions("1")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // 第三方库 AppUpdate
        // 每个应用拥有不同的authorities，防止相同在同一个手机上无法同时安装
        val _id = applicationId ?: ""
        resValue("string", "authorities", _id)

        ndk {
            abiFilters.addAll(setOf("armeabi-v7a", "arm64-v8a"))
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        getByName("debug") {
            // 默认值 true
            // isDebuggable = true
            applicationIdSuffix = ".debug"
            signingConfigs["debug"]
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfigs["release"]
        }
    }

    applicationVariants.all {
        outputs.forEach { output ->
            check(output is com.android.build.gradle.internal.api.ApkVariantOutputImpl)
            output.outputFileName = "VideoPlayer_${versionName}.apk"
            // if (output is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
            //     if (buildType.name == "debug") {
            //         output.outputFileName =
            //                 "VideoPlayer_${flavorName}_${versionName}_${buildType.name}.apk"
            //     } else if (buildType.name == "release") {
            //         output.outputFileName = "VideoPlayer_${flavorName}_${versionName}.apk"
            //     }
            // }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    adbOptions {
        timeOutInMs = 20 * 60 * 1000  // 20 minutes
        installOptions("-d", "-t")
    }

    aaptOptions {
        // 用于 glimpse-android
        noCompress("tflite")
        noCompress("lite")
    }

    packagingOptions {
        exclude("META-INF/library_release.kotlin_module")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":QMediaPlayer"))
    implementation(project(":MediaSpider"))
    implementation(project(":ffmpeg"))
    implementation(project(":danmu"))


    // implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.30")
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.android.volley)
    implementation("androidx.preference:preference:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.room:room-runtime:2.5.1")
    implementation("androidx.room:room-ktx:2.5.1")
    ksp("androidx.room:room-compiler:2.5.1")
    implementation("androidx.paging:paging-runtime:3.1.1")
    implementation("androidx.paging:paging-runtime-ktx:3.1.1")
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.browser:browser:1.5.0")

    implementation("com.google.android.material:material:1.3.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    implementation(libs.insert.koin.koin.android)
    implementation(libs.insert.koin.koin.androidx.compose)
    implementation(libs.insert.koin.koin.androidx.workmanager)

    implementation("com.orhanobut:logger:2.2.0")

    api("com.squareup.retrofit2:retrofit:2.10.0")
    api("com.squareup.retrofit2:converter-moshi:2.10.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")

    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.1")
    implementation("com.github.franmontiel:PersistentCookieJar:v1.0.1")

    implementation("com.github.bumptech.glide:glide:4.12.0")
    ksp("com.github.bumptech.glide:compiler:4.12.0")

    implementation("com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.4")
    implementation("com.youth.banner:banner:1.4.10")
    // 腾讯X5Webview
    implementation("com.tencent.tbs.tbssdk:sdk:43697")
    implementation("q.rorbin:VerticalTabLayout:1.2.5")
    implementation("com.hyman:flowlayout-lib:1.1.2")
    implementation("de.psdev.licensesdialog:licensesdialog:2.1.0")
    // AppUpdate，简单的实现App更新下载和安装
    implementation("com.azhon:appupdateX:2.8.0")
    // launch a custom activity when your app crashes,
    // instead of showing the hated "Unfortunately, X has stopped" dialog.
    implementation("cat.ereza:customactivityoncrash:2.3.0")
    implementation("com.github.Kennyc1012:MultiStateView:2.1.2")
    implementation("com.airbnb.android:lottie:3.4.1")
    implementation("com.github.smarxpan:NotchScreenTool:0.0.1")
    // 基于tensorflow的智能内容感知的图片crop, 即任意尺寸条件的crop都能crop出图片的主要内容, 比如人像...
    // https://github.com/the-super-toys/glimpse-android
    implementation("com.github.the-super-toys.glimpse-android:glimpse-core:0.0.5")
    implementation("com.github.the-super-toys.glimpse-android:glimpse-glide:0.0.5")
    implementation("org.tensorflow:tensorflow-lite:0.0.0-nightly")

    ///////////////////////////////////////////////////////////////////////////
    // 以下是 unit test 依赖
    ///////////////////////////////////////////////////////////////////////////
    testImplementation("junit:junit:4.13")
    testImplementation("org.mockito:mockito-core:3.3.0")
    testImplementation("io.insert-koin:koin-test:3.1.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
    testImplementation("androidx.test:core:1.2.0")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("com.google.truth:truth:0.42")
    testImplementation("io.insert-koin:koin-test:3.1.2")
    // http://hamcrest.org/JavaHamcrest/distributables
    testImplementation("org.hamcrest:hamcrest:2.2")
    // json包含在android sdk中, 但是单元测试中没有该包
    // 报错 Method put in org.json.JSONObject not mocked
    // 解决办法: https://blog.csdn.net/u010513377/article/details/102938807
    // json maven: https://mvnrepository.com/artifact/org.json/json?__cf_chl_captcha_tk__=a667358e389f181a56f6e51d65b1d5c9fa21ae78-1610526080-0-Acd6MaR0FBsffq3-jcACIoj8zFKdit0FyTt9VF_-mZEVnhOhopOEXa1XQDcl9RnMvJWW_gsY5T1v4LlnsUguWeBTPuzooChL0t3sDSLkb84W7XabHgbtcFciKj7_Jzzk4jUPb88XLXUZIV1eVMrF2L8pTaDPbD0V7EZphTJmKEdLvCcYyXl-rj71X13idbv2nSoSAI5MsovjdrjaePJtHYigF5r_y1DxGJ1zon48KgVMvLT5Upt9N9jd7WPiuYinlOOzryfuJ9vV7tc_tBANDqg0wQLEho9HMaWEcQuXQrKLeaXcf0jiMe7ktQjqVg0F-RAkChEbgj0zii8O5a5n3J7XYyGHR1mrN20heedQYlmBQK6YaKdrkO05zthYjNkX_sUtIG1VMQR6w78R8Ox2wutbS5r8p4z5jf3aJqWgqN-5FKENeE1nJHNqusZEC86YKUApc3qJqg_e5V3KpkXoad-0w1xg8grluQMVWcTYNkFQa6VtEbeIn4uIPXdAJ5ZqIpW3JCYGfM_tjQMzC0ZO4HAMxLP1w4gY3UZ7DBtiu_B7kXeRc5XwpU1txvveyJNp13Hax6JfSRROLY6qUGC7418fUfkGJfWISBAdOe9IiruN
    testImplementation("org.json:json:20201115")
    // https://github.com/jraska/livedata-testing
    testImplementation("com.jraska.livedata:testing-ktx:1.1.2")
}