package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView.ShaderInterface
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * Applies back-light filling to the video.
 *
 * @author sheraz.khilji
 */
class FillLightEffect(strength: Float) : ShaderInterface {
    private val strength = 0f
    fun getShader(mGlSurfaceView: GLSurfaceView?): String {
        val fade_gamma = 0.3f
        val amt = 1.0f - strength
        val mult = 1.0f / (amt * 0.7f + 0.3f)
        val faded = fade_gamma + (1.0f - fade_gamma) * mult
        val igamma = 1.0f / faded
        val multString = "mult = $mult;\n"
        val igammaString = "igamma = $igamma;\n"
        return """#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES sTexture;
 float mult;
 float igamma;
varying vec2 vTextureCoord;
void main()
{
$multString$igammaString  const vec3 color_weights = vec3(0.25, 0.5, 0.25);
  vec4 color = texture2D(sTexture, vTextureCoord);
  float lightmask = dot(color.rgb, color_weights);
  float backmask = (1.0 - lightmask);
  vec3 ones = vec3(1.0, 1.0, 1.0);
  vec3 diff = pow(mult * color.rgb, igamma * ones) - color.rgb;
  diff = min(diff, 1.0);
  vec3 new_color = min(color.rgb + diff * backmask, 1.0);
  gl_FragColor = vec4(new_color, color.a);
}
"""
    }

    /**
     * Initialize Effect
     *
     * @param strength Float, between 0 and 1. 0 means no change.
     */
    init {
        var strength = strength
        if (strength < 0.0f) strength = 0f
        if (strength > 1.0f) strength = 1f
        this.strength = strength
    }
}