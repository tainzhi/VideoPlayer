package com.qfq.tainzhi.videoplayer.widget.media;

import android.view.View;
import android.widget.MediaController;

/**
 * Created by muqing on 2019/6/1.
 * Email: qfq61@qq.com
 */
public interface IMediaController {
    void hide();
    
    boolean isShowing();
    
    void setAnchorView(View view);
    
    void setEnabled(boolean enabled);
    
    void setMediaPlayer(MediaController.MediaPlayerControl player);
    
    void show(int timeout);
    
    void show();
    
    //----------
    // Extends
    //----------
    void showOnce(View view);
}
