package com.jeremy.lychee.model.news;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 频道类
 *
 * @author xiaozhongzhong
 */
public class Channel implements Parcelable {
    /**
     * 每日看点界面
     */
    public static final int TYPE_DAILY = 2;
    /**
     * 特殊频道
     */
    public static final int TYPE_SPECIAL = 1;
    /**
     * 一般新闻
     */
    public static final int TYPE_COMMON = 0;


    public static final String TID_SPECIAL = "-100";

    /**
     * 频道标题
     */
    private String title;
    private String alias;
    private int type;
    private String tid;


//    频道显示名	ios传参值	android传参值
//    最头条	-1	recommend 打点用空字符串
//    时事	11	polity
//    娱乐八卦	7	fun
//    互联网	2	it
//    财经	17	economy
//    足球	18	football
//    篮球	1	basketball
//    综合体育	16	sport
//    军事	3	militery
//    科学	15	science
//    时尚	6	fashion
//    房产	8	estate
//    汽车	13	car
//    游戏	10	game
//    历史	5	lishi
//    数码	9	digit



    public Channel() {
        super();
    }

    public Channel(String title, String alias, int type, String id) {
        super();
        this.title = title;
        this.alias = alias;
        this.type = type;
        this.tid = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.alias);
        dest.writeInt(this.type);
        dest.writeString(this.tid);
    }

    private Channel(Parcel in) {
        this.title = in.readString();
        this.alias = in.readString();
        this.type = in.readInt();
        this.tid = in.readString();
    }

    public static final Creator<Channel> CREATOR = new Creator<Channel>() {
        public Channel createFromParcel(Parcel source) {
            return new Channel(source);
        }

        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };
}
