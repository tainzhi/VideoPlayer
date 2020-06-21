package com.tainzhi.android.videoplayer.binding

import android.content.ContentUris
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.tainzhi.android.common.util.FormatUtil.formatMediaDuration
import com.tainzhi.android.videoplayer.App

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/12 19:54
 * @description:
 **/


@BindingAdapter(
        "videoUri",
        "thumbPlaceHolder",
        "cornerRadius",
        "thumbnailWidth",
        "thumbnailHeight",
        requireAll = false
)
fun bindVideoThumbnail(
        imageView: ImageView,
        videoUri: Uri,
        placeHolder: Int? = null,
        cornerRadius: Int? = null,
        width: Int? = null,
        height: Int? = null
) {
    var thumbnail: Bitmap? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        thumbnail =
                App.CONTEXT.contentResolver.loadThumbnail(
                        videoUri, Size(320, 240), null)
    } else {
        thumbnail = MediaStore.Video.Thumbnails.getThumbnail(App.CONTEXT.contentResolver, ContentUris.parseId(videoUri), MediaStore.Images.Thumbnails.MICRO_KIND,
                BitmapFactory.Options())
    }
    val options = RequestOptions()
    placeHolder?.let {options.placeholder(placeHolder)}
    cornerRadius?.let { options.transform(RoundedCorners(it)) }
    Glide.with(imageView.context).load(thumbnail)
            .apply(options)
            .into(imageView)
}

@BindingAdapter(
    "duration"
)
fun bindDuration(textView: TextView, duration: Long) {
    textView.text = duration.formatMediaDuration()
}
