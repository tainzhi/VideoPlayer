package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * 简单模糊
 * Created by guoshuyu on 2017/9/17.
 */
class SampleBlurEffect : ShaderInterface {
    var blur = 1f

    constructor() : super() {}
    constructor(blur: Float) {
        this.blur = blur
    }

    fun getShader(mGlSurfaceView: GLSurfaceView?): String {
        return """#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES sTexture;
varying vec2 vTextureCoord;
const float blurSize = $blur/800.0;
const float weightSum = 70.0 + 2.0 * (1.0 + 8.0 + 28.0 + 56.0);

void main(void)
{
   vec4 sum = vec4(0.0);

   sum += texture2D(sTexture, vec2(vTextureCoord.x - 4.0*blurSize, vTextureCoord.y)) * 1.0 / weightSum;
   sum += texture2D(sTexture, vec2(vTextureCoord.x - 3.0*blurSize, vTextureCoord.y)) * 8.0 / weightSum;
   sum += texture2D(sTexture, vec2(vTextureCoord.x - 2.0*blurSize, vTextureCoord.y)) * 28.0 / weightSum;
   sum += texture2D(sTexture, vec2(vTextureCoord.x - blurSize, vTextureCoord.y)) * 56.0 / weightSum;
   sum += texture2D(sTexture, vec2(vTextureCoord.x, vTextureCoord.y)) * 70.0 / weightSum;
   sum += texture2D(sTexture, vec2(vTextureCoord.x + blurSize, vTextureCoord.y)) * 56.0 / weightSum;
   sum += texture2D(sTexture, vec2(vTextureCoord.x + 2.0*blurSize, vTextureCoord.y)) * 28.0 / weightSum;
   sum += texture2D(sTexture, vec2(vTextureCoord.x + 3.0*blurSize, vTextureCoord.y)) * 8.0 / weightSum;
   sum += texture2D(sTexture, vec2(vTextureCoord.x + 4.0*blurSize, vTextureCoord.y)) * 1.0 / weightSum;

   gl_FragColor = sum;
}"""
    }
}