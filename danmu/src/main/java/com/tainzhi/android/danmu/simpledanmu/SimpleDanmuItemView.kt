package com.tainzhi.android.danmu.simpledanmu

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.tainzhi.android.danmu.R
import kotlin.random.Random

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/10 16:38
 * @description:
 **/

class SimpleDanmuItemView(context: Context, simpleDanmuBean: SimpleDanmuBean) : LinearLayout(context) {

    init {
        val view = inflate(context, R.layout.danmu_item, null)
        addView(view)

        view.findViewById<ImageView>(R.id.avatar).setImageResource(simpleDanmuBean.avatarId)
        view.findViewById<TextView>(R.id.tv_name).text = simpleDanmuBean.userName
        view.findViewById<TextView>(R.id.tv_content).text = simpleDanmuBean.content
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