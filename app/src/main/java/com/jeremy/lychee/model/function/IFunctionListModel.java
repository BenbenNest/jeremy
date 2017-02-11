package com.jeremy.lychee.model.function;

import android.util.SparseArray;

import com.jeremy.lychee.bean.FunctionListData;

/**
 * Created by benbennest on 16/4/25.
 */
public interface IFunctionListModel {
    SparseArray<FunctionListData> getFunctionList();

    FunctionListData getFunction(int pos);
}
