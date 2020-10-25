buildscript {
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
    
    allprojects {
        repositories {
            maven { setUrl("https://maven.aliyun.com/repository/public/") }
            maven { setUrl("https://maven.aliyun.com/repository/jcenter/")}
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
}
