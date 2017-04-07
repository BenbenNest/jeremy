package com.jeremy.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.jeremy.demo.R;
import com.jeremy.inject.ViewInject;
import com.jeremy.viewinject.annotation.BindView;
import com.jeremy.viewinject.annotation.OnClick;

public class AnnotationActivity extends AppCompatActivity {

    @BindView(R.id.btn_annotation)
    Button btn_annotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation);
        ViewInject.inject(this);

    }

    @OnClick(R.id.btn_annotation)
    public void click() {
        Toast.makeText(getApplicationContext(), "我是注解的OnClick事件", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    //    /**
//     * 获取直播状态
//     * @param id
//     * @return
//     */
//    @GET("/api/1/content/{id}")
//    Call<BaseModel> getLiveState(@Path("id") String id);


}
