package com.qfq.tainzhi.videoplayer.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.chad.library.adapter.base.BaseQuickAdapter
import com.orhanobut.logger.Logger
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.adapters.TVChannelAdapter
import com.qfq.tainzhi.videoplayer.bean.TVChannelBean
import com.qfq.tainzhi.videoplayer.mvp.presenter.TVPresenter
import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.ITVPresenter
import com.qfq.tainzhi.videoplayer.ui.activity.IjkPlayerActivity
import java.util.*

/**
 * Created by muqing on 2019/6/4.
 * Email: qfq61@qq.com
 */
class TVFragment : BaseFragment(), OnRefreshListener {
    private var mView: View? = null
    private var mRecyclerView: RecyclerView? = null
    private var mRefreshLayout: SwipeRefreshLayout? = null
    private var mChannelList: MutableList<TVChannelBean?>? = null
    private var mTVPresenter: ITVPresenter? = null
    private var mAdapter: TVChannelAdapter? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_tv, container, false)
        mRefreshLayout = mView.findViewById(R.id.tv_refresh_layout)
        mRecyclerView = mView.findViewById(R.id.tv_recyvler_view)
        mChannelList = ArrayList()
        mTVPresenter = TVPresenter(this)
        mAdapter = TVChannelAdapter(context, mChannelList)
        mAdapter.setOnItemClickListener(BaseQuickAdapter.OnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int ->
            startPlay(mChannelList.get(position).getUrl(),
                      mChannelList.get(position).getName())
        })
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setLayoutManager(LinearLayoutManager(activity))
        mRecyclerView.addItemDecoration(DividerItemDecoration(context,
                                                              LinearLayoutManager.VERTICAL))
        mRecyclerView.setAdapter(mAdapter)
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context, R.color.zhuganzi))
        mRefreshLayout.setOnRefreshListener(this)
        mRefreshLayout.setRefreshing(true)
        mTVPresenter.getChannelList()
        return mView
    }
    
    override fun onRefresh() {
        mRefreshLayout.setRefreshing(false)
    }
    
    fun showData(channelList: MutableList<TVChannelBean?>?) {
        mChannelList.clear()
        mChannelList.addAll(channelList)
        mAdapter.notifyDataSetChanged()
    }
    
    fun onLoadComplete() {
        mRefreshLayout.setRefreshing(false)
    }
    
    override fun onDoubleClick() {
        Logger.d("")
    }
    
    private fun startPlay(url: String?, name: String?) {
        val intent = Intent(context,
                            IjkPlayerActivity::class.java)
        intent.data = Uri.parse(url)
        Logger.d(url)
        intent.putExtra("title", name)
        startActivity(intent)
    }
    
    companion object {
        private var mInstance: TVFragment? = null
        fun newInstance(): TVFragment? {
            if (mInstance == null) {
                mInstance = TVFragment()
            }
            return mInstance
        }
    }
}