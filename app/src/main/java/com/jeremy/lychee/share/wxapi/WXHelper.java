package com.jeremy.lychee.share.wxapi;

import com.jeremy.lychee.base.ContentApplication;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ToastHelper;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信登陆帮助类
 * 
 * @author zourongbo
 *
 */
public class WXHelper {
	public static WXHelper INSTANCE = new WXHelper();
	private WXHelper() {
	}

    private IWXAPI mWXAPI;
    
    public void init() {
        mWXAPI = WXAPIFactory.createWXAPI(ContentApplication.getInstance(), WXConfig.WX_APP_ID, true);
        mWXAPI.registerApp(WXConfig.WX_APP_ID);
    }
    
	public void loginWeixin() {
		init();
		if (!AppUtil.isNetAvailable(ContentApplication.getInstance()) || !mWXAPI.isWXAppInstalled()) {
            if(!mWXAPI.isWXAppInstalled()) {
            	ToastHelper.getInstance(ContentApplication.getInstance()).toast(com.jeremy.lychee.R.string.login_weixin_uninstalled);
				QEventBus.getEventBus().post(new Events.LoginErr());
            	return;
            }
        }
		// send oauth request
		SendAuth.Req req = new SendAuth.Req();
		req.scope = WXConfig.WX_APP_SCOPE;
		req.state = "kandian";
		mWXAPI.sendReq(req);
	}
}