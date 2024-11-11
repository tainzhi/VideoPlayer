package com.tainzhi.android.videoplayer.util

import androidx.lifecycle.LiveData

/**
 * @author:       tainzhi
 * @mail:         qfq61@qq.com
 * @date:         2020/4/9 10:28
 * @description:
 **/

class AbsentLiveData<T : Any?> private constructor(): LiveData<T>() {
    init {
        postValue(null)
    }

    companion object {
        fun <T>create(): LiveData<T> {
            return AbsentLiveData()
        }
    }
}