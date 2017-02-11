package com.jeremy.lychee.share.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jeremy.lychee.R;
import com.jeremy.lychee.utils.ToastHelper;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.mm.sdk.modelbase.BaseResp;

public class WeiboEntryActivity extends Activity implements IWeiboHandler.Response {
	private IWeiboShareAPI weiboAPI = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weiboAPI = WeiboShareSDK.createWeiboAPI(this, Config.WEIBO_APP_KEY, false);
        // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
        // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
        // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
        // 失败返回 false，不调用上述回调
        try {
			weiboAPI.handleWeiboResponse(getIntent(), this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    }
    
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
		weiboAPI.handleWeiboResponse(intent, this);
	}



	@Override
	public void onResponse(BaseResponse resp) {
		// TODO Auto-generated method stub
		int result = 0;
		
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = R.string.share_ok;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.share_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.share_deny;
			break;
		default:
			//result = R.string.share_unknown;
			break;
		}
		if(result != 0) {
		    ToastHelper.getInstance(getApplicationContext()).toast(result);
		}
		finish();
	}
}