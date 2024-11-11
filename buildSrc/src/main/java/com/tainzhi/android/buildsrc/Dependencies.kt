package com.tainzhi.android.buildsrc

object Libs {

    const val androidToolBuildGradle = "com.android.tools.build:gradle:7.0.2"

    const val junit = "junit:junit:4.13"

    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.1"

    object Configs {
        const val minSdkVersion = 26
        const val targetSdkVersion = 30
        const val compileSdkVersion = 30
        const val buildToolsVersion = "30.0.2"

    }

    /*
    plugins {
        id(Plugins.androidApplication)
        id(Plugins.crashlytics)
        kotlin(Plugins.kotlinAndroid)
        kotlin(Plugins.kotlinExtensions)
        kotlin(Plugins.kapt)
    }
     */
    object Plugins {
        const val androidApplication = "com.android.application"
        const val kotlinAndroid = "android"
        const val kapt = "kapt"
        const val detekt = "io.gitlab.arturbosch.detekt"
        const val crashlytics = "io.fabric"
    }

    // Mockito framework
    object Mockito {
        private const val version = "3.3.0"

        // for unit test
        const val core = "org.mockito:mockito-core:$version"

        // for android test
        const val android = "org.mockito:mockito-android:$version"
    }

    object Google {
        const val material = "com.google.android.material:material:1.3.0"
        const val firebaseCore = "com.google.firebase:firebase-core:17.2.2"
        const val crashlytics = "com.crashlytics.sdk.android:crashlytics:2.10.1"
        const val gmsGoogleServices = "com.google.gms:google-services:4.3.3"
        const val fabricPlugin = "io.fabric.tools:gradle:1.31.2"

        const val openSourceLicensesPlugin = "com.google.android.gms:oss-licenses-plugin:0.10.1"
        const val openSourceLicensesLibrary = "com.google.android.gms:play-services-oss-licenses:17.0.0"

        const val truth = "com.google.truth:truth:0.42"

        const val gson = "com.google.code.gson:gson:2.8.6"
    }

    object Kotlin {
        const val version = "1.5.30"
        const val stdlibJdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${version}"
        const val stdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${version}"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object Coroutines {
        // https://github.com/kotlin/kotlinx.coroutines/blob/master/README.md#using-in-your-projects
        private const val version = "1.5.2"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val rx2 = "org.jetbrains.kotlinx:kotlinx-coroutines-rx2:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.2.0"
        const val coreKtx = "androidx.core:core-ktx:1.3.2"
        const val archCoreTesting = "androidx.arch.core:core-testing:2.1.0"
        const val browser = "androidx.browser:browser:1.2.0"
        const val collection = "androidx.collection:collection-ktx:1.1.0"
        const val palette = "androidx.palette:palette:1.0.0"
        const val recyclerview = "androidx.recyclerview:recyclerview:1.1.0"
        const val emoji = "androidx.emoji:emoji:1.0.0"
        const val swiperefresh = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0-alpha03"
        const val viewPager2 = "androidx.viewpager2:viewpager2:1.0.0"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.4"

        const val preference = "androidx.preference:preference:1.1.0"

        object Navigation {
            private const val version = "2.3.5"
            const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:$version"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
            const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
        }

        object Fragment {
            private const val version = "1.3.0"
            const val fragment = "androidx.fragment:fragment:$version"
            const val fragmentKtx = "androidx.fragment:fragment-ktx:$version"
        }

        object WorkManager {
            private const val version = "2.3.4"
            const val workRuntimeKtx = "androidx.work:work-runtime-ktx:$version"
            const val workTesting = "androidx.work:work-testing:$version"
        }

        object Test {
            private const val version = "1.2.0"

            // test: Robolectric environment
            const val core = "androidx.test:core:$version"
            const val runner = "androidx.test:runner:$version"
            const val rules = "androidx.test:rules:$version"

            object Ext {
                private const val version = "1.1.0"
                const val junit = "androidx.test.ext:junit:$version"

                // assertions
                const val truth = "androidx.test.ext:truth:$version"
                const val junitKtx = "androidx.test.ext:junit-ktx:$version"
            }


            object Espresso {
                private const val version = "3.2.0"
                const val core = "androidx.test.espresso:espresso-core:$version"
                const val contrib = "androidx.test.espresso:espresso-contrib:$version"
                const val intents = "androidx.test.espresso:espresso-intents:$version"
                const val accessibility = "androidx.test.espresso:espresso-accessibility:$version"
                const val web = "androidx.test.espresso:espresso-web:$version"

                object Idling {
                    const val concurrent = "androidx.test.espresso.idling:idling-concurrent:$version"
                    const val resources = "androidx.test.espresso:espresso-idling-resource:$version"
                }
            }
        }

        object Paging {
            private const val version = "2.1.2"
            const val runtime = "androidx.paging:paging-runtime:$version"
            const val runtimeKtx = "androidx.paging:paging-runtime-ktx:$version"

            // without Android dependencies for testing
            const val common = "androidx.paging:paging-common:$version"
        }

        object Lifecycle {

            private const val version = "2.2.0"

            // ViewModel
            const val viewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"

            // LiveData
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$version"

            // https://github.com/jraska/livedata-testing
            const val livedataTesting = "com.jraska.livedata:testing-ktx:1.1.2"

            // Lifecycles only (without ViewModel or LiveData)
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"

            // Annotation processor
            const val compiler = "androidx.lifecycle:lifecycle-compiler:$version"

            // Saved state module for ViewModel
            const val viewmodelSaveState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:$version"

            // optional - helpers for implementing LifecycleOwner in a Service
            const val service = "androidx.lifecycle:lifecycle-service:$version"

            // optional - ProcessLifecycleOwner provides a lifecycle for the whole application process
            const val process = "androidx.lifecycle:lifecycle-process:$version"

            // optional - ReactiveStreams support for LiveData
            const val reactiveStreams = "androidx.lifecycle:lifecycle-reactivestreams-ktx:$version"

            // optional - Test helpers for LiveData
            const val testing = "androidx.arch.core:core-testing:$version"
        }

        object Room {
            private const val version = "2.3.0-beta01"
            const val common = "androidx.room:room-common:$version"
            const val runtime = "androidx.room:room-runtime:$version"
            const val compiler = "androidx.room:room-compiler:$version"
            const val ktx = "androidx.room:room-ktx:$version"
            const val testing = "androidx.room:room-testing:$version"
        }

        object Work {
            private const val version = "2.3.0"
            const val runtimeKtx = "androidx.work:work-runtime-ktx:$version"
            const val workTesting = "androidx.work:work-testing:$version"
        }
    }

    // http://hamcrest.org/JavaHamcrest/distributables
    const val hamcrest = "org.hamcrest:hamcrest:2.2"

    object RxJava {
        const val rxJava = "io.reactivex.rxjava2:rxjava:2.2.11"
        const val rxAndroid = "io.reactivex.rxjava2:rxandroid:2.1.1"
    }

    object Dagger {
        private const val version = "2.16"
        const val dagger = "com.google.dagger:dagger:$version"
        const val android = "com.google.dagger:dagger-android:$version"
        const val androidSupport = "com.google.dagger:dagger-android-support:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"
        const val androidProcessor = "com.google.dagger:dagger-android-processor:$version"
    }

    object Retrofit {
        private const val version = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val rxjavaAdapter = "com.squareup.retrofit2:adapter-rxjava2:$version"
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:$version"
        const val moshiConverter = "com.squareup.retrofit2:converter-moshi:$version"
    }

    object Moshi {
        private const val version = "1.12.0"
        const val moshi = "com.squareup.moshi:moshi-kotlin:$version"
        const val codeGen = "com.squareup.moshi:moshi-kotlin-codegen:$version"
    }

    object OkHttp {
        private const val version = "4.9.1"
        const val okhttp = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
        const val mockWebServer = "com.squareup.okhttp3:mockwebserver:$version"
    }

    object Koin {
        private const val koin_version = "3.1.2"

        // Koin core features
        const val core = "io.insert-koin:koin-core:$koin_version"

        // Koin test features
        const val test = "io.insert-koin:koin-test:$koin_version"

        // Koin main features for Android (Scope,ViewModel ...)
        const val android = "io.insert-koin:koin-android:$koin_version"

        // Koin for Jetpack WorkManager
        const val androidXWorkManager = "io.insert-koin:koin-androidx-workmanager:$koin_version"

        // Koin for Jetpack Compose
        const val androidXCompose = "io.insert-koin:koin-androidx-compose:$koin_version"

        // Koin for JUnit 4
        const val testJunit4 = "io.insert-koin:koin-test-junit4:$koin_version"

        // Koin for JUnit 5
        const val testJunit5 = "io.insert-koin:koin-test-junit5:$koin_version"
    }

    object Glide {
        private const val version = "4.12.0"
        const val glide = "com.github.bumptech.glide:glide:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
    }


    object AspectJX {
        const val hujiangAspectJX = "com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.8"
        const val aspectJrt = "org.aspectj:aspectjrt:1.9.2"
    }

    object ARouter {
        const val api = "com.alibaba:arouter-api:1.5.0"
        const val compiler = "com.alibaba:arouter-compiler:1.2.2"
        const val register = "com.alibaba:arouter-register:1.0.2"
    }

    const val timber = "com.jakewharton.timber:timber:4.7.1"

    const val logger = "com.orhanobut:logger:2.2.0"

    const val cookietar = "com.github.franmontiel:PersistentCookieJar:v1.0.1"

    const val baseRecyclerViewAdapterHelper = "com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.4"

    // https://github.com/afollestad/recyclical
    // extensible Kotlin DSL for setting up and manipulating RecyclerView
    const val recyclical = "com.afollestad:recyclical:1.1.1"

    const val youthBanner = "com.youth.banner:banner:1.4.10"

    // 腾讯X5Webview
    const val tencentTbssdk = "com.tencent.tbs.tbssdk:sdk:43697"
    const val buglyCrashReport = "com.tencent.bugly:crashreport:3.1.0"
    const val buglyNativeCrashReport = "com.tencent.bugly:nativecrashreport:3.7.1"

    // bugly符号表有最新的版本，但是在jcenter上找不到
    const val buglyUploadMapping = "com.tencent.bugly:symtabfileuploader:2.2.1"

    const val verticalTabLayout = "q.rorbin:VerticalTabLayout:1.2.5"

    const val flowlayout = "com.hyman:flowlayout-lib:1.1.2"

    const val licenseDialog = "de.psdev.licensesdialog:licensesdialog:2.1.0"

    // AppUpdate，简单的实现App更新下载和安装
    const val appUpdate = "com.azhon:appupdateX:2.8.0"

    // launch a custom activity when your app crashes,
    // instead of showing the hated "Unfortunately, X has stopped" dialog.
    const val activityOnCrash = "cat.ereza:customactivityoncrash:2.3.0"

    // Didi/DoraemonKit
    // http://xingyun.xiaojukeji.com/docs/dokit/#/androidGuide
    object DoKit {
        private const val version = "3.5.0-beta01"
        const val debugVersion = "io.github.didi.dokit:dokitx:${version}"
        const val releaseVersion = "io.github.didi.dokit:dokitx-no-op:${version}"

        // AOP包括以下几个功能:
        // 1)百度、腾讯、高德地图的经纬度模拟
        // 2)UrlConnection、Okhttp 抓包以及后续的接口hook功能
        // 3)App 启动耗时统计 4)慢函数 5)大图
        const val plugin = "io.github.didi.dokit:dokitx-plugin:${version}"
    }

    const val multiStateView =  "com.github.Kennyc1012:MultiStateView:2.1.2"

    const val jsoup = "org.jsoup:jsoup:1.13.1"

    const val robolectric = "org.robolectric:robolectric:4.1"

    const val volley = "com.android.volley:volley:1.1.1"

    // airbnb lottie-android
    const val lottie = "com.airbnb.android:lottie:3.4.1"

    // 标题栏toolbar
    const val titlebar = "com.wuhenzhizao:titlebar:1.2.0"

    // 全面屏,刘海屏全屏
    const val notchScreenTool = "com.github.smarxpan:NotchScreenTool:0.0.1"

    const val xmlToJson = "com.github.smart-fun:XmlToJson:1.4.5"

    // json包含在android sdk中, 但是单元测试中没有该包
    // 报错 Method put in org.json.JSONObject not mocked
    // 解决办法: https://blog.csdn.net/u010513377/article/details/102938807
    // json maven: https://mvnrepository.com/artifact/org.json/json?__cf_chl_captcha_tk__=a667358e389f181a56f6e51d65b1d5c9fa21ae78-1610526080-0-Acd6MaR0FBsffq3-jcACIoj8zFKdit0FyTt9VF_-mZEVnhOhopOEXa1XQDcl9RnMvJWW_gsY5T1v4LlnsUguWeBTPuzooChL0t3sDSLkb84W7XabHgbtcFciKj7_Jzzk4jUPb88XLXUZIV1eVMrF2L8pTaDPbD0V7EZphTJmKEdLvCcYyXl-rj71X13idbv2nSoSAI5MsovjdrjaePJtHYigF5r_y1DxGJ1zon48KgVMvLT5Upt9N9jd7WPiuYinlOOzryfuJ9vV7tc_tBANDqg0wQLEho9HMaWEcQuXQrKLeaXcf0jiMe7ktQjqVg0F-RAkChEbgj0zii8O5a5n3J7XYyGHR1mrN20heedQYlmBQK6YaKdrkO05zthYjNkX_sUtIG1VMQR6w78R8Ox2wutbS5r8p4z5jf3aJqWgqN-5FKENeE1nJHNqusZEC86YKUApc3qJqg_e5V3KpkXoad-0w1xg8grluQMVWcTYNkFQa6VtEbeIn4uIPXdAJ5ZqIpW3JCYGfM_tjQMzC0ZO4HAMxLP1w4gY3UZ7DBtiu_B7kXeRc5XwpU1txvveyJNp13Hax6JfSRROLY6qUGC7418fUfkGJfWISBAdOe9IiruN
    const val json = "org.json:json:20201115"

    // 方便设置状态栏和导航栏效果
    // https://github.com/Zackratos/UltimateBarX
    const val ultimatebarx = "com.zackratos.ultimatebarx:ultimatebarx:0.3.3"


    // 基于tensorflow的智能内容感知的图片crop, 即任意尺寸条件的crop都能crop出图片的主要内容, 比如人像...
    // https://github.com/the-super-toys/glimpse-android
    object Glimpse {
        private const val version = "0.0.5"
        const val core = "com.github.the-super-toys.glimpse-android:glimpse-core:0.0.5"

        //only required for glide extensions
        const val glide = "com.github.the-super-toys.glimpse-android:glimpse-glide:0.0.5"

        //only required for coil extensions
        const val coil = "com.github.the-super-toys.glimpse-android:glimpse-coil:0.0.5"
    }

    const val tensorflowLite = "org.tensorflow:tensorflow-lite:0.0.0-nightly"

    const val circleImageView = "de.hdodenhof:circleimageview:3.1.0"
}
