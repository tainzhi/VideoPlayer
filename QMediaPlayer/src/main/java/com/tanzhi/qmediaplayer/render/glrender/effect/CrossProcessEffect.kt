package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView.ShaderInterface
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * Applies a cross process effect on video, in which the red and green channels
 * are enhanced while the blue channel is restricted.
 *
 * @author sheraz.khilji
 */
class CrossProcessEffect
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
  vec3 ncolor = vec3(0.0, 0.0, 0.0);
  float value;
  if (color.r < 0.5) {
    value = color.r;
  } else {
    value = 1.0 - color.r;
  }
  float red = 4.0 * value * value * value;
  if (color.r < 0.5) {
    ncolor.r = red;
  } else {
    ncolor.r = 1.0 - red;
  }
  if (color.g < 0.5) {
    value = color.g;
  } else {
    value = 1.0 - color.g;
  }
  float green = 2.0 * value * value;
  if (color.g < 0.5) {
    ncolor.g = green;
  } else {
    ncolor.g = 1.0 - green;
  }
  ncolor.b = color.b * 0.5 + 0.25;
  gl_FragColor = vec4(ncolor.rgb, color.a);
}
"""
    }
}