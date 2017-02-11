
package com.jeremy.lychee.model.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author xiaozhongzhong
 */
public class HotNews implements Parcelable {

    private long id;
    private String title;
    private String ltitle;
    private String keyword;
    private String uptime;
    private String createtime;
    private String url;
    private String isnew;
    private String img;
    private String cls;
    private String idx;
    private String pdate;
    private String show;
    private String domain;
    private String site_name;
    private String content;
    private String m;
    private String gk;
    private String flag;
    private int n_t;

    public int getN_t() {
        return n_t;
    }

    public void setN_t(int n_t) {
        this.n_t = n_t;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getGk() {
        return gk;
    }

    public void setGk(String gk) {
        this.gk = gk;
    }

    /**
     * 更新专题时专题的类型 0：新增专题 1：已更新专题 2：普通专题没有变化
     */
    private int updateType;

    /**
     * 更新专题时专题的类型 0：新增专题 1：已更新专题 2：普通专题没有变化
     * 
     * @return
     */
    public int getUpdateType() {
        return updateType;
    }

    /**
     * 更新专题时专题的类型 0：新增专题 1：已更新专题 2：普通专题没有变化(已读)
     * 
     * @param updateType
     */
    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIsnew() {
        return isnew;
    }

    public void setIsnew(String isnew) {
        this.isnew = isnew;
    }

    public String getImg() {
        if (img.contains("|")) {
            String[] urls = img.split("\\|");
            if (urls != null && urls.length > 0) {
                return urls[0];
            }
        }
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getPdate() {
        return pdate;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getLtitle() {
        return ltitle;
    }

    public void setLtitle(String ltitle) {
        this.ltitle = ltitle;
    }

    public HotNews(Parcel in) {
        id = in.readLong();
        title = in.readString();
        ltitle = in.readString();
        keyword = in.readString();
        uptime = in.readString();
        createtime = in.readString();
        url = in.readString();
        isnew = in.readString();
        img = in.readString();
        cls = in.readString();
        idx = in.readString();
        pdate = in.readString();
        show = in.readString();
        domain = in.readString();
        site_name = in.readString();
        content = in.readString();
        m = in.readString();
        gk = in.readString();
        flag = in.readString();
        n_t = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(ltitle);
        dest.writeString(keyword);
        dest.writeString(uptime);
        dest.writeString(createtime);
        dest.writeString(url);
        dest.writeString(isnew);
        dest.writeString(img);
        dest.writeString(cls);
        dest.writeString(idx);
        dest.writeString(pdate);
        dest.writeString(show);
        dest.writeString(domain);
        dest.writeString(site_name);
        dest.writeString(content);
        dest.writeString(m);
        dest.writeString(gk);
        dest.writeString(flag);
        dest.writeInt(n_t);
    }

    public static final Creator<HotNews> CREATOR = new Creator<HotNews>() {
        public HotNews createFromParcel(Parcel in) {
            return new HotNews(in);
        }

        public HotNews[] newArray(int size) {
            return new HotNews[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
