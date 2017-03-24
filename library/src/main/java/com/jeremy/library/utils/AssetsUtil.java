package com.jeremy.library.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.AssetManager.AssetInputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.Log;
import android.webkit.WebView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AssetsUtil {
    private static final String TAG = "AssetsUtil";


//    String[] list(String path);//列出该目录下的下级文件和文件夹名称
//
//    InputStream open(String fileName);//以顺序读取模式打开文件，默认模式为ACCESS_STREAMING
//
//    InputStream open(String fileName, int accessMode);//以指定模式打开文件。读取模式有以下几种：
//    //ACCESS_UNKNOWN : 未指定具体的读取模式
//    //ACCESS_RANDOM : 随机读取
//    //ACCESS_STREAMING : 顺序读取
//    //ACCESS_BUFFER : 缓存读取
//    void close()//关闭AssetManager实例

    public static void loadHtml(WebView webView, String filePath) {
        webView.loadUrl("file:///android_asset/html/index.htmll");
    }

    /**
     * 从assets目录中复制整个文件夹内容
     *
     * @param context Context 使用CopyFiles类的Activity
     * @param oldPath String  原文件路径  如：/aa
     * @param newPath String  复制后路径  如：xx:/bb/cc
     */
    public void copyFilesFassets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {//如果是目录
                File file = new File(newPath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //如果捕捉到错误则通知UI线程
//            MainActivity.handler.sendEmptyMessage(COPY_FALSE);
        }
    }

    public static void readMusicAssert(Context context, String music) {
        AssetManager am = context.getAssets();
        // 打开指定音乐文件,获取assets目录下指定文件的AssetFileDescriptor对象
        MediaPlayer mPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor afd = am.openFd(music);
            mPlayer.reset();
// 使用MediaPlayer加载指定的声音文件。
            mPlayer.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
// 准备声音
            mPlayer.prepare();
// 播放
            mPlayer.start();
        } catch (Exception e) {

        }
    }

    public static String getTxtFromAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStream is = context.getAssets().open(fileName);
            int lenght = is.available();
            byte[] buffer = new byte[lenght];
            is.read(buffer);
            result = new String(buffer, "utf8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Bitmap getImgFromAssets(Context context, String fileName) {
        Bitmap bitmap = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            if (is instanceof AssetInputStream) {
                Log.d(TAG, "is instanceof AssetInputStream");
            } else {
                Log.d(TAG, "is not instanceof AssetInputStream");
            }
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
