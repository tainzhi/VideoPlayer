package com.qfq.tainzhi.videoplayer.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.util.Log
import com.jakewharton.disklrucache.DiskLruCache
import java.io.*

/**
 * Created by Administrator on 2016/3/9.
 */
class DiskLruImageCache {
    private var mDiskCache: DiskLruCache? = null
    private var mCompressFormat: CompressFormat? = CompressFormat.JPEG
    private var mCompressQuality: Int = 70
    
    constructor(
            context: Context?, uniqueName: String?, diskCacheSize: Int,
            compressFormat: CompressFormat?, quality: Int) {
        try {
            val diskCacheDir: File? = getDiskCacheDir(context, uniqueName)
            mDiskCache = DiskLruCache.open(diskCacheDir, APP_VERSION, VALUE_COUNT, diskCacheSize.toLong())
            mCompressFormat = compressFormat
            mCompressQuality = quality
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    
    constructor(context: Context?, uniqueName: String?, diskCacheSize: Int) {
        try {
            val diskCacheDir: File? = getDiskCacheDir(context, uniqueName)
            mDiskCache = DiskLruCache.open(diskCacheDir, APP_VERSION, VALUE_COUNT, diskCacheSize.toLong())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    
    @Throws(IOException::class, FileNotFoundException::class)
    private fun writeBitmapToFile(bitmap: Bitmap?, editor: DiskLruCache.Editor?): Boolean {
        var out: OutputStream? = null
        try {
            out = BufferedOutputStream(editor.newOutputStream(0), IO_BUFFER_SIZE)
            return bitmap.compress(mCompressFormat, mCompressQuality, out)
        } finally {
            if (out != null) {
                out.close()
            }
        }
    }
    
    private fun getDiskCacheDir(context: Context?, uniqueName: String?): File? {
        
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        val cachePath: String? = if ((Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) ||
                !isExternalStorageRemovable()) getExternalCacheDir(context).getPath() else context.getCacheDir().getPath()
        return File(cachePath + File.separator + uniqueName)
    }
    
    fun put(key: String?, data: Bitmap?) {
        var editor: DiskLruCache.Editor? = null
        try {
            editor = mDiskCache.edit(key)
            if (editor == null) {
                return
            }
            if (writeBitmapToFile(data, editor)) {
                mDiskCache.flush()
                editor.commit()
                Log.d(TAG, "image put on disk cache " + key)
            } else {
                editor.abort()
                Log.e(TAG, "ERROR on: image put on disk cache " + key)
            }
        } catch (e: IOException) {
            Log.e(TAG, "ERROR on: image put on disk cache " + key)
            try {
                if (editor != null) {
                    editor.abort()
                }
            } catch (ignored: IOException) {
            }
        }
    }
    
    fun put(key: Int, bitmap: Bitmap?) {
        put(key.toString(), bitmap)
    }
    
    fun getBitmap(key: String?): Bitmap? {
        var bitmap: Bitmap? = null
        var snapshot: DiskLruCache.Snapshot? = null
        try {
            snapshot = mDiskCache.get(key)
            if (snapshot == null) {
                return null
            }
            val `in`: InputStream? = snapshot.getInputStream(0)
            if (`in` != null) {
                val buffIn: BufferedInputStream? = BufferedInputStream(`in`, IO_BUFFER_SIZE)
                bitmap = BitmapFactory.decodeStream(buffIn)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (snapshot != null) {
                snapshot.close()
            }
        }
        Log.d(TAG, if (bitmap == null) "" else "image read from disk " + key)
        return bitmap
    }
    
    fun getBitmap(key: Int): Bitmap? {
        return getBitmap(key.toString())
    }
    
    fun containsKey(key: String?): Boolean {
        var contained: Boolean = false
        var snapshot: DiskLruCache.Snapshot? = null
        try {
            snapshot = mDiskCache.get(key)
            contained = snapshot != null
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (snapshot != null) {
                snapshot.close()
            }
        }
        return contained
    }
    
    fun clearCache() {
        Log.d(TAG, "disk cache CLEARED")
        try {
            mDiskCache.delete()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    
    fun getCacheFolder(): File? {
        return mDiskCache.getDirectory()
    }
    
    companion object {
        private val APP_VERSION: Int = 1
        private val VALUE_COUNT: Int = 1
        private val IO_BUFFER_SIZE: Int = 8 * 1024
        private val TAG: String? = "VideoPlayer/DiskLruImageCache"
        fun isExternalStorageRemovable(): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                return Environment.isExternalStorageRemovable()
            }
            return true
        }
        
        fun getExternalCacheDir(context: Context?): File? {
            if (hasExternalCacheDir()) {
                return context.getExternalCacheDir()
            }
            
            // Before Froyo we need to construct the external cache dir ourselves
            val cacheDir: String? = "/Android/data/" + context.getPackageName() + "/cache/"
            return File(Environment.getExternalStorageDirectory().getPath() + cacheDir)
        }
        
        fun hasExternalCacheDir(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO
        }
    }
}