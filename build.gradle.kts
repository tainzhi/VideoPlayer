buildscript {
    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/public/") }
        maven { setUrl("https://maven.aliyun.com/repository/jcenter/")}
        google()
        mavenCentral()

        dependencies {
            classpath("com.android.tools.build:gradle:7.0.2")
            classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5")
            classpath("com.tencent.bugly:symtabfileuploader:2.2.1")
            classpath(kotlin("gradle-plugin", "1.5.30"))
        }
    }
}

allprojects {
    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/public/") }
        maven { setUrl("https://maven.aliyun.com/repository/jcenter/") }
        google()
        mavenCentral()
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
