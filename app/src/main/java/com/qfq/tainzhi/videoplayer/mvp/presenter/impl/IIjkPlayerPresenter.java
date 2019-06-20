package com.qfq.tainzhi.videoplayer.mvp.presenter.impl;

/**
 * Created by muqing on 2019/6/20.
 * Email: qfq61@qq.com
 */
public interface IIjkPlayerPresenter {
    void getSystemTime();
    
    void unRigister();
    
    void autoHidePanel();
    
    void getPlayerInfo(int roomId);
    
    void dismissVolAlpha();
}
