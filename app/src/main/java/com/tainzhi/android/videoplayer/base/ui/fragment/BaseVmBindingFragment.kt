package com.tainzhi.android.videoplayer.base.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

/**
 * @author:       tainzhi
 * @mail:         qfq61@qq.com
 * @date:         2020/1/18 15:28
 * @description:  使用 ViewModel, ViewDataBinding 的Fragment
 **/

abstract class BaseVmBindingFragment<VM : ViewModel, BD : ViewDataBinding> : BaseFragment() {

    protected lateinit var mViewModel: VM
    protected lateinit var mBinding: BD

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // initVM() 一定要在super.onViewCreated()之前执行
        // 因为super.onViewCreated() -> initView() ->需要用到ViewModel
        mViewModel = initVM()
        super.onViewCreated(view, savedInstanceState)
        startObserve()
    }

    abstract fun initVM(): VM
    open fun startObserve() {}
}