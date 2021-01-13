package com.tainzhi.android.videoplayer.ui.play

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tainzhi.android.common.CoroutineDispatcherProvider
import com.tainzhi.android.videoplayer.network.ResultOf
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
        private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    private val _roomUrl = MutableLiveData<ResultOf<String>>()
    val roomUrl
        get() = _roomUrl


    fun getRoomCircuit(id: String) {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            douyuRepository.getRoomUrl(id).collect {
                _roomUrl.postValue(it)
            }
        }
    }
}