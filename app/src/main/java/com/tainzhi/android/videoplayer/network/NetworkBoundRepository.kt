package com.tainzhi.android.videoplayer.network

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.orhanobut.logger.Logger
import com.tainzhi.android.videoplayer.network.ResultOf.Loading
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

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
abstract class NetworkBoundRepository<RESULT, REQUEST> {
    fun asFlow() = flow<ResultOf<RESULT>> {
        // 1. 发送 Loading
        emit(Loading())
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
                emit(ResultOf.success(localData))
            } else {
                emit(ResultOf.error("fetched remote data is invalid"))
            }
        } else {
            // 3.2 本地数据没有过期, 发送刚才加载的本地数据
            emit(ResultOf.success(localData))
        }

        // todo 如果不delay, 为啥会出现数据出现然后, 闪消失
    }.onEach {
        delay(50L)
    }.catch { e ->
        Logger.e(e.toString())
        emit(ResultOf.error(e.toString()))
    }

    open fun isValidData(data: REQUEST) = true

    protected abstract fun shouldFetch(): Boolean

    @WorkerThread
    protected abstract suspend fun saveRemoteData(response: REQUEST)

    @MainThread
    protected abstract suspend fun fetchFromLocal(): RESULT

    @MainThread
    protected abstract suspend fun fetchFromRemote(): REQUEST
}