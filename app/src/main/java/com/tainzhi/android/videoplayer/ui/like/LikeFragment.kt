package com.tainzhi.android.videoplayer.ui.like

import com.tainzhi.android.common.base.ui.BaseVmBindingFragment
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.databinding.FragmentLikeBinding
import com.tainzhi.android.videoplayer.widget.ChoosePlayerDialogFragment
import org.koin.androidx.viewmodel.ext.android.getViewModel

class LikeFragment : BaseVmBindingFragment<LikeViewModel, FragmentLikeBinding>() {
    override fun getLayoutResId() = R.layout.fragment_like

    override fun initVM(): LikeViewModel = getViewModel()

    override fun initView() {
        mBinding.choosePlayerTv.setOnClickListener {
            fragmentManager?.let { it1 -> ChoosePlayerDialogFragment().show(it1, "choosePlayerDialog") }
        }
        mBinding.viewModel = mViewModel
    }

    override fun initData() {
    }

    override fun startObserve() {
    }

}