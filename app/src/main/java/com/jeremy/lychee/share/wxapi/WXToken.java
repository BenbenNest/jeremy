package com.jeremy.lychee.share.wxapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 微信Token信息类
 * 
 * @author zourongbo
 *
 */
public class WXToken extends WXResult implements Parcelable {

    private String access_token;
    private long expires_in;
    private String refresh_token;
    private String openid;
    private String scope;

    public String getAccess_token() {
		return access_token;
	}
    public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
    public long getExpires_in() {
		return expires_in;
	}
    public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}
    public String getRefresh_token() {
		return refresh_token;
	}
    public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
    public String getOpenid() {
		return openid;
	}
    public void setOpenid(String openid) {
		this.openid = openid;
	}
    public String getScope() {
		return scope;
	}
    public void setScope(String scope) {
		this.scope = scope;
	}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	super.writeToParcel(dest, flags);
        dest.writeString(this.access_token);
        dest.writeLong(this.expires_in);
        dest.writeString(this.refresh_token);
        dest.writeString(this.openid);
        dest.writeString(this.scope);
    }

    public WXToken() {
    	super();
    }

    private WXToken(Parcel in) {
    	super(in);
        this.access_token = in.readString();
        this.expires_in = in.readLong();
        this.refresh_token = in.readString();
        this.openid = in.readString();
        this.scope = in.readString();
    }

    public static final Creator<WXToken> CREATOR = new Creator<WXToken>() {
        public WXToken createFromParcel(Parcel source) {
            return new WXToken(source);
        }

        public WXToken[] newArray(int size) {
            return new WXToken[size];
        }
    };
}