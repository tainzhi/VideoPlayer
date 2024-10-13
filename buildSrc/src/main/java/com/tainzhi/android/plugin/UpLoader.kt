package com.tainzhi.android.plugin// package com.tainzhi.android.plugin
//
// import com.squareup.moshi.Moshi
// import okhttp3.MediaType.Companion.toMediaType
// import okhttp3.MultipartBody
// import okhttp3.OkHttpClient
// import okhttp3.Request
// import okhttp3.RequestBody.Companion.asRequestBody
// import java.io.File
// import java.util.concurrent.TimeUnit
// import com.tainzhi.android.plugin.bean.ResBean
// import com.tainzhi.android.plugin.bean.Data
// import com.tainzhi.android.Util
//
// /**
//  * File:     UpLoader
//  * Author:   tainzhi
//  * Created:  2020/12/30 21:33
//  * Mail:     QFQ61@qq.com
//  * Description:
//  */
// class UpLoader(private val config: PgyConfig) {
//     companion object {
//         const val password = "1234"
//     }
//
//     fun upload(
//         file: File
//     ): String {
//         val fileBody = file.asRequestBody("multipart/form-data".toMediaType())
//
//         val requestBody = MultipartBody.Builder()
//             .setType(MultipartBody.FORM)
//
//             //key 后面换成从配置中获取
//             .addFormDataPart("_api_key", config.apiKey)
//
//             //文件
//             .addFormDataPart("file", file.name, fileBody)
//
//             //	(必填)应用安装方式，值为(2,3)。2：密码安装，3：邀请安装
//             .addFormDataPart("buildInstallType", "${1}")
//
//             //(必填) 设置App安装密码
//             .addFormDataPart("buildPassword", password)
//             .addFormDataPart("buildName", file.name)
//
//             //选填) 版本更新描述，请传空字符串，或不传。
//             .addFormDataPart("buildUpdateDescription", config.updateDescription)
//             .build()
//
//         val request = Request.Builder()
//             .url("https://www.pgyer.com/apiv2/app/upload")
//             .post(requestBody)
//             .build()
//
//         val execute = OkHttpClient
//             .Builder()
//             .readTimeout(1, TimeUnit.MINUTES)
//             .writeTimeout(3, TimeUnit.MINUTES)
//             .build()
//             .newCall(request)
//             .execute()
//
//         val responseString = execute.body?.string()
//         if (responseString != null) {
//             val resBean = Moshi.Builder().build().adapter(ResBean::class.java).fromJson(responseString)
//             if (resBean != null) {
//                 val result = generateUpdateContent(resBean.data)
//
//                 //写入到上传日志文件中
//                 writeJournal(
//                     file.parent,
//                     resBean.data.buildBuildVersion,
//                     resBean.data.buildVersion,
//                     result
//                 )
//                 return result
//             }
//             return "上传失败..."
//         }else {
//             return "上传失败..."
//         }
//     }
//
//     private fun writeJournal(path: String, build: String?,apkVersion:String?, content: String) {
//         val file = File("${path}/${apkVersion}__build(${build})__${Util.getSystemTime()}.txt")
//         file.writeText(content)
//     }
//
//     private fun generateUpdateContent(resData: Data) : String{
//         return  """
// ========================result begin=======================
// 应用: ${resData.buildName}
// 版本: ${resData.buildVersion}
// 短连接: https://www.pgyer.com/${resData.buildShortcutUrl}
// 二维码地址: ${resData.buildQRCodeURL}
// 更新说明: ${resData.buildUpdateDescription}
// ========================result end=========================
//                 """.trimIndent()
//     }
// }