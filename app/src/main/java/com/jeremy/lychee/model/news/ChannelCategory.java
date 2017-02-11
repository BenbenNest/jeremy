package com.jeremy.lychee.model.news;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ChannelCategory {

    /**
     * pic : http://p1.qhimg.com/t014e71d4bd7dd7fbf2.png?size=720x319
     * text : 精彩从这里开始
     */

    private Intro start;
    /**
     * cid : 800
     * cname : 文艺范
     * enname : Literature
     * icon : http://p1.qhimg.com/t0122b346a71fcaaa2a.png?size=50x51
     * sub_num : 12
     * subs : [{"cid":"100","cname":"测试1","icon":"http://p3.qhimg.com/t01032946afad36ee65.png?size=297x299"},{"cid":"101","cname":"测试2","icon":"http://p3.qhimg.com/t01032946afad36ee65.png?size=297x299"}]
     */

    private List<ChannelInfo> list;

    public void setStart(Intro start) {
        this.start = start;
    }

    public void setList(List<ChannelInfo> list) {
        this.list = list;
    }

    public Intro getStart() {
        return start;
    }

    public List<ChannelInfo> getList() {
        return list;
    }

    public static class Intro {
        private String pic;
        private String text;

        public void setPic(String pic) {
            this.pic = pic;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getPic() {
            return pic;
        }

        public String getText() {
            return text;
        }
    }

    public static class ChannelInfo {
        private String cid;
        private String cname;
        private String enname;
        private String icon;
        private String sub_num;
        /**
         * cid : 100
         * cname : 测试1
         * icon : http://p3.qhimg.com/t01032946afad36ee65.png?size=297x299
         */

        private List<SubChannel> subs;

        public void setCid(String cid) {
            this.cid = cid;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }

        public void setEnname(String enname) {
            this.enname = enname;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setSub_num(String sub_num) {
            this.sub_num = sub_num;
        }

        public void setSubs(List<SubChannel> subs) {
            this.subs = subs;
        }

        public String getCid() {
            return cid;
        }

        public String getCname() {
            return cname;
        }

        public String getEnname() {
            return enname;
        }

        public String getIcon() {
            return icon;
        }

        public String getSub_num() {
            return sub_num;
        }

        public List<SubChannel> getSubs() {
            return subs;
        }

        public static class SubChannel implements Parcelable {
            private String cid;
            private String cname;
            private String icon;
            private String rec_img;

            public void setCid(String cid) {
                this.cid = cid;
            }

            public void setCname(String cname) {
                this.cname = cname;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getCid() {
                return cid;
            }

            public String getCname() {
                return cname;
            }

            public String getIcon() {
                return icon;
            }

            public String getRec_img() {
                return rec_img;
            }

            public void setRec_img(String rec_img) {
                this.rec_img = rec_img;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.cid);
                dest.writeString(this.cname);
                dest.writeString(this.icon);
                dest.writeString(this.rec_img);
            }

            public SubChannel() {
            }

            protected SubChannel(Parcel in) {
                this.cid = in.readString();
                this.cname = in.readString();
                this.icon = in.readString();
                this.rec_img = in.readString();
            }

            public static final Parcelable.Creator<SubChannel> CREATOR = new Parcelable.Creator<SubChannel>() {
                public SubChannel createFromParcel(Parcel source) {
                    return new SubChannel(source);
                }

                public SubChannel[] newArray(int size) {
                    return new SubChannel[size];
                }
            };
        }
    }
}
