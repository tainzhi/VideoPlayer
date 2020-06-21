package com.tainzhi.android.videoplayer.repository

import android.content.ContentUris
import android.database.Cursor.FIELD_TYPE_STRING
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.tainzhi.android.common.base.BaseRepository
import com.tainzhi.android.common.util.FormatUtil.formatMediaDate
import com.tainzhi.android.common.util.FormatUtil.formatMediaSize
import com.tainzhi.android.videoplayer.App
import com.tainzhi.android.videoplayer.bean.LocalVideo

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/12 10:32
 * @description:
 **/

class LocalVideoRepository : BaseRepository() {

    fun getLocalVideoList(): List<LocalVideo> {
        val list = arrayListOf<LocalVideo>()
        val videoColumnsProjection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.DATE_TAKEN,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.WIDTH,
                MediaStore.Video.Media.HEIGHT,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.RESOLUTION
        )
        // 以添加时间倒序查询
        val queryOrder = MediaStore.Video.Media.DATE_ADDED + " DESC"
        App.CONTEXT.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoColumnsProjection, null, null, queryOrder)?.use { cursor ->
            // 不要使用 getColumnIndexOrThrow(), 因为某些columen会抛出异常, 导致循环退出, 而不能查询所有的条目
            val dataColumn = cursor.getColumnIndex(MediaStore.MediaColumns.DATA)
            val resolutionColumn = cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION)
            val dateTakenColumn = cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN)
            try {
                while (cursor.moveToNext()) {
                    // TODO: 2020/6/12  通过bucketName添加一一个列表选项, 先展示视频目录, 再展示目录下的视频
                    // 在android 9 (28) miui 11上, data为 绝对路径
                    // bucketName 为video所在而目录
                    // val dumpCursorString = DatabaseUtils.dumpCurrentRowToString(cursor)
                    val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                    val contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                    // 视频大小是以B为单位的整数数字, 故故需转成10KB, 10MB, 10GB
                    val size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE)).formatMediaSize()
                    var data = if (cursor.getType(dataColumn) == FIELD_TYPE_STRING) {
                                            cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                                        } else ""
                    val title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                    // 视频大小是以秒(s)为单位的整数数字, 故需转成00:00:00
                    val duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))
                    var resolution = ""
                    if (cursor.getType(resolutionColumn) == FIELD_TYPE_STRING) {
                        resolution = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION))
                    }
                    val orientation = "0"
                    // *1000, 因为DATE_ADDED and DATE_MODIFIED 为seconds
                    // DATE_TAKEN 为millis
                    val dateAdded = (cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED)) * 1000).formatMediaDate()
                    val dateModified = (cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED)) * 1000).formatMediaDate()
                    var dateTaken = ""
                    if (cursor.getType(dateTakenColumn) == FIELD_TYPE_STRING) {
                        dateTaken = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN)).formatMediaDate()
                    }
                    val bucketName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                    list += LocalVideo(contentUri, size, data, title, duration, resolution, orientation, dateAdded, dateModified, dateTaken, bucketName)
                }
            } catch (e: Exception) {
                Log.e("LocalVideoRepository", e.toString())
            } finally {
                return list
            }
        }
        return list
    }

    fun deleteVideo(uri: Uri) {
        try {
            val deleteId = App.CONTEXT.contentResolver.delete(uri, null, null)
        } catch (e: Exception) {
            throw(e)
        }
    }
}