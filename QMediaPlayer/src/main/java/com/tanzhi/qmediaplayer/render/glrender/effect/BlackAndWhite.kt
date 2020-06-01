package com.tanzhi.qmediaplayer.render.glrender.effect

import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/1 14:58
 * @description:
 **/

class BlackAndWhite {
}

/**
 * 黑白滤镜，
 * Converts the video into black and white colors
 *
 * @author sheraz.khilji
 */
class BlackAndWhiteEffect : ShaderInterface {
    override var shader = """
        #extension GL_OES_EGL_image_external : require
        precision mediump float;
        varying vec2 vTextureCoord;
        uniform samplerExternalOES sTexture;
        void main() {
            vec4 color = texture2D(sTexture, vTextureCoord);
            float colorR = (color.r + color.g + color.b) / 3.0;
            float colorG = (color.r + color.g + color.b) / 3.0;
            float colorB = (color.r + color.g + color.b) / 3.0;
            gl_FragColor = vec4(colorR, colorG, colorB, color.a);
        }
    """
}
