package com.tainzhi.android.videoplayer.network

import androidx.lifecycle.MutableLiveData
import com.tainzhi.android.videoplayer.network.Result.Success

/**
 * File:     Result
 * Author:   tainzhi
 * Created:  2020/12/3 19:30
 * Mail:     QFQ61@qq.com
 * Description: Result Management for UI & Data.
 */

sealed class Result<out T> {
    class Loading<T> : Result<T>()

    data class Success<T>(val data: T) : Result<T>()

    data class SuccessEndData<T>(val data: T) : Result<T>()

    data class Error<T>(val message: String) : Result<T>()

    companion object {

        /**
         * Returns [Result.Loading] instance.
         */
        fun <T> loading() = Loading<T>()

        /**
         * Returns [Result.Success] instance.
         * @param data Data to emit with status.
         */
        fun <T> success(data: T) =
                Success(data)

        /**
         * 返回最后一组数据, 没有更多的数据了
         */
        fun <T> successEndData(data: T) =
                SuccessEndData(data)

        /**
         * Returns [Result.Error] instance.
         * @param message Description of failure.
         */
        fun <T> error(message: String) =
                Error<T>(message)
    }
}

/**
 * Updates value of [liveData] if [Result] is of type [Success]
 */
inline fun <reified T> Result<T>.updateOnSuccess(liveData: MutableLiveData<T>) {
    if (this is Success) {
        liveData.postValue(data)
    }
}

val <T> Result<T>.data: T?
    get() = (this as? Success)?.data
