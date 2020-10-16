package com.tainzhi.android.videoplayer.ui.like

import com.tainzhi.android.common.base.ui.BaseVmBindingFragment
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.databinding.FragmentLikeBinding
import com.tainzhi.android.videoplayer.widget.ChoosePlayerDialogFragment
import com.tainzhi.android.videoplayer.widget.ChoosePlayerRenderTypeDialogFragment
import com.tainzhi.android.videoplayer.widget.ChooseThemeDialogFragment
import org.koin.androidx.viewmodel.ext.android.getViewModel

class LikeFragment : BaseVmBindingFragment<LikeViewModel, FragmentLikeBinding>() {
    override fun getLayoutResId() = R.layout.fragment_like

    override fun initVM(): LikeViewModel = getViewModel()

    override fun initView() {
        mBinding.viewModel = mViewModel
        mBinding.chooseThemeTv.setOnClickListener {
            fragmentManager?.let { ChooseThemeDialogFragment().show(it, "chooseThemeDialog") }
        }
        mBinding.choosePlayerTv.setOnClickListener {
            fragmentManager?.let { ChoosePlayerDialogFragment().show(it, "choosePlayerDialog") }
        }
        mBinding.choosePlayerRenderTv.setOnClickListener {
            fragmentManager?.let { ChoosePlayerRenderTypeDialogFragment().show(it, "choosePlayerRenderDialog") }
        }
    }

    override fun initData() {
    }

    override fun startObserve() {
    }

}