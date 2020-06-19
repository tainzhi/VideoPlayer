package com.tainzhi.android.videoplayer.ui.tv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tainzhi.android.common.CoroutinesDispatcherProvider
import com.tainzhi.android.common.base.ui.BaseViewModel
import com.tainzhi.android.videoplayer.bean.Tv
import com.tainzhi.android.videoplayer.repository.TVRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class TVViewModel(private val tvRepository: TVRepository,
                  private val dispatcherProvider: CoroutinesDispatcherProvider
) : BaseViewModel() {
    private val _tvList: MutableLiveData<List<Tv>> = MutableLiveData()
    val tvList: LiveData<List<Tv>>
        get() = _tvList
    private val _tvPrograms = MutableLiveData<Map<String, String>>()
    val tvPrograms: LiveData<Map<String, String>>
        get() = _tvPrograms

    /**
     * 从数据库获取卫视列表
     */
    fun getTVList() {
        launch {
            val result = tvRepository.loadTVs()
            emitData(result)
        }
    }

    /**
     * 获取每个卫视当前的直播节目
     */
    fun getTVProgram() {
        launch {
            val tvPrograms = tvRepository.loadTVProgram()
            _tvPrograms.postValue(tvPrograms)
        }
    }

    /**
     * 从本地数据库获取卫视列表的同时, 请求网络电视猫获取改卫视正在播放的节目
     * 从本地读取tv列表比网络请求获取tv正在播放的节目快很多
     * 不能把两者请求结果同步处理
     */
    fun getTVListAndProgram() {
        launch {
                val resultList = async {  tvRepository.loadTVs() }
                val resultProgram = async { tvRepository.loadTVProgram() }
                val list = resultList.await()
                val program = resultProgram.await()
                list.forEach { it.broadingProgram = program[it.id] ?: ""}
                emitData(list)
            }
    }


    private suspend fun emitData(data: List<Tv>) {
        withContext(dispatcherProvider.main) {
            _tvList.value = data
        }
    }

}