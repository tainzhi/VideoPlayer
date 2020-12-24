package com.tainzhi.android.videoplayer.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import com.tainzhi.android.common.CoroutinesDispatcherProvider
import com.tainzhi.android.videoplayer.network.Result
import com.tainzhi.android.videoplayer.repository.MovieRepository
import com.tainzhi.mediaspider.film.bean.Classify
import com.tainzhi.mediaspider.film.bean.DetailData
import com.tainzhi.mediaspider.film.bean.HomeChannelData
import kotlinx.coroutines.launch

/**
 * File:     MovieViewModel
 * Author:   tainzhi
 * Created:  2020/12/21 07:52
 * Mail:     QFQ61@qq.com
 * Description:
 */
class MovieViewModel(
        private val movieRepository: MovieRepository,
        private val dispatcherProvider: CoroutinesDispatcherProvider,
) : ViewModel() {

    // 用于标记Adapter是否重置数据, 还是添加数据
    var isRefreshLoading = false

    private val _classifyLiveData = MutableLiveData<List<Classify>>()
    val classifyListLiveData: LiveData<List<Classify>>
        get() = _classifyLiveData

    private val _channelLiveData = MutableLiveData<Result<List<HomeChannelData>>>()
    val channelListLiveData: LiveData<Result<List<HomeChannelData>>>
        get() = _channelLiveData

    private val _movie = MutableLiveData<DetailData>()
    val movie: LiveData<DetailData>
        get() = _movie

    fun getHomeData() {
        viewModelScope.launch(dispatcherProvider.io) {

            movieRepository.movieManager.curUseSourceConfig().requestHomeData { homeData ->
                _classifyLiveData.postValue(homeData?.classifyList)
            }
        }
    }

    private var channelCurPage = 1
    fun getChannelData(channelId: String, isRefresh: Boolean = false) {
        isRefreshLoading = isRefresh
        if (isRefresh) {
            channelCurPage = 1
        } else {
            channelCurPage++
        }
        viewModelScope.launch(dispatcherProvider.io) {
            movieRepository.movieManager.curUseSourceConfig().requestHomeChannelData(channelCurPage, channelId) { channelData ->
                if (channelData == null) {
                    _channelLiveData.postValue(Result.successEndData(emptyList()))
                } else {
                    _channelLiveData.postValue(Result.successEndData(channelData))
                }
            }
        }
    }

    fun getMovieDetail(id: String) {
        viewModelScope.launch(dispatcherProvider.io) {
            movieRepository.movieManager.curUseSourceConfig().requestDetailData(id) { movie ->
                _movie.postValue(movie)
            }
        }
    }

    val movieSource = liveData<List<Pair<String, String>>> {
        emit(movieRepository.getMovieSourceList())
    }

    var defaultSourceKey = movieRepository.movieManager.defaultSourceKey

    fun changeMovieSource(sourceKey: String?) {
        if (sourceKey != null) {
            movieRepository.movieManager.defaultSourceKey = sourceKey
        } else {
            Logger.e("sourceKey is null")
        }
    }
}