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
import com.tainzhi.mediaspider.film.bean.SearchResultData
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

    fun getChannelData(channelId: String, page: Int) {
        viewModelScope.launch(dispatcherProvider.io) {
            movieRepository.movieManager.curUseSourceConfig().requestHomeChannelData(page, channelId) { channelData ->
                if (channelData == null) {
                    _channelLiveData.postValue(Result.successEndData(emptyList()))
                } else {
                    _channelLiveData.postValue(Result.success(channelData))
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

    private val _searchResult = MutableLiveData<List<SearchResultData>>()
    val searchResult: LiveData<List<SearchResultData>>
        get() = _searchResult

    private var searchCurPage = 1

    // FIXME: 2020/12/26  对于更多的搜索结果, 分页显示
    fun searchMovie(key: String) {
        movieRepository.movieManager.curUseSourceConfig().requestSearchData(key, searchCurPage) {
            viewModelScope.launch(dispatcherProvider.io) {
                movieRepository.movieManager.curUseSourceConfig().requestSearchData(key, searchCurPage) {
                    _searchResult.postValue(it)
                }
            }
        }
    }
}