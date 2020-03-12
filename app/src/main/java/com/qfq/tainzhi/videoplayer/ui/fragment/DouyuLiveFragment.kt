package com.qfq.tainzhi.videoplayer.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.adapters.DouyuChannelRoomAdapter
import com.qfq.tainzhi.videoplayer.bean.DouyuRoomBean
import com.qfq.tainzhi.videoplayer.mvp.presenter.DouyuLivePresenter
import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.IDouyuLivePresenter
import com.qfq.tainzhi.videoplayer.ui.activity.DefaultPlayActivity
import java.util.*

/**
 * Created by muqing on 2019/6/13.
 * Email: qfq61@qq.com
 */
class DouyuLiveFragment(private val mChannelId: Int, private val mChannelTitle: String?) : Fragment(), OnRefreshListener {
    private var mRefreshLayout: SwipeRefreshLayout? = null
    private var mRecyclerView: RecyclerView? = null
    private var mView: View? = null
    private var mChannelRooms: MutableList<DouyuRoomBean?>? = null
    private var mDouyuLivePresenter: IDouyuLivePresenter? = null
    private var mAdapter: DouyuChannelRoomAdapter? = null
    private var mOffset = 0
    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_douyu_live, container,
                                 false)
        mRefreshLayout = mView.findViewById(R.id.douyu_live_refresh_layout)
        mRecyclerView = mView.findViewById(R.id.douyu_channel_room_list)
        mChannelRooms = ArrayList()
        mDouyuLivePresenter = DouyuLivePresenter(this)
        mDouyuLivePresenter.getRoomList(mChannelId, mChannelTitle, mOffset)
        mAdapter = DouyuChannelRoomAdapter(context, mChannelRooms)
        mAdapter.setOnItemClickListener(BaseQuickAdapter.OnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int ->
            startPlay(mChannelRooms.get(position).getRoom_id(),
                      mChannelRooms.get(position).getNickname())
        })
        mAdapter.setOnLoadMoreListener(RequestLoadMoreListener { onLoadMore() }, mRecyclerView)
        initView()
        val parent = mView.getParent() as ViewGroup
        parent?.removeView(mView)
        return mView
    }
    
    private fun initView() {
        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            // FIXME: 2019/6/14 功能无法实现: 第一行1列, 其余行2列
            override fun getSpanSize(position: Int): Int {
                return if (position == 0) 1 else gridLayoutManager.spanCount
            }
        }
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context, R.color.zhuganzi))
        mRefreshLayout.setOnRefreshListener(this)
        mRecyclerView.setLayoutManager(gridLayoutManager)
        mRecyclerView.setAdapter(mAdapter)
        mRefreshLayout.setRefreshing(true)
    }
    
    override fun onRefresh() {
        mRefreshLayout.setRefreshing(true)
        mOffset = 0
        mDouyuLivePresenter.getRoomList(mChannelId, mChannelTitle, mOffset)
    }
    
    fun showData(rooms: MutableList<DouyuRoomBean?>?) {
        if (mOffset == 0) {
            mChannelRooms.clear()
            mChannelRooms.addAll(rooms)
        } else {
            mChannelRooms.addAll(rooms)
        }
        mAdapter.notifyDataSetChanged()
    }
    
    fun onLoadMore() {
        mOffset += 20
        mRefreshLayout.setRefreshing(true)
        mDouyuLivePresenter.getRoomList(mChannelId, mChannelTitle, mOffset)
    }
    
    fun setLoadComplete() {
        mRefreshLayout.setRefreshing(false)
        mAdapter.loadMoreComplete()
    }
    
    private fun startPlay(roomId: Int, title: String?) {
        // remove this fragment
        val ft = fragmentManager.beginTransaction()
        ft.remove(this)
        ft.show(DouyuFragment.Companion.newInstance())
        ft.commit()
        
        // REFACTOR: 2019/6/21 待重构 怎么通过房间号获取房间的直播源
        val path = "https://tc-tct.douyucdn2" +
                ".cn/dyliveflv1a/288016rlols5_4000p.flv?wsAuth=8b486029039b56bea5890018f8fbc0c5&token=web-h5-89457769-288016-88ecb324a2c68d24b31f3321f9e5b8bdd61f2d4174ff5fb3&logo=0&expire=0&did=2c3861dd383f06343e559cf200051501&ver=Douyu_219050705&pt=2&st=0&mix=0&isp="
        val intent = Intent(context,
                            DefaultPlayActivity::class.java)
        intent.data = Uri.parse(path)
        intent.putExtra("title", title)
        startActivity(intent)
    }
    
}