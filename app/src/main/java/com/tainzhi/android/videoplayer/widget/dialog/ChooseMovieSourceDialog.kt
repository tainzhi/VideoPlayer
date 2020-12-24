package com.tainzhi.android.videoplayer.widget.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tainzhi.android.videoplayer.ui.movie.MovieViewModel
import com.tainzhi.android.videoplayer.widget.dialog.ChooseMovieSourceDialog.SourceHolder
import org.koin.android.ext.android.inject

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/9 15:01
 * @description: 选择播放器类型
 **/

class ChooseMovieSourceDialog : AppCompatDialogFragment() {

    private val movieViewModel: MovieViewModel by inject()

    private lateinit var listAdapter: ArrayAdapter<SourceHolder>
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        listAdapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_list_item_single_choice)

        return MaterialAlertDialogBuilder(requireContext())
                .setTitle("选择电影源")
                .setSingleChoiceItems(listAdapter, 0) { _, position ->
                    movieViewModel.changeMovieSource(listAdapter.getItem(position)?.key)
                    dismiss()
                }
                .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieViewModel.movieSource.observe(this) {
            listAdapter.addAll(it.map { item ->
                if (item.first == movieViewModel.defaultSourceKey) {
                    setItemChecked(
                            item.toSourceHolder()
                    )
                }
                item.toSourceHolder()
            })
        }
    }

    /**
     * 选择当前默认设置的视频源
     */
    private fun setItemChecked(default: SourceHolder) {
        (dialog as AlertDialog).listView.setItemChecked(
                listAdapter.getPosition(
                        default
                ), true)
    }

    data class SourceHolder(val key: String, val title: String) {
        override fun toString() = title
    }
}

fun Pair<String, String>.toSourceHolder() = SourceHolder(this.first, this.second)
