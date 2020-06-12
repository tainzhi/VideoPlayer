package com.tainzhi.android.videoplayer.binding

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/12 19:54
 * @description:
 **/


@BindingAdapter(
        "thumbnail",
        "thumbnailWidth",
        "thumbnailHeight",
        requireAll = false
)
fun bindVideoThumbnail(
        imageView: ImageView,
        thumbnail: Bitmap?,
        width: Int? = null,
        height: Int? = null
) {
    if (thumbnail == null) return
    Glide.with(imageView.context).load(thumbnail)
            .into(imageView)
}
