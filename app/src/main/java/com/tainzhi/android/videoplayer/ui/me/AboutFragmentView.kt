package com.tainzhi.android.videoplayer.ui.me

import com.tainzhi.android.common.base.ui.fragment.BaseViewBindingFragment
import com.tainzhi.android.videoplayer.BuildConfig
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.databinding.AboutFragmentBinding
import com.tainzhi.android.videoplayer.util.openBrowser
import com.tainzhi.android.videoplayer.util.replyUrl
import com.tainzhi.android.videoplayer.util.sourceUrl
import com.tainzhi.android.videoplayer.widget.dialog.MeDialog

class AboutFragmentView : BaseViewBindingFragment<AboutFragmentBinding>() {
    override fun getLayoutResId(): Int = R.layout.about_fragment

    override fun initView() {
        with(mBinding) {
            versionTv.text = BuildConfig.VERSION_NAME

            suggestion.setOnClickListener {
                requireContext().openBrowser(replyUrl)
            }
            source.setOnClickListener {
                requireContext().openBrowser(sourceUrl)
            }
            developer.setOnClickListener {
                fragmentManager?.let {
                    MeDialog().show(it, "MeDialog")
                }
            }
        }
    }
}