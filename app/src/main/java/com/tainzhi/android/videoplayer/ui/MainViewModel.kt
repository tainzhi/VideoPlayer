package com.tainzhi.android.videoplayer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/18 下午1:29
 * @description:
 **/

class MainViewModel : ViewModel() {
    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    private val _showCenterTitle = MutableLiveData<Boolean>()
    val showCenterTitle: LiveData<Boolean>
        get() = _showCenterTitle

    private val _showSearchView = MutableLiveData<Boolean>()
    val showSearchView: LiveData<Boolean>
        get() = _showSearchView

    private val _searchString = MutableLiveData<String>()
    val searchString: LiveData<String>
        get() = _searchString

    fun updateToolbarCenterTitle(title: String) {
        _title.postValue(title)
    }

    fun updateToolbarCenterTitleVisibility(visible: Boolean) {
        _showCenterTitle.postValue(visible)
    }

    fun updateToolbarSearchView(visible: Boolean) {
        _showSearchView.postValue(visible)
    }

    fun postSearchString(search: String) {
        _searchString.postValue(search)
    }
}