package com.tainzhi.android.videoplayer.ui.play

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tainzhi.android.common.CoroutinesDispatcherProvider
import com.tainzhi.android.common.base.ui.BaseViewModel
import com.tainzhi.android.videoplayer.repository.DouyuRepository
import kotlinx.coroutines.withContext

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/20 17:35
 * @description:
 **/

class PlayDouyuViewModel(
        private val douyuRepository: DouyuRepository,
        private val coroutinesDispatcherProvider: CoroutinesDispatcherProvider
): BaseViewModel() {

    private val _roomCircuitId = MutableLiveData<String>()
    val roomCircuitId: LiveData<String>
        get() = _roomCircuitId

    val circuit1 = "http://tx2play1.douyucdn.cn/live/%s_550.flv"
    val circuit2 = "http://hdls1a.douyucdn.cn/live/%s.flv"
    val circuit5 = "https://tc-tct.douyucdn2.cn/dyliveflv1a/%s_550.flv"



    fun getRoomCircuit(id: String) {
        launch {
            withContext(coroutinesDispatcherProvider.io) {
                val result = douyuRepository.getRoomCircuitId(id)
                _roomCircuitId.postValue(String.format(circuit1, result))
            }
        }
    }
}