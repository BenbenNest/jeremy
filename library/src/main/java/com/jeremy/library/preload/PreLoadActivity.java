package com.jeremy.library.preload;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 使用预加载方式
 */
public class PreLoadActivity extends AppCompatActivity {

    private PreLoader<String> preLoader;
    private TextView textView;
//    TimeWatcher allTime;
    private TextView logTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //开始总计时
//        allTime = TimeWatcher.obtainAndStart("total");
        //启动预加载
        preLoader = PreLoader.preLoad(loader, listener);
        //开始布局初始化的计时
//        TimeWatcher timeWatcher = TimeWatcher.obtainAndStart("init layout");
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setTitle("使用PreLoader");
        try {
            //模拟耗时
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(PreLoadActivity.this, RxPreLoadActivity.class));
//            }
//        });
//        textView = (TextView)findViewById(R.id.textView);
//        logTextView = (TextView)findViewById(R.id.log);
//        //UI布局初始化计时结束
//        String s = timeWatcher.stopAndPrint();
//        logTextView.append(s + "\n");
        //初始化完成，可以在UI上显示加载的数据
        preLoader.readyToGetData();
    }

    /**
     * 数据加载
     */
    PreLoader.Loader<String> loader = new PreLoader.Loader<String>() {
        @Override
        public String load() {
//            TimeWatcher timeWatcher = TimeWatcher.obtainAndStart("load data");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            String time = timeWatcher.stopAndPrint();
            return "result:" ;
        }
    };
    /**
     * 数据显示
     */
    PreLoader.Listener<String> listener = new PreLoader.Listener<String>() {
        @Override
        public void onDataArrived(String s) {
            textView.setText(s);
            //总耗时结束
//            String total = allTime.stopAndPrint();
//            logTextView.append(s + "\n" + total + "\n");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        preLoader.destroy();
    }
}
