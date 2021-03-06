package com.tainzhi.android.videoplayer.widget.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/14 13:18
 * @description:
 **/


class CheckPermissionDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog =
            AlertDialog.Builder(activity)
                    .setTitle("权限设置")
                    .setMessage("缺少读取本机视频的权限, 请到Settings设置")
                    .setPositiveButton("设置") { _, _ ->
                        val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:${requireActivity().packageName}")
                        ).apply {
                            addCategory(Intent.CATEGORY_DEFAULT)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        startActivity(intent)
                    }
                    .setNegativeButton("退出") { _, _ ->
                        requireActivity().finish()
                    }
                    .create()
}

fun Activity.showCheckPermissionDialog(fragmentManager: FragmentManager) {
    CheckPermissionDialog().apply {
        // arguments = Bundle().apply {
        //     putString("title", "title")
        // }
    }.show(fragmentManager, "Check Permission")
}

