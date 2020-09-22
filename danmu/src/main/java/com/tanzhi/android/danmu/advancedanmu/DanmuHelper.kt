package com.tanzhi.android.danmu.advancedanmu

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.tanzhi.android.danmu.R
import com.tanzhi.android.danmu.advancedanmu.model.DanmuEntity
import com.tanzhi.android.danmu.advancedanmu.model.DanmuSystem
import com.tanzhi.android.danmu.advancedanmu.model.DanmuUser
import com.tanzhi.android.danmu.advancedanmu.view.IDanmu
import com.tanzhi.android.danmu.dpToPx
import com.tanzhi.android.danmu.spToPx
import java.lang.ref.WeakReference

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/11 17:10
 * @description: 弹幕库使用帮助类
 * 建议凡是弹幕中涉及到的图片，大小控制在50kb以内，尺寸控制在100x100以内（单位像素）
 **/

class DanMuHelper(val context: Context, private val danmuView: IDanmu) {
    private val danmuViewReference = WeakReference<IDanmu>(danmuView)
    fun release() {
    }
    
    fun addDanmu(danmuEntity: DanmuEntity, broadcast: Boolean) {
        val danmuModel: DanmuModel = createDanmuModel(danmuEntity)
        danmuViewReference.get()?.add(danmuModel)
    }
    
    private fun createDanmuModel(entity: DanmuEntity): DanmuModel = DanmuModel().apply {
        displayType = DanmuModel.RIGHT_TO_LEFT
        priority = DanmuModel.PRIORITY_NORMAL
        marginLeft = context.dpToPx(30)
        
        when (entity) {
            is DanmuUser -> {
                val avatarSize = context.dpToPx<Int>(30)
                avatarWidth = 30
                avatarHeight = 30
                avatar = Glide.with(context)
                    .asBitmap()
                    .load(entity.avator)
                    .override(avatarWidth, avatarHeight)
                    .circleCrop()
                    .submit()
                    .get()
                val drawable = ContextCompat.getDrawable(context, getLevelResId(entity.level))
                levelBitmap = drawable2Bitmap(drawable)
                levelBitmapWidth = context.dpToPx(33)
                levelBitmapHeight = context.dpToPx(16)
                levelBitmapMarginLeft = context.dpToPx(5)
        
                levelText = entity.level.toString()
                levelTextColor = ContextCompat.getColor(context, android.R.color.white)
                levelTextSize = context.spToPx(14)
        
                val spannableString = SpannableString("${entity.name}:${entity.text}").apply {
                    setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(context, android.R.color.white)),
                        0, entity.name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                text = spannableString
                enableTouch = true
            }
            is DanmuSystem -> {
                val spannableString = SpannableString("${entity.name}:${entity.text}").apply {
                    setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(context, android.R.color.white)),
                        0, entity.name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                text = spannableString
                enableTouch = false
            }
        }
        
        
        textSize = context.spToPx(14)
        textColor = ContextCompat.getColor(context, android.R.color.holo_green_dark)
        textMarginLeft = context.dpToPx(5)
        
        textBackground = ContextCompat.getDrawable(context, R.drawable.corners_danmu)
        textBackgroundPadding.set(
            context.dpToPx(15),
            context.dpToPx(3),
            context.dpToPx(15),
            context.dpToPx(3)
        )
        
    }
    
    /**
     * Drawable转换成Bitmap
     *
     * @param drawable
     * @return
     */
    private fun drawable2Bitmap(drawable: Drawable?): Bitmap? {
        return if (drawable is BitmapDrawable) {
            // 转换成Bitmap
            drawable.bitmap
        } else if (drawable is NinePatchDrawable) {
            // .9图片转换成Bitmap
            val bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                if (drawable.getOpacity() != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(
                0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight()
            )
            drawable.draw(canvas)
            bitmap
        } else {
            null
        }
    }

    /**
     * 设置等级
     *
     * @param level level=100表示主播
     */
    private fun getLevelResId(level: Int)= when (level) {
            in 0..5 -> R.drawable.icon_level_stage_zero
            in 6..10 -> R.drawable.icon_level_stage_two
            in 11..15 -> R.drawable.icon_level_stage_three
            in 16..20 -> R.drawable.icon_level_stage_four
            in 21..25 -> R.drawable.icon_level_stage_five
            in 26..30 -> R.drawable.icon_level_stage_six
            else -> R.drawable.icon_level_stage_six
        }
}