package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.graphics.Color
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView.ShaderInterface
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * Representation of video using only two color tones.
 *
 * @author sheraz.khilji
 */
class DuotoneEffect(mFirstColor: Int, mSecondColor: Int) : ShaderInterface {
    // Default values
    private val mFirstColor = Color.MAGENTA
    private val mSecondColor = Color.YELLOW
    fun getShader(mGlSurfaceView: GLSurfaceView?): String {
        val first = floatArrayOf(Color.red(mFirstColor) / 255f,
                Color.green(mFirstColor) / 255f, Color.blue(mFirstColor) / 255f)
        val second = floatArrayOf(Color.red(mSecondColor) / 255f,
                Color.green(mSecondColor) / 255f,
                Color.blue(mSecondColor) / 255f)
        val firstColorString = arrayOfNulls<String>(3)
        val secondColorString = arrayOfNulls<String>(3)
        firstColorString[0] = """
            first[0] = ${first[0]};
            
            """.trimIndent()
        firstColorString[1] = """
            first[1] = ${first[1]};
            
            """.trimIndent()
        firstColorString[2] = """
            first[2] = ${first[2]};
            
            """.trimIndent()
        secondColorString[0] = """
            second[0] = ${second[0]};
            
            """.trimIndent()
        secondColorString[1] = """
            second[1] = ${second[1]};
            
            """.trimIndent()
        secondColorString[2] = """
            second[2] = ${second[2]};
            
            """.trimIndent()
        return """#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES sTexture;
 vec3 first;
 vec3 second;
varying vec2 vTextureCoord;
void main() {
${firstColorString[0]}${firstColorString[1]}${firstColorString[2]}${secondColorString[0]}${secondColorString[1]}${secondColorString[2]}  vec4 color = texture2D(sTexture, vTextureCoord);
  float energy = (color.r + color.g + color.b) * 0.3333;
  vec3 new_color = (1.0 - energy) * first + energy * second;
  gl_FragColor = vec4(new_color.rgb, color.a);
}
"""
    }

    /**
     * Initialize effect
     *
     * @param mFirstColor Integer, representing an ARGB color with 8 bits per channel.
     * May be created using Color class.
     * @param mSecondColor Integer, representing an ARGB color with 8 bits per channel.
     * May be created using Color class.
     */
    init {
        this.mFirstColor = mFirstColor
        this.mSecondColor = mSecondColor
    }
}