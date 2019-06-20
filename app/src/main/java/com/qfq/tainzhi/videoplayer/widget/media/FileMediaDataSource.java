package com.qfq.tainzhi.videoplayer.widget.media;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import tv.danmaku.ijk.media.player.misc.IMediaDataSource;

/**
 * Created by muqing on 2019/6/1.
 * Email: qfq61@qq.com
 */
public class FileMediaDataSource implements IMediaDataSource {
    private RandomAccessFile mFile;
    private long mFileSize;
    
    public FileMediaDataSource(File file) throws IOException {
        mFile = new RandomAccessFile(file, "r");
        mFileSize = mFile.length();
    }
    
    @Override
    public int readAt(long position, byte[] buffer, int offset, int size) throws IOException {
        if (mFile.getFilePointer() != position)
            mFile.seek(position);
        
        if (size == 0)
            return 0;
        
        return mFile.read(buffer, 0, size);
    }
    
    @Override
    public long getSize() throws IOException {
        return mFileSize;
    }
    
    @Override
    public void close() throws IOException {
        mFileSize = 0;
        mFile.close();
        mFile = null;
    }
}
