/**
 * 
 */

package com.jeremy.lychee.activity.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.share.weibo.WeiboHelper;
import com.jeremy.lychee.share.wxapi.WXHelper;

public class LoginActivity extends Activity implements OnClickListener {
	private static final int SLEEP_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QEventBus.getEventBus().register(this);
        setContentView(com.jeremy.lychee.R.layout.activity_login);
        initViews();
    }

    private void initViews() {
        findViewById(com.jeremy.lychee.R.id.rl_login_weixin).setOnClickListener(this);
        findViewById(com.jeremy.lychee.R.id.rl_login_btn).setOnClickListener(this);
        findViewById(com.jeremy.lychee.R.id.rl_login_phone).setOnClickListener(this);
        findViewById(com.jeremy.lychee.R.id.btn_cancel).setOnClickListener(this);
        findViewById(com.jeremy.lychee.R.id.view_login_top).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        WeiboHelper.INSTANCE.onAuthorizeResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void postExit() {
        new Handler().postDelayed(LoginActivity.this::forceExit, SLEEP_TIME);
    }
    
	private void forceExit() {
		finish();
	}

	@Override
	public void onClick(View v) {
        switch (v.getId()) {
            case com.jeremy.lychee.R.id.rl_login_btn:
                if (AppUtil.isNetAvailable(getApplicationContext())) {
                    boolean res = WeiboHelper.INSTANCE.loginWeibo(this);
                    setResult(RESULT_OK);
                    if (!res) {
                        postExit();
                    }
                } else {
                    ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.net_error);
                    postExit();
                }
                break;
            case com.jeremy.lychee.R.id.rl_login_weixin:
                if (AppUtil.isNetAvailable(getApplicationContext())) {
                    WXHelper.INSTANCE.loginWeixin();
                    setResult(RESULT_OK);
                } else {
                    ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.net_error);
                }
                postExit();
                break;
            case com.jeremy.lychee.R.id.rl_login_phone:
                //TODO phone num login
                startActivity(new Intent(LoginActivity.this, PhoneLoginActivity.class));
                setResult(RESULT_OK);
                postExit();
                break;
            case com.jeremy.lychee.R.id.btn_cancel:
//                QEventBus.getEventBus().post(new Events.IgnoreRefreshNextTime(
//                        new Events.IgnoreRefreshNextTime.IsIgnore(true)));
                forceExit();
                break;
            case com.jeremy.lychee.R.id.view_login_top:
                forceExit();
                break;
            default:
                break;
        }
    }

    public void onEvent(Events.LoginErr event) {
        forceExit();
    }

    public void onEvent(Events.LoginOk event) {
        forceExit();
    }

    @Override
    protected void onDestroy() {
        WeiboHelper.INSTANCE.removeSsoHandler();
        super.onDestroy();
        QEventBus.getEventBus().unregister(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
