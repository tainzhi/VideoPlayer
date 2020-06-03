package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView.ShaderInterface
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * Applies Posterization effect to video.
 *
 * @author sheraz.khilji
 */
class PosterizeEffect
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
  vec3 pcolor;
  pcolor.r = (color.r >= 0.5) ? 0.75 : 0.25;
  pcolor.g = (color.g >= 0.5) ? 0.75 : 0.25;
  pcolor.b = (color.b >= 0.5) ? 0.75 : 0.25;
  gl_FragColor = vec4(pcolor, color.a);
}
"""
    }
}