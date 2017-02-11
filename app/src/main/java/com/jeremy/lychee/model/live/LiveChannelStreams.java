package com.jeremy.lychee.model.live;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class LiveChannelStreams implements Parcelable {

    /**
     * play_status : 2
     * video_stream : [{"stream_vbt":"标清","stream_type":3,"stream_url":"http://play.kandian.360.cn/83/1b/831b9a5b-5138-2421-89f1-ddc3d0aa7298/mp4h.mp4"}]
     * ts : 1452137509
     * utoken : 22e587f6c0c0dd5df711bf03c09e964e
     */

    private String play_status;
    private String ts;
    private String utoken;
    /**
     * stream_vbt : 标清
     * stream_type : 3
     * stream_url : http://play.kandian.360.cn/83/1b/831b9a5b-5138-2421-89f1-ddc3d0aa7298/mp4h.mp4
     */

    private List<VideoStreamEntity> video_stream;

    public void setPlay_status(String play_status) {
        this.play_status = play_status;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public void setUtoken(String utoken) {
        this.utoken = utoken;
    }

    public void setVideo_stream(List<VideoStreamEntity> video_stream) {
        this.video_stream = video_stream;
    }

    public String getPlay_status() {
        return play_status;
    }

    public String getTs() {
        return ts;
    }

    public String getUtoken() {
        return utoken;
    }

    public List<VideoStreamEntity> getVideo_stream() {
        return video_stream;
    }

    public static class VideoStreamEntity implements Parcelable {
        private String stream_vbt;
        private int stream_type;
        private String stream_url;
        private String stream_rate;

        public void setStream_vbt(String stream_vbt) {
            this.stream_vbt = stream_vbt;
        }

        public void setStream_type(int stream_type) {
            this.stream_type = stream_type;
        }

        public void setStream_url(String stream_url) {
            this.stream_url = stream_url;
        }

        public void setStream_rate(String stream_rate) {
            this.stream_rate = stream_rate;
        }

        public String getStream_vbt() {
            return stream_vbt;
        }

        public int getStream_type() {
            return stream_type;
        }

        public String getStream_url() {
            return stream_url;
        }

        public String getStream_rate() {
            return stream_rate;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.stream_vbt);
            dest.writeInt(this.stream_type);
            dest.writeString(this.stream_url);
            dest.writeString(this.stream_rate);
        }

        public VideoStreamEntity() {
        }

        protected VideoStreamEntity(Parcel in) {
            this.stream_vbt = in.readString();
            this.stream_type = in.readInt();
            this.stream_url = in.readString();
            this.stream_rate = in.readString();
        }

        public static final Parcelable.Creator<VideoStreamEntity> CREATOR = new Parcelable.Creator<VideoStreamEntity>() {
            public VideoStreamEntity createFromParcel(Parcel source) {
                return new VideoStreamEntity(source);
            }

            public VideoStreamEntity[] newArray(int size) {
                return new VideoStreamEntity[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.play_status);
        dest.writeString(this.ts);
        dest.writeString(this.utoken);
        dest.writeTypedList(video_stream);
    }

    public LiveChannelStreams() {
    }

    protected LiveChannelStreams(Parcel in) {
        this.play_status = in.readString();
        this.ts = in.readString();
        this.utoken = in.readString();
        this.video_stream = in.createTypedArrayList(VideoStreamEntity.CREATOR);
    }

    public static final Parcelable.Creator<LiveChannelStreams> CREATOR = new Parcelable.Creator<LiveChannelStreams>() {
        public LiveChannelStreams createFromParcel(Parcel source) {
            return new LiveChannelStreams(source);
        }

        public LiveChannelStreams[] newArray(int size) {
            return new LiveChannelStreams[size];
        }
    };
}
