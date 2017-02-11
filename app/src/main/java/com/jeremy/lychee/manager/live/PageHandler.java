package com.jeremy.lychee.manager.live;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.Pair;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

@SuppressWarnings("unchecked")
public class PageHandler<T extends List> implements Parcelable {
    private HashMap<String, Integer> pageCached = new HashMap<>();
    private HashMap<String, Boolean> noMoreData = new HashMap<>();

    private static final int DEFAULT_ITEM_PER_PAGE = 10;
    private int itemPerPage;

    public PageHandler() {
        this(DEFAULT_ITEM_PER_PAGE);
    }

    public PageHandler(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

    public Observable.Transformer<T, Pair<Integer, T>> apply(String id, boolean loadMore) {
        return observable -> observable
                .doOnNext(it -> {
                    if (!loadMore) {
                        pageCached.remove(id);
                        noMoreData.remove(id);
                    }
                })
                .map(it -> new Pair<>(getPageToCache(id, loadMore), it))
                .doOnNext(it -> {
                    setPageCached(id, loadMore);
                    noMoreData.put(id, it.second.size() < itemPerPage);
                });
    }

    public boolean isNoMoreData(String id) {
        if (!noMoreData.containsKey(id)) {
            return false;
        }
        return noMoreData.get(id);
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public int getPageToCache(String id, boolean loadMore) {
        if (!loadMore || !pageCached.containsKey(id)) {
            return 1;
        }

        return pageCached.get(id) + 1;
    }

    private void setPageCached(String id, boolean loadMore) {
        pageCached.put(id, getPageToCache(id, loadMore));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(this.pageCached);
        dest.writeMap(this.noMoreData);
        dest.writeInt(this.itemPerPage);
    }

    protected PageHandler(Parcel in) {
        this.pageCached = in.readHashMap(HashMap.class.getClassLoader());
        this.noMoreData = in.readHashMap(HashMap.class.getClassLoader());
        this.itemPerPage = in.readInt();
    }

    public static final Parcelable.Creator<PageHandler> CREATOR = new Parcelable.Creator<PageHandler>() {
        public PageHandler createFromParcel(Parcel source) {
            return new PageHandler(source);
        }

        public PageHandler[] newArray(int size) {
            return new PageHandler[size];
        }
    };
}
