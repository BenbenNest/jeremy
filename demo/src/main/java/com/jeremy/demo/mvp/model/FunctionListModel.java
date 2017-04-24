package com.jeremy.demo.mvp.model;


import com.jeremy.demo.mvp.bean.FunctionData;

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
        FunctionData data;

        data = new FunctionData("Fragment监听返回键", "com.jeremy.demo.fragmentwithback.FragmentWithBackActivity", "Fragment监听返回键", 0);
        list.add(data);

        data = new FunctionData("DroidPlugin插件", packageName + "DroidPluginActivity", "DroidPlugin插件", 0);
        list.add(data);

        data = new FunctionData("数字签名", packageName + "DigtalSignActivity", "数字签名", 0);
        list.add(data);

        data = new FunctionData("摇一摇", packageName + "ShakeActivity", "摇一摇", 0);
        list.add(data);

        data = new FunctionData("拍照裁剪", packageName + "PickPhotoActivity", "拍照裁剪", 0);
        list.add(data);

        data = new FunctionData("WebView视频全屏", packageName + "VideoFullScreenActivity", "WebView视频全屏", 0);
        list.add(data);

        data = new FunctionData("回弹效果", packageName + "SpringBackActivity", "回弹效果", 0);
        list.add(data);

        data = new FunctionData("APP检测更新自动升级", packageName + "CheckUpdateActivity", "APP检测更新自动升级", 0);
        list.add(data);

        data = new FunctionData("WebView中长按图片保存", packageName + "WebViewActivity", "WebView中长按图片保存", 0);
        list.add(data);

        data = new FunctionData("心形气泡点赞效果", packageName + "BubbleHeartActivity", "心形气泡点赞效果", 0);
        list.add(data);

        data = new FunctionData("饼状图", packageName + "PieChartActivity", "饼状图", 0);
        list.add(data);

        data = new FunctionData("仿遥控器按钮", packageName + "CustomViewActivity", "仿遥控器按钮", 0);
        list.add(data);

        data = new FunctionData("H5拉起APP", packageName + "WakeUpAppActivity", "通过H5页面拉起App", 0);
        list.add(data);

        data = new FunctionData("HTTPS", packageName + "HttpsActivity", "Android实现Https功能", 0);
        list.add(data);

//        data = new FunctionData("子线程更新UI", packageName + "ThreadActivity", "Android子线程更新UI", 0);
//        list.add(data);

        data = new FunctionData("Android重力感应动画", packageName + "SenserActivity", "Android重力感应动画", 0);
        list.add(data);

        data = new FunctionData("运行时注解实现ButterKnife的ViewInject功能", packageName + "ViewInjectActivity", "基于运行时注解的ViewInject框架", 0);
        list.add(data);

        data = new FunctionData("编译时注解框架", packageName + "AnnotationActivity", "基于注解的ViewInject框架", 0);
        list.add(data);

        data = new FunctionData("APP进程间通信", packageName + "AIDLActivity", "使用AIDL完成APP内和APP间进程通信", 0);
        list.add(data);

        data = new FunctionData("友盟推送", packageName + "YouMengActivity", "友盟推送", 0);
        list.add(data);

        data = new FunctionData("数字签名", packageName + "DigtalSignActivity", "数字签名", 0);
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
