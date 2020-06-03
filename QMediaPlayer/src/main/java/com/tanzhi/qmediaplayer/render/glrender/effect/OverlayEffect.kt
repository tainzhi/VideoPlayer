package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * 重叠
 * Created by guoshuyu on 2017/9/17.
 */
class OverlayEffect : ShaderInterface {
    var fstep = 0.0015f

    constructor() : super() {}
    constructor(fstep: Float) {
        this.fstep = fstep
    }

    fun getShader(mGlSurfaceView: GLSurfaceView?): String {
        return """#extension GL_OES_EGL_image_external : require
precision mediump float;
             uniform samplerExternalOES sTexture;
             varying vec2 vTextureCoord;
  
             void main()
             {
              vec4 sample0,sample1,sample2,sample3;
              float fstep=$fstep;
              sample0=texture2D(sTexture,vec2(vTextureCoord.x-fstep,vTextureCoord.y-fstep));
              sample1=texture2D(sTexture,vec2(vTextureCoord.x+fstep,vTextureCoord.y-fstep));
              sample2=texture2D(sTexture,vec2(vTextureCoord.x+fstep,vTextureCoord.y+fstep));
              sample3=texture2D(sTexture,vec2(vTextureCoord.x-fstep,vTextureCoord.y+fstep));
              vec4 color=(sample0+sample1+sample2+sample3) / 4.0;
              gl_FragColor=color;
             } """
    }
}