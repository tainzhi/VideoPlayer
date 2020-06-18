package com.tainzhi.android.videoplayer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tainzhi.android.common.base.ui.BaseViewModel

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/18 下午1:29
 * @description:
 **/

class MainViewModel: BaseViewModel() {
    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    fun updateToolbarTitle(title: String) {
        _title.postValue(title)
    }
}