package com.tanzhi.android.danmu.advancedanmu.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tanzhi.android.danmu.advancedanmu.DanmuController
import com.tanzhi.android.danmu.advancedanmu.DanmuModel

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/10 19:47
 * @description:
 **/

class DanmuView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), IDanmu {

    private val danmuController = DanmuController()
    private val onDanmuViewTouchListeners = mutableListOf<OnDanmuViewTouchListener>()
    private val onDanmuParentViewTouchCallbackListener: OnDanmuParentViewTouchCallbackListener? = null

    private var drawFinished = false

    fun prepare() {
        danmuController.setSpeedController
        danmuController.prepare()
    }

    override fun add(danmuModel: DanmuModel) {
        danmuController.addDanmuView(-1, danmuModel)
    }

    override fun add(index: Int, danmuModel: DanmuModel) {
        TODO("Not yet implemented")
    }

    override fun jumpQueue(danmuViews: List<DanmuModel>) {
        TODO("Not yet implemented")
    }

    override fun lockDraw() {
        TODO("Not yet implemented")
    }

    override fun forceSleep() {
        TODO("Not yet implemented")
    }

    override fun hideAllDanmuView(hideAll: Boolean) {
        TODO("Not yet implemented")
    }

    override fun hideNormalDanmu(hide: Boolean) {
        TODO("Not yet implemented")
    }

    override fun hasCanTouchDanmus(): Boolean {
        TODO("Not yet implemented")
    }
}