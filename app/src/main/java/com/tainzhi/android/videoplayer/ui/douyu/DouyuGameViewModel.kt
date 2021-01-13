package com.tainzhi.android.videoplayer.ui.douyu

import androidx.lifecycle.MutableLiveData
import com.tainzhi.android.common.BaseViewModel
import com.tainzhi.android.common.CoroutineDispatcherProvider
import com.tainzhi.android.videoplayer.bean.DouyuRoom
import com.tainzhi.android.videoplayer.network.ResultOf
import com.tainzhi.android.videoplayer.repository.DouyuRepository
import kotlinx.coroutines.flow.collect

class DouyuGameViewModel(private val douyuRepository: DouyuRepository,
                         coroutineDispatcherProvider: CoroutineDispatcherProvider
) : BaseViewModel(coroutineDispatcherProvider) {

    var isRefreshLoading = false

    private val _gameRooms = MutableLiveData<ResultOf<List<DouyuRoom>>>()
    val gameRooms
        get() = _gameRooms

    /**
     * 获取gameId下的直播房间
     *
     * @param gameId 直播游戏id
     * @param isRefresh 是否重新加载, true 刷新, 只加载第一个page; false, 加载下一个page
     */
    fun getGameRooms(gameId: String, isRefresh: Boolean = true) {
        isRefreshLoading = isRefresh
        launchIO {
            douyuRepository.getGameRooms(gameId, isRefresh).collect { result ->
                _gameRooms.postValue(result)
            }
        }
    }
}
