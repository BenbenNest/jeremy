package com.jeremy.demo.jacoco;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.jeremy.demo.activity.SplashActivity;
import com.jeremy.library.utils.StorageUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by changqing on 2018/7/9.
 */

public class JacocoInstrumentation extends Instrumentation {
    public static String TAG = "JacocoInstrumentation:";
//    private static String DEFAULT_COVERAGE_FILE_PATH = "/mnt/sdcard/coverage/coverage.ec";

    private static JacocoInstrumentation sInstrumentation;

    private final Bundle mResults = new Bundle();

    private Intent mIntent;

    private boolean mCoverage = true;

    private String mCoverageFilePath;

    public JacocoInstrumentation() {

    }

    /**
     * Called when the instrumentation is starting, before any application code
     * has been loaded.  Usually this will be implemented to simply call
     * {@link #start} to begin the instrumentation thread, which will then
     * continue execution in
     */
    @Override
    public void onCreate(Bundle arguments) {
        Log.d(TAG, "onCreate(" + arguments + ")");
        super.onCreate(arguments);
        sInstrumentation = this;
        if (arguments != null) {
            //参数:命令行里输入 -e coverage true -e coverageFile ....
            mCoverage = getBooleanArgument(arguments, "coverage");
            mCoverageFilePath = arguments.getString("coverageFile");
        }
        //将第一个加载的activity设置给intent
        mIntent = new Intent(getTargetContext(), getLaunchActiviyClass());
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start();
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();
        Looper.prepare();
        //与第一个加载的activity同步启动
        startActivitySync(mIntent);
    }

    public static void generateJacocoReport() {
        if (sInstrumentation != null) {
            sInstrumentation.startGenerateCoverageReport();
        }
    }

    /**
     * 获取dos中输入的Boolean参数，如果没有输入则返回false
     *
     * @param arguments
     * @param tag
     * @return
     */
    private boolean getBooleanArgument(Bundle arguments, String tag) {
        String tagString = arguments.getString(tag);
        return tagString != null && Boolean.parseBoolean(tagString);
//        return true;
    }


    /**
     * 获得文件路径，可自定义
     *
     * @return
     */
    private String getCoverageFilePath() {
        if (mCoverageFilePath == null) {
            return StorageUtils.getCoverageFile(getContext()).getAbsolutePath();
        } else {
            return mCoverageFilePath;
        }
    }

    /**
     * 产生报告，Out=new FileOutputStream(path,true)
     * 第二个参数一定要为true，否则不能持续收集，每次都会覆盖掉上一次
     */
    private void generateCoverageReport() {
        Log.d(TAG, "generateCoverageReport():" + getCoverageFilePath());
        OutputStream out = null;
        try {
            out = new FileOutputStream(getCoverageFilePath(), true);
            Object agent = Class.forName("org.jacoco.agent.rt.RT")
                    .getMethod("getAgent")
                    .invoke(null);
            out.write((byte[]) agent.getClass().getMethod("getExecutionData", boolean.class)
                    .invoke(agent, false));
        } catch (Exception e) {
            Log.d(TAG, e.toString(), e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean setCoverageFilePath(String filePath) {
        if (filePath != null && filePath.length() > 0) {
            mCoverageFilePath = filePath;
            return true;
        }
        return false;
    }

    private void startGenerateCoverageReport() {
        Log.d(TAG, "onActivityFinished()");
        if (mCoverage) {
            generateCoverageReport();
        }
        finish(Activity.RESULT_OK, mResults);
    }

    protected Class<?> getLaunchActiviyClass() {
        return SplashActivity.class;
    }

}
