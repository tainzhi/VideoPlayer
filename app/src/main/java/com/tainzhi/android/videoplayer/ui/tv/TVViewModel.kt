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
            val result = tvRepository.loadTvs()
            emitData(result)
        }
    }

    /**
     * 获取每个卫视当前的直播节目
     */
    fun getTVProgram() {
        launch {
            withContext(dispatcherProvider.default) {
                val result = tvRepository.loadTvProgram()
            }
        }
    }

    fun getTVListAndProgram() {
        launch {
            withContext(dispatcherProvider.default) {
                val list = async { _tvList.value = tvRepository.loadTvs() }
                val program = async { tvRepository.loadTvProgram() }
            }
        }
    }


    private suspend fun emitData(data: List<Tv>) {
        withContext(dispatcherProvider.main) {
            _tvList.value = data
        }
    }

}