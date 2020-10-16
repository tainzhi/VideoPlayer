package com.tainzhi.android.videoplayer.widget

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tainzhi.android.videoplayer.repository.PreferenceRepository
import com.tainzhi.android.videoplayer.util.Theme
import org.koin.android.ext.android.inject

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/9 15:01
 * @description: 选择主题
 **/

class ChooseThemeDialogFragment : AppCompatDialogFragment() {

    private val preferenceRepository: PreferenceRepository by inject()

    private lateinit var listAdapter: ArrayAdapter<ThemeType>
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        listAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_single_choice
        )
        listAdapter.addAll(
            listOf(
                ThemeType(Theme.MODE_NIGHT_NO, "默认红色"),
                ThemeType(Theme.MODE_NIGHT_YES, "暗夜模式"),
                ThemeType(Theme.MODE_GRAY, "全局置灰")
            )
        )

        return MaterialAlertDialogBuilder(context)
            .setTitle("Choose Theme")
            .setSingleChoiceItems(listAdapter, 0) { _, position ->
                listAdapter.getItem(position)?.type?.let {
                    preferenceRepository.selectedTheme = it
                }
                dismiss()
            }
            .create()
    }

    override fun onStart() {
        super.onStart()

        // 主题从 1 开始, 故要 -1作为index
        val renderIndex = preferenceRepository.selectedTheme - 1
        (dialog as AlertDialog).listView.setItemChecked(renderIndex, true)
    }

    data class ThemeType(val type: Int, val title: String) {

        override fun toString() = title
    }
}
