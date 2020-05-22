package com.tanzhi.qmediaplayer

import android.net.Uri

/**
 * @author: tainzhi
 * @mail: qfq61@qq.com
 * @date: 2019-11-10 19:34
 * @description:
 */
class DataSource {
    var url: String? = null
    var title: String? = null
    var uri: Uri? = null

    constructor(url: String?, title: String?) {
        this.url = url
        this.title = title
    }

    constructor(uri: Uri?) {
        this.uri = uri
    }

}