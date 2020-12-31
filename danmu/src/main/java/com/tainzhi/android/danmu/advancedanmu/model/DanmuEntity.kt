package com.tainzhi.android.danmu.advancedanmu.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/10 11:25
 * @description:
 **/

@SuppressLint("ParcelCreator")
@Parcelize
data class DanmuEntity(val avatar: String, val name: String, val userId: String, val level: Int, val role: Int, val text: String) : Parcelable
