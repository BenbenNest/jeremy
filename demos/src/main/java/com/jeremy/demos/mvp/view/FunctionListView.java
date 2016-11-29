package com.jeremy.demos.mvp.view;


import com.jeremy.demos.mvp.bean.FunctionData;
import com.jeremy.library.mvp.MvpView;

import java.util.ArrayList;

/**
 * Created by benbennest on 16/8/24.
 */
public interface FunctionListView extends MvpView {
    void onGetDataSuccess(ArrayList<FunctionData> list);

}
