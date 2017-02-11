package com.jeremy.lychee.share.wxapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 微信返回结果基础类
 * 
 * @author zourongbo
 *
 */
public class WXResult implements Parcelable {
    private int errcode;
    private String errmsg;

    public int getErrcode() {
		return errcode;
	}
    
    public void setErrcode(int errcode) {
		this.errcode = errcode;
	}
    
    public String getErrmsg() {
		return errmsg;
	}
    
    public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.errcode);
        dest.writeString(this.errmsg);
    }

    public WXResult() {
    }

    public WXResult(Parcel in) {
        this.errcode = in.readInt();
        this.errmsg = in.readString();
    }

    public static final Creator<WXResult> CREATOR = new Creator<WXResult>() {
        public WXResult createFromParcel(Parcel source) {
            return new WXResult(source);
        }

        public WXResult[] newArray(int size) {
            return new WXResult[size];
        }
    };
}