buildscript {
    val kotlin_version by extra("1.4.21")
    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/public/") }
        maven { setUrl("https://maven.aliyun.com/repository/jcenter/")}
        google()
        jcenter()


        dependencies {
            classpath(com.tainzhi.android.buildsrc.Libs.androidGradlePlugin)
            classpath(com.tainzhi.android.buildsrc.Libs.Kotlin.gradlePlugin)
            classpath(com.tainzhi.android.buildsrc.Libs.AndroidX.Navigation.safeArgs)
            classpath(com.tainzhi.android.buildsrc.Libs.buglyUploadMapping)
        }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

allprojects {
    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/public/") }
        maven { setUrl("https://maven.aliyun.com/repository/jcenter/") }
        google()
        jcenter()
        // for BaseRecyclerViewAdapterHelper
        maven {
            setUrl("https://jitpack.io")
        }
        // for robolectric
        maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
