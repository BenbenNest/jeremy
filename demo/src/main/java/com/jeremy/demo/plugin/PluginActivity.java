package com.jeremy.demo.plugin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jeremy.demo.R;

public class PluginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
        findViewById(R.id.bt_plugin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PluginActivity.this, ProxyActivity.class);
                intent.putExtra(ProxyActivity.EXTRA_DEX_PATH, "/mnt/sdcard/plugin.apk");
                startActivity(intent);
            }
        });
    }

}
