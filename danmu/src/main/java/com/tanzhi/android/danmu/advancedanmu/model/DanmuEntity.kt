package com.tanzhi.android.danmu.advancedanmu.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/10 11:25
 * @description:
 **/
 
sealed class DanmuEntity

@SuppressLint("ParcelCreator")
@Parcelize
data class DanmuUser(val avator: String, val name: String, val userId: String, val level: Int, val role: Int, val text: String, val richText: List<RichMessage>?): DanmuEntity(), Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class DanmuSystem(val avator: String, val name: String, val userId: String, val level: Int, val role: Int, val text: String, val richText: List<RichMessage>?): DanmuEntity(), Parcelable


@SuppressLint("ParcelCreator")
@Parcelize
data class RichMessage(val type: String, val content: String, val color: String, val extend: String, val giftId: Int): Parcelable
