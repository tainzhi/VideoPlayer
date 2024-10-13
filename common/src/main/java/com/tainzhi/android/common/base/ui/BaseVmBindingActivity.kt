package com.tainzhi.android.common.base.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

/**
 * @author:       tainzhi
 * @mail:         qfq61@qq.com
 * @date:         2020/1/18 15:26
 * @description:  Activity使用 ViewModel && ViewDataBinding
 **/

abstract class BaseVmBindingActivity<VM : ViewModel, BD : ViewDataBinding> : AppCompatActivity
() {
    protected lateinit var mBinding: BD
    lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, getLayoutResId())
        mBinding.lifecycleOwner = this

        mViewModel = initVM()
        initView()
        initData()
        startObserve()
    }

    abstract fun getLayoutResId(): Int
    abstract fun initVM(): VM
    open fun initView() {}
    open fun initData() {}
    open fun startObserve() {}
}