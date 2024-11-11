package com.tainzhi.android.videoplayer.ui.me

import com.tainzhi.android.videoplayer.base.ui.fragment.BaseVmBindingFragment
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.databinding.SettingFragmentBinding
import com.tainzhi.android.videoplayer.widget.dialog.ChoosePlayerDialogFragment
import com.tainzhi.android.videoplayer.widget.dialog.ChoosePlayerRenderTypeDialogFragment
import com.tainzhi.android.videoplayer.widget.dialog.ChooseThemeDialogFragment
import org.koin.androidx.viewmodel.ext.android.getViewModel

class SettingFragment : BaseVmBindingFragment<SettingViewModel, SettingFragmentBinding>() {
    override fun getLayoutResId() = R.layout.setting_fragment

    override fun initVM(): SettingViewModel = getViewModel()

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
}