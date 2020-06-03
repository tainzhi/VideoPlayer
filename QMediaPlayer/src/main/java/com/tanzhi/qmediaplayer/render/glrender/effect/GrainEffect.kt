package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView.ShaderInterface
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface
import java.util.*

/**
 * Applies film grain effect to video.
 *
 * @author sheraz.khilji
 */
class GrainEffect(strength: Float) : ShaderInterface {
    private var mWidth = 0
    private var mHeight = 0
    private val strength: Float
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
        val seed = floatArrayOf(mRandom!!.nextFloat(), mRandom!!.nextFloat())
        val scaleString = "scale = $strength;\n"
        val seedString = arrayOfNulls<String>(2)
        seedString[0] = """
            seed[0] = ${seed[0]};
            
            """.trimIndent()
        seedString[1] = """
            seed[1] = ${seed[1]};
            
            """.trimIndent()
        val stepX = """
            stepX = ${0.5f / mWidth};
            
            """.trimIndent()
        val stepY = """
            stepY = ${0.5f / mHeight};
            
            """.trimIndent()

        // locString[1] = "loc[1] = loc[1]+" + seedString[1] + ";\n";
        return """#extension GL_OES_EGL_image_external : require
precision mediump float;
 vec2 seed;
varying vec2 vTextureCoord;
uniform samplerExternalOES tex_sampler_0;
uniform samplerExternalOES tex_sampler_1;
float scale;
 float stepX;
 float stepY;
float rand(vec2 loc) {
  float theta1 = dot(loc, vec2(0.9898, 0.233));
  float theta2 = dot(loc, vec2(12.0, 78.0));
  float value = cos(theta1) * sin(theta2) + sin(theta1) * cos(theta2);
  float temp = mod(197.0 * value, 1.0) + value;
  float part1 = mod(220.0 * temp, 1.0) + temp;
  float part2 = value * 0.5453;
  float part3 = cos(theta1 + theta2) * 0.43758;
  float sum = (part1 + part2 + part3);
  return fract(sum)*scale;
}
void main() {
${seedString[0]}${seedString[1]}$scaleString$stepX$stepY  float noise = texture2D(tex_sampler_1, vTextureCoord + vec2(-stepX, -stepY)).r * 0.224;
  noise += texture2D(tex_sampler_1, vTextureCoord + vec2(-stepX, stepY)).r * 0.224;
  noise += texture2D(tex_sampler_1, vTextureCoord + vec2(stepX, -stepY)).r * 0.224;
  noise += texture2D(tex_sampler_1, vTextureCoord + vec2(stepX, stepY)).r * 0.224;
  noise += 0.4448;
  noise *= scale;
  vec4 color = texture2D(tex_sampler_0, vTextureCoord);
  float energy = 0.33333 * color.r + 0.33333 * color.g + 0.33333 * color.b;
  float mask = (1.0 - sqrt(energy));
  float weight = 1.0 - 1.333 * mask * noise;
  gl_FragColor = vec4(color.rgb * weight, color.a);
  gl_FragColor = gl_FragColor+vec4(rand(vTextureCoord + seed), rand(vTextureCoord + seed),rand(vTextureCoord + seed),1);
}
"""
    }

    /**
     * Initialize Effect
     *
     * @param strength Float, between 0 and 1. Zero means no distortion, while 1
     * indicates the maximum amount of adjustment.
     */
    init {
        var strength = strength
        if (strength < 0.0f) strength = 0.0f
        if (strength > 1.0f) strength = 1.0f
        this.strength = strength
    }
}