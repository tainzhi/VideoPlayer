package com.tainzhi.android.videoplayer.ui.tv

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.tainzhi.android.common.CoroutinesDispatcherProvider
import com.tainzhi.android.common.base.ui.BaseViewModel
import com.tainzhi.android.videoplayer.App
import com.tainzhi.android.videoplayer.bean.Tv
import com.tainzhi.android.videoplayer.livedatanet.State
import com.tainzhi.android.videoplayer.repository.TvRepository
import com.tainzhi.android.videoplayer.util.Start_UP_Create_Database
import com.tainzhi.mediaspider.bean.TvProgramBean
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

class TVViewModel(private val tvRepository: TvRepository,
                  private val dispatcherProvider: CoroutinesDispatcherProvider
) : BaseViewModel() {

    private val _tvList: MutableLiveData<List<Tv>> = MutableLiveData()
    val tvList: LiveData<List<Tv>>
        get() = _tvList
    private val _tvPrograms = MutableLiveData<Map<String, TvProgramBean>>()
    val tvPrograms: LiveData<Map<String, TvProgramBean>>
        get() = _tvPrograms

    val dataBaseStatus: LiveData<List<WorkInfo>>
        get() = WorkManager.getInstance(App.CONTEXT).getWorkInfosByTagLiveData(Start_UP_Create_Database)

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
            val tvPrograms = tvRepository.getTvPrograms()
            tvPrograms.collect { result ->
                when (result) {
                    is State.Success -> {
                        _tvPrograms.postValue(result.data)
                        Log.d("qfq", "state.success, ${result.data.size}")
                    }
                    is State.Error -> {
                        Log.d("qfq", "state.error")
                    }
                    is State.Loading -> {
                        Log.d("qfq", "state.loading")
                    }
                }
            }
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
            // val resultProgram = async { tvRepository.loadTVProgram() }
                val list = resultList.await()
            // val program = resultProgram.await()
            // list.forEach { it.broadingProgram = program[it.id] ?: ""}
                emitData(list)
            }
    }


    private suspend fun emitData(data: List<Tv>) {
        withContext(dispatcherProvider.main) {
            _tvList.value = data
        }
    }

}