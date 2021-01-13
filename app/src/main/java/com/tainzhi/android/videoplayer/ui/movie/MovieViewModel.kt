package com.tainzhi.android.videoplayer.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.orhanobut.logger.Logger
import com.tainzhi.android.common.BaseViewModel
import com.tainzhi.android.common.CoroutineDispatcherProvider
import com.tainzhi.android.videoplayer.network.ResultOf
import com.tainzhi.android.videoplayer.repository.MovieRepository
import com.tainzhi.mediaspider.movie.bean.Classify
import com.tainzhi.mediaspider.movie.bean.DetailData
import com.tainzhi.mediaspider.movie.bean.HomeChannelData
import com.tainzhi.mediaspider.movie.bean.SearchResultData

/**
 * File:     MovieViewModel
 * Author:   tainzhi
 * Created:  2020/12/21 07:52
 * Mail:     QFQ61@qq.com
 * Description:
 */
class MovieViewModel(
        private val movieRepository: MovieRepository,
        dispatcherProvider: CoroutineDispatcherProvider,
) : BaseViewModel(dispatcherProvider) {

    private val _classifyLiveData = MutableLiveData<List<Classify>>()
    val classifyListLiveData: LiveData<List<Classify>>
        get() = _classifyLiveData

    private val _channelLiveData = MutableLiveData<ResultOf<List<HomeChannelData>>>()
    val channelListLiveData: LiveData<ResultOf<List<HomeChannelData>>>
        get() = _channelLiveData

    private val _movie = MutableLiveData<DetailData>()
    val movie: LiveData<DetailData>
        get() = _movie

    fun getHomeData() {
        launchIO {

            movieRepository.movieManager.curUseSourceConfig().requestHomeData { homeData ->
                _classifyLiveData.postValue(homeData?.classifyList)
            }
        }
    }

    fun getChannelData(channelId: String, page: Int) {
        launchIO {
            movieRepository.movieManager.curUseSourceConfig().requestHomeChannelData(page, channelId) { channelData ->
                if (channelData.isNullOrEmpty()) {
                    _channelLiveData.postValue(ResultOf.successEndData(emptyList()))
                } else {
                    _channelLiveData.postValue(ResultOf.success(channelData))
                }
            }
        }
    }

    fun getMovieDetail(id: String) {
        launchIO {
            movieRepository.movieManager.curUseSourceConfig().requestDetailData(id) { movie ->
                _movie.postValue(movie)
            }
        }
    }

    val movieSource = liveData {
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

    private val _searchResult = MutableLiveData<ResultOf<List<SearchResultData>>>()
    val searchResult: LiveData<ResultOf<List<SearchResultData>>>
        get() = _searchResult

    fun searchMovie(key: String, page: Int) {
        launchIO {
            movieRepository.movieManager.curUseSourceConfig().requestSearchData(key, page) {
                if (it.isNullOrEmpty()) {
                    _searchResult.postValue(ResultOf.successEndData(emptyList()))
                } else {
                    _searchResult.postValue(ResultOf.success(it))
                }
            }
        }
    }
}