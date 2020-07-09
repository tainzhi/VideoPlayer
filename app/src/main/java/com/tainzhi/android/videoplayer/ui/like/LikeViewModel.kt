package com.tainzhi.android.videoplayer.ui.like

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tainzhi.android.common.CoroutinesDispatcherProvider
import com.tainzhi.android.common.base.ui.BaseViewModel
import com.tainzhi.android.videoplayer.repository.PreferenceRepository

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/9 10:09
 * @description:
 **/

class LikeViewModel(private val preferenceRepository: PreferenceRepository,
                    private val dispatcherProvider: CoroutinesDispatcherProvider
) : BaseViewModel() {

    private val _advertising = MutableLiveData<Boolean>()
    val advertising: LiveData<Boolean>
        get() = _advertising

    init {
        launch {
            _advertising.postValue(preferenceRepository.advertising)
        }
    }

    fun toggleAdvertising(checked: Boolean) {
        _advertising.postValue(checked)
        preferenceRepository.advertising = checked
    }
}