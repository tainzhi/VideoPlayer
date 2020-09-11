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
import com.tanzhi.android.danmu.R
import com.tanzhi.android.danmu.advancedanmu.model.DanmuEntity
import com.tanzhi.android.danmu.advancedanmu.view.IDanmu
import com.tanzhi.android.danmu.dpToPx
import java.lang.ref.WeakReference
import java.util.*

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/11 17:10
 * @description: 弹幕库使用帮助类
 * 建议凡是弹幕中涉及到的图片，大小控制在50kb以内，尺寸控制在100x100以内（单位像素）
 **/

class DanMuHelper(val context: Context) {
    private val danmuList =  mutableListOf<WeakReference<IDanmu>>()
    fun release() {
    }

    fun add(danMuViewParent: IDanmu) {
        danmuList.add(WeakReference<IDanmu>(danMuViewParent))
    }

    fun addDanmu(danmuEntity: DanmuEntity, broadcast: Boolean) {
            var danMuViewParent: WeakReference<IDanmu?> = danmuList!![0]
            if (!broadcast) {
                danMuViewParent = danmuList!![1]
            }
            val danMuView: DanmuModel = createDanMuView(danmuEntity)
            if (danMuViewParent != null && danMuView != null && danMuViewParent.get() != null) {
                danMuViewParent.get().add(danMuView)
            }
    }

    private fun createDanMuView(entity: DanmuEntity): DanmuModel {
        val danMuView = DanmuModel().apply {
            displayType = DanmuModel.RIGHT_TO_LEFT
            priority = DanmuModel.PRIORITY_NORMAL
            marginLeft = context.dpToPx(30)
        }
        danMuView.setDisplayType(DanmuModel.RIGHT_TO_LEFT)
        danMuView.setPriority(DanmuModel.NORMAL)
        danMuView.marginLeft = DimensionUtil.dpToPx(mContext, 30)
        if (entity.getType() === DanmakuEntity.DANMAKU_TYPE_USERCHAT) {
            // 图像
            val avatarSize: Int = DimensionUtil.dpToPx(mContext, 30)
            danMuView.avatarWidth = avatarSize
            danMuView.avatarHeight = avatarSize
            val avatarImageUrl: String = entity.getAvatar()
            Phoenix.with(mContext)
                    .setUrl(avatarImageUrl)
                    .setWidth(avatarSize)
                    .setHeight(avatarSize)
                    .setResult(object : IResult<Bitmap?>() {
                        fun onResult(bitmap: Bitmap?) {
                            danMuView.avatar = CircleBitmapTransform.transform(bitmap)
                        }
                    })
                    .load()

            // 等级
            val level: Int = entity.getLevel()
            val levelResId = getLevelResId(level)
            val drawable: Drawable = ContextCompat.getDrawable(mContext, levelResId)
            danMuView.levelBitmap = drawable2Bitmap(drawable)
            danMuView.levelBitmapWidth = DimensionUtil.dpToPx(mContext, 33)
            danMuView.levelBitmapHeight = DimensionUtil.dpToPx(mContext, 16)
            danMuView.levelMarginLeft = DimensionUtil.dpToPx(mContext, 5)
            if (level > 0 && level < 100) {
                danMuView.levelText = level.toString()
                danMuView.levelTextColor = ContextCompat.getColor(mContext, R.color.white)
                danMuView.levelTextSize = DimensionUtil.spToPx(mContext, 14)
            }

            // 显示的文本内容
            val name: String = entity.getName().toString() + "："
            val content: String = entity.getText()
            val spannableString = SpannableString(name + content)
            spannableString.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.white)),
                    0,
                    name.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            MLog.i("spannableString = $spannableString")
            danMuView.textSize = DimensionUtil.spToPx(mContext, 14)
            danMuView.textColor = ContextCompat.getColor(mContext, R.color.light_green)
            danMuView.textMarginLeft = DimensionUtil.dpToPx(mContext, 5)
            danMuView.text = spannableString

            // 弹幕文本背景
            danMuView.textBackground = ContextCompat.getDrawable(mContext, R.drawable.corners_danmu)
            danMuView.textBackgroundMarginLeft = DimensionUtil.dpToPx(mContext, 15)
            danMuView.textBackgroundPaddingTop = DimensionUtil.dpToPx(mContext, 3)
            danMuView.textBackgroundPaddingBottom = DimensionUtil.dpToPx(mContext, 3)
            danMuView.textBackgroundPaddingRight = DimensionUtil.dpToPx(mContext, 15)
            danMuView.enableTouch(true)
            danMuView.setOnTouchCallBackListener(object : OnDanMuTouchCallBackListener() {
                fun callBack(danMuView: DanmuModel?) {}
            })
        } else {
            // 显示的文本内容
            danMuView.textSize = DimensionUtil.spToPx(mContext, 14)
            danMuView.textColor = ContextCompat.getColor(mContext, R.color.light_green)
            danMuView.textMarginLeft = DimensionUtil.dpToPx(mContext, 5)
            if (entity.getRichText() != null) {
                danMuView.text = RichTextParse.parse(mContext, entity.getRichText(), DimensionUtil.spToPx(mContext, 18), false)
            } else {
                danMuView.text = entity.getText()
            }

            // 弹幕文本背景
            danMuView.textBackground = ContextCompat.getDrawable(mContext, R.drawable.corners_danmu)
            danMuView.textBackgroundMarginLeft = DimensionUtil.dpToPx(mContext, 15)
            danMuView.textBackgroundPaddingTop = DimensionUtil.dpToPx(mContext, 3)
            danMuView.textBackgroundPaddingBottom = DimensionUtil.dpToPx(mContext, 3)
            danMuView.textBackgroundPaddingRight = DimensionUtil.dpToPx(mContext, 15)
            danMuView.enableTouch(false)
        }
        return danMuView
    }

    /**
     * Drawable转换成Bitmap
     *
     * @param drawable
     * @return
     */
    private fun drawable2Bitmap(drawable: Drawable): Bitmap? {
        return if (drawable is BitmapDrawable) {
            // 转换成Bitmap
            drawable.bitmap
        } else if (drawable is NinePatchDrawable) {
            // .9图片转换成Bitmap
            val bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    if (drawable.getOpacity() != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight())
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