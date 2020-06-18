package com.tainzhi.android.videoplayer.adapter

import android.graphics.Canvas
import android.view.View
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.databinding.ItemLocalVideoBinding
import com.tainzhi.android.videoplayer.bean.LocalVideo
import java.util.*

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/12 15:00
 * @description:
 **/

class LocalVideoAdapter(private val goToPlay: (video: LocalVideo) -> Unit ) :
        BaseQuickAdapter<LocalVideo, LocalVideoViewHolder<ItemLocalVideoBinding>>(R.layout.item_local_video),
        Filterable
{

    var originalData =  ArrayList<LocalVideo>()

    init {
        setOnItemClickListener { _, _, position ->
            val clickedTv = data[position]
            goToPlay.invoke(clickedTv)
        }
        setDiffCallback(object: DiffUtil.ItemCallback<LocalVideo>() {
            override fun areItemsTheSame(oldItem: LocalVideo, newItem: LocalVideo): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: LocalVideo, newItem: LocalVideo): Boolean {
                return oldItem.hashCode() == newItem.hashCode() &&
                        oldItem.uri == newItem.uri
            }

        })
    }
    override fun convert(holder: LocalVideoViewHolder<ItemLocalVideoBinding>, item: LocalVideo) {
        holder.dataBinding?.apply {
            video = item
            holder.foreground = localVideoItemForeground
            holder.video  = item
            executePendingBindings()
        }
    }

    override fun setList(list: Collection<LocalVideo>?) {
        super.setList(list)
        originalData = data as ArrayList<LocalVideo>
    }

    override fun getFilter() = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val constraintString = constraint.toString()
            var filteredList: MutableList<LocalVideo> = ArrayList()
            if (constraintString.isEmpty()) {
                filteredList = originalData
            } else {
                for (v in originalData) {
                    if (v.title.contains(constraintString)) {
                        filteredList.add(v)
                    }
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            data = results.values as MutableList<LocalVideo>
            notifyDataSetChanged()
        }
    }


}


class LocalVideoViewHolder<BD : ViewDataBinding>(view: View): BaseDataBindingHolder<BD> (view) {
    var foreground: View? = null
    var video: LocalVideo? = null
}


class RecyclerItemTouchHelper(
        dragDirs: Int, swipDirs: Int,
        private val mListener: RecyclerItemTouchHelperListener) : ItemTouchHelper.SimpleCallback(dragDirs, swipDirs) {
    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        mListener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            val foreground: View = (viewHolder as LocalVideoViewHolder<*>).foreground!!
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(foreground)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val foreground: View = (viewHolder as LocalVideoViewHolder<*>).foreground!!
        ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foreground)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val foreground: View = (viewHolder as LocalVideoViewHolder<*>).foreground!!
        ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(c, recyclerView, foreground, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val foreground: View = (viewHolder as LocalVideoViewHolder<*>).foreground!!
        ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(c, recyclerView, foreground, dX, dY, actionState, isCurrentlyActive)
    }

    interface RecyclerItemTouchHelperListener {
        fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int)
    }

}
