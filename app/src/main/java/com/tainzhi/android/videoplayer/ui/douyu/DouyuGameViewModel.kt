package com.tainzhi.android.videoplayer.ui.douyu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tainzhi.android.common.CoroutinesDispatcherProvider
import com.tainzhi.android.videoplayer.bean.DouyuRoom
import com.tainzhi.android.videoplayer.network.State
import com.tainzhi.android.videoplayer.repository.DouyuRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DouyuGameViewModel(private val douyuRepository: DouyuRepository,
                         private val coroutinesDispatcherProvider: CoroutinesDispatcherProvider
) : ViewModel() {

    var isRefreshLoading = false

    private val _gameRooms = MutableLiveData<State<List<DouyuRoom>>>()
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
        viewModelScope.launch(coroutinesDispatcherProvider.io) {
            douyuRepository.getGameRooms(gameId, isRefresh).collect { result ->
                _gameRooms.postValue(result)
            }
        }
    }
}
