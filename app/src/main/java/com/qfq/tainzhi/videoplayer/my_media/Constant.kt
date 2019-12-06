package com.qfq.tainzhi.videoplayer.my_media

/**
 * @author:       tainzhi
 * @mail:         qfq61@qq.com
 * @date:         2019-12-06 14:20
 * @description:
 **/

object Constant {
    object PLAYER_TYPE {
        const val SYSTEM_PLAYER = 0
        const val IJK_PLAYER = 1
        const val EXO_PLAYER = 2

    }

    object RENDER_TYPE {
        const val SURFACE_VIEW = 0
        const val TEXTURE_VIEW = 1
    }

    object SCREEN_TYPE {
        const val SCREEN_FULL_SCREEN = 0
        const val SCREEN_TINY = 1
    }

    object ASPECT_RATIO {
        const val AR_ASPECT_FIT_PARENT = 0 // without clip
        const val AR_ASPECT_FILL_PARENT = 1 // may clip
        const val AR_ASPECT_WRAP_CONTENT = 2
        const val AR_MATCH_PARENT = 3
        const val AR_16_9_FIT_PARENT = 4 // w/h
        const val AR_4_3_FIT_PARENT = 5
    }
}