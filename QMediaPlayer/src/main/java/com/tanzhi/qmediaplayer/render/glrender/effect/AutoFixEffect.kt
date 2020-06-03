package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.opengl.GLSurfaceView
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * Attempts to auto-fix the video based on histogram equalization.
 *
 * @author sheraz.khilji
 */
class AutoFixEffect(scale: Float) : ShaderInterface {
    private val scale: Float
    fun getShader(mGlSurfaceView: GLSurfaceView?): String {
        return """#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES tex_sampler_0;
uniform samplerExternalOES tex_sampler_1;
uniform samplerExternalOES tex_sampler_2;
 float scale;
 float shift_scale;
 float hist_offset;
 float hist_scale;
 float density_offset;
 float density_scale;
varying vec2 vTextureCoord;
void main() {
  shift_scale = ${1.0f / 256f};
  hist_offset = ${0.5f / 766f};
  hist_scale = ${765f / 766f};
  density_offset = ${0.5f / 1024f};
  density_scale = ${1023f / 1024f};
  scale = $scale;
  const vec3 weights = vec3(0.33333, 0.33333, 0.33333);
  vec4 color = texture2D(tex_sampler_0, vTextureCoord);
  float energy = dot(color.rgb, weights);
  float mask_value = energy - 0.5;
  float alpha;
  if (mask_value > 0.0) {
    alpha = (pow(2.0 * mask_value, 1.5) - 1.0) * scale + 1.0;
  } else {
    alpha = (pow(2.0 * mask_value, 2.0) - 1.0) * scale + 1.0;
  }
  float index = energy * hist_scale + hist_offset;
  vec4 temp = texture2D(tex_sampler_1, vec2(index, 0.5));
  float value = temp.g + temp.r * shift_scale;
  index = value * density_scale + density_offset;
  temp = texture2D(tex_sampler_2, vec2(index, 0.5));
  value = temp.g + temp.r * shift_scale;
  float dst_energy = energy * alpha + value * (1.0 - alpha);
  float max_energy = energy / max(color.r, max(color.g, color.b));
  if (dst_energy > max_energy) {
    dst_energy = max_energy;
  }
  if (energy == 0.0) {
    gl_FragColor = color;
  } else {
    gl_FragColor = vec4(color.rgb * dst_energy / energy, color.a);
  }
}
"""
    }

    /**
     * Initialize Effect
     *
     * @param scale Float, between 0 and 1. Zero means no adjustment, while 1
     * indicates the maximum amount of adjustment.
     */
    init {
        var scale = scale
        if (scale < 0.0f) scale = 0.0f
        if (scale > 1.0f) scale = 1.0f
        this.scale = scale
    }
}