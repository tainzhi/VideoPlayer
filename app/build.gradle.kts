import com.tainzhi.android.buildsrc.Libs


plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize") // 弃用android.extensions后, 使用parcelize
    // kotlin("android.extensions") 弃用extension获取资源
    // kotlin("plugin.serialization")
    id("androidx.navigation.safeargs.kotlin")
    id("io.wusa.semver-git-plugin").version("2.3.7")
    id("com.tainzhi.android.plugin.upload")
}

apply {
    // from("../test_dependencies.gradle.kts")
    // 直接把 androidTestImplementation写在该目录报错, More than one file was found with OS independent path 'META-INF/AL2.0
    from("../test_dependencies.gradle")
}

android {
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
    compileSdkVersion(Libs.Configs.compileSdkVersion)
    buildToolsVersion(Libs.Configs.buildToolsVersion)

    defaultConfig {
        applicationId = "com.tainzhi.android.videoplayer"
        minSdkVersion(Libs.Configs.minSdkVersion)
        targetSdkVersion(Libs.Configs.targetSdkVersion)
        versionCode = semver.info.count
        versionName = semver.info.lastTag
        flavorDimensions("1")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // 第三方库 AppUpdate
        // 每个应用拥有不同的authorities，防止相同在同一个手机上无法同时安装
        val _id = applicationId ?: ""
        resValue("string", "authorities", _id)
    }

    buildFeatures {
        dataBinding = true
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

    productFlavors {
        create("pgy") {
            // dimension = ""
        }
    }

    applicationVariants.all {
        outputs.forEach { output ->
            check(output is com.android.build.gradle.internal.api.ApkVariantOutputImpl)
            if (output is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                if (buildType.name == "debug") {
                    output.outputFileName =
                            "VideoPlayer_${flavorName}_${versionName}_${buildType.name}.apk"
                } else if (buildType.name == "release") {
                    output.outputFileName = "VideoPlayer_${flavorName}_${versionName}.apk"
                }
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    adbOptions {
        timeOutInMs = 20 * 60 * 1000  // 20 minutes
        installOptions("-d", "-t")
    }

}

uploadConfig {
    apiKey = "99d9f637f9ca00b7ef97cdb2cdabd8ac"
    updateDescription = "fix gradle && upload"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":common"))
    implementation(project(":QMediaPlayer"))
    implementation(project(":MediaSpider"))
    // implementation(project(":ffmpeg"))
    implementation(project(":danmu"))

    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.preference)
    implementation(Libs.AndroidX.constraintlayout)
    implementation(Libs.AndroidX.viewPager2)
    implementation(Libs.AndroidX.swiperefresh)
    implementation(Libs.AndroidX.Navigation.fragmentKtx)
    implementation(Libs.AndroidX.Navigation.uiKtx)
    implementation(Libs.AndroidX.Lifecycle.viewmodelKtx)
    implementation(Libs.AndroidX.Lifecycle.livedata)
    implementation(Libs.AndroidX.Lifecycle.extensions)
    implementation(Libs.AndroidX.Room.runtime)
    implementation(Libs.AndroidX.Room.ktx)
    kapt(Libs.AndroidX.Room.compiler)
    implementation(Libs.AndroidX.Paging.runtime)
    implementation(Libs.AndroidX.Paging.runtimeKtx)
    implementation(Libs.AndroidX.Work.runtimeKtx)
    implementation(Libs.AndroidX.browser)

    implementation(Libs.Google.material)

    implementation(Libs.Coroutines.android)

    implementation(Libs.Koin.scope)
    implementation(Libs.Koin.viewmodel)
    implementation(Libs.Koin.fragment)
    implementation(Libs.Koin.ext)

    implementation(Libs.logger)

    api(Libs.Retrofit.retrofit)
    api(Libs.Retrofit.moshiConverter)
    api(Libs.Moshi.moshi)
    kapt(Libs.Moshi.codeGen)

    implementation(Libs.OkHttp.loggingInterceptor)
    testImplementation(Libs.OkHttp.mockWebServer)
    implementation(Libs.cookietar)

    implementation(Libs.Glide.glide)
    kapt(Libs.Glide.compiler)

    implementation(Libs.baseRecyclerViewAdapterHelper)
    implementation(Libs.youthBanner)
    implementation(Libs.tencentTbssdk)
    implementation(Libs.verticalTabLayout)
    implementation(Libs.flowlayout)
    implementation(Libs.licenseDialog)
    implementation(Libs.appUpdate)
    implementation(Libs.activityOnCrash)
    implementation(Libs.multiStateView)
    implementation(Libs.volley)
    implementation(Libs.lottie)
    implementation(Libs.notchScreenTool)
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
}

