import com.tainzhi.android.buildsrc.Libs

buildscript {
    val kotlin_version by extra("1.3.72")
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
    dependencies {
        "classpath"("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}
