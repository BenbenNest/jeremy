package com.jeremy.lychee.activity.function;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jeremy.lychee.R;
import com.jeremy.lychee.core.BaseToolbarActivity;
import com.jeremy.lychee.widget.BarrageView;

import java.lang.ref.WeakReference;
import java.util.Random;

import butterknife.Bind;

/**
 * Created by benbennest on 16/6/2.
 */
public class BarrageActivity extends BaseToolbarActivity implements BarrageView.OnRollEndListener {

    @Bind(R.id.danmu_layout)
    FrameLayout danmuLayout;

    //两两弹幕之间的间隔时间
    public static final int DELAY_TIME = 500;

    /**
     * 标签：程序是否处于暂停状态
     * 测试按Home后一分钟以上回到程序会发生满屏线程阻塞
     */
    private boolean isOnPause = false;

    private Random random = new Random();
    private MyHandler handler;
    Runnable createBarrageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ButterKnife.bind(this);

        setTitle("弹幕效果");
        //读取文字资源
        final String[] texts = getResources().getStringArray(R.array.default_text_array);

        //设置宽高全屏
        final ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        handler = new MyHandler(this);
        createBarrageView = new Runnable() {
            @Override
            public void run() {
                if (!isOnPause) {
                    //新建一条弹幕，并设置文字
                    final BarrageView barrageView = new BarrageView(BarrageActivity.this);
                    barrageView.setText(texts[random.nextInt(texts.length)]); //随机设置文字
                    danmuLayout.addView(barrageView, lp);
//                    addContentView(barrageView, lp);
                }
                //发送下一条消息
                handler.postDelayed(this, DELAY_TIME);
            }
        };
        handler.post(createBarrageView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOnPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(createBarrageView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_barrage;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onRollEnd() {

    }

//    @Override
//    public void onRollEnd(BarrageView view) {
//        danmuLayout.removeView(view);
//    }

    private static class MyHandler extends Handler {
        private WeakReference<BarrageActivity> ref;

        MyHandler(BarrageActivity ac) {
            ref = new WeakReference<>(ac);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                BarrageActivity ac = ref.get();
                if (ac != null) {

                }
            }
        }
    }

}
