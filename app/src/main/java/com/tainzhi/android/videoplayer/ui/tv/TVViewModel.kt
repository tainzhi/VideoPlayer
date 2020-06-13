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
     * 获取tvId卫视的直播源线路中的第一个
     */
    fun getTVCircuit(tvId: String) : String {
        val tvSources =  tvRepository.loadTVSource(tvId)
        return if (tvSources.isEmpty()) "" else tvSources[0]
    }

    /**
     * 获取每个卫视当前的直播节目
     */
    fun getTVProgram() {
        launch {
            withContext(dispatcherProvider.default) {
                val result = tvRepository.loadTVProgram()
            }
        }
    }

    /**
     * 从本地数据库获取卫视列表的同时, 请求网络电视猫获取改卫视正在播放的节目
     * todo: 等效于同步实现. 更好的方式是在Adapter中每隔固定时间请求网络, 自动更新该卫视的正在直播节目
     */
    fun getTVListAndProgram() {
        launch {
            withContext(dispatcherProvider.default) {
                val resultList = async {  tvRepository.loadTVs() }
                val resultProgram = async { tvRepository.loadTVProgram() }
                val list = resultList.await()
                val program = resultProgram.await()
                list.forEach { it.broadingProgram = program[it.id] ?: ""}
                emitData(list)
                }
            }
    }


    private suspend fun emitData(data: List<Tv>) {
        withContext(dispatcherProvider.main) {
            _tvList.value = data
        }
    }

}