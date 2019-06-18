package com.qfq.tainzhi.videoplayer.mvp.model;

import android.content.Context;
import android.renderscript.ScriptGroup;

import com.orhanobut.logger.Logger;
import com.qfq.tainzhi.videoplayer.R;
import com.qfq.tainzhi.videoplayer.adapters.LocalVideoAdapter;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableSerialized;

/**
 * Created by muqing on 2019/6/18.
 * Email: qfq61@qq.com
 */
public class TVModel {
    
    private Context mContext;
    
    public TVModel(Context context) {
        this.mContext = context;
    }
    
    public Observable<List<String>> getChannels() {
        return Observable.just(getDefaultChannels());
    }

    public List<String> getDefaultChannels() {
        List<String> result = new ArrayList<>();
        try {
            InputStream instream =
                    mContext.getResources().openRawResource(R.raw.default_tv_channels);
            if (instream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(instream);
                BufferedReader buffer = new BufferedReader(inputStreamReader);
    
                String line;
                //            3*file*http://116.199.5.51:8114/index.m3u8?Fsv_chan_hls_se_idx=10&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8
                //            3*title*CCTV  2   财经Vk
                Pattern channelUrlPattern = Pattern.compile("file.(http.*m3u8)");
                Pattern channelPattern = Pattern.compile("title.(.*)[\n\f\r]{0,2}");
                boolean pair = false;
                while ((line = buffer.readLine()) != null) {
                    Matcher channelUrlMatcher = channelUrlPattern.matcher(line);
                    if (channelUrlMatcher.find()) {
                        String url = channelUrlMatcher.group(1);
                        result.add(url);
                        pair = true;
                        continue;
                    }
                    Matcher channelMatcher = channelPattern.matcher(line);
                    if (channelMatcher.find() && pair) {
                        pair = false;
                        String name = channelMatcher.group(1);
                        result.add(name);
                    }
                }
            }
            instream.close();
        } catch(java.io.FileNotFoundException e) {
            Logger.d("default tv channel file not found!");
        }
        catch (IOException e) {
        }
        return result;
    }
}
