package com.jeremy.lychee.model;

import android.text.TextUtils;

/*
 * 分享内容封装类
 */
public class ShareInfo {
	public static final int IS_IMG_LOGO = 1;
	public static final int ISNOT_IMG_LOGO = 0;

	public static final int SHARECONTENT_NEWS = 0;
	public static final int SHARECONTENT_LIVE = 1;
	private String url;
	private String nid;
	private String title;
	private String content;
	private String imgPath;
	private String imgUrl;
	private int type = ISNOT_IMG_LOGO;



    private int shareContentType;   //分享类型:文章  视频
	
	public ShareInfo() {
	}
	
	public ShareInfo(String url, String nid, String title, String content, String imgUrl, String imgPath,int contentType) {

		this.url = url;
		this.nid = nid;
		this.title = title;
		if(TextUtils.isEmpty(content)){
			this.content = title;
		} else {
			this.content = content;
		}
		this.imgUrl = imgUrl;
		this.imgPath = imgPath;
		this.shareContentType = contentType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}
    public int getShareContentType() {
        return shareContentType;
    }

    public void setShareContentType(int shareContentType) {
        this.shareContentType = shareContentType;
    }

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}