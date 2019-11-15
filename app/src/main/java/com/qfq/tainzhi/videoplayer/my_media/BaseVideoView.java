package com.qfq.tainzhi.videoplayer.my_media;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qfq.tainzhi.videoplayer.my_media.DataSource;
import com.qfq.tainzhi.videoplayer.my_media.IRenderView;
import com.qfq.tainzhi.videoplayer.my_media.VideoPlayerBase;
import com.qfq.tainzhi.videoplayer.my_media.VideoPlayerSystem;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.TextureMediaPlayer;

/**
 * Created by muqing on 2019/6/1.
 * Email: qfq61@qq.com
 * Description: 基本 VideoView, 没有各种操作控件 Controller
 */
public class BaseVideoView extends FrameLayout {
	private String TAG = "BaseVideoView";
	private Uri mUri;
	private Map<String, String> mHeaders;
	
	public static BaseVideoView CURRENT_VIDEO_VIEW;
	public Class mediaPlayerClass;
	public VideoPlayerBase mediaPlayer;
	
	public DataSource mDataSource;
	
	// all possible internal states
	public static final int STATE_ERROR = -1;
	public static final int STATE_IDLE = 0;
	public static final int STATE_PREPARING = 1;
	public static final int STATE_PREPARED = 2;
	public static final int STATE_PLAYING = 3;
	public static final int STATE_PAUSED = 4;
	public static final int STATE_PLAYBACK_COMPLETED = 5;
	
	// mCurrentState is a VideoView object's current state.
	// mTargetState is the state that a method caller intends to reach.
	// For instance, regardless the VideoView object's current state,
	// calling pause() intends to bring the object to a target state
	// of STATE_PAUSED.
	private int mCurrentState = STATE_IDLE;
	private int mTargetState = STATE_IDLE;
	
	// All the stuff we need for playing and showing a video
	private IRenderView.ISurfaceHolder mSurfaceHolder = null;
	private VideoPlayerBase mMediaPlayer = null;
	// private int         mAudioSession;
	private int mVideoWidth;
	private int mVideoHeight;
	private int mSurfaceWidth;
	private int mSurfaceHeight;
	private int mVideoRotationDegree;
	private int mAspectRatio; //视频缩放比例
	private int mCurrentBufferPercentage;
	private long mSeekWhenPrepared;  // recording the seek position while preparing
	private boolean mCanPause = true;
	private boolean mCanSeekBack;
	private boolean mCanSeekForward;
	
	/** Subtitle rendering widget overlaid on top of the video. */
	// private RenderingWidget mSubtitleWidget;
	
	/**
	 * Listener for changes to subtitle data, used to redraw when needed.
	 */
	// private RenderingWidget.OnChangedListener mSubtitlesChangedListener;
	
	private Context mAppContext;
	private IRenderView mRenderView;
	private int mVideoSarNum;
	private int mVideoSarDen;
	private boolean usingAndroidPlayer=false;
	private boolean usingMediaCodec=false;
	private boolean usingMediaCodecAutoRotate=false;
	private boolean usingOpenSLES=false;
	private String pixelFormat="";//Auto Select=,RGB 565=fcc-rv16,RGB 888X=fcc-rv32,YV12=fcc-yv12,默认为RGB 888X
	private boolean enableBackgroundPlay=false;
	private boolean enableSurfaceView=true;
	private boolean enableTextureView=false;
	private boolean enableNoView=false;
	
	public BaseVideoView(@NonNull Context context) {
		super(context);
		initView(context);
	}
	
	public BaseVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	public BaseVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}
	
	public BaseVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView(context);
	}
	
	// REMOVED: onMeasure
	// REMOVED: onInitializeAccessibilityEvent
	// REMOVED: onInitializeAccessibilityNodeInfo
	// REMOVED: resolveAdjustedSize
	
	private void initView(Context context) {
		mAppContext = context.getApplicationContext();
		
		initBackground();
		initRenders();
		
		mVideoWidth = 0;
		mVideoHeight = 0;
		// REMOVED: getHolder().addCallback(mSHCallback);
		// REMOVED: getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		setFocusable(true);
		setFocusableInTouchMode(true);
		requestFocus();
		// REMOVED: mPendingSubtitleTracks = new Vector<Pair<InputStream, MediaFormat>>();
		mCurrentState = STATE_IDLE;
		mTargetState = STATE_IDLE;
		initPlayer();
	}
	
	private void initPlayer() {
		this.mAspectRatio = IRenderView.AR_ASPECT_FILL_PARENT;
		this.mediaPlayerClass = VideoPlayerSystem.class;
		onStateIdle();
	}
	
	public void setUp(String url, String title) {
		// 默认使用android 系统自带 MediaPlayer 播放视频, 并填满屏幕
		setUp(new DataSource(url, title), IRenderView.AR_ASPECT_FILL_PARENT,
				VideoPlayerSystem.class);
	}
	
	public void setUp(Uri uri) {
		setUp(new DataSource(uri), IRenderView.AR_ASPECT_FIT_PARENT,
				VideoPlayerSystem.class);
	}
	
	public void setUp(DataSource dataSource, int aspectRatio, Class mediaPlayerClass) {
		this.mDataSource = dataSource;
		this.mAspectRatio = IRenderView.AR_ASPECT_FILL_PARENT;
		this.mediaPlayerClass = mediaPlayerClass;
		onStateIdle();
		// openVideo();
	}
	
	public void onStateIdle() {
		mCurrentState = STATE_IDLE;
		if (mediaPlayer != null) mediaPlayer.release();
	}
	
	public void onStatePrepare() {
		mCurrentState = STATE_PREPARED;
	}
	
	public void onPrepared() {}
	
	public void onAutoCompletion() {
	
	}
	
	public void setBufferProgress(int percent) {}
	
	public void onSeekComplete() {};
	
	public void onError(int what, int extra) {};
	
	public void onInfo(int what, int extra) {};
	
	public void onVideoSizeChanged(int width, int height) {};
	
	public void setRenderView(IRenderView renderView) {
		if (mRenderView != null) {
			// if (mMediaPlayer != null)
			// 	mMediaPlayer.setDisplay(null);

			View renderUIView = mRenderView.getView();
			mRenderView.removeRenderCallback(mSHCallback);
			mRenderView = null;
			removeView(renderUIView);
		}

		if (renderView == null)
			return;

		mRenderView = renderView;
		renderView.setAspectRatio(mCurrentAspectRatio);
		if (mVideoWidth > 0 && mVideoHeight > 0)
			renderView.setVideoSize(mVideoWidth, mVideoHeight);
		if (mVideoSarNum > 0 && mVideoSarDen > 0)
			renderView.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);

		View renderUIView = mRenderView.getView();
		LayoutParams lp = new LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT,
				Gravity.CENTER);
		renderUIView.setLayoutParams(lp);
		addView(renderUIView);

		mRenderView.addRenderCallback(mSHCallback);
		mRenderView.setVideoRotation(mVideoRotationDegree);
	}

	
	public void setRender(int render) {
		switch (render) {
			case RENDER_NONE:
				setRenderView(null);
				break;
			case RENDER_TEXTURE_VIEW: {
				// TextureRenderView renderView = new TextureRenderView(getContext());
				// if (mMediaPlayer != null) {
				// 	renderView.getSurfaceHolder().bindToMediaPlayer(mMediaPlayer);
				// 	renderView.setVideoSize(mMediaPlayer.getVideoWidth(), mMediaPlayer.getVideoHeight());
				// 	renderView.setVideoSampleAspectRatio(mMediaPlayer.getVideoSarNum(), mMediaPlayer.getVideoSarDen());
				// 	renderView.setAspectRatio(mCurrentAspectRatio);
				// } setRenderView(renderView);
				break;
			}
			case RENDER_SURFACE_VIEW: {
				SurfaceRenderView renderView = new SurfaceRenderView(getContext());
				setRenderView(renderView);
				break;
			}
			default:
				Log.e(TAG, String.format(Locale.getDefault(), "invalid render %d\n", render));
				break;
		}
	}
	
	/**
	 * Sets video path.
	 *
	 * @param path the path of the video.
	 */
	public void setVideoPath(String path) {
		setVideoURI(Uri.parse(path));
	}
	
	/**
	 * Sets video URI.
	 *
	 * @param uri the URI of the video.
	 */
	public void setVideoURI(Uri uri) {
		setVideoURI(uri, null);
	}
	
	/**
	 * Sets video URI using specific headers.
	 *
	 * @param uri     the URI of the video.
	 * @param headers the headers for the URI request.
	 *                Note that the cross domain redirection is allowed by default, but that can be
	 *                changed with key/value pairs through the headers parameter with
	 *                "android-allow-cross-domain-redirect" as the key and "0" or "1" as the value
	 *                to disallow or allow cross domain redirection.
	 */
	private void setVideoURI(Uri uri, Map<String, String> headers) {
		mUri = uri;
		mHeaders = headers;
		mSeekWhenPrepared = 0;
		openVideo();
		requestLayout();
		invalidate();
	}
	
	// REMOVED: addSubtitleSource
	// REMOVED: mPendingSubtitleTracks
	
	// public void stopPlayback() {
	// 	if (mMediaPlayer != null) {
	// 		mMediaPlayer.stop();
	// 		mMediaPlayer.release();
	// 		mMediaPlayer = null;
	// 		mCurrentState = STATE_IDLE;
	// 		mTargetState = STATE_IDLE;
	// 		AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
	// 		am.abandonAudioFocus(null);
	// 	}
	// }
	
	private void openVideo() {
		// we shouldn't clear the target state, because somebody might have
		// called start() previously
		try {
			Constructor<VideoPlayerBase> constructor =
					mediaPlayerClass.getConstructor(VideoPlayerBase.class);
			this.mediaPlayer =constructor.newInstance(this);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
		am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
	}
	
	
	// REMOVED: mSHCallback
	private void bindSurfaceHolder(VideoPlayerBase mp, IRenderView.ISurfaceHolder holder) {
		if (mp == null)
			return;
		
		if (holder == null) {
			// mp.setDisplay(null);
			return;
		}
		
		holder.bindToMediaPlayer(mp);
	}
	
	IRenderView.IRenderCallback mSHCallback = new IRenderView.IRenderCallback() {
		@Override
		public void onSurfaceChanged(@NonNull IRenderView.ISurfaceHolder holder, int format, int w, int h) {
			if (holder.getRenderView() != mRenderView) {
				Log.e(TAG, "onSurfaceChanged: unmatched render callback\n");
				return;
			}
			
			mSurfaceWidth = w;
			mSurfaceHeight = h;
			boolean isValidState = (mTargetState == STATE_PLAYING);
			boolean hasValidSize = !mRenderView.shouldWaitForResize() || (mVideoWidth == w && mVideoHeight == h);
			// if (mMediaPlayer != null && isValidState && hasValidSize) {
			// 	if (mSeekWhenPrepared != 0) {
			// 		seekTo((int) mSeekWhenPrepared);
			// 	}
			// 	start();
			// }
		}
		
		@Override
		public void onSurfaceCreated(@NonNull IRenderView.ISurfaceHolder holder, int width, int height) {
			if (holder.getRenderView() != mRenderView) {
				Log.e(TAG, "onSurfaceCreated: unmatched render callback\n");
				return;
			}
			
			mSurfaceHolder = holder;
			if (mediaPlayer != null)
				bindSurfaceHolder(mediaPlayer, holder);
			else
				openVideo();
		}
		
		@Override
		public void onSurfaceDestroyed(@NonNull IRenderView.ISurfaceHolder holder) {
			if (holder.getRenderView() != mRenderView) {
				Log.e(TAG, "onSurfaceDestroyed: unmatched render callback\n");
				return;
			}
			
			// after we return from this we can't use the surface any more
			mSurfaceHolder = null;
			// REMOVED: if (mMediaController != null) mMediaController.hide();
			// REMOVED: release(true);
			// releaseWithoutStop();
		}
	};
	
	// public void releaseWithoutStop() {
	// 	if (mMediaPlayer != null)
	// 		mMediaPlayer.setDisplay(null);
	// }
	
	/*
	 * release the media player in any state
	 */
	public void release(boolean cleartargetstate) {
		if (mMediaPlayer != null) {
			// mMediaPlayer.reset();
			mMediaPlayer.release();
			mMediaPlayer = null;
			// REMOVED: mPendingSubtitleTracks.clear();
			mCurrentState = STATE_IDLE;
			if (cleartargetstate) {
				mTargetState = STATE_IDLE;
			}
			AudioManager am = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
			am.abandonAudioFocus(null);
		}
	}
	
	// REMOVED: getAudioSessionId();
	// REMOVED: onAttachedToWindow();
	// REMOVED: onDetachedFromWindow();
	// REMOVED: onLayout();
	// REMOVED: draw();
	// REMOVED: measureAndLayoutSubtitleWidget();
	// REMOVED: setSubtitleWidget();
	// REMOVED: getSubtitleLooper();
	
	//-------------------------
	// Extend: Aspect Ratio
	//-------------------------
	
	private static final int[] s_allAspectRatio = {
			IRenderView.AR_ASPECT_FIT_PARENT,
			IRenderView.AR_ASPECT_FILL_PARENT,
			IRenderView.AR_ASPECT_WRAP_CONTENT,
			IRenderView.AR_MATCH_PARENT,
			IRenderView.AR_16_9_FIT_PARENT,
			IRenderView.AR_4_3_FIT_PARENT};
	private int mCurrentAspectRatioIndex = 0;
	private int mCurrentAspectRatio = s_allAspectRatio[mCurrentAspectRatioIndex];
	
	public int toggleAspectRatio() {
		mCurrentAspectRatioIndex++;
		mCurrentAspectRatioIndex %= s_allAspectRatio.length;
		
		mCurrentAspectRatio = s_allAspectRatio[mCurrentAspectRatioIndex];
		if (mRenderView != null)
			mRenderView.setAspectRatio(mCurrentAspectRatio);
		return mCurrentAspectRatio;
	}
	
	//-------------------------
	// Extend: Render
	//-------------------------
	public static final int RENDER_NONE = 0;
	public static final int RENDER_SURFACE_VIEW = 1;
	public static final int RENDER_TEXTURE_VIEW = 2;
	
	private List<Integer> mAllRenders = new ArrayList<Integer>();
	private int mCurrentRenderIndex = 0;
	private int mCurrentRender = RENDER_NONE;
	
	private void initRenders() {
		mAllRenders.clear();
		
		if (enableSurfaceView)
			mAllRenders.add(RENDER_SURFACE_VIEW);
		if (enableTextureView && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			mAllRenders.add(RENDER_TEXTURE_VIEW);
		if (enableNoView)
			mAllRenders.add(RENDER_NONE);
		
		if (mAllRenders.isEmpty())
			mAllRenders.add(RENDER_SURFACE_VIEW);
		mCurrentRender = mAllRenders.get(mCurrentRenderIndex);
		setRender(mCurrentRender);
	}
	
	public int toggleRender() {
		mCurrentRenderIndex++;
		mCurrentRenderIndex %= mAllRenders.size();
		
		mCurrentRender = mAllRenders.get(mCurrentRenderIndex);
		setRender(mCurrentRender);
		return mCurrentRender;
	}
	
	
	//-------------------------
	// Extend: Background
	//-------------------------
	
	
	private void initBackground() {
		if (enableBackgroundPlay) {
			//            MediaPlayerService.intentToStart(getContext());
			//            mMediaPlayer = MediaPlayerService.getMediaPlayer();
		}
	}
	
	public void setAspectRatio(int aspectRatio) {
		for (int i = 0; i < s_allAspectRatio.length; i++) {
			if (s_allAspectRatio[i]==aspectRatio) {
				mCurrentAspectRatioIndex=i;
				if (mRenderView != null){
					mRenderView.setAspectRatio(mCurrentAspectRatio);
				}
				break;
			}
		}
	}
}

