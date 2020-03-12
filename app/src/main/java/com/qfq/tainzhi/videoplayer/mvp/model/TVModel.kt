package com.qfq.tainzhi.videoplayer.mvp.model

import android.content.Context
import com.orhanobut.logger.Logger
import com.qfq.tainzhi.videoplayer.R
import io.reactivex.Observable
import java.io.*
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by muqing on 2019/6/18.
 * Email: qfq61@qq.com
 */
class TVModel constructor(private val mContext: Context?) {
    fun getChannels(): Observable<MutableList<String?>?>? {
        return Observable.just(getDefaultChannels())
    }
    
    fun getDefaultChannels(): MutableList<String?>? {
        val result: MutableList<String?>? = ArrayList()
        try {
            val inputStream: InputStream? = mContext.getResources().openRawResource(R.raw.default_tv_channels)
            val buffer: BufferedReader? = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            //            3*file*http://116.199.5.51:8114/index.m3u8?Fsv_chan_hls_se_idx=10&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8
            //            3*title*CCTV  2   财经Vk
            val channelUrlPattern: Pattern? = Pattern.compile("file.(http.*m3u8)")
            val channelPattern: Pattern? = Pattern.compile("title.(.*)[\n\f\r]{0,2}")
            var pair: Boolean = false
            while ((buffer.readLine().also({ line = it })) != null) {
                val channelUrlMatcher: Matcher? = channelUrlPattern.matcher(line)
                if (channelUrlMatcher.find()) {
                    val url: String? = channelUrlMatcher.group(1)
                    result.add(url)
                    pair = true
                    continue
                }
                val channelMatcher: Matcher? = channelPattern.matcher(line)
                if (channelMatcher.find() && pair) {
                    pair = false
                    val name: String? = channelMatcher.group(1)
                    result.add(name)
                }
            }
            inputStream.close()
        } catch (e: FileNotFoundException) {
            Logger.d("default tv channel file not found!")
        } catch (e: IOException) {
        }
        return result
    }
    
}