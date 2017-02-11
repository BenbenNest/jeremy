package com.jeremy.lychee.manager.live;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.support.v4.util.Pair;
import android.widget.Toast;

import com.jeremy.lychee.activity.news.CommentActivity;
import com.jeremy.lychee.activity.user.LoginActivity;
import com.jeremy.lychee.activity.user.TransmitActivity;
import com.jeremy.lychee.base.Constants;
import com.jeremy.lychee.base.ContentApplication;
import com.jeremy.lychee.db.ThumbsUp;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.model.ShareInfo;
import com.jeremy.lychee.model.live.LiveVideoInfo;
import com.jeremy.lychee.widget.shareWindow.ShareManager;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class LiveVideoManager {
    public static final int TRANSMIT_REQ_CODE = 1;
    public static final int VIEW_COMMENT_REQ_CODE = 2;

    private Subject<Pair<Integer, Object>, Pair<Integer, Object>> callbackSubject;

    @IntDef({LiveVideoCallback.ON_PRE_THUMBS_UP, LiveVideoCallback.ON_POST_THUMBS_UP, LiveVideoCallback.ON_PRE_COMMENT,
            LiveVideoCallback.ON_POST_COMMENT, LiveVideoCallback.ON_PRE_SHARE_VIDEO, LiveVideoCallback.ON_POST_SHARE_VIDEO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LiveVideoCallback {
        int ON_PRE_THUMBS_UP = 1;
        int ON_POST_THUMBS_UP = 2;
        int ON_PRE_COMMENT = 3;
        int ON_POST_COMMENT = 4;
        int ON_PRE_SHARE_VIDEO = 5;
        int ON_POST_SHARE_VIDEO = 6;
    }

    static private LiveVideoManager instance = new LiveVideoManager();

    static public LiveVideoManager getInstance() {
        return instance;
    }

    private LiveVideoManager() {
        callbackSubject = new SerializedSubject<>(PublishSubject.create());
    }

    public Object registerCallback(int type, Action1<Object> callback) {
        if (callbackSubject == null) {
            return null;
        }

        return callbackSubject.asObservable()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(it -> type == it.first)
                .map(it -> it.second)
                .subscribe(callback);
    }

    public void unRegisterCallback(Object o) {
        Subscription subscription = (Subscription) o;
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public boolean isLogin() {
        return !Session.isUserInfoEmpty();
    }

    public void login(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public void thumbsUp(Context context, LiveVideoInfo videoInfo) {
        if (isThumbsUped(videoInfo)) {
            Toast.makeText(context, com.jeremy.lychee.R.string.comment_digged, Toast.LENGTH_SHORT).show();
            return;
        }

        callbackSubject.onNext(new Pair<>(LiveVideoCallback.ON_PRE_THUMBS_UP, videoInfo));

        Observable<ModelBase> observable = OldRetroAdapter.getService().punch(videoInfo.getVideo_key())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());

        if (context instanceof RxAppCompatActivity) {
            observable = observable.compose(((RxAppCompatActivity) context).bindToLifecycle());
        }

        observable
                .doOnNext(it -> ContentApplication.getDaoSession().getThumbsUpDao().insert(new ThumbsUp(videoInfo.getId(), true)))
                .subscribe(it -> {
                    videoInfo.setDing(String.format("%d", Integer.parseInt(videoInfo.getDing()) + 1));
                    callbackSubject.onNext(new Pair<>(LiveVideoCallback.ON_POST_THUMBS_UP, videoInfo));
                }, Throwable::printStackTrace);
    }

    public boolean isThumbsUped(LiveVideoInfo videoInfo) {
        try {
            ThumbsUp thumbsUpRecord = ContentApplication.getDaoSession().getThumbsUpDao().load(videoInfo.getId());
            return thumbsUpRecord != null;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public void comment(Context context, LiveVideoInfo videoInfo) {
        if (isLogin()) {
            callbackSubject.onNext(new Pair<>(LiveVideoCallback.ON_PRE_COMMENT, videoInfo));
            Intent intent = new Intent(context, TransmitActivity.class)
                    .putExtra(Constants.BUNDLE_KEY_NID, videoInfo.getId())//转推用ID
                    .putExtra(Constants.BUNDLE_KEY_COMMENT_NID, videoInfo.getVideo_key())//评论用ID
                    .putExtra(Constants.BUNDLE_KEY_TYPE, 2)
                    .putExtra(CommentActivity.COMMENTS_TITLE, videoInfo.getVideo_name());
            ((Activity) context).startActivityForResult(intent, TRANSMIT_REQ_CODE);
            callbackSubject.onNext(new Pair<>(LiveVideoCallback.ON_POST_COMMENT, videoInfo));
        } else {
            login(context);
        }
    }

    public void shareVideo(Context context, LiveVideoInfo videoInfo) {
        if (isLogin()) {
            callbackSubject.onNext(new Pair<>(LiveVideoCallback.ON_PRE_SHARE_VIDEO, videoInfo));

            ShareInfo shareInfo = new ShareInfo(videoInfo.getShare(),
                    videoInfo.getId(),
                    videoInfo.getVideo_name(),
                    videoInfo.getVideo_column_name(),
                    videoInfo.getVideo_img(),
                    null,
                    ShareInfo.SHARECONTENT_LIVE);

            new ShareManager((Activity) context, shareInfo, true,
                    () -> {/*TODO：打点*/})
                    .show().setOnDialogDismissListener(dialog -> {
                callbackSubject.onNext(new Pair<>(LiveVideoCallback.ON_POST_SHARE_VIDEO, videoInfo));
            });
        } else {
            login(context);
        }
    }

    public void viewComment(Context context, LiveVideoInfo videoInfo) {
        if (isLogin()) {
            CommentActivity.startActivity(context, VIEW_COMMENT_REQ_CODE, 2, videoInfo.getVideo_key(), videoInfo.getVideo_name(), videoInfo.getId(), videoInfo.getNews_from());
        } else {
            login(context);
        }
    }
}
