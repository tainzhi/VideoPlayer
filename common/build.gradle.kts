import com.tainzhi.android.buildsrc.Libs

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = Libs.Configs.compileSdkVersion
    buildToolsVersion = Libs.Configs.buildToolsVersion

    defaultConfig {
        minSdk = Libs.Configs.minSdkVersion
        targetSdk = Libs.Configs.targetSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        dataBinding = true
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    api(Libs.Kotlin.stdlib)
    api(Libs.AndroidX.Lifecycle.viewmodelKtx)
    api(Libs.AndroidX.Lifecycle.livedata)
    api(Libs.AndroidX.appcompat)
    api(Libs.AndroidX.coreKtx)
    api(Libs.AndroidX.Fragment.fragment)
    api(Libs.AndroidX.Fragment.fragmentKtx)
    api(Libs.AndroidX.constraintlayout)
    api(Libs.Google.material)
    api(Libs.Coroutines.android)
    api(Libs.Retrofit.retrofit)
    api(Libs.Retrofit.moshiConverter)
    api(Libs.Moshi.moshi)
    kapt(Libs.Moshi.codeGen)
    api(Libs.OkHttp.loggingInterceptor)
    api(Libs.Glide.glide)

}