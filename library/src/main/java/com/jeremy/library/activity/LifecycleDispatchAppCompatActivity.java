package com.jeremy.library.activity;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by benbennest on 17/2/14.
 */

public class LifecycleDispatchAppCompatActivity extends AppCompatActivity {
    public LifecycleDispatchAppCompatActivity() {
    }

//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(ApplicationHelper.PRE_ICS) {
//            MainLifecycleDispatcher.get().onActivityCreated(this, savedInstanceState);
//        }
//
//    }
//
//    protected void onStart() {
//        super.onStart();
//        if(ApplicationHelper.PRE_ICS) {
//            MainLifecycleDispatcher.get().onActivityStarted(this);
//        }
//
//    }
//
//    protected void onResume() {
//        super.onResume();
//        if(ApplicationHelper.PRE_ICS) {
//            MainLifecycleDispatcher.get().onActivityResumed(this);
//        }
//
//    }
//
//    protected void onPause() {
//        super.onPause();
//        if(ApplicationHelper.PRE_ICS) {
//            MainLifecycleDispatcher.get().onActivityPaused(this);
//        }
//
//    }
//
//    protected void onStop() {
//        super.onStop();
//        if(ApplicationHelper.PRE_ICS) {
//            MainLifecycleDispatcher.get().onActivityStopped(this);
//        }
//
//    }
//
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        if(ApplicationHelper.PRE_ICS) {
//            MainLifecycleDispatcher.get().onActivitySaveInstanceState(this, outState);
//        }
//
//    }
//
//    protected void onDestroy() {
//        super.onDestroy();
//        if(ApplicationHelper.PRE_ICS) {
//            MainLifecycleDispatcher.get().onActivityDestroyed(this);
//        }
//
//    }
}
