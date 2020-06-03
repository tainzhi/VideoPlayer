package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView.ShaderInterface
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * Applies lomo-camera style effect to video.
 *
 * @author sheraz.khilji
 */
class VignetteEffect(scale: Float) : ShaderInterface {
    private var mWidth = 0
    private var mHeight = 0
    private var mScale = 0f
    private val mShade = 0.85f

    /**
     * Init all values that will be used by this shader.
     *
     * @param mGlSurfaceView which is responsible for displaying your video
     */
    private fun initValues(mGlSurfaceView: GLSurfaceView) {
        mWidth = mGlSurfaceView.width
        mHeight = mGlSurfaceView.height
    }

    fun getShader(mGlSurfaceView: GLSurfaceView): String {
        initValues(mGlSurfaceView)
        val scale = FloatArray(2)
        if (mWidth > mHeight) {
            scale[0] = 1f
            scale[1] = mHeight.toFloat() / mWidth
        } else {
            scale[0] = mWidth.toFloat() / mHeight
            scale[1] = 1f
        }
        val max_dist = Math.sqrt(scale[0] * scale[0] + scale[1]
                * scale[1].toDouble()).toFloat() * 0.5f
        val scaleString = arrayOfNulls<String>(2)
        scaleString[0] = """
            scale[0] = ${scale[0]};
            
            """.trimIndent()
        scaleString[1] = """
            scale[1] = ${scale[1]};
            
            """.trimIndent()
        val inv_max_distString = """
            inv_max_dist = ${1.0f / max_dist};
            
            """.trimIndent()
        val shadeString = "shade = $mShade;\n"

        // The 'range' is between 1.3 to 0.6. When scale is zero then range is
        // 1.3
        // which means no vignette at all because the luminousity difference is
        // less than 1/256 and will cause nothing.
        val rangeString = """
            range = ${1.30f - Math.sqrt(mScale.toDouble()).toFloat() * 0.7f};
            
            """.trimIndent()
        return """#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES sTexture;
 float range;
 float inv_max_dist;
 float shade;
 vec2 scale;
varying vec2 vTextureCoord;
void main() {
${scaleString[0]}${scaleString[1]}$inv_max_distString$shadeString$rangeString  const float slope = 20.0;
  vec2 coord = vTextureCoord - vec2(0.5, 0.5);
  float dist = length(coord * scale);
  float lumen = shade / (1.0 + exp((dist * inv_max_dist - range) * slope)) + (1.0 - shade);
  vec4 color = texture2D(sTexture, vTextureCoord);
  gl_FragColor = vec4(color.rgb * lumen, color.a);
}
"""
    }

    /**
     * Initialize Effect
     *
     * @param scale Float, between 0 and 1. 0 means no change.
     */
    init {
        var scale = scale
        if (scale < 0.0f) scale = 0.0f
        if (scale > 1.0f) scale = 1.0f
        mScale = scale
    }
}