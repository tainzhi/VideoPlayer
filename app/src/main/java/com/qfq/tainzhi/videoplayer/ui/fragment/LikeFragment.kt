package com.qfq.tainzhi.videoplayer.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qfq.tainzhi.videoplayer.R

/**
 * Created by muqing on 2019/6/4.
 * Email: qfq61@qq.com
 */
class LikeFragment : BaseFragment() {
    private var mView: View? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_local_video, container, false)
        return mView
    }
    
    override fun onDoubleClick() {
        Log.i(TAG, "onDoubleClick: ")
    }
    
    companion object {
        val TAG: String? = "VideoPlayer/FragmentLike"
        var mInstance: LikeFragment? = null
        fun newInstance(): LikeFragment? {
            if (mInstance == null) {
                mInstance = LikeFragment()
            }
            return mInstance
        }
    }
}