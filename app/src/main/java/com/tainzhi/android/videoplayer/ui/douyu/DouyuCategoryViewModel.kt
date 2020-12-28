package com.tainzhi.android.videoplayer.ui.douyu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tainzhi.android.common.CoroutinesDispatcherProvider
import com.tainzhi.android.videoplayer.bean.DouyuGame
import com.tainzhi.android.videoplayer.network.ResultOf
import com.tainzhi.android.videoplayer.repository.DouyuRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 09:32
 * @description: 斗鱼分类页面
 **/
class DouyuCategoryViewModel(private val douyuRepository: DouyuRepository,
                             private val coroutineProvider: CoroutinesDispatcherProvider
) : ViewModel() {
    private val _games = MutableLiveData<List<DouyuGame>>()
    val games: LiveData<List<DouyuGame>>
        get() = _games


    fun getDouyuRooms() {
        viewModelScope.launch(coroutineProvider.io) {
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