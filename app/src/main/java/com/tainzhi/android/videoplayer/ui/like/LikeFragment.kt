package com.tainzhi.android.videoplayer.ui.like

import com.tainzhi.android.common.base.ui.BaseVmBindingFragment
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.databinding.FragmentLikeBinding
import com.tainzhi.android.videoplayer.widget.ChoosePlayerDialogFragment
import com.tainzhi.android.videoplayer.widget.ChoosePlayerRenderTypeDialogFragment
import org.koin.androidx.viewmodel.ext.android.getViewModel

class LikeFragment : BaseVmBindingFragment<LikeViewModel, FragmentLikeBinding>() {
    override fun getLayoutResId() = R.layout.fragment_like

    override fun initVM(): LikeViewModel = getViewModel()

    override fun initView() {
        mBinding.viewModel = mViewModel
        mBinding.choosePlayerTv.setOnClickListener {
            fragmentManager?.let { ChoosePlayerDialogFragment().show(it, "choosePlayerDialog") }
        }
        mBinding.choosePlayerRenderTv.setOnClickListener {
            fragmentManager?.let { ChoosePlayerRenderTypeDialogFragment().show(it, "choosePlayerRenderDialog")}
        }
    }

    override fun initData() {
    }

    override fun startObserve() {
    }

}