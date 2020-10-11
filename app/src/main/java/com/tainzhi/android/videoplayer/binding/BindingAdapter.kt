package com.tainzhi.android.videoplayer.binding

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.tainzhi.android.common.util.FormatUtil.formatMediaDuration
import com.tainzhi.android.videoplayer.App
import com.tainzhi.android.videoplayer.R

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
    val thumbnail: Bitmap? =
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    App.CONTEXT.contentResolver.loadThumbnail(
                            videoUri, Size(320, 240), null)
                } else {
                    // val file = videoUri.toFile()
                    // android 29 才能使用
                    // ThumbnailUtils.createVideoThumbnail(videoUri.toFile(), Size(320, 240), null)
                    MediaStore.Video.Thumbnails.getThumbnail(App.CONTEXT.contentResolver,
                            ContentUris.parseId(videoUri),
                            MediaStore.Images.Thumbnails.MINI_KIND,
                            BitmapFactory.Options().apply { inSampleSize = 4 })
                }
            } catch (e: Exception) {
                Log.e("BindingAdapter", "${videoUri.path}, ${e.message}")
                null
            } ?: return
    val options = RequestOptions()
    placeHolder?.let { options.placeholder(placeHolder) }
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

private const val CHROME_PACKAGE = "com.android.chrome"

@BindingAdapter("websiteLink", "hideWhenEmpty", requireAll = false)
fun websiteLink(
        button: View,
        url: String?,
        hideWhenEmpty: Boolean = false
) {
    if (url.isNullOrEmpty()) {
        if (hideWhenEmpty) {
            button.isVisible = false
        } else {
            button.isClickable = false
        }
        return
    }
    button.isVisible = true
    button.setOnClickListener {
        openWebsiteUrl(it.context, url)
    }
}

fun openWebsiteUrl(context: Context, url: String) {
    if (url.isBlank()) {
        return
    }
    openWebsiteUri(context, Uri.parse(url))
}

fun openWebsiteUri(context: Context, uri: Uri) {
    if (context.isChromeCustomTabsSupported()) {
        CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                .build()
                .launchUrl(context, uri)
    } else {
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
}

private fun Context.isChromeCustomTabsSupported(): Boolean {
    val serviceIntent = Intent(CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION)
    serviceIntent.setPackage(CHROME_PACKAGE)
    val resolveInfos = packageManager.queryIntentServices(serviceIntent, 0)
    return !resolveInfos.isNullOrEmpty()
}

