// package com.tainzhi.android.plugin
//
// import com.android.build.gradle.AppExtension
// import org.gradle.api.Action
// import org.gradle.api.Plugin
// import org.gradle.api.Project
// import java.io.File
// import com.tainzhi.android.Constant
// import com.tainzhi.android.Util
//
// /**
//  * File:     Upload
//  * Author:   tainzhi
//  * Created:  2020/12/30 16:48
//  * Mail:     QFQ61@qq.com
//  * Description: 自动上传安装包到蒲公英
//  */
// class AutoUpload : Plugin<Project> {
//
//     override fun apply(project: Project) {
//         project.extensions.create(Constant.autoUploadExtensionName, PgyConfig::class.java)
//         val config = project.extensions.getByName(Constant.autoUploadExtensionName) as PgyConfig
//         project.afterEvaluate {
//             this.task("AutoUpload") {
//                 this.description = "upload release apk to 蒲公英"
//                 this.group = "AutoUpload"
//                 var outputFile: File? = null
//                 project.extensions.getByType(AppExtension::class.java)
//                     .applicationVariants.forEach {
//                         it.outputs.forEach { output ->
//                             // like: pgy-debug
//                             if (output.name == "${config.flavor}-${config.buildType}") {
//                                 outputFile = output.outputFile
//                             }
//                         }
//                     }
//                 outputFile?.let { file ->
//                     // assemblePgyRelease, 首字母必须大写, 在dependsOn
//                     // 而在 ./gradlew assemblepgyrelease可以小写
//                     this.dependsOn("assemble${Util.upperCaseFirst(config.flavor)}${Util.upperCaseFirst(config.buildType)}").also {
//                         it.actions.add(Action {
//                             println("begin upload")
//                             dispatchUpload(file, config)
//                         })
//                     }
//                 }
//             }
//         }
//     }
//
//     private fun dispatchUpload(outFile: File, config: PgyConfig) {
//         val result = UpLoader(config).upload(outFile)
//         println(
//             """
// ---------------------------------------------------------------------------------------------------
//                                          upload success
// ---------------------------------------------------------------------------------------------------
// $result
//             """.trimIndent()
//         )
//     }
// }
//
