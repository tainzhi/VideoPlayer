package com.tainzhi.android.common.widget

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.tainzhi.android.common.R.layout

/**
 * File:     Toolbar
 * Author:   tainzhi
 * Created:  2020/12/30 11:26
 * Mail:     QFQ61@qq.com
 * Description:
 */
class Toolbar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        inflate(context, layout.toolbar_view, this)

        attrs?.let {

        }

    }

}
