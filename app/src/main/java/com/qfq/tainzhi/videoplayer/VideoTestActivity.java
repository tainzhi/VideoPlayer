package com.qfq.tainzhi.videoplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.orhanobut.logger.Logger;
import com.qfq.tainzhi.videoplayer.my_media.VideoView;

public class VideoTestActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_test);
		
		 VideoView videoView = findViewById(R.id.video_view);
		
		
		Intent intent = getIntent();
		Uri mVideoUri = intent.getData();
		Bundle bundle = intent.getExtras();
		String mVideoTitle = bundle.getString(VIDEO_NAME);
		long mVideoDuration = bundle.getLong(VIDEO_DURATION, 0);
		int mVideoProgress = bundle.getInt(VIDEO_PROGRESS, 0);
		
		videoView.setVideoUri(mVideoUri);
	}
	
	
	private static String VIDEO_RUL = "url";
	private static String VIDEO_NAME = "name";
	private static String VIDEO_DURATION = "duration";
	private static String VIDEO_PROGRESS = "progress";
	
	public static void startPlay(Context starter, Uri uri, String name, long duration, long progress) {
		Intent intent = new Intent(starter, VideoTestActivity.class);
		intent.setData(uri);
		Logger.d(uri.toString());
		intent.putExtra(VIDEO_NAME, name);
		intent.putExtra(VIDEO_DURATION, duration);
		intent.putExtra(VIDEO_PROGRESS, progress);
		starter.startActivity(intent);
	}
}
