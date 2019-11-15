package com.qfq.tainzhi.videoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import com.qfq.tainzhi.videoplayer.my_media.BaseVideoView;

public class VideoTestActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_test);
		
		 BaseVideoView videoView = findViewById(R.id.video_view);
		
		
		Intent intent = getIntent();
		Uri mVideoUri = intent.getData();
		Bundle bundle = intent.getExtras();
		String mVideoTitle = bundle.getString("title");
		long mVideoDuration = bundle.getLong("duration", 0);
		int mVideoProgress = bundle.getInt("progress", 0);
		
		// videoView.setUp(mVideoUri);
	}
}
