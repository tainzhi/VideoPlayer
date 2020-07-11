package com.tanzhi.android.danmu

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import org.w3c.dom.Text
import kotlin.random.Random

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/10 16:38
 * @description:
 **/

class DanmuItemView( context: Context, danmuBean: DanmuBean) : LinearLayout(context) {

    init {
        val view = inflate(context, R.layout.danmu_item, this)

        view.findViewById<ImageView>(R.id.avator).setImageResource(danmuBean.avatorId)
        view.findViewById<TextView>(R.id.tv_name).text = danmuBean.userName
        view.findViewById<TextView>(R.id.tv_content).text = danmuBean.content
    }

    /**
     * 设置任意高度
     * @param heightPixels 高度
     */
    fun randomVerticalPosition( heightPixels: Int) {
        val randomTop = Random.nextInt(heightPixels)
        val params = layoutParams as RelativeLayout.LayoutParams
        params.topMargin = randomTop
        layoutParams = params
    }
}