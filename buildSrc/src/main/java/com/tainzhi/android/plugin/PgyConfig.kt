package com.tainzhi.android.plugin

/**
 * File:     UpLoadPgy
 * Author:   tainzhi
 * Created:  2020/12/31 14:13
 * Mail:     QFQ61@qq.com
 * Description:
 */
open class PgyConfig {
    var apiKey = ""
    var flavor = "pgy" // 默认只上传pgy, 当然要确保要渠道flavor: pgy
    var buildType = "release" // 默认只上传release版本
    var updateDescription = ""
}