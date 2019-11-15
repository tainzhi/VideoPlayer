package com.qfq.tainzhi.videoplayer.my_media;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.ISurfaceTextureHolder;

/**
 * @author: tainzhi
 * @mail: qfq61@qq.com
 * @date: 2019-11-11 15:29
 * @description:
 **/

public class SurfaceRenderView extends SurfaceView implements IRenderView {
	
	private final String TAG = this.getClass().getSimpleName();
	
	private MeasureHelper measureHelper;
	private SurfaceCallback surfaceCallback;
	
	public SurfaceRenderView(Context context) {
		super(context);
		initView(context);
	}
	
	public SurfaceRenderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	public SurfaceRenderView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}
	
	public SurfaceRenderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView(context);
	}
	
	private void initView(Context context) {
		measureHelper = new MeasureHelper(this);
		surfaceCallback = new SurfaceCallback(this);
		getHolder().addCallback(surfaceCallback);
		getHolder().setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
	}
	
	@Override
	public View getView() {
		return this;
	}
	
	@Override
	public boolean shouldWaitForResize() {
		return true;
	}
	
	@Override
	public void setVideoSize(int videoWidth, int videoHeight) {
		if (videoWidth > 0 && videoHeight > 0) {
			measureHelper.setVideoSize(videoWidth, videoHeight);
			getHolder().setFixedSize(videoWidth, videoHeight);
			requestLayout();
		}
	}
	
	@Override
	public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
		if (videoSarNum > 0 && videoSarDen > 0) {
			measureHelper.setVideoSampleAspectRatio(videoSarNum, videoSarDen);
			requestLayout();
		}
	}
	
	@Override
	public void setVideoRotation(int degree) {
		Log.e(TAG, "SurfaceView doesn't support rotation (" + degree + ")!\n");
	}
	
	@Override
	public void setAspectRatio(int aspectRatio) {
		measureHelper.setAspectRatio(aspectRatio);
		requestLayout();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(measureHelper.getMeasuredWidth(), measureHelper.getMeasuredHeight());
	}
	
	private static final class InternalSurfaceHolder implements IRenderView.ISurfaceHolder {
		private SurfaceRenderView surfaceRenderView;
		private SurfaceHolder surfaceHolder;
		
		public InternalSurfaceHolder(SurfaceRenderView surfaceRenderView,
		                             SurfaceHolder surfaceHolder) {
			this.surfaceRenderView = surfaceRenderView;
			this.surfaceHolder = surfaceHolder;
		}
		
		public void bindToMediaPlayer(VideoPlayerBase mp) {
			if (mp != null) {
				if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) &&
						    (mp instanceof ISurfaceTextureHolder)) {
					ISurfaceTextureHolder textureHolder = (ISurfaceTextureHolder) mp;
					textureHolder.setSurfaceTexture(null);
				}
				// mp.setDisplay(surfaceHolder);
			}
		}
		
		@NonNull
		@Override
		public IRenderView getRenderView() {
			return surfaceRenderView;
		}
		
		@Nullable
		@Override
		public SurfaceHolder getSurfaceHolder() {
			return surfaceHolder;
		}
		
		@Nullable
		@Override
		public SurfaceTexture getSurfaceTexture() {
			return null;
		}
		
		@Nullable
		@Override
		public Surface openSurface() {
			if (surfaceHolder == null) {
				return null;
			}
			return surfaceHolder.getSurface();
		}
	}
	
	@Override
	public void addRenderCallback(IRenderCallback callback) {
		surfaceCallback.addRenderCallback(callback);
	}
	
	@Override
	public void removeRenderCallback(IRenderCallback callback) {
		surfaceCallback.removeRenderCallback(callback);
	}
	
	
	private static final class SurfaceCallback implements SurfaceHolder.Callback {
		private SurfaceHolder mSurfaceHolder;
		private boolean isFormatChanged;
		private int format;
		private int width;
		private int height;
		
		private WeakReference<SurfaceRenderView> weakSurfaceView;
		private Map<IRenderCallback, Object> renderCallbackMap = new ConcurrentHashMap<>();
		
		public SurfaceCallback(SurfaceRenderView surfaceView) {
			weakSurfaceView = new WeakReference<>(surfaceView);
		}
		
		public void addRenderCallback(IRenderCallback callback) {
			renderCallbackMap.put(callback, callback);
			
			ISurfaceHolder surfaceHolder = null;
			if (mSurfaceHolder != null) {
				if (surfaceHolder == null) {
					surfaceHolder = new InternalSurfaceHolder(weakSurfaceView.get(),
							mSurfaceHolder);
				}
				callback.onSurfaceCreated(surfaceHolder, width, height);
			}
			
			if (isFormatChanged) {
				if (surfaceHolder == null) {
					surfaceHolder = new InternalSurfaceHolder(weakSurfaceView.get(), mSurfaceHolder);
				}
				callback.onSurfaceChanged(surfaceHolder, format, width, height);
			}
		}
		
		public void removeRenderCallback(IRenderCallback callback) {
			renderCallbackMap.remove(callback);
		}
		
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			mSurfaceHolder = holder;
			isFormatChanged = false;
			format = 0;
			width = 0;
			height = 0;
			
			ISurfaceHolder surfaceHolder = new InternalSurfaceHolder(weakSurfaceView.get(),
					mSurfaceHolder);
			for (IRenderCallback callback: renderCallbackMap.keySet()) {
				callback.onSurfaceCreated(surfaceHolder, 0, 0);
			}
		}
		
		@Override
		public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
			mSurfaceHolder = surfaceHolder;
			isFormatChanged = true;
			format = i;
			width = i1;
			height = i2;
			
			ISurfaceHolder surfaceHolder1 = new InternalSurfaceHolder(weakSurfaceView.get(),
					mSurfaceHolder);
			for (IRenderCallback callback: renderCallbackMap.keySet()) {
				callback.onSurfaceChanged(surfaceHolder1, i, i1, i2);
			}
		}
		
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			mSurfaceHolder = null;
			isFormatChanged = false;
			format = 0;
			width = 0;
			height = 0;
			
			ISurfaceHolder surfaceHolder = new InternalSurfaceHolder(weakSurfaceView.get(),
					mSurfaceHolder);
			for (IRenderCallback callback : renderCallbackMap.keySet()) {
				callback.onSurfaceDestroyed(surfaceHolder);
			}
		}
	}
	
	//-------------------------
	// Accessibility
	//-------------------------
	@Override
	public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
		super.onInitializeAccessibilityEvent(event);
		event.setClassName(SurfaceRenderView.class.getName());
	}
	
	@Override
	public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
		super.onInitializeAccessibilityNodeInfo(info);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			info.setClassName(SurfaceRenderView.class.getName());
		}
	}
}
