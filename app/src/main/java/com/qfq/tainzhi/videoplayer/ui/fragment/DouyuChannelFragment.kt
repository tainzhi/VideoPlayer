package com.qfq.tainzhi.videoplayer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import butterknife.BindView
import butterknife.ButterKnife
import com.chad.library.adapter.base.BaseQuickAdapter
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.R2
import com.qfq.tainzhi.videoplayer.adapters.DouyuChannelAdapter
import com.qfq.tainzhi.videoplayer.bean.DouyuChannelBean
import com.qfq.tainzhi.videoplayer.mvp.presenter.DouyuChannelPresenter
import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.IDouyuChannelPresenter
import java.util.*

/**
 * Created by muqing on 2019/6/17.
 * Email: qfq61@qq.com
 */
class DouyuChannelFragment constructor() : Fragment(), OnRefreshListener {
    @kotlin.jvm.JvmField
    @BindView(R2.id.douyu_channel_list)
    var mRecyclerView: RecyclerView? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.douyu_channel_refresh_layout)
    var mRefreshLayout: SwipeRefreshLayout? = null
    private var mView: View? = null
    private var mChannels: MutableList<DouyuChannelBean?>? = null
    private var mDouyuChannelPresenter: IDouyuChannelPresenter? = null
    private var mAdapter: DouyuChannelAdapter? = null
    public override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_douyu_channel,
                                 container, false)
        ButterKnife.bind(this, mView)
        mChannels = ArrayList()
        mDouyuChannelPresenter = DouyuChannelPresenter(this)
        mAdapter = DouyuChannelAdapter(getContext(),
                                       mChannels)
        mAdapter.setOnItemClickListener(BaseQuickAdapter.OnItemClickListener({ adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int ->
                                                                                 val ft: FragmentTransaction? = getFragmentManager().beginTransaction()
                                                                                 ft.remove(this)
                                                                                 ft.add(R.id.container,
                                                                                        DouyuLiveFragment(mChannels.get(position).getId(),
                                                                                                          mChannels.get(position).getName()),
                                                                                        DOUYU_CHANNEL_TO_LIVE)
                                                                                 ft.commit()
                                                                             }))
        mRecyclerView.setAdapter(mAdapter)
        mRecyclerView.setLayoutManager(GridLayoutManager(getContext(), 3))
        mDouyuChannelPresenter.getChannelList()
        mRefreshLayout.setRefreshing(true)
        return mView
    }
    
    fun showData(channels: MutableList<DouyuChannelBean?>?) {
        mChannels.clear()
        mChannels.addAll(channels)
        mAdapter.notifyDataSetChanged()
    }
    
    fun setLoadComplete() {
        mRefreshLayout.setRefreshing(false)
    }
    
    public override fun onRefresh() {
        mRefreshLayout.setRefreshing(false)
    }
    
    companion object {
        var DOUYU_CHANNEL_TAG: String? = "douyu_channel"
        var DOUYU_CHANNEL_TO_LIVE: String? = "from_channel_to_live"
        private var mInstance: DouyuChannelFragment? = null
        fun newInstance(): DouyuChannelFragment? {
            if (mInstance == null) {
                mInstance = DouyuChannelFragment()
            }
            return mInstance
        }
        
        fun getInstance(): DouyuChannelFragment? {
            return mInstance
        }
    }
}