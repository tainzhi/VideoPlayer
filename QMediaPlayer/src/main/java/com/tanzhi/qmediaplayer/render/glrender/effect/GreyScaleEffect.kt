package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView.ShaderInterface
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * Converts video to GreyScale.
 *
 * @author sheraz.khilji
 */
class GreyScaleEffect
/**
 * Initialize Effect
 */
    : ShaderInterface {
    fun getShader(mGlSurfaceView: GLSurfaceView?): String {
        return """#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES sTexture;
varying vec2 vTextureCoord;
void main() {
  vec4 color = texture2D(sTexture, vTextureCoord);
  float y = dot(color, vec4(0.299, 0.587, 0.114, 0));
  gl_FragColor = vec4(y, y, y, color.a);
}
"""
    }
}