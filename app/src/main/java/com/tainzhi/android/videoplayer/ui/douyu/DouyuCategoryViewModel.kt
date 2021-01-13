package com.tainzhi.android.videoplayer.ui.douyu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tainzhi.android.common.BaseViewModel
import com.tainzhi.android.common.CoroutineDispatcherProvider
import com.tainzhi.android.videoplayer.bean.DouyuGame
import com.tainzhi.android.videoplayer.network.ResultOf
import com.tainzhi.android.videoplayer.repository.DouyuRepository
import kotlinx.coroutines.flow.collect

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 09:32
 * @description: 斗鱼分类页面
 **/
class DouyuCategoryViewModel(private val douyuRepository: DouyuRepository,
                             coroutineProvider: CoroutineDispatcherProvider
) : BaseViewModel(coroutineProvider) {
    private val _games = MutableLiveData<List<DouyuGame>>()
    val games: LiveData<List<DouyuGame>>
        get() = _games


    fun getDouyuRooms() {
        launchIO {
            douyuRepository.getAllGames().collect {
                when (it) {
                    is ResultOf.Success -> {
                        _games.postValue(it.data)
                    }
                    else -> Unit
                }
            }
        }
    }

}