package com.jeremy.demo.skin.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.jeremy.demo.R;
import com.jeremy.demo.skin.SkinManager;
import com.jeremy.demo.skin.test.skin.Skin;
import com.jeremy.demo.skin.test.skin.SkinUtils;
import com.jeremy.library.utils.StorageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class SkinActivity extends Activity {

    String TAG = "SkinActivity";
    /**
     * 从服务器拉取的皮肤表
     */
    List<Skin> skins = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);
        skins.add(new Skin("1b2f2c216fa46226158f7845b16b126b", "1111111.skin", "app-skin-debug" +
                ".apk"));
    }

    /**
     * 下载皮肤包
     */
    private void selectSkin(Skin skin) {
        File theme = new File(StorageUtils.getFilesDir(this), "theme");
        if (theme.exists() && theme.isFile()) {
            theme.delete();
//            Files.delete(theme.getAbsolutePath());
        }
        theme.mkdirs();
        File skinFile = skin.getSkinFile(theme);
        if (skinFile.exists()) {
            Log.e(TAG, "皮肤已存在,开始换肤");
            skinFile.delete();
//            return;
        }
        Log.e(TAG, "皮肤不存在,开始下载");
        //临时文件
        File tempSkin = new File(skinFile.getParentFile(), skin.name + ".temp");
        try (FileOutputStream fos = new FileOutputStream(tempSkin); InputStream is = getAssets().open(skin.url)) {
            //假设下载皮肤包
            byte[] bytes = new byte[10240];
            int len;
            while ((len = is.read(bytes)) != -1) {
                fos.write(bytes, 0, len);
            }
            //下载成功，将皮肤包信息insert已下载数据库
            Log.e(TAG, "皮肤包下载完成开始校验");
            //皮肤包的md5校验 防止下载文件损坏(但是会减慢速度。从数据库查询已下载皮肤表数据库中保留md5字段)
            if (TextUtils.equals(SkinUtils.getSkinMD5(tempSkin), skin.md5)) {
                Log.e(TAG, "校验成功,修改文件名。");
                tempSkin.renameTo(skinFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void change(View view) {
        //使用第0个皮肤
        Skin skin = skins.get(0);
        selectSkin(skin);
        //换肤
        SkinManager.getInstance().loadSkin(skin.path);
    }

    public void restore(View view) {
        SkinManager.getInstance().loadSkin(null);
    }
}
