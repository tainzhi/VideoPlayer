package com.tainzhi.android.videoplayer.ui.douyu

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.databinding.DouyuGameFragmentBinding
import com.tainzhi.android.common.base.ui.BaseVMFragment
import com.tainzhi.android.videoplayer.adapter.DouyuRoomAdapter
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 09:32
 * @description: 斗鱼频道页面, 该页面展示当前游戏分类的所有房间列表
 **/
class DouyuGameFragment(private val gameId: Int,
                        private val gameName: String
) : BaseVMFragment<DouyuGameViewModel>(useBinding = true) {

    private val douyuRoomAdapter by lazy(LazyThreadSafetyMode.NONE){
        DouyuRoomAdapter() { room ->
        //
        //     PlayActivity.startPlay(requireActivity(),
        //             // Uri.parse(room.)
        //     )
        }
    }
    private val binding : DouyuGameFragmentBinding by lazy { mBinding as DouyuGameFragmentBinding}
    override fun getLayoutResId() = R.layout.douyu_game_fragment

    override fun initVM(): DouyuGameViewModel = getViewModel()

    override fun initView() {
        binding.douyuGameRecyclerView.run {
            adapter = douyuRoomAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    override fun initData() {
        mViewModel.getGameRooms(gameId.toString())
    }

    override fun startObserve() {
        mViewModel.rooms.observe(viewLifecycleOwner, Observer { rooms->
            douyuRoomAdapter.setList(rooms)
        })
    }

}