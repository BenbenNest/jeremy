package com.jeremy.lychee;

import android.app.Activity;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Scroller;

import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;

import java.util.concurrent.Executors;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String str = new String("abc");
        System.out.println(str.getClass().getClassLoader());
        ClassLoader classLoader = MainActivity.this.getClassLoader();
        do {
            System.out.println(classLoader);
            classLoader = classLoader.getParent();
        } while (classLoader != null);
        System.out.println(Button.class.getClassLoader());

        BlockCanary.install(this, new BlockCanaryContext()).start();
        Scroller
    }
}
