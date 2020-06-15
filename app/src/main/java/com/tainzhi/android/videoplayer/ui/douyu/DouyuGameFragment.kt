package com.tainzhi.android.videoplayer.ui.douyu

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
class DouyuGameFragment : BaseVMFragment<DouyuGameViewModel>(useBinding = true) {

    private val localVideoAdapter by lazy(LazyThreadSafetyMode.NONE){
        DouyuRoomAdapter() { room ->

            PlayActivity.startPlay(requireActivity(),
                    video.uri,
                    video.duration
            )
        }
    }
    private val binding : DouyuGameFragmentBinding
    override fun getLayoutResId() = R.layout.douyu_game_fragment

    override fun initVM(): DouyuGameViewModel = getViewModel()

    override fun initView() {

    }

    override fun initData() {
        TODO("Not yet implemented")
    }

    override fun startObserve() {
        TODO("Not yet implemented")
    }

}