package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView.ShaderInterface
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * Adjusts color temperature of the video.
 *
 * @author sheraz.khilji
 */
class TemperatureEffect(scale: Float) : ShaderInterface {
    private val scale = 0f
    fun getShader(mGlSurfaceView: GLSurfaceView?): String {
        val scaleString = """
            scale = ${2.0f * scale - 1.0f};
            
            """.trimIndent()
        return """#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES sTexture;
 float scale;
varying vec2 vTextureCoord;
void main() {
$scaleString  vec4 color = texture2D(sTexture, vTextureCoord);
  vec3 new_color = color.rgb;
  new_color.r = color.r + color.r * ( 1.0 - color.r) * scale;
  new_color.b = color.b - color.b * ( 1.0 - color.b) * scale;
  if (scale > 0.0) {
    new_color.g = color.g + color.g * ( 1.0 - color.g) * scale * 0.25;
  }
  float max_value = max(new_color.r, max(new_color.g, new_color.b));
  if (max_value > 1.0) {
     new_color /= max_value;
  }
  gl_FragColor = vec4(new_color, color.a);
}
"""
    }

    /**
     * Initialize Effect
     *
     * @param scale Float, between 0 and 1, with 0 indicating cool, and 1
     * indicating warm. A value of of 0.5 indicates no change.
     */
    init {
        var scale = scale
        if (scale < 0.0f) scale = 0.0f
        if (scale > 1.0f) scale = 1.0f
        this.scale = scale
    }
}