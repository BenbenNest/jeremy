package com.jeremy.lychee.model.news;

import java.io.Serializable;
import java.util.List;

/**
 * Created by benbennest on 16/6/5.
 */
public class NewsDetailContent implements Serializable {
    private static final long serialVersionUID = 1038087104408607208L;


    /**
     * video : 0
     * content : [{"type":"video","value":"{\"title\":\"\\u89c6\\u9891\",\"items\":[{\"video_id\":758918,\"customSort\":0,\"id\":\"758918\",\"video\":[{\"stream_vbt\":\"\\u6d41\\u7545\",\"stream_type\":3,\"stream_url\":\"http:\\/\\/play2.kandian.360.cn\\/vod_xinwen\\/vod-media\\/821992_2108421_20160605145916.m3u8\",\"stream_rate\":\"500\"},{\"stream_vbt\":\"\\u6807\\u6e05\",\"stream_type\":3,\"stream_url\":\"http:\\/\\/play2.kandian.360.cn\\/vod_xinwen\\/vod-media\\/821992_2108422_20160605145916.m3u8\",\"stream_rate\":\"800\"},{\"stream_vbt\":\"\\u9ad8\\u6e05\",\"stream_type\":3,\"stream_url\":\"http:\\/\\/play2.kandian.360.cn\\/vod_xinwen\\/vod-media\\/821992_2108423_20160605145916.m3u8\",\"stream_rate\":\"1000\"}],\"src\":\"BTV\\u751f\\u4ea7\\u5e73\\u53f0\",\"title\":\"\\u4fc4\\u7f57\\u65af\\u7f8e\\u5973\\u5bcc\\u4e8c\\u4ee3\\u98d9\\u8f66\\u62d8\\u6355 \\u8fdd\\u7ae0342\\u6b21\",\"imgurl\":\"http:\\/\\/p8.qhimg.com\\/t0136e226f821416f75.jpg?size=400x400\",\"url\":\"http:\\/\\/play2.kandian.360.cn\\/vod_xinwen\\/vod-media\\/821992_2108423_20160605145916.m3u8\",\"type\":\"3\",\"tag\":\"\"}]}"},{"type":"img","value":"http://p33.qhimg.com/dmfd/__90/t019fc22a8eda0eb3f5.webp?size=634x476?zoom_out=80","title":""},{"type":"txt","value":"6月4日报道，俄罗斯一位女富二代Bagdasaryan在街头飙车拒捕，而这并不是她的第一次，据交警称，该名女富二代已经超速违章高达342次，需缴纳超过10000欧的罚款。但是这笔资金已经由她的富豪父亲全部付清。"},{"type":"img","value":"http://p31.qhimg.com/dmfd/__90/t018bf7aeb226bb05a2.webp?size=448x598?zoom_out=80","title":""},{"type":"txt","value":"Bagdasaryan在社交网络上Po出自己内衣夹着钞票的照片。"},{"type":"img","value":"http://p33.qhimg.com/dmfd/__90/t01a628cba042df63d9.webp?size=552x598?zoom_out=80","title":""},{"type":"txt","value":"agdasaryan经常在自己的社交网络上Po出自己奢靡的个人生活。"},{"type":"img","value":"http://p31.qhimg.com/dmfd/__90/t01ad9dcf8d301ef62f.webp?size=478x598?zoom_out=80","title":""},{"type":"txt","value":"Bagdasaryan日常照。"},{"type":"img","value":"http://p34.qhimg.com/dmfd/__90/t013e3dcc916e1fc421.webp?size=634x570?zoom_out=80","title":""},{"type":"txt","value":"Bagdasaryan在社交网络PO出自己火烧钞票的照片。"},{"type":"img","value":"http://p31.qhimg.com/dmfd/__90/t019d09315e4cc7a832.webp?size=634x432?zoom_out=80","title":""},{"type":"txt","value":"Bagdasaryan和朋友的豪车一字排开。"},{"type":"img","value":"http://p32.qhimg.com/dmfd/__90/t01c7e9b558f9341552.webp?size=634x325?zoom_out=80","title":""},{"type":"txt","value":"早在2015年Bagdasaryan曾经卷入一场因为其超速驾驶而导致的车祸，车祸中她的两位朋友身亡。尽管她没有被指控，但很多民众认为她这种利用权力和金钱处理事情的方式让人愤怒。"},{"type":"img","value":"http://p33.qhimg.com/dmfd/__90/t01e67dce241da5ac14.webp?size=478x598?zoom_out=80","title":""},{"type":"txt","value":"Bagdasaryan生活照。"},{"type":"img","value":"http://p35.qhimg.com/dmfd/__90/t01cb973262e3fc8e70.webp?size=447x598?zoom_out=80","title":""},{"type":"txt","value":"Bagdasaryan生活照。"},{"type":"img","value":"http://p32.qhimg.com/dmfd/__90/t01213f2a7823ba2db1.webp?size=634x558?zoom_out=80","title":""},{"type":"txt","value":"Bagdasaryan生活照。"}]
     * title : 俄罗斯富二代女子飙车拒捕 违章342次
     * img : 1
     * allqimg : 1
     * time : 1465113385
     * summary : 该名女富二代已经超速违章高达342次，需缴纳超过10000欧的罚款。但是这笔资金已经由她的富豪父亲全部付清。
     * news_type : 1
     * source : 中华网
     * media : {}
     * n_t : 1000
     * errno : 0
     * wapurl : http://p.m.btime.com/detail?id=195648&news_from=1
     * topic : []
     * tags : []
     * transmit_num : 0
     * transmit_data : []
     * transmit_type : 1
     * shorturl : http://t.m.so.com/t/N_RdUzJ
     * news_from : 1
     * template : 1
     * author_uid :
     * url : http://news.btime.com/picture/20160605/n195648.shtml
     * vinfo : {}
     */

    private String video;
    private String title;
    private int img;
    private int allqimg;
    private int time;
    private String summary;
    private String news_type;
    private String source;
    private int n_t;
    private int errno;
    private String wapurl;
    private int transmit_num;
    private String transmit_type;
    private String shorturl;
    private String news_from;
    private String template;
    private String author_uid;
    private String url;
    /**
     * type : video
     * value : {"title":"\u89c6\u9891","items":[{"video_id":758918,"customSort":0,"id":"758918","video":[{"stream_vbt":"\u6d41\u7545","stream_type":3,"stream_url":"http:\/\/play2.kandian.360.cn\/vod_xinwen\/vod-media\/821992_2108421_20160605145916.m3u8","stream_rate":"500"},{"stream_vbt":"\u6807\u6e05","stream_type":3,"stream_url":"http:\/\/play2.kandian.360.cn\/vod_xinwen\/vod-media\/821992_2108422_20160605145916.m3u8","stream_rate":"800"},{"stream_vbt":"\u9ad8\u6e05","stream_type":3,"stream_url":"http:\/\/play2.kandian.360.cn\/vod_xinwen\/vod-media\/821992_2108423_20160605145916.m3u8","stream_rate":"1000"}],"src":"BTV\u751f\u4ea7\u5e73\u53f0","title":"\u4fc4\u7f57\u65af\u7f8e\u5973\u5bcc\u4e8c\u4ee3\u98d9\u8f66\u62d8\u6355 \u8fdd\u7ae0342\u6b21","imgurl":"http:\/\/p8.qhimg.com\/t0136e226f821416f75.jpg?size=400x400","url":"http:\/\/play2.kandian.360.cn\/vod_xinwen\/vod-media\/821992_2108423_20160605145916.m3u8","type":"3","tag":""}]}
     */

    private List<Content> content;

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getAllqimg() {
        return allqimg;
    }

    public void setAllqimg(int allqimg) {
        this.allqimg = allqimg;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getNews_type() {
        return news_type;
    }

    public void setNews_type(String news_type) {
        this.news_type = news_type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getN_t() {
        return n_t;
    }

    public void setN_t(int n_t) {
        this.n_t = n_t;
    }

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getWapurl() {
        return wapurl;
    }

    public void setWapurl(String wapurl) {
        this.wapurl = wapurl;
    }

    public int getTransmit_num() {
        return transmit_num;
    }

    public void setTransmit_num(int transmit_num) {
        this.transmit_num = transmit_num;
    }

    public String getTransmit_type() {
        return transmit_type;
    }

    public void setTransmit_type(String transmit_type) {
        this.transmit_type = transmit_type;
    }

    public String getShorturl() {
        return shorturl;
    }

    public void setShorturl(String shorturl) {
        this.shorturl = shorturl;
    }

    public String getNews_from() {
        return news_from;
    }

    public void setNews_from(String news_from) {
        this.news_from = news_from;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getAuthor_uid() {
        return author_uid;
    }

    public void setAuthor_uid(String author_uid) {
        this.author_uid = author_uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public static class Content implements Serializable {
        private static final long serialVersionUID = 5954147268038586145L;

        private String type;
        private String value;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
