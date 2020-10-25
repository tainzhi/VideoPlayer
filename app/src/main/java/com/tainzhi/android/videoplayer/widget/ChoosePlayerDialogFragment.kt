package com.tainzhi.android.videoplayer.widget

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tainzhi.android.videoplayer.repository.PreferenceRepository
import com.tainzhi.qmediaplayer.Constant
import org.koin.android.ext.android.inject

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/9 15:01
 * @description: 选择播放器类型
 **/

class ChoosePlayerDialogFragment : AppCompatDialogFragment() {

    private val preferenceRepository: PreferenceRepository by inject()

    private lateinit var listAdapter: ArrayAdapter<PlayerTypeHolder>
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        listAdapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_list_item_single_choice)
        listAdapter.addAll(
                listOf(
                        PlayerTypeHolder(Constant.PlayerType.SYSTEM_PLAYER, "System Player"),
                        PlayerTypeHolder(Constant.PlayerType.IJK_PLAYER, "IjkPlayer"),
                        PlayerTypeHolder(Constant.PlayerType.EXO_PLAYER, "ExoPlayer")
                )
        )

        return MaterialAlertDialogBuilder(requireContext())
                .setTitle("Choose Player")
                .setSingleChoiceItems(listAdapter, 0) { _, position ->
                    listAdapter.getItem(position)?.type?.let {
                        preferenceRepository.playerType = it
                    }
                    dismiss()
                }
                .create()
    }

    override fun onStart() {
        super.onStart()

        val playerTypeIndex = preferenceRepository.playerType ?: 0
        (dialog as AlertDialog).listView.setItemChecked(playerTypeIndex, true)
    }

    data class PlayerTypeHolder(val type: Int, val title: String) {
        override fun toString() = title
    }
}
