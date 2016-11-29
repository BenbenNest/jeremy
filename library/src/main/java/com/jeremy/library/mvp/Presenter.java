package com.jeremy.library.mvp;

/**
 * Created by benbennest on 16/8/24.
 */
public interface Presenter<V extends MvpView> {
    void attachView(V MvpView);

    void detachView();
}
