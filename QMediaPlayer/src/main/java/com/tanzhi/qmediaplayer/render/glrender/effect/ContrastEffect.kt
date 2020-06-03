package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView.ShaderInterface
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * Adjusts the contrast of the video.
 *
 * @author sheraz.khilji
 */
class ContrastEffect(contrast: Float) : ShaderInterface {
    private val contrast: Float
    fun getShader(mGlSurfaceView: GLSurfaceView?): String {
        return """#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES sTexture;
 float contrast;
varying vec2 vTextureCoord;
void main() {
  contrast =$contrast;
  vec4 color = texture2D(sTexture, vTextureCoord);
  color -= 0.5;
  color *= contrast;
  color += 0.5;
  gl_FragColor = color;
}
"""
    }

    /**
     * Initialize Effect
     *
     * @param contrast Range should be between 0.1- 2.0 with 1.0 being normal.
     */
    init {
        var contrast = contrast
        if (contrast < 0.1f) contrast = 0.1f
        if (contrast > 2.0f) contrast = 2.0f
        this.contrast = contrast
    }
}