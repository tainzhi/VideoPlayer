package com.tainzhi.android.videoplayer.bean

import android.net.Uri

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/12 11:25
 * @description:
 **/

data class LocalVideo(
        var uri: Uri,
        var size: String,
        var data: String,
        var title: String,
        var duration: String,
        var resolution: String,
        var orientation: String,
        var dateAdded: String,
        var dateModified: String,
        var dateTaken: String,
        var bucketName: String
)