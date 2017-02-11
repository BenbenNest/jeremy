package com.jeremy.lychee.share.wxapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 微信登陆用户类
 * 
 * @author zourongbo
 *
 */
public class WXUser extends WXResult implements Parcelable {

    private String openid;
    private String nickname;
    private int sex;
    private String province;
    private String city;
    private String country;
    private String unionid;
    private String headimgurl;

    public String getHeadimgurl() {
		return headimgurl;
	}
    public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
    public String getOpenid() {
		return openid;
	}
    public void setOpenid(String openid) {
		this.openid = openid;
	}
    public String getNickname() {
		return nickname;
	}
    public void setNickname(String nickname) {
		this.nickname = nickname;
	}
    public int getSex() {
		return sex;
	}
    public void setSex(int sex) {
		this.sex = sex;
	}
    public String getProvince() {
		return province;
	}
    public void setProvince(String province) {
		this.province = province;
	}
    public String getCity() {
		return city;
	}
    public void setCity(String city) {
		this.city = city;
	}
    public String getCountry() {
		return country;
	}
    public void setCountry(String country) {
		this.country = country;
	}
    public String getUnionid() {
		return unionid;
	}
    public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	super.writeToParcel(dest, flags);
        dest.writeString(this.openid);
        dest.writeString(this.nickname);
        dest.writeInt(this.sex);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.country);
        dest.writeString(this.unionid);
        dest.writeString(this.headimgurl);
    }

    public WXUser() {
    	super();
    }

    private WXUser(Parcel in) {
    	super(in);
        this.openid = in.readString();
        this.nickname = in.readString();
        this.sex = in.readInt();
        this.province = in.readString();
        this.city = in.readString();
        this.country = in.readString();
        this.unionid = in.readString();
        this.headimgurl = in.readString();
    }

    public static final Creator<WXUser> CREATOR = new Creator<WXUser>() {
        public WXUser createFromParcel(Parcel source) {
            return new WXUser(source);
        }

        public WXUser[] newArray(int size) {
            return new WXUser[size];
        }
    };
}