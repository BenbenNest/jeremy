package com.jeremy.demo.mvp.presenter;

import com.jeremy.demo.mvp.model.GankAllModel;
import com.jeremy.demo.mvp.view.GankAllView;
import com.jeremy.library.mvp.BasePresenter;

/**
 * Created by changqing.zhao on 2017/5/8.
 */

public class GankAllPresenter extends BasePresenter<GankAllView> {
    GankAllModel gankAllModel;

    public GankAllPresenter() {
        gankAllModel = new GankAllModel();
    }

//    public void getData() {
//        Observable<GankList> observable = gankAllModel.getGankList(0);
//        getMvpView().onDataSuccess(observable);
//    }


//        RetroAdapter.getService().getAll()
//                .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe(new Observer<GankList>() {
//        @Override
//        public void onSubscribe(Disposable d) {
//            //Disposable是1.x的Subscription改名的，因为Reactive-Streams规范用这个名称，为了避免重复
//            //这个回调方法是在2.0之后新添加的
//            //可以使用d.dispose()方法来取消订阅
//            //由于1.x中Observable不能合理的背压，导致了无法意料的 MissingBackpressureException，所以在2.x中，添加了Flowable来支持背压，而把Observable设计成非背压的。
//            //还有一点需要注意的就是，在上边注释中也有，onSubscribe(Disposable d)这个回调方法是在2.x中添加的，Dispose参数是由1.x中的Subscription改名的，为了避免名称冲突！
//        }
//
//        @Override
//        public void onNext(GankList gankList) {
//            list = gankList;
//        }
//
//        @Override
//        public void onError(Throwable e) {
//
//        }
//
//        @Override
//        public void onComplete() {
//
//        }
//    });


}
