dependencies {
    ///////////////////////////////////////////////////////////////////////////
    // 以下是 android test 依赖
    ///////////////////////////////////////////////////////////////////////////
    // com.tainzhi.android.buildsrc.Libs.Koin for Unit test and instrumented test
    androidTestImplementation(com.tainzhi.android.buildsrc.Libs.Koin.test)
    androidTestImplementation(com.tainzhi.android.buildsrc.Libs.Coroutines.test)
    // Core Library
    androidTestImplementation(com.tainzhi.android.buildsrc.Libs.AndroidX.Test.core)
    androidTestImplementation(com.tainzhi.android.buildsrc.Libs.AndroidX.Test.runner)
    androidTestImplementation(com.tainzhi.android.buildsrc.Libs.AndroidX.Test.rules)
    androidTestImplementation(com.tainzhi.android.buildsrc.Libs.AndroidX.Test.Ext.truth)
    androidTestImplementation(com.tainzhi.android.buildsrc.Libs.AndroidX.Test.Ext.junit)
    androidTestImplementation(com.tainzhi.android.buildsrc.Libs.Google.truth)
    // espresso
    androidTestImplementation(com.tainzhi.android.buildsrc.Libs.AndroidX.Test.Espresso.core)
    androidTestImplementation(com.tainzhi.android.buildsrc.Libs.AndroidX.Test.Espresso.contrib)
    androidTestImplementation(com.tainzhi.android.buildsrc.Libs.AndroidX.Test.Espresso.intents)
    androidTestImplementation(com.tainzhi.android.buildsrc.Libs.AndroidX.Test.Espresso.accessibility)
    androidTestImplementation(com.tainzhi.android.buildsrc.Libs.AndroidX.Test.Espresso.web)
    androidTestImplementation(com.tainzhi.android.buildsrc.Libs.AndroidX.Test.Espresso.Idling.concurrent)
    androidTestImplementation(com.tainzhi.android.buildsrc.Libs.AndroidX.Test.Espresso.Idling.resources)
    androidTestImplementation(com.tainzhi.android.buildsrc.Libs.AndroidX.Work.workTesting)
}