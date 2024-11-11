package com.tainzhi.android.videoplayer.binding

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.tainzhi.android.videoplayer.util.FormatUtil.formatMediaDuration
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.util.fromN
import glimpse.glide.GlimpseTransformation
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/12 19:54
 * @description:
 **/


@BindingAdapter("visibleUnless")
fun bindVisibleUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

@BindingAdapter("goneUnless")
fun bindGoneUnless(view: View, gone: Boolean) {
    view.visibility = if (gone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("htmlText")
fun bindHtmlText(view:TextView,html:String){
    view.text = if (fromN()) Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY) else Html.fromHtml(html)
}

@BindingAdapter("srcVectorDrawable")
fun bindImage(imageView: ImageView, srcId: Int) {
    imageView.setImageResource(srcId)
}

@BindingAdapter(
        "imageUrl",
        "imagePlaceholder",
        "circleCropImage",
        "roundCornerRadiusImage",
        "crossFadeImage",
        "overrideImageWidth",
        "overrideImageHeight",
        "imageLoadListener",
        requireAll = false
)
fun bindImage(
        imageView: ImageView,
        imageUrl: String?,
        placeholder: Int? = null,
        circleCrop: Boolean? = false,
        roundCornerRadius: Int? = null,
        crossFade: Boolean? = false,
        overrideWidth: Int? = null,
        overrideHeight: Int? = null,
        listener: RequestListener<Drawable>?
) {
    if (imageUrl == null) return
    var request = Glide.with(imageView.context).load(imageUrl)
    if (placeholder != null) {
        request = request.placeholder(placeholder)
    }
    if (circleCrop == true) {
        request = request.circleCrop()
    }
    if (roundCornerRadius != null) {
        request = if (circleCrop == true) {
            request.transform(CenterCrop(), RoundedCorners(roundCornerRadius))
        } else {
            request.transform(RoundedCorners(roundCornerRadius))
        }
    }
    if (crossFade == true) {
        request = request.transition(DrawableTransitionOptions.withCrossFade())
    }
    if (overrideWidth != null && overrideHeight != null) {
        request = request.override(overrideWidth, overrideHeight)
    }
    if (listener != null) {
        request = request.listener(listener)
    }
    request.into(imageView)
}

@BindingAdapter(value = ["afterTextChanged"])
fun EditText.afterTextChanged(action: () -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            action()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}

@BindingAdapter(value = ["dateText"])
fun convertDateToString(textView: TextView, date: Date) {
    textView.text = SimpleDateFormat.getInstance().format(date)
}

@BindingAdapter(
    "videoUri",
    "thumbPlaceHolder",
    "cornerRadius",
    "thumbnailWidth",
    "thumbnailHeight",
    "frame",
    requireAll = false
)
fun bindVideoThumbnail(
    imageView: ImageView,
    videoUri: Uri,
    placeHolder: Int? = null,
    cornerRadius: Int? = null,
    frame: Long = 10,
    width: Int? = null,
    height: Int? = null
) {
    // val retriver = MediaMetadataRetriever()
    // val image = retriver.getFrameAtTime(10000L)
    // val thumbnail: Bitmap? =
    //     try {
    //         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    //             App.CONTEXT.contentResolver.loadThumbnail(
    //                 videoUri, Size(320, 240), null
    //             )
    //         } else {
    //             // val file = videoUri.toFile()
    //             // android 29 才能使用
    //             // ThumbnailUtils.createVideoThumbnail(videoUri.toFile(), Size(320, 240), null)
    //             MediaStore.Video.Thumbnails.getThumbnail(App.CONTEXT.contentResolver,
    //                 ContentUris.parseId(videoUri),
    //                 MediaStore.Images.Thumbnails.MINI_KIND,
    //                 BitmapFactory.Options().apply { inSampleSize = 4 })
    //         }
    //     } catch (e: Exception) {
    //         Log.e("BindingAdapter", "${videoUri.path}, ${e.message}")
    //         null
    //     } ?: return
    // val options = RequestOptions()
    // placeHolder?.let { options.placeholder(placeHolder) }
    // cornerRadius?.let { options.transform(CenterCrop(), RoundedCorners(it), GlimpseTransformation()) }
    // Glide.with(imageView.context).load(thumbnail)
    //     .apply(options)
    //     .into(imageView)
    val options = RequestOptions()
            .frame(frame)
    placeHolder?.let { options.placeholder(placeHolder) }
    cornerRadius?.let { options.transform(CenterCrop(), RoundedCorners(it), GlimpseTransformation()) }
    Glide.with(imageView.context)
            // .setDefaultRequestOptions(
            //         // RequestOptions().frame(10).placeholder(placeHolder)
            //         RequestOptions().frame(100)
            // )
            .load(videoUri)
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


