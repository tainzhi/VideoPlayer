package com.qfq.tainzhi.videoplayer.media

import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import tv.danmaku.ijk.media.player.MediaPlayerProxy
import tv.danmaku.ijk.media.player.TextureMediaPlayer

/**
 * Created by muqing on 2019/6/1.
 * Email: qfq61@qq.com
 */
object MediaPlayerCompat {
    fun getName(mp: IMediaPlayer?): String? {
        if (mp == null) {
            return "null"
        } else if (mp is TextureMediaPlayer) {
            val sb: StringBuilder? = StringBuilder("TextureMediaPlayer <")
            val internalMediaPlayer: IMediaPlayer? = (mp as TextureMediaPlayer?).getInternalMediaPlayer()
            if (internalMediaPlayer == null) {
                sb.append("null>")
            } else {
                sb.append(internalMediaPlayer.javaClass.getSimpleName())
                sb.append(">")
            }
            return sb.toString()
        } else {
            return mp.javaClass.getSimpleName()
        }
    }
    
    fun getIjkMediaPlayer(mp: IMediaPlayer?): IjkMediaPlayer? {
        var ijkMediaPlayer: IjkMediaPlayer? = null
        if (mp == null) {
            return null
        }
        if (mp is IjkMediaPlayer) {
            ijkMediaPlayer = mp as IjkMediaPlayer?
        } else if (mp is MediaPlayerProxy && (mp as MediaPlayerProxy?).getInternalMediaPlayer() is IjkMediaPlayer) {
            ijkMediaPlayer = (mp as MediaPlayerProxy?).getInternalMediaPlayer() as IjkMediaPlayer?
        }
        return ijkMediaPlayer
    }
    
    fun selectTrack(mp: IMediaPlayer?, stream: Int) {
        val ijkMediaPlayer: IjkMediaPlayer? = getIjkMediaPlayer(mp)
        if (ijkMediaPlayer == null) return
        ijkMediaPlayer.selectTrack(stream)
    }
    
    fun deselectTrack(mp: IMediaPlayer?, stream: Int) {
        val ijkMediaPlayer: IjkMediaPlayer? = getIjkMediaPlayer(mp)
        if (ijkMediaPlayer == null) return
        ijkMediaPlayer.deselectTrack(stream)
    }
    
    fun getSelectedTrack(mp: IMediaPlayer?, trackType: Int): Int {
        val ijkMediaPlayer: IjkMediaPlayer? = getIjkMediaPlayer(mp)
        if (ijkMediaPlayer == null) return -1
        return ijkMediaPlayer.getSelectedTrack(trackType)
    }
}