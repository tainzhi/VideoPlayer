package com.tainzhi.android.videoplayer.ui.douyu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tainzhi.android.common.CoroutinesDispatcherProvider
import com.tainzhi.android.common.base.Result
import com.tainzhi.android.common.base.ui.BaseViewModel
import com.tainzhi.android.videoplayer.bean.DouyuRoom
import com.tainzhi.android.videoplayer.repository.DouyuRepository
import kotlinx.coroutines.withContext

class DouyuGameViewModel(private val douyuRepository: DouyuRepository,
                         private val coroutinesDispatcherProvider: CoroutinesDispatcherProvider
) : BaseViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState : LiveData<UiState>
        get() = _uiState

    private var currentOffset = 0
    private var limit = 21 // 因为第一页加载21,方便形成首行横跨两格; 从第二页开始加载20个

    /**
     * 获取gameId下的直播房间
     *
     * @param gameId 直播游戏id
     * @param isRefresh 是否重新加载, true 刷新, 只加载第一个page; false, 加载下一个page
     */
    fun getGameRooms(gameId: String, isRefresh: Boolean = true) {
        launch {
            if (isRefresh) {
                currentOffset = 0
                limit = 21
            } else {
                limit = 20
            }
            emitUiState(showLoading = true)
            val result = douyuRepository.getGameRooms(gameId, currentOffset, limit)
            if (result is Result.Success) {
                val resultData = result.data
                if (resultData.size < limit) {
                    // 加载数据到底, 所有的数据都加载完了
                    emitUiState(showLoading = false, showSuccess = resultData, showEnd = true)
                    return@launch
                }
                currentOffset += limit
                emitUiState(showLoading = false, showSuccess = resultData, showEnd = false)
            } else if (result is Result.Error) {
                emitUiState(showLoading = false, showError = result.exception.message)
            } else if (result is Result.NetUnavailable) {
                emitUiState(showLoading = false, showError = result.exception.message)
            }
        }
    }


    private suspend fun emitUiState(

            showLoading: Boolean = false,
            showError: String? = null,
            showSuccess: List<DouyuRoom>? = null,
            showEnd: Boolean = false,
            isRefresh: Boolean = false
    ) {
        withContext(coroutinesDispatcherProvider.main) {
            val uiModel = UiState(showLoading, showError, showSuccess, showEnd, isRefresh)
            _uiState.value = uiModel
        }
    }
}

data class UiState(
        var showLoading: Boolean = false,
        var showError: String? = null,
        var showSuccess: List<DouyuRoom>? = null,
        var showEnd: Boolean = false,
        var isRefresh: Boolean = false
)
