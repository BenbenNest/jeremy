package com.jeremy.demo.mvp.presenter;


import com.jeremy.demo.mvp.bean.FunctionData;
import com.jeremy.demo.mvp.model.FunctionListModel;
import com.jeremy.demo.mvp.view.FunctionListView;
import com.jeremy.library.mvp.BasePresenter;

import java.util.ArrayList;

/**
 * Created by benbennest on 16/8/25.
 */
public class FunctionListPresenter extends BasePresenter<FunctionListView> {
    FunctionListModel mFunctionListModel;

    public FunctionListPresenter() {
        mFunctionListModel = new FunctionListModel();
    }

    public void getData() {
        ArrayList<FunctionData> list = mFunctionListModel.getFunctionList();
        FunctionListPresenter.this.getMvpView().onGetDataSuccess(list);
    }

}
