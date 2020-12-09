package com.tainzhi.android.videoplayer.livedatanet

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import retrofit2.Response

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
    fun asFlow() = flow<State<RESULT>> {
        emit(State.Loading())

        var localData = fetchFromLocal()
        if (localData == null || shouldFetch()) {
            val apiResponse = fetchFromRemote()
            val apiBody = apiResponse.body()
            if (apiResponse.isSuccessful && apiBody != null) {
                saveRemoteData(apiBody)
            } else {
                emit(State.error(apiResponse.message()))
            }
            localData = fetchFromLocal()
        }
        emit(State.success(localData))

        // todo 如果不delay, 为啥会出现数据出现然后, 闪消失
    }.onEach { delay(20L) }.catch { e ->
        Log.d("NetworkBoundRepository", "data repository error", e)
        emit(State.error("NetworkBoundRepository error! cannot get data from local db or db: ${e.message}"))
        e.message?.let { Log.e("NetworkBoundRepository", it) }
    }

    protected abstract fun shouldFetch(): Boolean

    @WorkerThread
    protected abstract suspend fun saveRemoteData(response: REQUEST)

    @MainThread
    protected abstract fun fetchFromLocal(): RESULT

    @MainThread
    protected abstract suspend fun fetchFromRemote(): Response<REQUEST>
}