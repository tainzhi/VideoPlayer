package com.tainzhi.android.videoplayer.ui.play

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tainzhi.android.common.CoroutinesDispatcherProvider
import com.tainzhi.android.videoplayer.network.Result
import com.tainzhi.android.videoplayer.repository.DouyuRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/20 17:35
 * @description:
 **/

class PlayDouyuViewModel(
        private val douyuRepository: DouyuRepository,
        private val coroutinesDispatcherProvider: CoroutinesDispatcherProvider
) : ViewModel() {

    private val _roomUrl = MutableLiveData<Result<String>>()
    val roomUrl
        get() = _roomUrl


    fun getRoomCircuit(id: String) {
        viewModelScope.launch(coroutinesDispatcherProvider.io) {
            douyuRepository.getRoomUrl(id).collect {
                _roomUrl.postValue(it)
            }
        }
    }
}