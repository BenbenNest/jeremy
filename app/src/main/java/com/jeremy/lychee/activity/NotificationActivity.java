
package com.jeremy.lychee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jeremy.lychee.model.update.UpdateApkHelper;
import com.jeremy.lychee.model.update.UpdateInfo;

public class NotificationActivity extends Activity {
    public final static String EXTRA_DOWNLOAD = "EXTRA_DOWNLOAD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Now finish, which will drop the user in to the activity that was at
        // the top
        // of the task stack
        Intent intent = getIntent();
        if (intent != null) {
        	try {
				Object object = intent.getParcelableExtra(EXTRA_DOWNLOAD);
				if(object!=null){
					UpdateInfo updateInfo = intent.getParcelableExtra(EXTRA_DOWNLOAD);
					new UpdateApkHelper(this, updateInfo).showDownloadNotification(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        finish();
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
