package com.qfq.tainzhi.videoplayer.my_media;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
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
import tv.danmaku.ijk.media.player.ISurfaceTextureHost;

/**
 * @author: tainzhi
 * @mail: qfq61@qq.com
 * @date: 2019-11-11 14:14
 * @description:
 **/

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class TextureRenderView extends TextureView implements IRenderView {
	
	private static final String TAG = "TextureRenderView";
	private MeasureHelper measureHelper;
	private SurfaceCallback surfaceCallback;
	
	public TextureRenderView(Context context) {
		super(context);
		initView(context);
	}
	
	public TextureRenderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	public TextureRenderView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public TextureRenderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView(context);
	}
	
	private void initView(Context context) {
		measureHelper = new MeasureHelper(this);
		surfaceCallback = new SurfaceCallback(this);
		setSurfaceTextureListener(surfaceCallback);
	}
	
	@Override
	public View getView() {
		return this;
	}
	
	@Override
	public boolean shouldWaitForResize() {
		return false;
	}
	
	//------------------------
	// layout & measure
	//------------------------
	@Override
	public void setVideoSize(int videoWidth, int videoHeight) {
		if (videoWidth > 0 && videoHeight > 0) {
			measureHelper.setVideoSize(videoWidth, videoHeight);
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
		measureHelper.setVideoRotation(degree);
		setRotation(degree);
	}
	
	@Override
	public void setAspectRatio(int aspectRatio) {
		measureHelper.setVideoRotation(aspectRatio);
		requestLayout();
	}
	
	@Override
	public void addRenderCallback(@NonNull IRenderCallback callback) {
		surfaceCallback.addRenderCallback(callback);
	}
	
	@Override
	public void removeRenderCallback(@NonNull IRenderCallback callback) {
		surfaceCallback.removeReanderCallback(callback);
	}
	
	
	//-----------------
	// TextureViewHolder
	//-----------------
	
	public IRenderView.ISurfaceHolder getSurfaceHodler() {
		return new InternalSurfaceHolder(this, surfaceCallback.surfaceTexture, surfaceCallback);
	}
	
	@Override
	public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
		super.onInitializeAccessibilityEvent(event);
		event.setClassName(TextureRenderView.class.getName());
	}
	
	@Override
	public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
		super.onInitializeAccessibilityNodeInfo(info);
		info.setClassName(TextureRenderView.class.getName());
	}
	
	@Override
	protected void onDetachedFromWindow() {
		surfaceCallback.willDetachFromWindow();
		super.onDetachedFromWindow();
		surfaceCallback.didDetachFromWindow();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		measureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(measureHelper.getMeasuredWidth(), measureHelper.getMeasuredHeight());
	}
	
	//--------------------
	// Accessibility
	//--------------------
	
	private static final class InternalSurfaceHolder implements IRenderView.ISurfaceHolder {
		private TextureRenderView textureRenderView;
		private SurfaceTexture surfaceTexture;
		private ISurfaceTextureHost iSurfaceTextureHost;
		
		public InternalSurfaceHolder(TextureRenderView textureRenderView,
		                             SurfaceTexture surfaceTexture,
		                             ISurfaceTextureHost textureHost) {
			this.textureRenderView = textureRenderView;
			this.surfaceTexture = surfaceTexture;
			this.iSurfaceTextureHost = textureHost;
		}
		
		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
		public void bindToMediaPlayer(IMediaPlayer mp) {
			if (mp == null)
				return;
			if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) &&
					    (mp instanceof ISurfaceTextureHolder)) {
				ISurfaceTextureHolder textureHolder = (ISurfaceTextureHolder) mp;
				textureRenderView.surfaceCallback.setOwnSurfaceTexture(false);
				
				SurfaceTexture surfaceTexture = textureHolder.getSurfaceTexture();
				if (surfaceTexture != null) {
					textureRenderView.setSurfaceTexture(surfaceTexture);
				} else {
					textureHolder.setSurfaceTexture(this.surfaceTexture);
					textureHolder.setSurfaceTextureHost(this.textureRenderView.surfaceCallback);
				}
			} else {
				mp.setSurface(openSurface());
			}
		}
		
		@NonNull
		@Override
		public IRenderView getRenderView() {
			return textureRenderView;
		}
		
		@Nullable
		@Override
		public SurfaceHolder getSurfaceHolder() {
			return null;
		}
		
		@Nullable
		@Override
		public Surface openSurface() {
			if (surfaceTexture == null)
				return null;
			return new Surface(surfaceTexture);
		}
		
		@Nullable
		@Override
		public SurfaceTexture getSurfaceTexture() {
			return surfaceTexture;
		}
	}
	
	private static final class SurfaceCallback implements SurfaceTextureListener,
			                                                      ISurfaceTextureHost {
		private SurfaceTexture surfaceTexture;
		private boolean isFormatChanged;
		private int width;
		private int height;
		
		private boolean ownSurfaceTexture = true;
		private boolean willDetachFromWindow = false;
		private boolean didDetachFromWindow = false;
		
		private WeakReference<TextureRenderView> weakReandView;
		private Map<IRenderCallback, Object> renderCallbackMap = new ConcurrentHashMap<>();
		
		public SurfaceCallback(TextureRenderView reanderView) {
			weakReandView = new WeakReference<>(reanderView);
		}
		
		public void setOwnSurfaceTexture(boolean ownSurfaceTexture) {
			this.ownSurfaceTexture = ownSurfaceTexture;
		}
		
		public void addRenderCallback(IRenderCallback callback) {
			renderCallbackMap.put(callback, callback);
			
			ISurfaceHolder surfaceHolder = null;
			if (surfaceTexture != null) {
				if (surfaceHolder == null) {
					surfaceHolder = new InternalSurfaceHolder(weakReandView.get(), surfaceTexture
							, this);
				}
				callback.onSurfaceCreated(surfaceHolder, width, height);
			}
			
			if (isFormatChanged) {
				if (surfaceHolder == null) {
					surfaceHolder = new InternalSurfaceHolder(weakReandView.get(), surfaceTexture
							, this);
				}
				callback.onSurfaceChanged(surfaceHolder, 0, width, height);
			}
		}
		
		public void removeReanderCallback(IRenderCallback callback) {
			renderCallbackMap.remove(callback);
		}
		
		@Override
		public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
			this.surfaceTexture = surfaceTexture;
			isFormatChanged = false;
			width = 0;
			height = 0;
			
			ISurfaceHolder surfaceHolder = new InternalSurfaceHolder(weakReandView.get(),
					surfaceTexture, this);
			for (IRenderCallback callback : renderCallbackMap.keySet()) {
				callback.onSurfaceCreated(surfaceHolder, 0, 0);
			}
		}
		
		@Override
		public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int i, int i1) {
			surfaceTexture = surface;
			isFormatChanged = true;
			width = i;
			height = i1;
			
			ISurfaceHolder surfaceHolder = new InternalSurfaceHolder(weakReandView.get(),
					surface, this);
			for (IRenderCallback callback : renderCallbackMap.keySet()) {
				callback.onSurfaceChanged(surfaceHolder, 0, width, height);
			}
		}
		
		@Override
		public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
			surfaceTexture = surface;
			isFormatChanged = false;
			width = 0;
			height = 0;
			
			ISurfaceHolder surfaceHolder = new InternalSurfaceHolder(weakReandView.get(), surface
					, this);
			for (IRenderCallback callback : renderCallbackMap.keySet()) {
				callback.onSurfaceDestroyed(surfaceHolder);
			}
			
			return ownSurfaceTexture;
		}
		
		@Override
		public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
		
		}
		
		//-------------------------
		// ISurfaceTextureHost
		//-------------------------
		
		@Override
		public void releaseSurfaceTexture(SurfaceTexture surfaceTexture) {
			if (surfaceTexture == null) {
				Log.d(TAG, "releaseSurfaceTexture: null");
			} else if (didDetachFromWindow) {
				if (surfaceTexture != this.surfaceTexture) {
					Log.d(TAG, "releaseSurfaceTexture: didDetachFromWindow(): release different SurfaceTexture");
					surfaceTexture.release();
				} else if (!ownSurfaceTexture) {
					Log.d(TAG, "releaseSurfaceTexture: didDetachFromWindow(): release detached SurfaceTexture");
					surfaceTexture.release();
				} else {
					Log.d(TAG, "releaseSurfaceTexture: didDetachFromWindow(): already released by TextureView");
				}
			} else if (willDetachFromWindow) {
				if (surfaceTexture != this.surfaceTexture) {
					Log.d(TAG, "releaseSurfaceTexture: willDetachFromWindow(): release different SurfaceTexture");
					surfaceTexture.release();
				} else if (!ownSurfaceTexture) {
					Log.d(TAG, "releaseSurfaceTexture: willDetachFromWindow(): re-attach SurfaceTexture to TextureView");
					setOwnSurfaceTexture(true);
				} else {
					Log.d(TAG, "releaseSurfaceTexture: willDetachFromWindow(): will released by TextureView");
				}
			} else {
				if (surfaceTexture != this.surfaceTexture) {
					Log.d(TAG, "releaseSurfaceTexture: alive: release different SurfaceTexture");
					surfaceTexture.release();
				} else if (!ownSurfaceTexture) {
					Log.d(TAG, "releaseSurfaceTexture: alive: re-attach SurfaceTexture to TextureView");
					setOwnSurfaceTexture(true);
				} else {
					Log.d(TAG, "releaseSurfaceTexture: alive: will released by TextureView");
				}
			}
		}
		
		public void willDetachFromWindow() {
			Log.d(TAG, "willDetachFromWindow()");
			willDetachFromWindow = true;
		}
		
		public void didDetachFromWindow() {
			Log.d(TAG, "didDetachFromWindow()");
			didDetachFromWindow = true;
		}
	}
}
