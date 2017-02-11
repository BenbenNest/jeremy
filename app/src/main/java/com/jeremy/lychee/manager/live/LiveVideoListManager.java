package com.jeremy.lychee.manager.live;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.Pair;
import android.text.TextUtils;

import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.model.live.LiveVideoInfo;
import com.jeremy.lychee.net.OldRetroAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

public class LiveVideoListManager implements Parcelable {
    static public LiveVideoListManager createInstance() {
        return new LiveVideoListManager();
    }

    private LiveVideoListManager() {
    }

    private PageHandler<List<LiveVideoInfo>> pageHandler = new PageHandler<>();

    public Observable<Pair<Integer, List<LiveVideoInfo>>> getLiveVideoList(String topicId, String id, String tag, boolean loadMore) {
        String uniqueId = uniqueId(topicId, id, tag);

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(topicId)) {
            return Observable.just(
                    new Pair<Integer, List<LiveVideoInfo>>(0, new ArrayList<LiveVideoInfo>()));
        }
        return OldRetroAdapter.getService().getLiveVideoList(Integer.parseInt(id), Integer.parseInt(topicId), tag, pageHandler.getPageToCache(uniqueId, loadMore), pageHandler.getItemPerPage())
                .map(ModelBase::getData)
                .compose(pageHandler.apply(uniqueId, loadMore))
                .subscribeOn(Schedulers.io());
    }

    public boolean isNoMoreData(String topicId, String id, String tag) {
        return pageHandler.isNoMoreData(uniqueId(topicId, id, tag));
    }

    private String uniqueId(String topicId, String id, String tag) {
        return topicId + id + tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.pageHandler, 0);
    }

    protected LiveVideoListManager(Parcel in) {
        this.pageHandler = in.readParcelable(PageHandler.class.getClassLoader());
    }

    public static final Parcelable.Creator<LiveVideoListManager> CREATOR = new Parcelable.Creator<LiveVideoListManager>() {
        public LiveVideoListManager createFromParcel(Parcel source) {
            return new LiveVideoListManager(source);
        }

        public LiveVideoListManager[] newArray(int size) {
            return new LiveVideoListManager[size];
        }
    };
}
