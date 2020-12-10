package com.tainzhi.android.videoplayer.ui.tv

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
import kotlinx.coroutines.flow.collect

class TVViewModel(private val tvRepository: TvRepository,
                  private val dispatcherProvider: CoroutinesDispatcherProvider
) : BaseViewModel() {

    private val _tvList: MutableLiveData<List<Tv>> = MutableLiveData()
    val tvList: LiveData<List<Tv>>
        get() = _tvList
    private val _tvPrograms = MutableLiveData<State<Map<String, TvProgramBean>>>()
    val tvPrograms: LiveData<State<Map<String, TvProgramBean>>>
        get() = _tvPrograms

    val dataBaseStatus: LiveData<List<WorkInfo>>
        get() = WorkManager.getInstance(App.CONTEXT).getWorkInfosByTagLiveData(Start_UP_Create_Database)


    /**
     * 从数据库获取卫视列表
     */
    fun getTVList() {
        launch {
            val result = tvRepository.loadTVs()
            _tvList.postValue(result)
        }
    }

    /**
     * 获取每个卫视当前的直播节目
     */
    fun getTVProgram() {
        launch {
            val tvPrograms = tvRepository.getTvPrograms()
            tvPrograms.collect {
                _tvPrograms.postValue(it)
            }
        }
    }

}