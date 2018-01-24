package com.jeremy.hotfix;

import android.app.Application;
import android.os.Environment;

/**
 * Created by changqing on 2018/1/8.
 */

public class HotPatchApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        HotPatch.init(this);
        // 获取补丁，如果存在就执行注入操作
        String dexPath = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/patch_dex.jar");
        HotPatch.inject(dexPath);
    }


}
