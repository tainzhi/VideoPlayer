package com.tainzhi.android.videoplayer.livedatanet

import androidx.annotation.IntDef

/**
 * File:     Resource
 * Author:   tainzhi
 * Created:  2020/12/3 19:30
 * Mail:     QFQ61@qq.com
 * Description:
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.EXPRESSION)
@MustBeDocumented
@IntDef(SUCCESS, ERROR, LOADING)
annotation class Status


const val SUCCESS = 0
const val ERROR = 1
const val LOADING = 2

/**
 * A generic class that holds a value with its loading status
 */
data class Resource<out T>(@Status val status: Int, val data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(LOADING, data, null)
        }
    }
}

