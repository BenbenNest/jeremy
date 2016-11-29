package com.jeremy.demos.mvp.presenter;


import com.jeremy.demos.mvp.bean.FunctionData;
import com.jeremy.demos.mvp.model.FunctionListModel;
import com.jeremy.demos.mvp.view.FunctionListView;
import com.jeremy.librarys.mvp.BasePresenter;

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
