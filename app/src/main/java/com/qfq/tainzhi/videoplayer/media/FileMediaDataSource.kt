package com.qfq.tainzhi.videoplayer.media

import tv.danmaku.ijk.media.player.misc.IMediaDataSource
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile

/**
 * Created by muqing on 2019/6/1.
 * Email: qfq61@qq.com
 */
class FileMediaDataSource constructor(file: File?) : IMediaDataSource {
    private var mFile: RandomAccessFile?
    private var mFileSize: Long
    
    @Throws(IOException::class)
    public override fun readAt(position: Long, buffer: ByteArray?, offset: Int, size: Int): Int {
        if (mFile.getFilePointer() != position) mFile.seek(position)
        if (size == 0) return 0
        return mFile.read(buffer, 0, size)
    }
    
    @Throws(IOException::class)
    public override fun getSize(): Long {
        return mFileSize
    }
    
    @Throws(IOException::class)
    public override fun close() {
        mFileSize = 0
        mFile.close()
        mFile = null
    }
    
    init {
        mFile = RandomAccessFile(file, "r")
        mFileSize = mFile.length()
    }
}