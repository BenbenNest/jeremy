package com.jeremy.lychee.model.function.impl;

import android.util.SparseArray;

import com.jeremy.lychee.bean.FunctionListData;
import com.jeremy.lychee.model.function.IFunctionListModel;

import java.util.ArrayList;

/**
 * Created by benbennest on 16/4/25.
 */
public class FunctionListModel implements IFunctionListModel {
    public static final int FUNCTION_CURTAIN = 1000;
    public static final int FUNCTION_BARRAGE = 1001;
    public static final int FUNCTION_NEWS_DETAIL = 1002;
    public static final int FUNCTION_BOOK_READER = 1003;
    public static final int FUNCTION_WAVE_VIEW = 1004;
    public static final int FUNCTION_ANNOTATION = 1005;
    public static final int FUNCTION_MULTI_THREAD_DOWN = 1006;
    public static final int FUNCTION_DYNAMIC = 1007;
    public static final int FUNCTION_AIDL = 1008;


    private String packageName = "com.jeremy.lychee.activity.function.";
    private ArrayList<Integer> mOrderList = new ArrayList<Integer>();
    private SparseArray<FunctionListData> mFunctionList = new SparseArray<FunctionListData>();

    public FunctionListModel() {
        initData();
    }

    @Override
    public SparseArray<FunctionListData> getFunctionList() {
        return mFunctionList;
    }

    private void initData() {

        FunctionListData data = new FunctionListData("窗帘效果", packageName + "CurtainActivity", "窗帘效果", 0);
        mFunctionList.put(FUNCTION_CURTAIN, data);
        mOrderList.add(FUNCTION_CURTAIN);

        data = new FunctionListData("弹幕效果", packageName + "BarrageActivity", "弹幕效果", 0);
        mFunctionList.put(FUNCTION_BARRAGE, data);
        mOrderList.add(FUNCTION_BARRAGE);

//        data = new FunctionListData("新闻详情", packageName + "NewsDetailActivity", "利用WebView和EJS模版实现的新闻详情页面", 0);
        data = new FunctionListData("WebView", packageName + "NewsDetailActivity", "新闻详情", 0);
        mFunctionList.put(FUNCTION_NEWS_DETAIL, data);
        mOrderList.add(FUNCTION_NEWS_DETAIL);

        data = new FunctionListData("电子书阅读", packageName + "BookReaderActivity", "电子书阅读", 0);
        mFunctionList.put(FUNCTION_BOOK_READER, data);
        mOrderList.add(FUNCTION_BOOK_READER);

        data = new FunctionListData("波浪动画", packageName + "WaveViewActivity", "波浪动画", 0);
        mFunctionList.put(FUNCTION_WAVE_VIEW, data);
        mOrderList.add(FUNCTION_WAVE_VIEW);

        data = new FunctionListData("注解", packageName + "AnnotationActivity", "注解", 0);
        mFunctionList.put(FUNCTION_ANNOTATION, data);
        mOrderList.add(FUNCTION_ANNOTATION);

        data = new FunctionListData("多线程断点续传下载", packageName + "MultiThreadDownloadActivity", "多线程断点续传下载", 0);
        mFunctionList.put(FUNCTION_MULTI_THREAD_DOWN, data);
        mOrderList.add(FUNCTION_MULTI_THREAD_DOWN);

        data = new FunctionListData("动态流式布局", packageName + "DynamicLayoutActivity", "动态流式布局", 0);
        mFunctionList.put(FUNCTION_DYNAMIC, data);
        mOrderList.add(FUNCTION_DYNAMIC);

        data = new FunctionListData("AIDL", packageName + "AIDLActivity", "AIDL", 0);
        mFunctionList.put(FUNCTION_AIDL, data);
        mOrderList.add(FUNCTION_AIDL);
    }

    public int getKey(int pos) {
        if (mOrderList != null && mOrderList.size() > pos) {
            return mOrderList.get(pos);
        } else {
            return 0;
        }
    }

    @Override
    public FunctionListData getFunction(int pos) {
        int key = mOrderList.get(pos);
        FunctionListData data = mFunctionList.get(key);
        return data;
    }

}
