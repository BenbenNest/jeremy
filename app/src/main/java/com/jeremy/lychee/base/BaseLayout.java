package com.jeremy.lychee.base;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public abstract class BaseLayout extends FrameLayout{

    public BaseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        doInit(context);
    }

    public BaseLayout(Context context){
        super(context);
        doInit(context);
    }

    public void doInit(final Context context){
        onInflate(context);
        ButterKnife.bind(this);
        new Handler().post(() ->
                Observable.just((Activity) context)
                        .compose(((RxAppCompatActivity) context).bindToLifecycle())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::initUI, Throwable::printStackTrace));
    }

    protected abstract void onInflate(Context activity);
    protected abstract void initUI(Activity activity);

}
