package com.jeremy.lychee.init;

import android.content.Intent;

public interface InitializationDelegate {

    void onInitializeStart();
    void onInitializeComplete();
    void onInitializeFailure();
    boolean isActivityDestroyed();
    void onNewIntentAfterInit(Intent intent);
    boolean onActivityResultAfterInit(int requestCode, int resultCode, Intent data);
    void initializeAsync();

}