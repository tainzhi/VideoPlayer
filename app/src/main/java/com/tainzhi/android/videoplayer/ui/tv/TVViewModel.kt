package com.tainzhi.android.videoplayer.ui.tv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.tainzhi.android.videoplayer.base.ui.BaseViewModel
import com.tainzhi.android.videoplayer.util.CoroutineDispatcherProvider
import com.tainzhi.android.videoplayer.App
import com.tainzhi.android.videoplayer.bean.Tv
import com.tainzhi.android.videoplayer.network.ResultOf
import com.tainzhi.android.videoplayer.network.doIfSuccess
import com.tainzhi.android.videoplayer.repository.TVRepository
import com.tainzhi.android.videoplayer.util.Start_UP_Create_Database
import com.tainzhi.mediaspider.spider.TvProgramBean
import kotlinx.coroutines.flow.collect

class TVViewModel(private val tVRepository: TVRepository,
                  dispatcherProvider: CoroutineDispatcherProvider
) : BaseViewModel(dispatcherProvider) {

    private val _tvList: MutableLiveData<List<Tv>> = MutableLiveData()
    val tvList: LiveData<List<Tv>>
        get() = _tvList

    private val _tvPrograms = MutableLiveData<ResultOf<Map<String, TvProgramBean>>>()
    val tvPrograms: LiveData<ResultOf<Map<String, TvProgramBean>>>
        get() = _tvPrograms

    val dataBaseStatus: LiveData<List<WorkInfo>>
        get() = WorkManager.getInstance(App.CONTEXT).getWorkInfosByTagLiveData(Start_UP_Create_Database)


    /**
     * 从数据库获取卫视列表
     */
    fun getTVList() {
        launchIO {
            tVRepository.loadTVs()
                    .doIfSuccess { _tvList.postValue(it) }
        }
    }

    /**
     * 获取每个卫视当前的直播节目
     */
    fun getTVProgram() {
        launchIO {
            tVRepository.getTvPrograms().collect {
                _tvPrograms.postValue(it)
            }
        }
    }
}