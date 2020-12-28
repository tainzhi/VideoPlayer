package com.tainzhi.android.videoplayer.network

import com.tainzhi.android.videoplayer.network.ResultOf.Success

/**
 * File:     Result
 * Author:   tainzhi
 * Created:  2020/12/3 19:30
 * Mail:     QFQ61@qq.com
 * Description: Result Management for UI & Data.
 */

// 为什么 Result --> ResultOf了
// 因为kotlin.Result的存在, 使得代码提示混乱, 为了区分, 改成ResultOf
sealed class ResultOf<out T> {
    class Loading<T> : ResultOf<T>()

    data class Success<T>(val data: T) : ResultOf<T>()

    data class SuccessEndData<T>(val data: T) : ResultOf<T>()

    data class Error<T>(val message: String, val throwable: Throwable? = null) : ResultOf<T>()

    companion object {

        /**
         * Returns [ResultOf.Loading] instance.
         */
        fun <T> loading() = Loading<T>()

        /**
         * Returns [ResultOf.Success] instance.
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
         * Returns [ResultOf.Error] instance.
         * @param message Description of failure.
         */
        fun <T> error(message: String) =
                Error<T>(message)
    }
}

val <T> ResultOf<T>.data: T?
    get() = (this as? Success)?.data

inline fun <reified T> ResultOf<T>.doIfError(callback: (error: String?, throwable: Throwable?) -> Unit) {
    if (this is ResultOf.Error) {
        callback(message, throwable)
    }
}

inline fun <reified T> ResultOf<T>.doIfSuccess(callback: (value: T) -> Unit) {
    if (this is ResultOf.Success) {
        callback(data)
    }
}

// inline fun <reified T, reified R> ResultOf<T>.map(transform: (T) -> R): ResultOf<R> {
//     return when (this) {
//         is ResultOf.Success -> ResultOf.success(transform(data))
//         is ResultOf.Loading -> ResultOf.loading()
//         else -> ResultOf.error(this.message)
//     }
// }