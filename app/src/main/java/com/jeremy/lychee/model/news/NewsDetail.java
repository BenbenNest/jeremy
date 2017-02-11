
package com.jeremy.lychee.model.news;

import android.os.Parcel;
import android.os.Parcelable;

import com.jeremy.lychee.db.WeMediaChannel;

import java.util.ArrayList;

public class NewsDetail implements Parcelable {

    private String title;
    private String time;
    private String wapurl;
    private String errno;
    private String url;
    private ArrayList<NewsContent> content;
    private String source;
    private String shorturl;
    
    private String slogan;
    private String subhead;
    private String newtags;
    private String lanmu;

    private String summary;
    private String transmit_num;
    private int transmit_type;

    private WeMediaChannel media;

    private ArrayList<TransmitData> transmit_data;

    public void setSummary(String summary) {
        this.summary = summary;
    }
    public String getSummary() {
        return summary;
    }
    public String getLanmu() {
		return lanmu;
	}

	public void setLanmu(String lanmu) {
		this.lanmu = lanmu;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public String getSubhead() {
		return subhead;
	}

	public void setSubhead(String subhead) {
		this.subhead = subhead;
	}

	public String getNewtags() {
		return newtags;
	}

	public void setNewtags(String newtags) {
		this.newtags = newtags;
	}

    public String getWapurl() {
        return wapurl;
    }

    public void setWapurl(String wapurl) {
        this.wapurl = wapurl;
    }

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<NewsContent> getContent() {
        return content;
    }

    public void setContent(ArrayList<NewsContent> content) {
        this.content = content;
    }

    public String getShorturl() {
        return shorturl;
    }

    public void setShorturl(String shorturl) {
        this.shorturl = shorturl;
    }

    public String getTransmit_num() {
        return transmit_num;
    }

    public void setTransmit_num(String transmit_num) {
        this.transmit_num = transmit_num;
    }

    public ArrayList<TransmitData> getTransmit_data() {
        return transmit_data;
    }

    public void setTransmit_data(ArrayList<TransmitData> transmit_data) {
        this.transmit_data = transmit_data;
    }

    public int getTransmit_type() {
        return transmit_type;
    }

    public void setTransmit_type(int transmit_type) {
        this.transmit_type = transmit_type;
    }

    public WeMediaChannel getMedia() {
        return media;
    }

    public void setMedia(WeMediaChannel media) {
        this.media = media;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.time);
        dest.writeString(this.wapurl);
        dest.writeString(this.errno);
        dest.writeString(this.url);
        dest.writeSerializable(this.content);
        dest.writeString(this.source);
        dest.writeString(this.shorturl);

        dest.writeString(this.slogan);
        dest.writeString(this.subhead);
        dest.writeString(this.newtags);
        dest.writeString(this.transmit_num);
        dest.writeInt(this.transmit_type);
        dest.writeSerializable(this.transmit_data);
    }

    public NewsDetail() {
    }

    private NewsDetail(Parcel in) {
        this.title = in.readString();
        this.time = in.readString();
        this.wapurl = in.readString();
        this.errno = in.readString();
        this.url = in.readString();
        this.content = (ArrayList<NewsContent>) in.readSerializable();
        this.source = in.readString();
        this.shorturl = in.readString();
        
        this.slogan = in.readString();
        this.subhead = in.readString();
        this.newtags = in.readString();
        this.transmit_num = in.readString();
        this.transmit_type = in.readInt();
        this.transmit_data = (ArrayList<TransmitData>) in.readSerializable();
    }

    public static final Creator<NewsDetail> CREATOR = new Creator<NewsDetail>() {
        public NewsDetail createFromParcel(Parcel source) {
            return new NewsDetail(source);
        }

        public NewsDetail[] newArray(int size) {
            return new NewsDetail[size];
        }
    };
}
