package com.qfq.tainzhi.videoplayer.my_media;

/**
 * @author: tainzhi
 * @mail: qfq61@qq.com
 * @date: 2019-11-10 19:34
 * @description:
 **/

public class DataSource {
	private String url;
	private String title;
	
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
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
}
