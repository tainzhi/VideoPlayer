plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "2.0.0-1.0.21"
}

android {
    namespace = "com.tainzhi.mediaspider"
    compileSdk = 34

    defaultConfig {
        minSdk = 29
        targetSdk = 34
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

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
        kotlinOptions {
            jvmTarget = "11"
        }
    }

    // 本地 unit test默认的resoruce目录为 `src/test/resources`
    // 所以unit test中, javaClass.classLoader!!.getResourceAsStream()的目录就是 src/test/resources
    // 添加`src/main`后, 可以直接访问`src/main/assets`目录下的文件
    // 比如 javaClass.classLoader!!.getResourceAsStream("assets/file.json")
    sourceSets["test"].resources.setSrcDirs(listOf("src/main", "src/test/resources"))
}

dependencies {
    implementation(files("libs/rhino-1.7.13.jar"))
    implementation("org.jsoup:jsoup:1.13.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.android.volley:volley:1.1.1")
    implementation("com.github.smart-fun:XmlToJson:1.4.5")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")

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
    testImplementation("org.robolectric:robolectric:4.1")
    testImplementation("androidx.test.ext:junit:1.1.0")

}