package com.jeremy.demo.mvp.view;

import com.jeremy.demo.mvp.bean.FunctionData;
import com.jeremy.library.mvp.MvpView;

import java.util.ArrayList;

/**
 * Created by benbennest on 16/8/24.
 */
public interface FunctionListView extends MvpView {
    void onGetDataSuccess(ArrayList<FunctionData> list);

}
