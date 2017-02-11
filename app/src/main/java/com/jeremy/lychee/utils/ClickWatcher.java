package com.jeremy.lychee.utils;

import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by wangp on 15-12-11.
 */
public class ClickWatcher {
    private int mInterval;
    private ICallBack mCb;
    private Subject<Void, Void> mSubject =
            new SerializedSubject<>(PublishSubject.create());
    {
        mSubject.asObservable()
                .subscribeOn(AndroidSchedulers.mainThread())
                .timeInterval(AndroidSchedulers.mainThread())
                .filter(it -> it.getIntervalInMilliseconds() > mInterval)
                .subscribe(it -> {
                    if (mCb != null) mCb.run();
                });
    }

    public ClickWatcher(int interval) {
        mInterval = interval;
    }

    public ClickWatcher setInterval(int interval){
        mInterval = interval;
        return this;
    }

    public void doClick(ICallBack cb) {
        mCb = cb;
        mSubject.onNext(null);
    }

    public interface ICallBack {
        void run();
    }
}


