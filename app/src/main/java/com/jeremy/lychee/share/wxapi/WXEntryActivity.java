package com.jeremy.lychee.share.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jeremy.lychee.channel.Md5Util;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.model.user.User;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.preference.AppConstant;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.utils.ToastHelper;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 微信回调Activity,用来处理微信登陆和微信分享
 * 
 * @author zourongbo
 *
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api = null;
	private Observable<ModelBase<User>> mUserObservable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	api = WXAPIFactory.createWXAPI(this.getApplicationContext(), WXConfig.WX_APP_ID, true);
    	api.registerApp(WXConfig.WX_APP_ID);
        try {
			api.handleIntent(getIntent(), this);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	if(api != null) {
    		api.unregisterApp();
    	}
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
    
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {

		switch (resp.getType()) {
		case ConstantsAPI.COMMAND_SENDAUTH:
			callbackFromLoginToWx(resp);
			break;
		case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
			callbackFromSendToWx(resp);
			break;
		}
	}

	private void callbackFromLoginToWx(BaseResp resp) {
		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				/**
				 * 1. upload sns(weibo, weixin) info to our own server
				 * 2. save our server user info
				 */
				SendAuth.Resp response = (SendAuth.Resp) resp;
				String access_token = "";
				String code = response.code;
				String from = AppConstant.UserFrom.FROM_WX;
				String fuid = "";


				String salt = System.currentTimeMillis() + "";
				String time = salt;
				String src = "lx_android";
				StringBuilder builder = new StringBuilder();
				builder.append("access_token=").append(access_token).append("&")
						.append("code=").append(code).append("&")
						.append("from=").append(from).append("&")
						.append("fuid=").append(fuid).append("&")
						.append("src=").append("lx_android").append("&")
						.append("u_salt=").append(salt).append("&")
						.append("u_time=").append(time).append("&")
						.append(AppConstant.APP_SECRET);
				String sign = Md5Util.getMD5code(builder.toString());
				sign = sign + salt;
				String url = OldRetroAdapter.HOST_IOS + "user/authlogin";
				mUserObservable = OldRetroAdapter.getService().getUserInfo(url, from, fuid, access_token, code, sign, salt, time, src);
				mUserObservable.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(new Observer<ModelBase<User>>() {
							@Override
							public void onCompleted() {
								finish();
							}

							@Override
							public void onError(Throwable e) {
								e.printStackTrace();
								QEventBus.getEventBus().post(new Events.LoginErr());
							}

							@Override
							public void onNext(ModelBase<User> userModelBase) {
								if (userModelBase != null && userModelBase.getErrno() == 0) {
									Session.save(userModelBase.getData(), true);
									QEventBus.getEventBus().post(new Events.LoginOk());
								} else {
									QEventBus.getEventBus().post(new Events.LoginErr());
								}
							}
						});
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				QEventBus.getEventBus().post(new Events.LoginCancel());
				finish();
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
			default:
				QEventBus.getEventBus().post(new Events.LoginCancel());
				finish();
				break;
		}
	}

	public void callbackFromSendToWx(BaseResp resp) {
		int result = 0;
		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				result = com.jeremy.lychee.R.string.share_ok;
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result = com.jeremy.lychee.R.string.share_cancel;
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = com.jeremy.lychee.R.string.share_deny;
				break;
			default:
				//result = R.string.share_unknown;
				result = com.jeremy.lychee.R.string.share_cancel;
				break;
		}
		ToastHelper.getInstance(getApplicationContext()).toast(result);
		finish();
	}
}