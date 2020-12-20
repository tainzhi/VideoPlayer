import com.tainzhi.android.buildsrc.Libs

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(Libs.Version.compileSdkVersion)
    buildToolsVersion(Libs.Version.buildToolsVersion)

    defaultConfig {
        minSdkVersion(Libs.Version.minSdkVersion)
        targetSdkVersion(Libs.Version.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    // 本地 unit test默认的resoruce目录为 `src/test/resources`
    // 所以unit test中, javaClass.classLoader!!.getResourceAsStream()的目录就是 src/test/resources
    // 添加`src/main`后, 可以直接访问`src/main/assets`目录下的文件
    // 比如 javaClass.classLoader!!.getResourceAsStream("assets/file.json")
    sourceSets["test"].resources.setSrcDirs(listOf("src/main", "src/test/resources"))
}

dependencies {
    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.jsoup)
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.volley)
    implementation(Libs.xmlToJson)
    implementation(Libs.Moshi.moshi)
    kapt(Libs.Moshi.codeGen)
    implementation(Libs.OkHttp.okhttp)

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
    testImplementation(Libs.robolectric)
    testImplementation(Libs.AndroidX.Test.Ext.junit)

}