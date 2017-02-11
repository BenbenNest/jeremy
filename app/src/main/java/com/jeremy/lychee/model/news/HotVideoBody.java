package com.jeremy.lychee.model.news;

/**
 * Created by chengyajun on 2016/3/11.
 * 列表传递到底层页视频对象
 */
public class HotVideoBody {
    private String video_id;
    private String source_type;
    private String tag;

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getSource_type() {
        return source_type;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
