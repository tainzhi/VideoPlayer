package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView.ShaderInterface
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * Adjusts the brightness of the video.
 *
 * @author sheraz.khilji
 */
class BrightnessEffect(brightnessvalue: Float) : ShaderInterface {
    private val brightnessValue: Float
    fun getShader(mGlSurfaceView: GLSurfaceView?): String {
        return """#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES sTexture;
float brightness ;
varying vec2 vTextureCoord;
void main() {
  brightness =$brightnessValue;
  vec4 color = texture2D(sTexture, vTextureCoord);
  gl_FragColor = brightness * color;
}
"""
    }

    /**
     * Initialize Effect
     *
     * @param brightnessvalue Range should be between 0.1- 2.0 with 1.0 being normal.
     */
    init {
        var brightnessvalue = brightnessvalue
        if (brightnessvalue < 0.1f) brightnessvalue = 0.1f
        if (brightnessvalue > 2.0f) brightnessvalue = 2.0f
        brightnessValue = brightnessvalue
    }
}