buildscript {
    repositories {
        dependencies {
            // classpath("com.android.tools.build:gradle:7.4.0")
            classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
            classpath("com.tencent.bugly:symtabfileuploader:2.2.1")
            // classpath(kotlin("gradle-plugin", "1.5.30"))
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

plugins {
    id ("com.android.application") version "8.4.0" apply false
    id ("com.android.library") version "8.4.0" apply false
    id ("org.jetbrains.kotlin.android") version "1.9.0" apply false
    // id("com.google.devtools.ksp") version "1.9.0-1.0.13"
}
