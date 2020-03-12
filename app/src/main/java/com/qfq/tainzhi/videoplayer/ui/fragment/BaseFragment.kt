package com.qfq.tainzhi.videoplayer.ui.fragment

import androidx.fragment.app.Fragment

/**
 * Created by muqing on 2019/6/10.
 * Email: qfq61@qq.com
 */
open class BaseFragment : Fragment() {
    open fun onDoubleClick() {}
    
    companion object {
        private val TAg: String? = "BaseFragment"
    }
}