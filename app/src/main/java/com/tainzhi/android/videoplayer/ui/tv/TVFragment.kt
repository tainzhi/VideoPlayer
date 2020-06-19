package com.tainzhi.android.videoplayer.ui.tv

import android.net.Uri
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.common.base.ui.BaseVmBindingFragment
import com.tainzhi.android.videoplayer.adapter.TVAdapter
import com.tainzhi.android.videoplayer.ui.PlayActivity
import com.tainzhi.android.videoplayer.databinding.TVFragmentBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel

class TVFragment : BaseVmBindingFragment<TVViewModel, TVFragmentBinding>() {

    private val tvAdapter by lazy(LazyThreadSafetyMode.NONE) {
        TVAdapter { tv ->
            if (tv.tvCircuit != null) {
                PlayActivity.startPlay(requireActivity(),
                        Uri.parse(tv.tvCircuit!![0]),
                        tv.name?.let { it } ?: "")

            } else {
                Log.e("TVFragment.TVAdapter", "no valid circuit, ${tv.name} 不能观看")
            }
        }
    }

    override fun getLayoutResId() = R.layout.t_v_fragment

    override fun initVM(): TVViewModel = getViewModel()

    override fun initView() {
        mBinding.tvRecyclerView.run {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = tvAdapter
        }
    }

    override fun initData() {
        mViewModel.getTVList()
        mViewModel.getTVProgram()
    }

    override fun startObserve() {
        mViewModel.apply {
            tvList.observe(viewLifecycleOwner, Observer { it ->
                tvAdapter.setList(it)
            })
            tvPrograms.observe(viewLifecycleOwner, Observer { it ->
                tvAdapter.data.forEach { tv ->
                    tv.broadingProgram = it[tv.id] ?: "没有查询到"
                }
                tvAdapter.notifyDataSetChanged()
            })
        }
    }

}