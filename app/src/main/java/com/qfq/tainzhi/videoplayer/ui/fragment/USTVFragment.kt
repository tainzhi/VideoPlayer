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
class USTVFragment : BaseFragment() {
    private var mView: View? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_local_video, container, false)
        return mView
    }
    
    override fun onDoubleClick() {
        Log.i(TAG, "onDoubleClick: ")
    }
    
    companion object {
        val TAG: String? = "VideoPlayer/FragmentUSTV"
        var mInstance: USTVFragment? = null
        fun newInstance(): USTVFragment? {
            if (mInstance == null) {
                mInstance = USTVFragment()
            }
            return mInstance
        }
    }
}