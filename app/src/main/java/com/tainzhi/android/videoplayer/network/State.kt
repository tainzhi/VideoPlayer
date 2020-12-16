package com.tainzhi.android.videoplayer.network

import androidx.lifecycle.MutableLiveData
import com.tainzhi.android.videoplayer.network.State.Success

/**
 * File:     Result
 * Author:   tainzhi
 * Created:  2020/12/3 19:30
 * Mail:     QFQ61@qq.com
 * Description: State Management for UI & Data.
 */

sealed class State<out T> {
    class Loading<T> : State<T>()

    data class Success<T>(val data: T) : State<T>()

    data class Error<T>(val message: String) : State<T>()

    companion object {

        /**
         * Returns [State.Loading] instance.
         */
        fun <T> loading() = Loading<T>()

        /**
         * Returns [State.Success] instance.
         * @param data Data to emit with status.
         */
        fun <T> success(data: T) =
                Success(data)

        /**
         * Returns [State.Error] instance.
         * @param message Description of failure.
         */
        fun <T> error(message: String) =
                Error<T>(message)
    }
}

/**
 * Updates value of [liveData] if [Result] is of type [Success]
 */
inline fun <reified T> State<T>.updateOnSuccess(liveData: MutableLiveData<T>) {
    if (this is Success) {
        liveData.value = data
    }
}
