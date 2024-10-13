package com.tainzhi.android.common.base.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tainzhi.android.common.base.ui.LazyLoad
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * @author:       tainzhi
 * @mail:         qfq61@qq.com
 * @date:         2020/1/18 15:00
 * @description:
 **/

abstract class BaseFragment : Fragment(), CoroutineScope by MainScope() {
    protected var isLoaded = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        if (this !is LazyLoad) {
            initData()
        }
    }

    override fun onStart() {
        super.onStart()
        if (!isLoaded && this is LazyLoad) {
            initData()
            isLoaded = true
        }
    }

    abstract fun getLayoutResId(): Int

    open fun initView() {}

    open fun initData() {}

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}

