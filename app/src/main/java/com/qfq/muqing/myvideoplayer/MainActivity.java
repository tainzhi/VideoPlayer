package com.qfq.muqing.myvideoplayer;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;

import com.qfq.muqing.myvideoplayer.adapters.StaggeredAdapter;
import com.qfq.muqing.myvideoplayer.callbacks.OnStaggeredAdapterInformation;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "VideoPlayer/MainActivity";
    private Context mContext;
    private CharSequence mTitle;
    private RecyclerView mList;
    private ViewStub mHintViewStub;
    private StaggeredAdapter mAdapter;
    private int mItemMargin;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = getTitle();
        mContext = getApplicationContext();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME );

        mList = (RecyclerView)findViewById(R.id.selection_list);
        mList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mItemMargin = getResources().getDimensionPixelOffset(R.dimen.item_margin);
        mList.addItemDecoration(new InsetDecoration(mContext, mItemMargin));

        mList.getItemAnimator().setAddDuration(1000);
        mList.getItemAnimator().setChangeDuration(1000);
        mList.getItemAnimator().setMoveDuration(1000);
        mList.getItemAnimator().setRemoveDuration(1000);

        //set item width, Window.getWidth - marginLeft - marginRight - 2 * 2 * Insets
        mAdapter = new StaggeredAdapter(mContext, (getWindowWidth() - 6 * mItemMargin)/2, mOnStaggeredAdapterInformation);
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mAdapter.setOnItemLongClickListener(mOnItemLongClickListener);
        mList.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private int getWindowWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(mContext, "onItemClick: " + position + ", id: " + id, Toast.LENGTH_SHORT).show();
            Uri videoUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
            String videoTitle = mAdapter.getVideoItemAtPosition(position).videoName;
            long videoDuration = mAdapter.getVideoItemAtPosition(position).videoDuration;
            Intent intent = new Intent(MainActivity.this, SingleVideoPlayerActivity.class);
            intent.setData(videoUri);
            intent.putExtra("title", videoTitle);
            intent.putExtra("duration", videoDuration);
            startActivity(intent);
        }

    };

    private AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(mContext, "onItemLongClick: " + position + ", id: " + id, Toast.LENGTH_SHORT).show();
            String filePath = mAdapter.getVideoItemAtPosition(position).videoPath;
            Intent startIntent = new Intent(MainActivity.this, DoubleVideoPlayerActivity.class);
            startIntent.putExtra("file", filePath);
            startActivity(startIntent);
            return true;
        }
    };

    private OnStaggeredAdapterInformation mOnStaggeredAdapterInformation = new OnStaggeredAdapterInformation() {
        @Override
        public void onStaggeredAdapterInformation() {
            mList.setVisibility(View.GONE);
            if (mHintViewStub == null) {
                mHintViewStub = (ViewStub)findViewById(R.id.viewstub_novideo_hint_layout_id);
                mHintViewStub.inflate();
            }
        }
    };
}
