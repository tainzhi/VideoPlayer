package com.qfq.tainzhi.videoplayer.media

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.MediaController
import androidx.appcompat.app.ActionBar
import java.util.*

/**
 * Created by muqing on 2019/6/1.
 * Email: qfq61@qq.com
 */
class AndroidMediaController : MediaController, IMediaController {
    private var mActionBar: ActionBar? = null
    
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }
    
    constructor(context: Context?, useFastForward: Boolean) : super(context, useFastForward) {
        initView(context)
    }
    
    constructor(context: Context?) : super(context) {
        initView(context)
    }
    
    private fun initView(context: Context?) {}
    fun setSupportActionBar(actionBar: ActionBar?) {
        mActionBar = actionBar
        if (isShowing()) {
            actionBar.show()
        } else {
            actionBar.hide()
        }
    }
    
    public override fun show() {
        super.show()
        if (mActionBar != null) mActionBar.show()
    }
    
    public override fun hide() {
        super.hide()
        if (mActionBar != null) mActionBar.hide()
        for (view: View? in mShowOnceArray) view.setVisibility(View.GONE)
        mShowOnceArray.clear()
    }
    
    //----------
    // Extends
    //----------
    private val mShowOnceArray: ArrayList<View?>? = ArrayList()
    public override fun showOnce(view: View) {
        mShowOnceArray.add(view)
        view.setVisibility(View.VISIBLE)
        show()
    }
}