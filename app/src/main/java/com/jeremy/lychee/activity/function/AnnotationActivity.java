package com.jeremy.lychee.activity.function;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jeremy.lychee.R;

public class AnnotationActivity extends AppCompatActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AnnotationActivity.class);
        context.startActivity(intent);
    }

//    @MyAnnotation("MyAnnotation:onCreate")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation);
    }


}
