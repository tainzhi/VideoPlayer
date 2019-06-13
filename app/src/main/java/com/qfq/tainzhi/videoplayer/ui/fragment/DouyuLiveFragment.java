package com.qfq.tainzhi.videoplayer.ui.fragment;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.orhanobut.logger.Logger;
import com.qfq.tainzhi.videoplayer.R;

import butterknife.ButterKnife;

/**
 * Created by muqing on 2019/6/13.
 * Email: qfq61@qq.com
 */
public class DouyuLiveFragment extends Fragment {
    private static final String CHANNEL_ID = "channel_id";
    private static final String CHANNEL_TITLE = "channel_title";
    private String mChannelTitle;
    private int mChannelId;
    
    private View mView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mChannelId = bundle.getInt(CHANNEL_ID, 0);
            mChannelTitle = bundle.getString(CHANNEL_TITLE);
        }
        Logger.d("%s", mChannelTitle);
    }
    public static DouyuLiveFragment newInstance(int id, String title) {
        Bundle bundle = new Bundle();
        bundle.putInt(CHANNEL_ID, id);
        bundle.putString(CHANNEL_TITLE, title);
        DouyuLiveFragment douyuLiveFragment = new DouyuLiveFragment();
        douyuLiveFragment.setArguments(bundle);
        return douyuLiveFragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                              @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_douyu_live, container,
                    false);
            ButterKnife.bind(this. mView);
        }
        // TODO: 2019/6/13
        return mView;
    }
}
