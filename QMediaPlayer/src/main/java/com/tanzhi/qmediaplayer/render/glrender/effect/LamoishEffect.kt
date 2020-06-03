package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView.ShaderInterface
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface
import java.util.*

/**
 * Applies lomo-camera style effect to video.
 *
 * @author sheraz.khilji
 */
class LamoishEffect
/**
 * Initialize Effect
 */
    : ShaderInterface {
    private var mWidth = 0
    private var mHeight = 0
    private var mRandom: Random? = null

    /**
     * Init all values that will be used by this shader.
     *
     * @param mGlSurfaceView which is responsible for displaying your video
     */
    private fun initValues(mGlSurfaceView: GLSurfaceView) {
        mWidth = mGlSurfaceView.width
        mHeight = mGlSurfaceView.height
        mRandom = Random(Date().time)
    }

    fun getShader(mGlSurfaceView: GLSurfaceView): String {
        initValues(mGlSurfaceView)
        val scale = FloatArray(2)
        if (mWidth > mHeight) {
            scale[0] = 1f
            scale[1] = mHeight.toFloat() / mWidth
        } else {
            scale[0] = mWidth.toFloat() / mHeight
            scale[1] = 1f
        }
        val max_dist = Math.sqrt(scale[0] * scale[0] + scale[1]
                * scale[1].toDouble()).toFloat() * 0.5f
        val seed = floatArrayOf(mRandom!!.nextFloat(), mRandom!!.nextFloat())
        val scaleString = arrayOfNulls<String>(2)
        val seedString = arrayOfNulls<String>(2)
        scaleString[0] = """
            scale[0] = ${scale[0]};
            
            """.trimIndent()
        scaleString[1] = """
            scale[1] = ${scale[1]};
            
            """.trimIndent()
        seedString[0] = """
            seed[0] = ${seed[0]};
            
            """.trimIndent()
        seedString[1] = """
            seed[1] = ${seed[1]};
            
            """.trimIndent()
        val inv_max_distString = """
            inv_max_dist = ${1.0f / max_dist};
            
            """.trimIndent()
        val stepsizeString = """
            stepsize = ${1.0f / 255.0f};
            
            """.trimIndent()
        val stepsizeXString = """
            stepsizeX = ${1.0f / mWidth};
            
            """.trimIndent()
        val stepsizeYString = """
            stepsizeY = ${1.0f / mHeight};
            
            """.trimIndent()
        return """#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES sTexture;
 vec2 seed;
 float stepsizeX;
 float stepsizeY;
 float stepsize;
 vec2 scale;
 float inv_max_dist;
varying vec2 vTextureCoord;
float rand(vec2 loc) {
  float theta1 = dot(loc, vec2(0.9898, 0.233));
  float theta2 = dot(loc, vec2(12.0, 78.0));
  float value = cos(theta1) * sin(theta2) + sin(theta1) * cos(theta2);
  float temp = mod(197.0 * value, 1.0) + value;
  float part1 = mod(220.0 * temp, 1.0) + temp;
  float part2 = value * 0.5453;
  float part3 = cos(theta1 + theta2) * 0.43758;
  return fract(part1 + part2 + part3);
}
void main() {
${scaleString[0]}${scaleString[1]}${seedString[0]}${seedString[1]}$inv_max_distString$stepsizeString$stepsizeXString$stepsizeYString  vec3 nbr_color = vec3(0.0, 0.0, 0.0);
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
  vec3 s_color = vec3(color.rgb + 0.3 * nbr_color);
  vec3 c_color = vec3(0.0, 0.0, 0.0);
  float value;
  if (s_color.r < 0.5) {
    value = s_color.r;
  } else {
    value = 1.0 - s_color.r;
  }
  float red = 4.0 * value * value * value;
  if (s_color.r < 0.5) {
    c_color.r = red;
  } else {
    c_color.r = 1.0 - red;
  }
  if (s_color.g < 0.5) {
    value = s_color.g;
  } else {
    value = 1.0 - s_color.g;
  }
  float green = 2.0 * value * value;
  if (s_color.g < 0.5) {
    c_color.g = green;
  } else {
    c_color.g = 1.0 - green;
  }
  c_color.b = s_color.b * 0.5 + 0.25;
  float dither = rand(vTextureCoord + seed);
  vec3 xform = clamp((c_color.rgb - 0.15) * 1.53846, 0.0, 1.0);
  vec3 temp = clamp((color.rgb + stepsize - 0.15) * 1.53846, 0.0, 1.0);
  vec3 bw_color = clamp(xform + (temp - xform) * (dither - 0.5), 0.0, 1.0);
  coord = vTextureCoord - vec2(0.5, 0.5);
  float dist = length(coord * scale);
  float lumen = 0.85 / (1.0 + exp((dist * inv_max_dist - 0.73) * 20.0)) + 0.15;
  gl_FragColor = vec4(bw_color * lumen, color.a);
}
"""
    }
}