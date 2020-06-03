package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView.ShaderInterface
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * Apply Hue effect on the video being played
 */
class HueEffect(hueDegrees: Float) : ShaderInterface {
    var hueValue: Float
    fun getShader(mGlSurfaceView: GLSurfaceView?): String {
        return """
            #extension GL_OES_EGL_image_external : require
            precision mediump float;
            varying vec2 vTextureCoord;
            uniform samplerExternalOES sTexture;
            float hue=$hueValue;
            void main() {
            vec4 kRGBToYPrime = vec4 (0.299, 0.587, 0.114, 0.0);
            vec4 kRGBToI = vec4 (0.595716, -0.274453, -0.321263, 0.0);
            vec4 kRGBToQ = vec4 (0.211456, -0.522591, 0.31135, 0.0);
            vec4 kYIQToR = vec4 (1.0, 0.9563, 0.6210, 0.0);
            vec4 kYIQToG = vec4 (1.0, -0.2721, -0.6474, 0.0);
            vec4 kYIQToB = vec4 (1.0, -1.1070, 1.7046, 0.0);
            vec4 color = texture2D(sTexture, vTextureCoord);
            float YPrime = dot(color, kRGBToYPrime);
            float I = dot(color, kRGBToI);
            float Q = dot(color, kRGBToQ);
            float chroma = sqrt (I * I + Q * Q);
            Q = chroma * sin (hue);
            I = chroma * cos (hue);
            vec4 yIQ = vec4 (YPrime, I, Q, 0.0);
            color.r = dot (yIQ, kYIQToR);
            color.g = dot (yIQ, kYIQToG);
            color.b = dot (yIQ, kYIQToB);
            gl_FragColor = color;
            }
            
            """.trimIndent()
    }

    /**
     * Initialize Effect
     *
     *
     * <img alt="Hue value chart" width="400" height="350" src="https://cloud.githubusercontent.com/assets/2201511/21810115/b99ac22a-d74a-11e6-9f6c-ef74d15c88c7.jpg"></img>
     *
     * @param hueDegrees Range of value should be between 0 to 360 degrees as described in the image above
     */
    init {
//      manipulating input value so that we can map it on 360 degree circle
        hueValue = ((hueDegrees - 45) / 45f + 0.5f) * -1
    }
}