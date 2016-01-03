package com.qfq.muqing.myvideoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;


public class PlayVideoActivity extends Activity {
    Button back;
    final Context context=this;
    VideoView v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_layout);
        v=(VideoView)findViewById(R.id.video);
        Intent i=getIntent();
        final String videoPath =i.getStringExtra("videoPath");

        Log.i("videoPath of video file......", videoPath);
        v.setVideoPath(videoPath);

        v.setMediaController(new MediaController(context));
        v.requestFocus();

        v.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer arg0) {
                v.start();
            }
        });

        v.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });
    }
}

