package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView.ShaderInterface
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * Converts video to Sepia tone.
 *
 * @author sheraz.khilji
 */
class SepiaEffect
/**
 * Initialize Effect
 */
    : ShaderInterface {
    fun getShader(mGlSurfaceView: GLSurfaceView?): String {
        val weights = floatArrayOf(805.0f / 2048.0f, 715.0f / 2048.0f,
                557.0f / 2048.0f, 1575.0f / 2048.0f, 1405.0f / 2048.0f,
                1097.0f / 2048.0f, 387.0f / 2048.0f, 344.0f / 2048.0f,
                268.0f / 2048.0f)
        val matrixString = arrayOfNulls<String>(9)
        matrixString[0] = """  matrix[0][0]=${weights[0]};
"""
        matrixString[1] = """  matrix[0][1]=${weights[1]};
"""
        matrixString[2] = """  matrix[0][2]=${weights[2]};
"""
        matrixString[3] = """  matrix[1][0]=${weights[3]};
"""
        matrixString[4] = """  matrix[1][1]=${weights[4]};
"""
        matrixString[5] = """  matrix[1][2]=${weights[5]};
"""
        matrixString[6] = """  matrix[2][0]=${weights[6]};
"""
        matrixString[7] = """  matrix[2][1]=${weights[7]};
"""
        matrixString[8] = """  matrix[2][2]=${weights[8]};
"""
        return """#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES sTexture;
 mat3 matrix;
varying vec2 vTextureCoord;
void main() {
${matrixString[0]}${matrixString[1]}${matrixString[2]}${matrixString[3]}${matrixString[4]}${matrixString[5]}${matrixString[6]}${matrixString[7]}${matrixString[8]}  vec4 color = texture2D(sTexture, vTextureCoord);
  vec3 new_color = min(matrix * color.rgb, 1.0);
  gl_FragColor = vec4(new_color.rgb, color.a);
}
"""
    }
}