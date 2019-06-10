package com.qfq.tainzhi.videoplayer.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.qfq.tainzhi.videoplayer.R;

/**
 * Created by muqing on 2019/6/4.
 * Email: qfq61@qq.com
 */
public class TVFragment extends BaseFragment {
    public static final String TAG = "VideoPlayer/FragmentLocalVideo";
    private static TVFragment mInstance = null;
    private View mView;
    
    public static TVFragment getInstance() {
        if (mInstance == null) {
            mInstance = new TVFragment();
        }
        return mInstance;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_local_video, container, false);
        return mView;
    }
    
    @Override
    public void onDoubleClick() {
        Log.i(TAG, "onDoubleClick: ");
    }
}
