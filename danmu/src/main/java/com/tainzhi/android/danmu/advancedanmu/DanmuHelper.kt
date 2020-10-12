package com.tainzhi.android.danmu.advancedanmu

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.graphics.drawable.VectorDrawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tainzhi.android.danmu.R
import com.tainzhi.android.danmu.advancedanmu.model.DanmuEntity
import com.tainzhi.android.danmu.advancedanmu.view.IDanmuContainer
import java.lang.ref.WeakReference

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/11 17:10
 * @description: 弹幕库使用帮助类
 * 建议凡是弹幕中涉及到的图片，大小控制在50kb以内，尺寸控制在100x100以内（单位像素）
 **/

class DanMuHelper(val context: Context, danmuContainerView: IDanmuContainer) {
    private val danmuReference = WeakReference<IDanmuContainer>(danmuContainerView)
    fun release() {
    }

    fun addDanmu(danmuEntity: DanmuEntity) {
        val danmu: Danmu = createDanmu(danmuEntity)
        danmuReference.get()?.add(danmu)
    }

    fun hideAllDanmu() {
        danmuReference.get()?.hideAllDanmu(true)
    }

    private fun createDanmu(entity: DanmuEntity): Danmu = Danmu().apply {
        displayType = Danmu.RIGHT_TO_LEFT
        priority = Danmu.PRIORITY_NORMAL

        // 不能用这个get方法: 因为
        // java.lang.IllegalArgumentException: You must call this method on a background thread
        //
        // avatar = Glide.with(context)
        //         .asBitmap()
        //         .override(avatarWidth, avatarHeight)
        //         .circleCrop()
        //     .load(entity.avatar)
        //         .submit()
        //         .get()
        Glide.with(context)
            .asBitmap()
            .load(entity.avatar)
            .override(avatarWidth, avatarHeight)
            .circleCrop()
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    avatar = drawable2Bitmap(
                        ContextCompat.getDrawable(context, R.drawable.ic_default_avatar)
                    )
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    avatar = resource
                    return false
                }
            })
            .submit()

        val drawable = ContextCompat.getDrawable(context, getLevelResId(entity.level))
        levelBitmap = drawable2Bitmap(drawable)

        levelText = entity.level.toString()
        levelTextColor = ContextCompat.getColor(context, android.R.color.white)

        // 弹幕内容 = 用户name + 弹幕内容
        val spannableString = SpannableString("${entity.name}:${entity.text}").apply {
            setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, android.R.color.white)),
                0, entity.name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        text = spannableString
        textColor = ContextCompat.getColor(context, android.R.color.holo_green_dark)

        textBackground = ContextCompat.getDrawable(context, R.drawable.corners_danmu)

        enableTouch = true
    }

    /**
     * Drawable转换成Bitmap
     *
     * @param drawable
     * @return
     */
    private fun drawable2Bitmap(drawable: Drawable?): Bitmap {
        return when (drawable) {
            is BitmapDrawable -> drawable.bitmap
            is NinePatchDrawable,
            is VectorDrawable -> {
                // .9图片转换成Bitmap
                val bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                drawable.setBounds(
                    0, 0, canvas.width, canvas.height
                )
                drawable.draw(canvas)
                bitmap
            }
            else -> {
                (drawable as BitmapDrawable).bitmap
            }
        }
    }

    /**
     * 设置等级
     *
     * @param level level=100表示主播
     */
    private fun getLevelResId(level: Int) = when (level) {
        in 0..5 -> R.drawable.icon_level_stage_zero
        in 6..10 -> R.drawable.icon_level_stage_two
        in 11..15 -> R.drawable.icon_level_stage_three
        in 16..20 -> R.drawable.icon_level_stage_four
        in 21..25 -> R.drawable.icon_level_stage_five
        in 26..30 -> R.drawable.icon_level_stage_six
        else -> R.drawable.icon_level_stage_six
    }
}