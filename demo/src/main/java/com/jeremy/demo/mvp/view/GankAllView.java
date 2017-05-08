package com.jeremy.demo.mvp.view;

import com.jeremy.demo.model.GankList;
import com.jeremy.library.mvp.MvpView;

import io.reactivex.Observable;

/**
 * Created by changqing.zhao on 2017/5/8.
 */

public interface GankAllView extends MvpView {
    void onDataSuccess(Observable<GankList> observable);
}
