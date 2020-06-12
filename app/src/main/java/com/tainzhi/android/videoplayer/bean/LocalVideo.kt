package com.tainzhi.android.videoplayer.bean

import android.graphics.Bitmap
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
        var title: String,
        var duration: Long,
        var resolution: String,
        var orientation: String,
        var dateTaken: String,
        var dateCreated: String,
        var dateModified: String,
        var thumbnail: Bitmap?
)