package com.tainzhi.android.videoplayer.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.lang.reflect.ParameterizedType

/**
 * File:     BaseViewBindingActivity
 * Author:   tainzhi
 * Created:  2021/1/1 13:15
 * Mail:     QFQ61@qq.com
 * Description: Activity使用ViewBinding
 */
open class BaseViewBindingActivity<VB : ViewBinding> : AppCompatActivity(), CoroutineScope by MainScope() {
    protected val mBinding: VB by lazy {
        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments[0] as Class<*>
        val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        method.invoke(null, layoutInflater) as VB
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        initView()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    open fun initView() {}
    open fun initData() {}
}