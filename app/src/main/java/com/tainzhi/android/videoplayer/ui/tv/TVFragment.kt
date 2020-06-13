package com.tainzhi.android.videoplayer.ui.tv

import android.net.Uri
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.databinding.TVFragmentBinding
import com.tainzhi.android.common.base.ui.BaseVMFragment
import com.tainzhi.android.videoplayer.adapter.TVAdapter
import com.tainzhi.android.videoplayer.ui.PlayVideoViewActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel

class TVFragment : BaseVMFragment<TVViewModel>(useBinding = true) {

    private val tvAdapter by lazy(LazyThreadSafetyMode.NONE) {
        TVAdapter() { tv ->
        PlayVideoViewActivity.startPlay(requireActivity(),
                    Uri.parse(mViewModel.getTVCircuit(tv.id)),
                tv.name ?: ""
        )
    } }

    override fun getLayoutResId() = R.layout.t_v_fragment

    override fun initVM(): TVViewModel = getViewModel()

    override fun initView() {
        (mBinding as TVFragmentBinding).tvRecyclerView.run {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = tvAdapter
        }
    }

    override fun initData() {
        mViewModel.getTVListAndProgram()
    }

    override fun startObserve() {
        mViewModel.apply {
            tvList.observe(viewLifecycleOwner, Observer {it ->
                tvAdapter.setList(it)
            })
        }
    }

}