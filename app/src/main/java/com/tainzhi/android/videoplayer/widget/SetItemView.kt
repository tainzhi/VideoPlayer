package com.tainzhi.android.videoplayer.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.switchmaterial.SwitchMaterial
import com.tainzhi.android.videoplayer.R

/**
 * File:     SetItemView
 * Author:   tainzhi
 * Created:  2020/12/25 13:22
 * Mail:     QFQ61@qq.com
 * Description: 单设置Item
 */
class SetItemView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        inflate(context, R.layout.set_item_view, this)

        attrs?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.SetItemView)

            this.findViewById<TextView>(R.id.itemText).text = a.getString(R.styleable.SetItemView_itemName)

            val needDivideLine = a.getBoolean(R.styleable.SetItemView_needDivideLine, false)
            this.findViewById<View>(R.id.divideLine).visibility = if (needDivideLine) View.VISIBLE else View.GONE

            val needSwitchBtn = a.getBoolean(R.styleable.SetItemView_needSwitchBtn, false)
            this.findViewById<SwitchMaterial>(R.id.switchBtn).visibility = if (needSwitchBtn) View.VISIBLE else View.GONE

            a.recycle()
        }

    }

}
