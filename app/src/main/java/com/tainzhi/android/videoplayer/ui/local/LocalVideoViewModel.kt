package com.tainzhi.android.videoplayer.ui.local

import androidx.lifecycle.MutableLiveData
import com.tainzhi.android.common.CoroutinesDispatcherProvider
import com.tainzhi.android.common.base.ui.BaseViewModel
import com.tainzhi.android.videoplayer.bean.LocalVideo
import com.tainzhi.android.videoplayer.repository.LocalVideoRepository
import kotlinx.coroutines.withContext

class LocalVideoViewModel(private val localVideoRepository: LocalVideoRepository,
                          private val dispatcherProvider: CoroutinesDispatcherProvider
) : BaseViewModel() {
    private val _localVideoList = MutableLiveData<List<LocalVideo>>()
    val localVideoList
        get() = _localVideoList

    fun getLocalVideos() {
        launch {
            val result = localVideoRepository.getLocalVideoList()
            emitData(result)
        }
    }

    private suspend fun emitData(videos: List<LocalVideo>) {
        withContext(dispatcherProvider.main) {
            _localVideoList.value = videos
        }
    }
}