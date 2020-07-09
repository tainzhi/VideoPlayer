package com.tainzhi.android.videoplayer.widget

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/9 15:01
 * @description:
 **/

class ChoosePlayerDialogFragment : AppCompatDialogFragment() {

    private lateinit var listAdapter: ArrayAdapter<PlayerHolder>
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        listAdapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_list_item_single_choice)
        listAdapter.add(PlayerHolder(1, "System Player"))
        listAdapter.add(PlayerHolder(2, "System Player"))
        listAdapter.add(PlayerHolder(2, "System Player"))
        return MaterialAlertDialogBuilder(context)
                .setTitle("Choose Player")
                .setSingleChoiceItems(listAdapter, 0) { _, _ ->
                    dismiss()
                }
                .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private data class PlayerHolder(val type: Int, val title: String) {
        override fun toString() = title
    }
}
