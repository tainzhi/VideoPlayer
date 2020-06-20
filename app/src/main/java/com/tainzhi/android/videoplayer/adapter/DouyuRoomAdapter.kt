package com.tainzhi.android.videoplayer.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.bean.DouyuRoom
import com.tainzhi.android.videoplayer.databinding.ItemDouyuRoomBinding
import com.tanzhi.qmediaplayer.dp

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 17:17
 * @description:
 **/

class DouyuRoomAdapter(private val goToPlay: (room : DouyuRoom) -> Unit) :
        BaseQuickAdapter<DouyuRoom, BaseDataBindingHolder<ItemDouyuRoomBinding>>(R.layout.item_douyu_room) ,
        LoadMoreModule
{

    init {
        setOnItemClickListener { _, _, position ->
            val clickedTv = data[position]
            goToPlay.invoke(clickedTv)
        }
    }
    override fun convert(holder: BaseDataBindingHolder<ItemDouyuRoomBinding>, item: DouyuRoom) {
        holder.dataBinding?.apply {
            room = item
            executePendingBindings()
        }
    }

}

class DouyuRoomItemDecoration:RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        val rect = Rect()
        // 第 0 项, 横跨两列, 不需要间距
        // 从第1项开始, 偶数列有右边距, 奇数列有左边距
        if (position > 0) {
            if (position % 2 == 1) {
                rect.right = 4.dp()
            } else if (position % 2 == 0) {
                rect.left = 4.dp()
            }
            outRect.set(rect)
        }
    }
}

