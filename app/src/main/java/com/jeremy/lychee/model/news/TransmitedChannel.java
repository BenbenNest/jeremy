package com.jeremy.lychee.model.news;

import com.jeremy.lychee.db.WeMediaChannel;

/**
 * Created by wangp on 15-12-2.
 */
public class TransmitedChannel {
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setSub_info(WeMediaChannel sub_info) {
        this.sub_info = sub_info;
    }

    public WeMediaChannel getChannelInfo() {
        return sub_info;
    }

    private long timestamp;
    private WeMediaChannel sub_info;
}
