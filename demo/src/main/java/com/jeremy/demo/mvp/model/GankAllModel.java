package com.jeremy.demo.mvp.model;

import com.jeremy.demo.model.GankList;
import com.jeremy.demo.net.RetroAdapter;

import io.reactivex.Observable;

/**
 * Created by changqing.zhao on 2017/5/8.
 */

public class GankAllModel {

    public Observable<GankList> getGankList(int page) {
        //        ToastUtils.showCenter(this, "test");\
        return RetroAdapter.getService().getAll();
    }

    
}
