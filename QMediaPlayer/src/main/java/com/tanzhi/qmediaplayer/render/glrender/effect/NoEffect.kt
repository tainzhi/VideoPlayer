package com.tanzhi.qmediaplayer.render.glrender.effect

import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/30 10:15
 * @description:
 **/

class NoEffect : ShaderInterface {
    override var shader = """
        #extension GL_OES_EGL_image_external : require
        precision mediump float;
        uniform samplerExternalOES sTexture;
        void main() {
            gl_FragColor = texture2D(sTexture, vTextureCoord);
        }
    """
}