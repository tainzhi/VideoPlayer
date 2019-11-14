package com.qfq.tainzhi.videoplayer.my_media;

import android.net.Uri;

/**
 * @author: tainzhi
 * @mail: qfq61@qq.com
 * @date: 2019-11-10 19:34
 * @description:
 **/

public class DataSource {
	private String url;
	private String title;
	private Uri uri;
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public DataSource(String url, String title) {
		this.url = url;
		this.title = title;
	}
	
	public DataSource(Uri uri) {
		this.uri = uri;
	}
	
	public Uri getUri() {
		return uri;
	}
	
	public void setUri(Uri uri) {
		this.uri = uri;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
}
