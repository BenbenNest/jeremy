package com.jeremy.library.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;


//场景1：华为手机遮挡了屏幕底部。
//
//        场景2：进入应用时，虚拟键自动缩回，留下空白区域。
//
//        需求：
//
//        需要安卓能自适应底部虚拟按键，用户隐藏虚拟按键时应用要占满整个屏幕，当用户启用虚拟键时，应用能往上收缩，等于是被底部虚拟按键顶上来。
//
//        需求很简单，实现起来却困难重重。
//
//        完美解决方案：
//
//        解释一下下面的代码，就是监听某个视图的变化，当可以看见的高度发生变化时，就对这个视图重新布局，保证视图不会被遮挡，也不会浪费屏幕空间。这一点尤其可用在像华为手机等可以隐藏和显示虚拟键盘上导致屏幕变化的手机上。
//然后在你需要解决这个问题的Activity的onCreate方法的setContentView(R.layout.content_frame);后面添加上
//setContentView(R.layout.content_frame);
//        AndroidBug54971Workaround.assistActivity(findViewById(android.R.id.content));

/**
 * Created by changqing.zhao on 2017/3/16.
 */
public class AndroidBug54971Workaround {
    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

    /**
     * 关联要监听的视图
     *
     * @param viewObserving
     */
    public static void assistActivity(View viewObserving) {
        new AndroidBug54971Workaround(viewObserving);
    }

    private View mViewObserved;//被监听的视图
    private int usableHeightPrevious;//视图变化前的可用高度
    private ViewGroup.LayoutParams frameLayoutParams;

    private AndroidBug54971Workaround(View viewObserving) {
        mViewObserved = viewObserving;
        //给View添加全局的布局监听器
        mViewObserved.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                resetLayoutByUsableHeight(computeUsableHeight());
            }
        });
        frameLayoutParams = mViewObserved.getLayoutParams();
    }

    private void resetLayoutByUsableHeight(int usableHeightNow) {
        //比较布局变化前后的View的可用高度
        if (usableHeightNow != usableHeightPrevious) {
            //如果两次高度不一致
            //将当前的View的可用高度设置成View的实际高度
            frameLayoutParams.height = usableHeightNow;
            mViewObserved.requestLayout();//请求重新布局
            usableHeightPrevious = usableHeightNow;
        }
    }

    /**
     * 计算视图可视高度
     *
     * @return
     */
    private int computeUsableHeight() {
        Rect r = new Rect();
        mViewObserved.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }
}
