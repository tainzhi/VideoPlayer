package com.tainzhi.android.videoplayer.ui.douyu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tainzhi.android.common.CoroutinesDispatcherProvider
import com.tainzhi.android.common.base.Result
import com.tainzhi.android.common.base.ui.BaseViewModel
import com.tainzhi.android.videoplayer.bean.DouyuGame
import com.tainzhi.android.videoplayer.repository.DouyuRepository
import kotlinx.coroutines.withContext

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 09:32
 * @description: 斗鱼分类页面
 **/
class DouyuCategoryViewModel(private val douyuRepository: DouyuRepository,
                             private val coroutineProvider: CoroutinesDispatcherProvider
) : BaseViewModel() {
    private val _games = MutableLiveData<List<DouyuGame>>()
    val games: LiveData<List<DouyuGame>>
        get() = _games


    fun getDouyuRooms() {
        launch {
            val result = douyuRepository.getAllGames()
            if (result is Result.Success) {
                emitData(result.data)
            }
        }
    }

    private suspend fun emitData(data: List<DouyuGame>) {
        withContext(coroutineProvider.main) {
            _games.value = data
        }
    }
}