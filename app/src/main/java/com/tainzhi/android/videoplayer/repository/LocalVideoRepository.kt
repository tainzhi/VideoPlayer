package com.tainzhi.android.videoplayer.repository

import android.content.ContentUris
import android.database.Cursor.FIELD_TYPE_STRING
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Images.Thumbnails.MICRO_KIND
import android.util.Log
import android.util.Size
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
                    val resolutionColumn = cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION)
                    val dateTakenColumn = cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN)
                    try {
                        while (cursor.moveToNext()) {
                            val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                            val contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                            // 视频大小是以B为单位的整数数字, 故故需转成10KB, 10MB, 10GB
                            val _size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                            val size = FormatUtil.formatMediaSize(_size)
                            val title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                            // 视频大小是以秒(s)为单位的整数数字, 故需转成00:00:00
                            val duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))
                            var resolution = ""
                            if (cursor.getType(resolutionColumn) == FIELD_TYPE_STRING) {
                                resolution = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION))
                            }
                            // TODO: 2020/6/12 通过resolution判断orientation
                            val orientation = "0"
                            val dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED))
                            val dateModified = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED))
                            var dateTaken = ""
                            if (cursor.getType(dateTakenColumn) == FIELD_TYPE_STRING) {
                                dateTaken = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN))
                            }
                            var thumbnail: Bitmap? = null
                            // if (cursor.getType(cursor.getColumnIndex(MediaStore.Video.Media.MINI_THUMB_MAGIC)) == FIELD_TYPE_INTEGER) {
                                // Load thumbnail of a specific media item.
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    thumbnail =
                                            App.CONTEXT.contentResolver.loadThumbnail(
                                                    contentUri, Size(320, 240), null)
                                } else {
                                    thumbnail = MediaStore.Video.Thumbnails.getThumbnail(App.CONTEXT.contentResolver, id, MICRO_KIND,
                                            BitmapFactory.Options())
                                }
                            // }
                            list += LocalVideo(contentUri, size, title, duration, resolution, orientation, dateTaken, dateAdded, dateModified, thumbnail)
                        }
                    } catch (e: Exception) {
                        Log.e("LocalVideoRepository", e.toString())
                    } finally {
                        return list
                    }
                }
        return list
    }
}