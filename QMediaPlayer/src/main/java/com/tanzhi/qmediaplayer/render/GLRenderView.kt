package com.tanzhi.qmediaplayer.render

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/29 16:32
 * @description:
 **/

// class GLRenderView: GLSurfaceView, IRenderView {
//
//     private val measureHelper: MeasureHelper = MeasureHelper(this)
//     private var surfaceCallback: SurfaceCallback? = null
//
//     constructor(context: Context) : super(context)
//
//     constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
//
//     override val view = this
//
//     override fun shouldWaitForResize(): Boolean {
//         return false
//     }
//
//     // ------------------------
//     // layout & measure
//     // ------------------------
//     override fun setVideoSize(videoWidth: Int, videoHeight: Int) {
//         if (videoWidth > 0 && videoHeight > 0) {
//             measureHelper!!.setVideoSize(videoWidth, videoHeight)
//             requestLayout()
//         }
//     }
//
//     override fun setVideoSampleAspectRatio(videoSarNum: Int, videoSarDen: Int) {
//         if (videoSarNum > 0 && videoSarDen > 0) {
//             measureHelper!!.setVideoSampleAspectRatio(videoSarNum, videoSarDen)
//             requestLayout()
//         }
//     }
//
//     override fun setVideoRotation(degree: Int) {
//         measureHelper!!.setVideoRotation(degree)
//         rotation = degree.toFloat()
//     }
//
//     override fun setAspectRatio(aspectRatio: Int) {
//         measureHelper!!.setVideoRotation(aspectRatio)
//         requestLayout()
//     }
//
//     override fun addRenderCallback(callback: IRenderView.IRenderCallback) {
//         TODO("Not yet implemented")
//     }
//
//     override fun removeRenderCallback(callback: IRenderView.IRenderCallback) {
//         TODO("Not yet implemented")
//     }
//
// }