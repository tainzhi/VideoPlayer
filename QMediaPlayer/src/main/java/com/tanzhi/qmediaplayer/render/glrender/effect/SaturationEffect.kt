package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView.ShaderInterface
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * Adjusts color saturation of video. There is still some issue with this
 * effect.
 *
 * @author sheraz.khilji
 */
class SaturationEffect(scale: Float) : ShaderInterface {
    private val scale = 0f
    fun getShader(mGlSurfaceView: GLSurfaceView?): String {
        val shift = 1.0f / 255.0f
        val weights = floatArrayOf(2f / 8f, 5f / 8f, 1f / 8f)
        val exponents = FloatArray(3)
        val weightsString = arrayOfNulls<String>(3)
        val exponentsString = arrayOfNulls<String>(3)
        exponentsString[0] = ""
        exponentsString[1] = ""
        exponentsString[2] = ""
        var scaleString = ""
        if (scale > 0.0f) {
            exponents[0] = 0.9f * scale + 1.0f
            exponents[1] = 2.1f * scale + 1.0f
            exponents[2] = 2.7f * scale + 1.0f
            exponentsString[0] = """
                exponents[0] = ${exponents[0]};
                
                """.trimIndent()
            exponentsString[1] = """
                exponents[1] = ${exponents[1]};
                
                """.trimIndent()
            exponentsString[2] = """
                exponents[2] = ${exponents[2]};
                
                """.trimIndent()
        } else scaleString = """
     scale = ${1.0f + scale};
     
     """.trimIndent()
        weightsString[0] = """
            weights[0] = ${weights[0]};
            
            """.trimIndent()
        weightsString[1] = """
            weights[1] = ${weights[1]};
            
            """.trimIndent()
        weightsString[2] = """
            weights[2] = ${weights[2]};
            
            """.trimIndent()
        val shiftString = "shift = $shift;\n"
        return """#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES sTexture;
 float scale;
 float shift;
 vec3 weights;
 vec3 exponents;
varying vec2 vTextureCoord;
void main() {
${weightsString[0]}${weightsString[1]}${weightsString[2]}$shiftString$scaleString  vec4 oldcolor = texture2D(sTexture, vTextureCoord);
  float kv = dot(oldcolor.rgb, weights) + shift;
  vec3 new_color = scale * oldcolor.rgb + (1.0 - scale) * kv;
  gl_FragColor= vec4(new_color, oldcolor.a);
${weightsString[0]}${weightsString[1]}${weightsString[2]}${exponentsString[0]}${exponentsString[1]}${exponentsString[2]}  vec4 color = texture2D(sTexture, vTextureCoord);
  float de = dot(color.rgb, weights);
  float inv_de = 1.0 / de;
  vec3 verynew_color = de * pow(color.rgb * inv_de, exponents);
  float max_color = max(max(max(verynew_color.r, verynew_color.g), verynew_color.b), 1.0);
  gl_FragColor = gl_FragColor+vec4(verynew_color / max_color, color.a);
}
"""
    }

    /**
     * Initialize Effect
     *
     * @param scale Float, between -1 and 1. 0 means no change, while -1 indicates
     * full desaturation, i.e. grayscale.
     */
    init {
        this.scale = scale
    }
}