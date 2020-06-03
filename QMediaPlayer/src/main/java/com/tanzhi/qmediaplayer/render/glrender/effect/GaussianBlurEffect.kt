package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView.ShaderInterface
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * 高斯模糊
 * Created by guoshuyu on 2017/9/17.
 */
class GaussianBlurEffect : ShaderInterface {
    private var radius = 1.0f
    private var blurTypeString = "vec2(1.0,0.0)"

    /**
     * Initialize Effect
     */
    constructor(radius: Float) {
        this.radius = radius
    }

    constructor(radius: Float, blurType: Int) {
        this.radius = radius
        when (blurType) {
            TYPEX -> blurTypeString = "vec2(1.0,0.0)"
            TYPEY -> blurTypeString = "vec2(0.0,1.0)"
            TYPEXY -> blurTypeString = "vec2(1.0,1.0)"
        }
    }

    fun getShader(mGlSurfaceView: GLSurfaceView?): String {
        return """#extension GL_OES_EGL_image_external : require
precision mediump float;
varying vec2 vTextureCoord;
uniform samplerExternalOES sTexture;
const float resolution=1024.0;
const float radius = $radius;
vec2 dir =$blurTypeString; //若为x模糊，可传入（1.0,0.0）  y模糊  （0.0,1.0）

void main() {
    //this will be our RGBA sum
    vec4 sum = vec4(0.0);
    
    //our original texcoord for this fragment
    vec2 tc = vTextureCoord;
    
    //the amount to blur, i.e. how far off center to sample from
    //1.0 -> blur by one pixel
    //2.0 -> blur by two pixels, etc.
    float blur = radius/resolution;
    
    //the direction of our blur
    //(1.0, 0.0) -> x-axis blur
    //(0.0, 1.0) -> y-axis blur
    float hstep = dir.x;
    float vstep = dir.y;
    
    
    //apply blurring, using a 9-tap filter with predefined gaussian weights
    
    sum += texture2D(sTexture, vec2(tc.x - 4.0*blur*hstep, tc.y - 4.0*blur*vstep)) * 0.0162162162;
    sum += texture2D(sTexture, vec2(tc.x - 3.0*blur*hstep, tc.y - 3.0*blur*vstep)) * 0.0540540541;
    sum += texture2D(sTexture, vec2(tc.x - 2.0*blur*hstep, tc.y - 2.0*blur*vstep)) * 0.1216216216;
    sum += texture2D(sTexture, vec2(tc.x - 1.0*blur*hstep, tc.y - 1.0*blur*vstep)) * 0.1945945946;
    
    sum += texture2D(sTexture, vec2(tc.x, tc.y)) * 0.2270270270;
    
    sum += texture2D(sTexture, vec2(tc.x + 1.0*blur*hstep, tc.y + 1.0*blur*vstep)) * 0.1945945946;
    sum += texture2D(sTexture, vec2(tc.x + 2.0*blur*hstep, tc.y + 2.0*blur*vstep)) * 0.1216216216;
    sum += texture2D(sTexture, vec2(tc.x + 3.0*blur*hstep, tc.y + 3.0*blur*vstep)) * 0.0540540541;
    sum += texture2D(sTexture, vec2(tc.x + 4.0*blur*hstep, tc.y + 4.0*blur*vstep)) * 0.0162162162;

    vec4 cc= texture2D(sTexture,vTextureCoord );

    //discard alpha for our simple demo, multiply by vertex color and return
    gl_FragColor =vec4(sum.rgb, cc.a);
}"""
    }

    companion object {
        const val TYPEX = 1
        const val TYPEY = 2
        const val TYPEXY = 3
    }
}