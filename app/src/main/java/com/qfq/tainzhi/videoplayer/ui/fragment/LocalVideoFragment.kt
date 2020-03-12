package com.qfq.tainzhi.videoplayer.ui.fragment

import android.app.AlertDialog
import android.content.ContentUris
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.orhanobut.logger.Logger
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.VideoTestActivity
import com.qfq.tainzhi.videoplayer.adapters.LocalVideoAdapter
import com.qfq.tainzhi.videoplayer.adapters.LocalVideoAdapter.OnBottomReachedListener
import com.qfq.tainzhi.videoplayer.bean.LocalVideoBean
import java.util.*

/**
 * Created by muqing on 2019/6/4.
 * Email: qfq61@qq.com
 */
class LocalVideoFragment() : BaseFragment(), OnRefreshListener {
    private var mNoVideoHint: ViewStub? = null
    private var mRecyclerView: RecyclerView? = null
    private var mRefreshLayout: SwipeRefreshLayout? = null
    private var mView: View? = null
    private val mLists: MutableList<LocalVideoBean?>? = ArrayList()
    private var mAdapter: LocalVideoAdapter? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_local_video, container,
                                 false)
        mRefreshLayout = mView.findViewById<View?>(R.id.refresh_layout) as SwipeRefreshLayout
        mRecyclerView = mView.findViewById<View?>(R.id.section_lists) as RecyclerView
        mNoVideoHint = mView.findViewById(R.id.viewstub_novideo_hint_layout_id)
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context, R.color.zhuganzi))
        mRefreshLayout.setOnRefreshListener(this)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setLayoutManager(LinearLayoutManager(activity))
        mRecyclerView.addItemDecoration(DividerItemDecoration(context,
                                                              LinearLayoutManager.VERTICAL))
        mAdapter = LocalVideoAdapter(context, mLists)
        mAdapter.setOnItemClickListener(LocalVideoAdapter.OnItemClickListener { view, position ->
            startPlay(mLists.get(position).getId(),
                      mLists.get(position).getTitle(),
                      mLists.get(position).getDuration())
        })
        mAdapter.setOnItemLongClickListener(object : LocalVideoAdapter.OnItemLongClickListener {
            override fun onItemLongClick(view: View?, position: Int) {
                Logger.d("")
                Toast.makeText(context,
                               "Long Click" + mLists.get(position).getPath(),
                               Toast.LENGTH_LONG)
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setMessage("删除\"" + mLists.get(position).getTitle())
                builder.setPositiveButton("确定",
                                          DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                                              mLists.removeAt(position)
                                              Logger.d(mLists.get(position).toString())
                                              mAdapter.notifyDataSetChanged()
                                              dialog.dismiss()
                                          })
                builder.setNegativeButton("取消",
                                          DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int -> dialog.dismiss() })
                builder.show()
            }
        })
        mAdapter.setOnBottomReachedListener(object : OnBottomReachedListener {
            override fun onBottomReached(position: Int) {
                Logger.d("position:%s", position)
            }
        })
        mRecyclerView.setAdapter(mAdapter)
        return mView
    }
    
    override fun onPause() {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        super.onPause()
        Logger.d("")
    }
    
    override fun onResume() {
        super.onResume()
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        Logger.d("")
    }
    
    override fun onRefresh() {
        mRefreshLayout.setRefreshing(true)
        // setAdapter();
        if (mLists.size == 0) {
            mNoVideoHint.setVisibility(View.VISIBLE)
            mRecyclerView.setVisibility(View.GONE)
        }
        mRefreshLayout.setRefreshing(false)
    }
    
    override fun onDoubleClick() {
        Logger.d("")
    }
    
    private fun startPlay(id: Int, title: String?, duration: Long) {
        val intent: Intent = Intent(context,
                                    VideoTestActivity::class.java)
        val videoUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id.toLong())
        intent.setData(videoUri)
        intent.putExtra("title", title)
        intent.putExtra("duration", duration)
        startActivity(intent)
    }
    
    companion object {
        private var mInstance: LocalVideoFragment? = null
        fun newInstance(): LocalVideoFragment? {
            if (mInstance == null) {
                mInstance = LocalVideoFragment()
            }
            return mInstance
        }
    }
}