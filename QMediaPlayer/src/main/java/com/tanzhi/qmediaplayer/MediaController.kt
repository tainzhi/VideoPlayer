package com.tanzhi.qmediaplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/22 17:03
 * @description: 播放控制器, 添加控制按钮, 控制[VideoView]播放
 **/

class MediaController(val context: Context) {

    private lateinit var root: ViewGroup
    private var isShowing = false

    lateinit var videoView: VideoView

    companion object {
        const val DefaultTimeout = 3000
    }

    fun setParentView(parent: ViewGroup) {
        root = parent
        val content = makeControllerView()
        parent.addView(content, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun makeControllerView() : View{
        val inflate = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        // TODO: 2020/5/23 orientaion inflate port or land
        val root = inflate.inflate(R.layout.media_controller_port, null)
        initControllView(root)
        return root
    }

    private fun initControllView(view: View) {

    }

    fun hide() {

    }

    fun show() {

    }

    // TODO: 2020/5/23
    // private val layoutChangeListener =
    //         OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
    //             updateFloatingWindowLayout()
    //             if (isShowing) {
    //                 windowManager.updateViewLayout(decor, decorLayoutParams)
    //             }
    //         }

}