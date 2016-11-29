package com.jeremy.demos.mvp.model;


import com.jeremy.demos.mvp.bean.FunctionData;

import java.util.ArrayList;

/**
 * Created by benbennest on 16/8/24.
 */
public class FunctionListModel {

    public static final int FUNCTION_CURTAIN = 1000;
    public static final int FUNCTION_BARRAGE = 1001;
    public static final int FUNCTION_NEWS_DETAIL = 1002;
    public static final int FUNCTION_BOOK_READER = 1003;
    public static final int FUNCTION_WAVE_VIEW = 1004;
    public static final int FUNCTION_ANNOTATION = 1005;


    private String packageName = "com.jeremy.demo.activity.";
    private ArrayList<FunctionData> list = new ArrayList<>();

    public FunctionListModel() {
        initData();
    }

    public ArrayList<FunctionData> getFunctionList() {
        return list;
    }

    private void initData() {
        FunctionData data = new FunctionData("友盟推送", packageName + "YouMengActivity", "友盟推送", 0);
        list.add(data);

        data = new FunctionData("APP进程间通信", packageName + "AIDLActivity", "使用AIDL完成APP内和APP间进程通信", 0);
        list.add(data);

//        FunctionData data = new FunctionData("窗帘效果", packageName + "CurtainActivity", "窗帘效果", 0);
//        mFunctionList.put(FUNCTION_CURTAIN, data);
//        mOrderList.add(FUNCTION_CURTAIN);
//
//        data = new FunctionData("弹幕效果", packageName + "BarrageActivity", "弹幕效果", 0);
//        mFunctionList.put(FUNCTION_BARRAGE, data);
//        mOrderList.add(FUNCTION_BARRAGE);
//
////        data = new FunctionData("新闻详情", packageName + "NewsDetailActivity", "利用WebView和EJS模版实现的新闻详情页面", 0);
//        data = new FunctionData("WebView", packageName + "NewsDetailActivity", "新闻详情", 0);
//        mFunctionList.put(FUNCTION_NEWS_DETAIL, data);
//        mOrderList.add(FUNCTION_NEWS_DETAIL);
//
//        data = new FunctionData("电子书阅读", packageName + "BookReaderActivity", "电子书阅读", 0);
//        mFunctionList.put(FUNCTION_BOOK_READER, data);
//        mOrderList.add(FUNCTION_BOOK_READER);
//
//        data = new FunctionData("波浪动画", packageName + "WaveViewActivity", "波浪动画", 0);
//        mFunctionList.put(FUNCTION_WAVE_VIEW, data);
//        mOrderList.add(FUNCTION_WAVE_VIEW);
//
//        data = new FunctionData("注解", packageName + "AnnotationActivity", "注解", 0);
//        mFunctionList.put(FUNCTION_ANNOTATION, data);
//        mOrderList.add(FUNCTION_ANNOTATION);
//
//        data = new FunctionData("多线程断点续传下载", packageName + "MultiThreadDownloadActivity", "多线程断点续传下载", 0);
//        mFunctionList.put(FUNCTION_ANNOTATION, data);
//        mOrderList.add(FUNCTION_ANNOTATION);
//
//        data = new FunctionData("动态流式布局", packageName + "DynamicLayoutActivity", "动态流式布局", 0);
//        mFunctionList.put(FUNCTION_ANNOTATION, data);
//        mOrderList.add(FUNCTION_ANNOTATION);
    }

    public FunctionData getFunction(int position) {
        return list.get(position);
    }

}
