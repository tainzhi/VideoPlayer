package com.tainzhi.android.videoplayer.base.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * File:     BaseViewModel
 * Author:   tainzhi
 * Created:  2021/1/1 14:02
 * Mail:     QFQ61@qq.com
 * Description:
 */
open class BaseViewModel(private val coroutineDispatcherProvider: com.tainzhi.android.videoplayer.util.CoroutineDispatcherProvider) :
        ViewModel() {
    fun launchIO(task: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            withContext(coroutineDispatcherProvider.io) {
                task()
            }
        }
    }

    fun launch(task: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(coroutineDispatcherProvider.default) {
            task()
        }
    }
}