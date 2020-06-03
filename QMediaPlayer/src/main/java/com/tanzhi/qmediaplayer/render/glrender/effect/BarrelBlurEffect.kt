package com.tanzhi.qmediaplayer.render.glrender.effect

import GSYVideoGLView.ShaderInterface
import android.opengl.GLSurfaceView
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface

/**
 * Barrel类型模糊
 * Created by guoshuyu on 2017/9/16.
 */
class BarrelBlurEffect : ShaderInterface {
    private var countLevel = 5

    constructor() {}
    constructor(countLevel: Int) {
        this.countLevel = countLevel
    }

    fun setCountLevel(countLevel: Int) {
        this.countLevel = countLevel
    }

    fun getShader(mGlSurfaceView: GLSurfaceView?): String {
        return """#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES sTexture;

varying vec2 vTextureCoord;
const float barrelPower = 0.5;
const int num_iter = $countLevel;
const float reci_num_iter_f = 1.0 / float(num_iter);

vec2 barrelDistortion(vec2 coord, float amt)
{
    vec2 cc = coord - 0.5;
    float dist = dot(cc, cc);
    return coord + cc * dist * amt;
}

float sat( float t )
{
    return clamp( t, 0.0, 1.0 );
}

float linterp( float t ) {
    return sat( 1.0 - abs( 2.0*t - 1.0 ) );
}

float remap( float t, float a, float b )
{
    return sat( (t - a) / (b - a) );
}

vec3 spectrum_offset( float t )
{
    vec3 ret;
    float lo = step(t,0.5);
    float hi = 1.0-lo;
    float w = linterp( remap( t, 1.0/6.0, 5.0/6.0 ) );
    ret = vec3(lo,1.0,hi) * vec3(1.0-w, w, 1.0-w);
  
    return pow( ret, vec3(1.0/2.2) );
}

void main()
{
    vec2 uv=(gl_FragCoord.xy/vTextureCoord.xy);
  
    vec3 sumcol = vec3(0.0);
    vec3 sumw = vec3(0.0);
    for ( int i=0; i<num_iter;++i )
    {
        float t = float(i) * reci_num_iter_f;
        vec3 w = spectrum_offset( t );
        sumw += w;
        sumcol += w * texture2D( sTexture, barrelDistortion(vTextureCoord, barrelPower*t ) ).rgb;
    }
    gl_FragColor = vec4(sumcol.rgb / sumw, 1.0);
}  """
    }
}