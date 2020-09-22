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
 * @description: 选择播放器渲染方法
 **/

class ChoosePlayerRenderTypeDialogFragment : AppCompatDialogFragment() {

    private val preferenceRepository: PreferenceRepository by inject()

    private lateinit var listAdapter: ArrayAdapter<PlayerRenderTypeHolder>
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        listAdapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_list_item_single_choice)
        listAdapter.addAll(
                listOf(
                        PlayerRenderTypeHolder(Constant.RenderType.SURFACE_VIEW, "SurfaceView"),
                        PlayerRenderTypeHolder(Constant.RenderType.TEXTURE_VIEW, "TextureView"),
                        PlayerRenderTypeHolder(Constant.RenderType.GL_SURFACE_VIEW, "GLSurfaceView")
                )
        )

        return MaterialAlertDialogBuilder(context)
                .setTitle("Choose Player Render")
                .setSingleChoiceItems(listAdapter, 0) { _, position ->
                    listAdapter.getItem(position)?.type?.let {
                        preferenceRepository.playerRenderType = it
                    }
                    dismiss()
                }
                .create()
    }

    override fun onStart() {
        super.onStart()

        val renderIndex = preferenceRepository.playerRenderType ?: 0
        (dialog as AlertDialog).listView.setItemChecked(renderIndex, true)
    }

    data class PlayerRenderTypeHolder(val type: Int, val title: String) {
        override fun toString() = title
    }
}
