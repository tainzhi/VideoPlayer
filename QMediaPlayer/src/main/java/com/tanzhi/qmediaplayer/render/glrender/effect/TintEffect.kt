package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.graphics.Color
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView.ShaderInterface
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * Tints the video with specified color..
 *
 * @author sheraz.khilji
 */
class TintEffect(color: Int) : ShaderInterface {
    private var mTint = -0xffff01
    fun getShader(mGlSurfaceView: GLSurfaceView?): String {
        val color_ratio = floatArrayOf(0.21f, 0.71f, 0.07f)
        val color_ratioString = arrayOfNulls<String>(3)
        color_ratioString[0] = """
            color_ratio[0] = ${color_ratio[0]};
            
            """.trimIndent()
        color_ratioString[1] = """
            color_ratio[1] = ${color_ratio[1]};
            
            """.trimIndent()
        color_ratioString[2] = """
            color_ratio[2] = ${color_ratio[2]};
            
            """.trimIndent()
        val tint_color = floatArrayOf(Color.red(mTint) / 255f,
                Color.green(mTint) / 255f, Color.blue(mTint) / 255f)
        val tintString = arrayOfNulls<String>(3)
        tintString[0] = """
            tint[0] = ${tint_color[0]};
            
            """.trimIndent()
        tintString[1] = """
            tint[1] = ${tint_color[1]};
            
            """.trimIndent()
        tintString[2] = """
            tint[2] = ${tint_color[2]};
            
            """.trimIndent()
        return """#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES sTexture;
 vec3 tint;
 vec3 color_ratio;
varying vec2 vTextureCoord;
void main() {
${color_ratioString[0]}${color_ratioString[1]}${color_ratioString[2]}${tintString[0]}${tintString[1]}${tintString[2]}  vec4 color = texture2D(sTexture, vTextureCoord);
  float avg_color = dot(color_ratio, color.rgb);
  vec3 new_color = min(0.8 * avg_color + 0.2 * tint, 1.0);
  gl_FragColor = vec4(new_color.rgb, color.a);
}
"""
    }

    /**
     * Initialize Effect
     *
     * @param color Integer, representing an ARGB color with 8 bits per channel.
     * May be created using Color class.
     */
    init {
        mTint = color
    }
}