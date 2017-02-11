package com.jeremy.lychee.view.function;

import android.util.SparseArray;

import com.jeremy.lychee.bean.FunctionListData;
import com.jeremy.lychee.core.mvp.MvpView;

/**
 * Created by benbennest on 16/4/22.
 */
public interface FunctionListView extends MvpView {
    void onGetDataSuccess(SparseArray<FunctionListData> list);
}
