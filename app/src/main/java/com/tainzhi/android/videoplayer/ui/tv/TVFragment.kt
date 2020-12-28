package com.tainzhi.android.videoplayer.ui.tv

import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tainzhi.android.common.base.ui.LazyLoad
import com.tainzhi.android.common.base.ui.fragment.BaseVmBindingFragment
import com.tainzhi.android.common.util.toast
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.adapter.TVAdapter
import com.tainzhi.android.videoplayer.databinding.TVFragmentBinding
import com.tainzhi.android.videoplayer.db.AppDataBase
import com.tainzhi.android.videoplayer.network.ResultOf
import com.tainzhi.android.videoplayer.ui.PlayActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel

class TVFragment : BaseVmBindingFragment<TVViewModel, TVFragmentBinding>(), LazyLoad {

    private val tvAdapter by lazy(LazyThreadSafetyMode.NONE) {
        TVAdapter { tv ->
            if (tv.tvCircuit != null) {
                PlayActivity.startPlay(requireActivity(),
                        Uri.parse(tv.tvCircuit!![0]),
                        tv.name ?: "")

            } else {
                Log.e("TVFragment.TVAdapter", "no valid circuit, ${tv.name} 不能观看")
            }
        }
    }

    override fun getLayoutResId() = R.layout.t_v_fragment

    override fun initVM(): TVViewModel = getViewModel()

    override fun initView() {
        with(mBinding) {
            tvRecyclerView.run {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = tvAdapter
            }
            tvRefreshLayout.run {
                setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.color_secondary))
                setOnRefreshListener { refresh() }
            }
        }
    }

    // 在dataBaseStates.observer()中有调用
    override fun initData() {
        refresh()
    }

    private fun refresh() {
        mViewModel.getTVList()
        mViewModel.getTVProgram()
    }

    override fun startObserve() {
        mViewModel.apply {

            // 第一次查询数据库, 发现没有数据, 会创建TV table
            // 创建数据库后, 每次进入该页面, 判断state.isFinished(), 然后 refresh()
            dataBaseStatus.observe(viewLifecycleOwner, { listOfInfos ->
                if (!listOfInfos.isNullOrEmpty() && !AppDataBase.firstCreateDb) {
                    if (listOfInfos[0].state.isFinished) {
                        refresh()
                        AppDataBase.firstCreateDb = true
                    }
                }
            })

            tvList.observe(viewLifecycleOwner) {
                tvAdapter.setList(it)
            }
            tvPrograms.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is ResultOf.Success -> {
                        tvAdapter.data.forEach { tv ->
                            tv.program = state.data[tv.id]
                        }
                        tvAdapter.notifyDataSetChanged()
                        mBinding.tvRefreshLayout.isRefreshing = false
                    }
                    is ResultOf.Loading -> {
                        mBinding.tvRefreshLayout.isRefreshing = true
                    }
                    is ResultOf.Error -> {
                        mBinding.tvRefreshLayout.isRefreshing = false
                        activity?.toast(state.message)
                    }
                    else -> Unit
                }
            }
        }
    }

}