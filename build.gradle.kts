buildscript {
    repositories {
        dependencies {
            classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
            classpath("com.tencent.bugly:symtabfileuploader:2.2.1")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
}
