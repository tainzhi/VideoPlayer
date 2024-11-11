package com.tainzhi.android.videoplayer.ui.me

import androidx.navigation.fragment.findNavController
import com.tainzhi.android.videoplayer.base.ui.fragment.BaseViewBindingFragment
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.databinding.MeFragmentBinding

class MeFragment : BaseViewBindingFragment<MeFragmentBinding>() {
    override fun getLayoutResId(): Int {
        return R.layout.me_fragment
    }

    override fun initView() {
        with(mBinding) {
            setting.setOnClickListener {
                findNavController().navigate(R.id.action_meFragment_to_settingFragment)
            }
            about.setOnClickListener {
                findNavController().navigate(R.id.action_meFragment_to_aboutFragment)
            }
        }
    }
}