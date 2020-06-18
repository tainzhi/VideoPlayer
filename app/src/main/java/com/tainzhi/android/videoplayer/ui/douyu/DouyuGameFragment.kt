package com.tainzhi.android.videoplayer.ui.douyu

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.databinding.DouyuGameFragmentBinding
import com.tainzhi.android.common.base.ui.BaseVMFragment
import com.tainzhi.android.videoplayer.adapter.DouyuRoomAdapter
import com.tainzhi.android.videoplayer.ui.PlayActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 09:32
 * @description: 斗鱼频道页面, 该页面展示当前游戏分类的所有房间列表
 **/
class DouyuGameFragment(
) : BaseVMFragment<DouyuGameViewModel>(useBinding = true) {

    private val gameId by lazy(LazyThreadSafetyMode.NONE) { arguments?.getString(GAME_ID)}
    private val gameName by lazy(LazyThreadSafetyMode.NONE) {arguments?.getString(GAME_NAME)}

    private val args: DouyuGameFragmentArgs by navArgs()

    companion object {
        private const val GAME_ID = "game_id"
        private const val GAME_NAME = "game_name"
        fun newInstance(gameId: String,
                        gameName: String): DouyuGameFragment {
            return DouyuGameFragment().apply {
                arguments = Bundle().apply {
                    putString(GAME_ID, gameId)
                    putString(GAME_NAME, gameName)
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
        (mBinding as DouyuGameFragmentBinding).douyuGameRecyclerView.run {
            adapter = douyuRoomAdapter
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }

        (requireActivity()).actionBar?.title = gameName
    }

    override fun initData() {
        // gameId 不为 null, DouyuFragment()从首页创建
        // gameId 为 null, DouyuFragment()从游戏分类创建
        val id = gameId ?: args.gameId
        mViewModel.getGameRooms(id)
    }

    override fun startObserve() {
        mViewModel.rooms.observe(viewLifecycleOwner, Observer { rooms->
            douyuRoomAdapter.setList(rooms)
        })
    }

}