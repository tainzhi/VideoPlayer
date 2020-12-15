package com.tainzhi.android.videoplayer.livedatanet

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.liveData

/**
 * File:     NetworkBoundRepository
 * Author:   tainzhi
 * Created:  2020/12/7 20:55
 * Mail:     QFQ61@qq.com
 * Description:
 * A repository which provides resource from local database as well as remote end point.
 *
 * [RESULT] represents the type for database.
 * [REQUEST] represents the type for network.
 */
abstract class NetworkBoundResponse<RESULT, REQUEST> {
    fun asLiveData() = liveData<State<RESULT>> {
        try {
            // 1. 发送 Loading
            emit(State.Loading())
            // 2. 加载本地数据
            var localData = fetchFromLocal()
            if (localData == null || shouldFetch()) {
                // 3.1 本地数据加载失败或者本地数据已经过期, 从remote加载数据
                val remoteResponse = fetchFromRemote()
                if (isValidData(remoteResponse)) {
                    // 4. 从remote加载数据成功, 先保存到本地
                    saveRemoteData(remoteResponse)
                    // 5. 再从本地取出, 发送成功数据
                    localData = fetchFromLocal()
                    emit(State.success(localData))
                } else {
                    emit(State.error("fetched remote data is invalid"))
                }
            } else {
                // 3.2 本地数据没有过期, 发送刚才加载的本地数据
                emit(State.success(localData))
            }
        } catch (e: Exception) {
            // Retrofit service中接口函数用suspend, 那么 retrofit捕获的exception也会传递到此处
            emit(State.error(e.message ?: "NetworkBoundResponse get data failed"))
        }
    }

    open fun isValidData(data: REQUEST) = true

    protected abstract fun shouldFetch(): Boolean

    @WorkerThread
    protected abstract fun saveRemoteData(response: REQUEST)

    @MainThread
    protected abstract fun fetchFromLocal(): RESULT

    @MainThread
    protected abstract suspend fun fetchFromRemote(): REQUEST
}