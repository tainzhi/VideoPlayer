package com.tainzhi.android.videoplayer.ui.tv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.tainzhi.android.common.CoroutinesDispatcherProvider
import com.tainzhi.android.videoplayer.App
import com.tainzhi.android.videoplayer.bean.Tv
import com.tainzhi.android.videoplayer.network.Result
import com.tainzhi.android.videoplayer.network.updateOnSuccess
import com.tainzhi.android.videoplayer.repository.TVRepository
import com.tainzhi.android.videoplayer.util.Start_UP_Create_Database
import com.tainzhi.android.videoplayer.spider.TvProgramBean
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TVViewModel(private val TVRepository: TVRepository,
                  private val dispatcherProvider: CoroutinesDispatcherProvider
) : ViewModel() {

    private val _tvList: MutableLiveData<List<Tv>> = MutableLiveData()
    val tvList: LiveData<List<Tv>>
        get() = _tvList

    private val _tvPrograms = MutableLiveData<Result<Map<String, TvProgramBean>>>()
    val tvPrograms: LiveData<Result<Map<String, TvProgramBean>>>
        get() = _tvPrograms

    val dataBaseStatus: LiveData<List<WorkInfo>>
        get() = WorkManager.getInstance(App.CONTEXT).getWorkInfosByTagLiveData(Start_UP_Create_Database)


    /**
     * 从数据库获取卫视列表
     */
    fun getTVList() {
        viewModelScope.launch(dispatcherProvider.io) {
            TVRepository.loadTVs()
                    .updateOnSuccess(_tvList)
        }
    }

    /**
     * 获取每个卫视当前的直播节目
     */
    fun getTVProgram() {
        viewModelScope.launch(dispatcherProvider.io) {
            TVRepository.getTvPrograms().collect {
                _tvPrograms.postValue(it)
            }
        }
    }

}