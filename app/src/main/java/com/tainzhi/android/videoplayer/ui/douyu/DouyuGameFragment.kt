package com.tainzhi.android.videoplayer.ui.douyu

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.databinding.DouyuGameFragmentBinding
import com.tainzhi.android.common.base.ui.BaseVmBindingFragment
import com.tainzhi.android.videoplayer.adapter.DouyuRoomAdapter
import com.tainzhi.android.videoplayer.ui.MainViewModel
import com.tainzhi.android.videoplayer.ui.PlayActivity
import com.tencent.bugly.proguard.u
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 09:32
 * @description: 斗鱼频道页面, 该页面展示当前游戏分类的所有房间列表
 **/
class DouyuGameFragment(
) : BaseVmBindingFragment<DouyuGameViewModel, DouyuGameFragmentBinding>() {

    // 通过 newInstance(gameId) 创建传参
    private val gameId by lazy(LazyThreadSafetyMode.NONE) { arguments?.getString(GAME_ID)}

    // 通过Navigation创建的 DouyuGameFragment 传参
    private val args: DouyuGameFragmentArgs by navArgs()

    companion object {
        private const val GAME_ID = "game_id"
        fun newInstance(gameId: String): DouyuGameFragment {
            return DouyuGameFragment().apply {
                arguments = Bundle().apply {
                    putString(GAME_ID, gameId)
                }
            }

        }
    }

    private val douyuRoomAdapter by lazy(LazyThreadSafetyMode.NONE){
        DouyuRoomAdapter() { room ->
            val roomCircuit = mViewModel.getRoomCircuit(room.room_id.toString())
            PlayActivity.startPlay(requireActivity(),
                    Uri.parse(roomCircuit),
                    room.room_name
            )
        }
    }

    override fun getLayoutResId() = R.layout.douyu_game_fragment

    override fun initVM(): DouyuGameViewModel = getViewModel()

    override fun initView() {
        mBinding.douyuGameRecyclerView.run {
            adapter = douyuRoomAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }

        // gameId 只从DouyuFragment传递, 而从DouyuCategoryFragment不传
        // 从分类创建的DouyuGameFragment需要有center title
        if (gameId == null) {
            getSharedViewModel<MainViewModel>().run {
                updateToolbarCenterTitle(args.gameName)
                updateToolbarCenterTitleVisibility(true)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 退出时, 不显示center title
        if (gameId == null) {
            getSharedViewModel<MainViewModel>().run {
                updateToolbarCenterTitleVisibility(false)
            }
        }
    }

    override fun initData() {
        mViewModel.getGameRooms(gameId ?: args.gameId)
    }

    override fun startObserve() {
        mViewModel.rooms.observe(viewLifecycleOwner, Observer { rooms->
            douyuRoomAdapter.setList(rooms)
        })
    }

}