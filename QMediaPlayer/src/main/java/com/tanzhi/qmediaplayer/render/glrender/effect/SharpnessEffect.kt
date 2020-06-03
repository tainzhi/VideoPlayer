package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView.ShaderInterface
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * Sharpens the video.
 *
 * @author sheraz.khilji
 */
class SharpnessEffect(scale: Float) : ShaderInterface {
    private var mWidth = 0
    private var mHeight = 0
    private val scale = 0f

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
        val stepsizeXString = """
            stepsizeX = ${1.0f / mWidth};
            
            """.trimIndent()
        val stepsizeYString = """
            stepsizeY = ${1.0f / mHeight};
            
            """.trimIndent()
        val scaleString = "scale = $scale;\n"
        return """#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES sTexture;
 float scale;
 float stepsizeX;
 float stepsizeY;
varying vec2 vTextureCoord;
void main() {
$stepsizeXString$stepsizeYString$scaleString  vec3 nbr_color = vec3(0.0, 0.0, 0.0);
  vec2 coord;
  vec4 color = texture2D(sTexture, vTextureCoord);
  coord.x = vTextureCoord.x - 0.5 * stepsizeX;
  coord.y = vTextureCoord.y - stepsizeY;
  nbr_color += texture2D(sTexture, coord).rgb - color.rgb;
  coord.x = vTextureCoord.x - stepsizeX;
  coord.y = vTextureCoord.y + 0.5 * stepsizeY;
  nbr_color += texture2D(sTexture, coord).rgb - color.rgb;
  coord.x = vTextureCoord.x + stepsizeX;
  coord.y = vTextureCoord.y - 0.5 * stepsizeY;
  nbr_color += texture2D(sTexture, coord).rgb - color.rgb;
  coord.x = vTextureCoord.x + stepsizeX;
  coord.y = vTextureCoord.y + 0.5 * stepsizeY;
  nbr_color += texture2D(sTexture, coord).rgb - color.rgb;
  gl_FragColor = vec4(color.rgb - 2.0 * scale * nbr_color, color.a);
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
        this.scale = scale
    }
}