package com.jeremy.lychee.presenter.function;

import android.util.SparseArray;

import com.jeremy.lychee.bean.FunctionListData;
import com.jeremy.lychee.core.mvp.BasePresenter;
import com.jeremy.lychee.model.function.IFunctionListModel;
import com.jeremy.lychee.model.function.impl.FunctionListModel;
import com.jeremy.lychee.view.function.FunctionListView;

/**
 * Created by benbennest on 16/4/25.
 */
public class FunctionListPresenter extends BasePresenter<FunctionListView> {

    IFunctionListModel mFunctionListModel;

    public FunctionListPresenter() {
        mFunctionListModel = new FunctionListModel();
    }

    public void getData() {
        SparseArray<FunctionListData> list = mFunctionListModel.getFunctionList();
        FunctionListPresenter.this.getMvpView().onGetDataSuccess(list);
//        Observable.just(list).map(new Func1<SparseArray<FunctionListData>, Object>() {
//            @Override
//            public Object call(SparseArray<FunctionListData> functionListDataSparseArray) {
//                return null;
//            }
//        }).subscribe(new Subscriber<Object>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Object o) {
//
//            }
//        });


//        recylerview.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
//        recylerview.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        recylerview.setLayoutManager(layoutManager);
//        FunctionAdapter adapter = new FunctionAdapter(mFunctionPresenter.getFunctionList());
////        Observable.just(mFunctionPresenter.getFunctionList())
////                .map(list -> new FunctionAdapter(list))
////                .subscribe(adapter -> recylerview.setAdapter(adapter));
//        adapter.setOnRecyclerViewListener(this);
//        recylerview.setAdapter(adapter);

    }

}
