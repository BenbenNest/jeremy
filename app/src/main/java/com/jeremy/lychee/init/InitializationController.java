package com.jeremy.lychee.init;

import android.content.Context;
import android.content.Intent;

import com.jeremy.lychee.utils.ThreadUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class controls the different asynchronous states during our initialization:
 * 1. During startBackgroundTasks(), we'll kick off loading the library and yield the call stack.
 * 2. We may receive a onStart() / onStop() call any point after that, whether or not
 *    the library has been loaded.
 */
public class InitializationController {
    private static final String TAG = "InitializationController";
    private final Context mContext;
    private List<Intent> mPendingNewIntents;
    private List<ActivityResult> mPendingActivityResults;
    private boolean mInitializationComplete;
    private final InitializationDelegate mActivityDelegate;

    /**
     * This class encapsulates a call to onActivityResult that has to be deferred because the native
     * library is not yet loaded.
     */
    static class ActivityResult {
        public final int requestCode;
        public final int resultCode;
        public final Intent data;

        public ActivityResult(int requestCode, int resultCode, Intent data) {
            this.requestCode = requestCode;
            this.resultCode = resultCode;
            this.data = data;
        }
    }

    /**
     * Create the InitializationController using the main loop and the application context.
     * It will be linked back to the activity via the given delegate.
     * @param context The context to pull the application context from.
     * @param activityDelegate The activity delegate for the owning activity.
     */
    public InitializationController(Context context,
                                    InitializationDelegate activityDelegate) {
        mContext = context.getApplicationContext();
        mActivityDelegate = activityDelegate;
    }
    /**
     * Start loading the native library in the background. This kicks off the native initialization
     * process.
     */
    public void startBackgroundTasks() {
        mInitializationComplete = false;
        // TODO(yusufo) : Investigate using an AsyncTask for this.
        new Thread() {
            @Override
            public void run() {
                if (mActivityDelegate == null ||
                            mActivityDelegate.isActivityDestroyed()) {
                    return;
                }
                mActivityDelegate.initializeAsync();
                mActivityDelegate.onInitializeComplete();
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onLibraryLoaded();
                    }
                });
            }
        }.start();
    }

    private void onLibraryLoaded() {
        mInitializationComplete = true;
        if (mActivityDelegate == null ||
                    mActivityDelegate.isActivityDestroyed()) {
            return;
        }
        mActivityDelegate.onInitializeComplete();
        startNowAndProcessPendingItems();
    }
    /**
     * Called when an activity gets an onNewIntent call and is done with java only tasks.
     * @param intent The intent that has arrived to the activity linked to the given delegate.
     */
    public void onNewIntent(Intent intent) {
        if (mInitializationComplete) {
            mActivityDelegate.onNewIntentAfterInit(intent);
        } else {
            if (mPendingNewIntents == null) mPendingNewIntents = new ArrayList<Intent>(1);
            mPendingNewIntents.add(intent);
        }
    }

    /**
     * This is the Android onActivityResult callback deferred, if necessary,
     * to when the native library has loaded.
     * @param requestCode The request code for the ActivityResult.
     * @param resultCode The result code for the ActivityResult.
     * @param data The intent that has been sent with the ActivityResult.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mInitializationComplete) {
            mActivityDelegate.onActivityResultAfterInit(requestCode, resultCode, data);
        } else {
            if (mPendingActivityResults == null) {
                mPendingActivityResults = new ArrayList<ActivityResult>(1);
            }
            mPendingActivityResults.add(new ActivityResult(requestCode, resultCode, data));
        }
    }

    private void startNowAndProcessPendingItems() {
        // onNewIntent and onActivityResult are called only when the activity is paused.
        // To match the non-deferred behavior, onStart should be called before any processing
        // of pending intents and activity results.
        // Note that if we needed ChromeActivityNativeDelegate.onResumeWithNative(), the pending
        // intents and activity results processing should have happened in the corresponding
        // resumeNowAndProcessPendingItems, just before the call to
        // ChromeActivityNativeDelegate.onResumeWithNative().

        if (mPendingNewIntents != null) {
            for (Intent intent : mPendingNewIntents) {
                mActivityDelegate.onNewIntentAfterInit(intent);
            }
            mPendingNewIntents = null;
        }

        if (mPendingActivityResults != null) {
            ActivityResult activityResult;
            for (int i = 0; i < mPendingActivityResults.size(); i++) {
                activityResult = mPendingActivityResults.get(i);
                mActivityDelegate.onActivityResultAfterInit(activityResult.requestCode,
                                                                    activityResult.resultCode, activityResult.data);
            }
            mPendingActivityResults = null;
        }
    }

}
