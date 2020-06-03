package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView.ShaderInterface
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * Inverts the video colors. This can also be known as negative Effect.
 *
 * @author sheraz.khilji
 */
class InvertColorsEffect
/**
 * Initialize Effect
 */
    : ShaderInterface {
    fun getShader(mGlSurfaceView: GLSurfaceView?): String {
        return """#extension GL_OES_EGL_image_external : require
precision mediump float;
varying vec2 vTextureCoord;
uniform samplerExternalOES sTexture;
void main() {
  vec4 color = texture2D(sTexture, vTextureCoord);
  float colorR = (1.0 - color.r) / 1.0;
  float colorG = (1.0 - color.g) / 1.0;
  float colorB = (1.0 - color.b) / 1.0;
  gl_FragColor = vec4(colorR, colorG, colorB, color.a);
}
"""
    }
}