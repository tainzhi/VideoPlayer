package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView.ShaderInterface
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * Apply Gamma Effect on Video being played
 */
class GammaEffect(gammaValue: Float) : ShaderInterface {
    private val gammaValue: Float
    fun getShader(mGlSurfaceView: GLSurfaceView?): String {
        return """
            #extension GL_OES_EGL_image_external : require
            precision mediump float;
            varying vec2 vTextureCoord;
            uniform samplerExternalOES sTexture;
            float gamma=$gammaValue;
            void main() {
            vec4 textureColor = texture2D(sTexture, vTextureCoord);
            gl_FragColor = vec4(pow(textureColor.rgb, vec3(gamma)), textureColor.w);
            }
            
            """.trimIndent()
    }

    /**
     * Initialize Effect
     *
     * @param gammaValue Range should be between 0.0 - 2.0 with 1.0 being normal.
     */
    init {
        var gammaValue = gammaValue
        if (gammaValue < 0.0f) gammaValue = 0.0f
        if (gammaValue > 2.0f) gammaValue = 2.0f
        this.gammaValue = gammaValue
    }
}