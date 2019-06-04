package com.qfq.tainzhi.videoplayer.mvp.view.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTabHost;

import com.qfq.tainzhi.videoplayer.R;
import com.qfq.tainzhi.videoplayer.R2;
import com.qfq.tainzhi.videoplayer.mvp.view.fragment.DouyuFragment;
import com.qfq.tainzhi.videoplayer.mvp.view.fragment.LikeFragment;
import com.qfq.tainzhi.videoplayer.mvp.view.fragment.LocalVideoFragment;
import com.qfq.tainzhi.videoplayer.mvp.view.fragment.TVFragment;
import com.qfq.tainzhi.videoplayer.mvp.view.fragment.USTVFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by muqing on 2019/6/4.
 * Email: qfq61@qq.com
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "VideoPlayer/MainActivity";
    @BindView(R2.id.tab_content)
    FrameLayout mTabContent;
    @BindView(R2.id.tab_host)
    FragmentTabHost mTabHost;
    @BindView(R2.id.real_tab_content)
    FrameLayout mRealTabContent;
    private final String mTabSpec[] = {"local", "douyu", "like", "tv", "ustv"};
    private final String mIndicator[] = {"本地视频", "斗鱼", "收藏", "电视", "美剧"};
    private final Class mFragmentClass[] = {
            LocalVideoFragment.class,
            DouyuFragment.class,
            LikeFragment.class,
            TVFragment.class,
            USTVFragment.class};
    
    // TODO: 2019/6/4 添加图片
    // private final int mTabImage[] = {
    //         R.drawable.tab_local_item,
    //                                         R.d
    // }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        
        if (mTabHost == null) {
            return;
        }
        mTabHost.setup(this, getSupportFragmentManager(), R.id.real_tab_content);
        for (int i = 0; i < mTabSpec.length; i++) {
            mTabHost.addTab(mTabHost.newTabSpec(mTabSpec[i]).setIndicator(getTabView(i)),
                    mFragmentClass[i], null);
        }
    }
    
    private View getTabView(int index) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.tab_item, null);
        // ImageView image = (ImageView)view.findViewById(R.id.tab_iamge);
        TextView text = (TextView)view.findViewById(R.id.tab_title);
        // image.setImageResource(mTabImage[index]);
        text.setText(mIndicator[index]);
        return view;
    }
}
