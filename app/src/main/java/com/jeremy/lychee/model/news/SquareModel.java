package com.jeremy.lychee.model.news;

import java.util.List;

public class SquareModel {


    private int pos;

    private List<ModularModel> list;

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setList(List<ModularModel> list) {
        this.list = list;
    }

    public int getPos() {
        return pos;
    }

    public List<ModularModel> getList() {
        return list;
    }

    public static class ModularModel {
        private String id;
        private String name;
        private String module;

        private List<ElementModel> data;

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setData(List<ElementModel> data) {
            this.data = data;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getModule() {
            return module;
        }

        public void setModule(String module) {
            this.module = module;
        }

        public List<ElementModel> getData() {
            return data;
        }


    }

    public static class ElementModel {
        private String cid;
        private String cname;
        private String ename;
        private String icon;

        private String id;
        private String nid;
        private String md5;
        private String album_pic;
        private String title;
        private String url;
        private String pdate;
        private String source;
        private String news_from;
        private String news_type;
        private String open_type;
        private String news_stick;
        private String news_stick_time;
        private String keywords;
        private String news_data;
        private String zm;
        private String comment;
        private String share;
        private String transmit_num;

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getCname() {
            return cname;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }

        public String getEname() {
            return ename;
        }

        public void setEname(String ename) {
            this.ename = ename;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNid() {
            return nid;
        }

        public void setNid(String nid) {
            this.nid = nid;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getAlbum_pic() {
            return album_pic;
        }

        public void setAlbum_pic(String album_pic) {
            this.album_pic = album_pic;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPdate() {
            return pdate;
        }

        public void setPdate(String pdate) {
            this.pdate = pdate;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getNews_from() {
            return news_from;
        }

        public void setNews_from(String news_from) {
            this.news_from = news_from;
        }

        public String getNews_type() {
            return news_type;
        }

        public void setNews_type(String news_type) {
            this.news_type = news_type;
        }

        public String getOpen_type() {
            return open_type;
        }

        public void setOpen_type(String open_type) {
            this.open_type = open_type;
        }

        public String getNews_stick() {
            return news_stick;
        }

        public void setNews_stick(String news_stick) {
            this.news_stick = news_stick;
        }

        public String getNews_stick_time() {
            return news_stick_time;
        }

        public void setNews_stick_time(String news_stick_time) {
            this.news_stick_time = news_stick_time;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public String getNews_data() {
            return news_data;
        }

        public void setNews_data(String news_data) {
            this.news_data = news_data;
        }

        public String getZm() {
            return zm;
        }

        public void setZm(String zm) {
            this.zm = zm;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getShare() {
            return share;
        }

        public void setShare(String share) {
            this.share = share;
        }

        public String getTransmit_num() {
            return transmit_num;
        }

        public void setTransmit_num(String transmit_num) {
            this.transmit_num = transmit_num;
        }
    }
}
