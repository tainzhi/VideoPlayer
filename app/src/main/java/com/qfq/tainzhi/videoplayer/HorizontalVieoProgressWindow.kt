package com.qfq.tainzhi.videoplayer

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.PopupWindow
import androidx.leanback.widget.HorizontalGridView
import com.qfq.tainzhi.videoplayer.adapters.HorizontalGridViewAdapter

/**
 * Created by Administrator on 2016/1/24.
 */
class HorizontalVieoProgressWindow constructor(
        private val mContext: Context?, private val mHandler: Handler?,
        private val mVideoUri: Uri?, private val mVideoDuration: Int, private val mVideoProgress: Int,
        private val mProgressThumbWidth: Int,
        private val mProgressThumbHeight: Int) {
    private var mHorizontalVideoProgressViewContainer: View? = null
    private var mHorizontalVideoProgressView: HorizontalGridView? = null
    private var mAdapter: HorizontalGridViewAdapter? = null
    private var mPopupWindow: PopupWindow? = null
    private val mOnItemClickListener: AdapterView.OnItemClickListener? = object : AdapterView.OnItemClickListener {
        public override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, videoProgress: Long) {
            mPopupWindow.dismiss()
            val msg: Message? = Message()
            msg.what = CONTROLLER_SEEK_TO
            msg.arg1 = videoProgress as Int
            mHandler.sendMessage(msg)
        }
    }
    
    private fun initView() {
        Log.v(TAG, "initView")
        mHorizontalVideoProgressViewContainer = View.inflate(mContext, R.layout.horizontal_video_progress_window_layout, null)
        mHorizontalVideoProgressView = mHorizontalVideoProgressViewContainer.findViewById<View?>(R.id.horizontalgridview_videoprogress_id) as HorizontalGridView?
        mAdapter = HorizontalGridViewAdapter(mContext, mVideoUri, mVideoDuration, mVideoProgress, mProgressThumbWidth, mProgressThumbHeight)
        mAdapter.setOnItemClickListener(mOnItemClickListener)
        mHorizontalVideoProgressView.setAdapter(mAdapter)
        mHorizontalVideoProgressView.setWindowAlignment(HorizontalGridView.WINDOW_ALIGN_BOTH_EDGE)
        mHorizontalVideoProgressView.setWindowAlignmentOffsetPercent(35f)
    }
    
    fun showAt(parentView: View?, x: Int, y: Int) {
        Log.v(TAG, "showAt")
        mPopupWindow = PopupWindow(mHorizontalVideoProgressViewContainer, mProgressThumbWidth, mProgressThumbHeight)
        mPopupWindow.setAnimationStyle(R.style.animation_single_video_player_progress_popwindow)
        mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT)
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT)
        //        popupWindow.showAtLocation(parentView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 300);
        mPopupWindow.showAsDropDown(parentView)
        mPopupWindow.update(x, y, -1, -1, true)
    }
    
    companion object {
        private val TAG: String? = "VideoPlayer/HorizontalVideoProgressWindow"
        private val CONTROLLER_SEEK_TO: Int = 1
    }
    
    init {
        initView()
    }
}