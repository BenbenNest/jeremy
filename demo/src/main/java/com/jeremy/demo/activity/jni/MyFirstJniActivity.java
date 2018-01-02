package com.jeremy.demo.activity.jni;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jeremy.demo.R;
import com.jeremy.demo.jni.MyJNI;

public class MyFirstJniActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_first_jni);

        TextView textView = new TextView(this);
        textView.setText(MyJNI.getStringFromC());
        System.out.print("jni:" + MyJNI.getStringFromC());
        setContentView(textView);
    }
}
