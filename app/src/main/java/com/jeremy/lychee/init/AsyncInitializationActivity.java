package com.jeremy.lychee.init;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;

import com.jeremy.lychee.base.ApiCompatibilityUtils;
import com.jeremy.lychee.utils.ViewServer;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * An activity that talks with application and activity level delegates for async initialization.
 */
public abstract class AsyncInitializationActivity extends RxAppCompatActivity implements InitializationDelegate{

    protected final Handler mHandler;
    private boolean mHierarchyViewDebug = false;

    // Time at which onCreate is called. This is realtime, counted in ms since device boot.
    private long mOnCreateTimestampMs;

    // Time at which onCreate is called. This is uptime, to be sent to native code.
    private long mOnCreateTimestampUptimeMs;
    private Bundle mSavedInstanceState;
    private boolean mDestroyed;
    private InitializationController mInitializationController;

    public AsyncInitializationActivity() {
        mHandler = new Handler();
    }

    @Override
    protected void onDestroy() {
        mDestroyed = true;
        super.onDestroy();
        // m:wbs HierarchyView Debug
        if (mHierarchyViewDebug) {
            mHierarchyViewDebug = false;
            ViewServer.get(this).removeWindow(this);
        }
    }

    private void setContentViewAndLoadLibrary() {
        // setContentView inflating the decorView and the basic UI hierarhcy as stubs.
        // This is done here before kicking long running I/O because inflation includes accessing
        // resource files(xmls etc) even if we are inflating views defined by the framework. If this
        // operation gets blocked because other long running I/O are running, we delay onCreate(),
        // onStart() and first draw consequently.

        onPreInflation();
        setContentView();
        onPostInflation();

        // Kick off long running IO tasks that can be done in parallel.
        mInitializationController = new InitializationController(this, this);
        mInitializationController.startBackgroundTasks();
    }


    @Override
    public abstract void initializeAsync();
    protected abstract void onPreInflation();
    protected abstract void onPostInflation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!isStartedUpCorrectly(getIntent())) {
            super.onCreate(null);
            ApiCompatibilityUtils.finishAndRemoveTask(this);
            return;
        }

        super.onCreate(savedInstanceState);

        mOnCreateTimestampMs = SystemClock.elapsedRealtime();
        mOnCreateTimestampUptimeMs = SystemClock.uptimeMillis();
        mSavedInstanceState = savedInstanceState;

//        // 适配 mx 删除smartbar
//        hideMXSmartBar(getWindow().getDecorView());

        setContentViewAndLoadLibrary();
        //TODO(wangp):HierarchyView Debug
        mHierarchyViewDebug = true;
        ViewServer.get(this).addWindow(this);

        // 禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public static boolean hasSmartBar() {
        try {
            Method method = Class.forName("android.os.Build").getMethod(
                    "hasSmartBar");
            return ((Boolean) method.invoke(null)).booleanValue();
        } catch (Exception e) {
        }

        if (Build.DEVICE.equals("mx2")) {
            return true;
        } else if (Build.DEVICE.equals("mx") || Build.DEVICE.equals("m9")) {
            return false;
        }
        return false;
    }

    @Deprecated
    public static void hideMXSmartBar(View decorView) {
        if (!hasSmartBar()) {
            return;
        }

        try {
            @SuppressWarnings("rawtypes")
            Class[] arrayOfClass = new Class[1];
            arrayOfClass[0] = Integer.TYPE;
            Method localMethod = View.class.getMethod("setSystemUiVisibility", arrayOfClass);
            Field localField = View.class.getField("SYSTEM_UI_FLAG_HIDE_NAVIGATION");
            Object[] arrayOfObject = new Object[1];

            try {
                arrayOfObject[0] = localField.get(null);
            } catch (Exception e) {

            }

            localMethod.invoke(decorView, arrayOfObject);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Whether or not the Activity was started up via a valid Intent.
     */
    protected boolean isStartedUpCorrectly(Intent intent) {
        return true;
    }

    /**
     * @return The elapsed real time for the activity creation in ms.
     */
    protected long getOnCreateTimestampUptimeMs() {
        return mOnCreateTimestampUptimeMs;
    }

    /**
     * @return The uptime for the activity creation in ms.
     */
    protected long getOnCreateTimestampMs() {
        return mOnCreateTimestampMs;
    }

    /**
     * @return The saved bundle for the last recorded state.
     */
    protected Bundle getSavedInstanceState() {
        return mSavedInstanceState;
    }

    /**
     * Resets the saved state and makes it unavailable for the rest of the activity lifecycle.
     */
    protected void resetSavedInstanceState() {
        mSavedInstanceState = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //HierarchyView Debug
        if (mHierarchyViewDebug) {
            ViewServer.get(this).setFocusedWindow(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent == null) return;
        mInitializationController.onNewIntent(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mInitializationController.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean isActivityDestroyed() {
        return mDestroyed;
    }

    @Override
    public void onNewIntentAfterInit(Intent intent) {
    }

    @Override
    public boolean onActivityResultAfterInit(int requestCode, int resultCode, Intent data) {
        return false;
    }

    /**
     * Extending classes should implement this and call {@link Activity#setContentView(int)} in it.
     */
    protected abstract void setContentView();


}
