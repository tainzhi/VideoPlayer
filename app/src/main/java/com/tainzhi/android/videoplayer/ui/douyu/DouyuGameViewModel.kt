package com.tainzhi.android.videoplayer.ui.douyu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tainzhi.android.common.CoroutinesDispatcherProvider
import com.tainzhi.android.common.base.Result
import com.tainzhi.android.common.base.ui.BaseViewModel
import com.tainzhi.android.videoplayer.bean.DouyuRoom
import com.tainzhi.android.videoplayer.repository.DouyuRepository
import com.tanzhi.mediaspider.DouyuSpider
import kotlinx.coroutines.withContext

class DouyuGameViewModel(private val douyuRepository: DouyuRepository,
                         private val coroutinesDispatcherProvider: CoroutinesDispatcherProvider
) : BaseViewModel() {

    private val _rooms =  MutableLiveData<List<DouyuRoom>>()
    val rooms: LiveData<List<DouyuRoom>>
        get() = _rooms


    fun getGameRooms(gameId: String) {
        launch {
            val result = douyuRepository.getGameRooms(gameId)
            if (result is Result.Success) {
                emitData(result.data)
            }
        }
    }

    /**
     * 获取直播间roomId的播放线路
     */
    fun getRoomCircuit(roomId: String): String {
        return DouyuSpider.getInstance().getRoomLive(roomId)
    }

    private suspend fun emitData(data: List<DouyuRoom>) {
        withContext(coroutinesDispatcherProvider.main) {
            _rooms.value = data
        }
    }

}