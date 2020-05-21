package com.qfq.tainzhi.videoplayer.my_media

import android.content.pm.ActivityInfo
import androidx.annotation.IntDef

/**
 * @author:       tainzhi
 * @mail:         qfq61@qq.com
 * @date:         2019-12-06 14:20
 * @description:
 **/

object Constant {
    object PlayerType {
        const val SYSTEM_PLAYER = 0 // Android系统自带的MediaPlayer
        const val IJK_PLAYER = 1    // IjkPlayer
        const val EXO_PLAYER = 2    // ExoPlayer
    }

    object RenderType {
        const val SURFACE_VIEW = 0
        const val TEXTURE_VIEW = 1
    }

    object ScreenType {
        const val FULL_SCREEN = 0
        const val TINY_SCREEN = 1
    }

    object AspectRatio {
        const val AR_ASPECT_FIT_PARENT = 0 // without clip
        const val AR_ASPECT_FILL_PARENT = 1 // may clip
        const val AR_ASPECT_WRAP_CONTENT = 2
        const val AR_MATCH_PARENT = 3
        const val AR_16_9_FIT_PARENT = 4 // w/h
        const val AR_4_3_FIT_PARENT = 5
    }

    object PlayState {
        const val STATE_IDLE = -1
        const val STATE_NORMAL = 0
        const val STATE_PREPARING = 1
        const val STATE_PREPARING_CHANGING_URL = 2
        const val STATE_PREPARED = 3
        const val STATE_PLAYING = 4
        const val STATE_PAUSE = 5
        const val STATE_AUTO_COMPLETE = 6
        const val STATE_ERROR = 7
    }

    const val FULL_SCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    const val NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    @Target(AnnotationTarget.PROPERTY, AnnotationTarget.PROPERTY_SETTER)
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(value = [
        RenderType.SURFACE_VIEW,
        RenderType.TEXTURE_VIEW
    ])
    annotation class RenderTypeMode

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(value = [
        PlayerType.SYSTEM_PLAYER,
        PlayerType.IJK_PLAYER,
        PlayerType.EXO_PLAYER
    ])
    annotation class PlayerTypeMode

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(value = [
        ScreenType.FULL_SCREEN,
        ScreenType.TINY_SCREEN
    ])
    annotation class ScreenTypeMode

    @Target(AnnotationTarget.PROPERTY)
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(value = [
        AspectRatio.AR_ASPECT_FIT_PARENT,
        AspectRatio.AR_ASPECT_FILL_PARENT,
        AspectRatio.AR_ASPECT_WRAP_CONTENT,
        AspectRatio.AR_MATCH_PARENT,
        AspectRatio.AR_16_9_FIT_PARENT,
        AspectRatio.AR_4_3_FIT_PARENT
    ])
    annotation class AspectRatioMode
}
