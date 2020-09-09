package com.tanzhi.android.danmu.advancedanmu

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tanzhi.android.danmu.advancedanmu.view.IDanmu
import com.tanzhi.android.danmu.advancedanmu.view.OnDanmuViewTouchListener

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/9 下午10:07
 * @description:
 **/

class DanmuView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), IDanmu {
    override fun hasCanTouchDanmus(): Boolean {
        TODO("Not yet implemented")
    }

    private val onDanmuViewTouchListeners = mutableListOf<OnDanmuViewTouchListener>()
    private val danmuController = DanmuController()

}