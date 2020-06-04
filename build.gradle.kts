import com.tainzhi.android.buildsrc.Libs

buildscript {
    repositories {
        
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
            google()
            jcenter()
            maven {
                setUrl("https://jitpack.io")
            }
        }
    }
    
    tasks.register("clean", Delete::class) {
        delete(rootProject.buildDir)
    }
}
