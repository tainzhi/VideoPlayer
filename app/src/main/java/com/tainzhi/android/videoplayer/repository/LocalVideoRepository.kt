package com.tainzhi.android.videoplayer.repository

import android.content.ContentUris
import android.provider.MediaStore
import com.tainzhi.android.common.base.BaseRepository
import com.tainzhi.android.common.util.FormatUtil
import com.tainzhi.android.videoplayer.App
import com.tainzhi.android.videoplayer.bean.LocalVideo

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/12 10:32
 * @description:
 **/

class LocalVideoRepository: BaseRepository() {

    fun getLocalVideoList() : List<LocalVideo> {
        val list = arrayListOf<LocalVideo>()
        val videoColumnsProjection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.RESOLUTION,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.DATE_TAKEN
        )
        App.CONTEXT.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                videoColumnsProjection, null, null, null)?.
                use { cursor ->
                    // 不要使用 getColumnIndexOrThrow(), 因为某些columen会抛出异常, 导致循环退出, 而不能查询所有的条目
                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                        val contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                        // 视频大小是以B为单位的整数数字, 故故需转成10KB, 10MB, 10GB
                        val _size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                        val size = FormatUtil.formatMediaSize(_size)
                        val title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                        // 视频大小是以秒(s)为单位的整数数字, 故需转成00:00:00
                        val duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))
                        val resolution = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION))
                        // TODO: 2020/6/12 通过resolution判断orientation
                        val orientation = "0"
                        val dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED))
                        val dateModified = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED))
                        val dateTaken = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN))

                        list += LocalVideo(contentUri, size, title,duration, resolution, orientation,dateTaken,dateAdded, dateModified)
                    }
                }
        return list
    }
}