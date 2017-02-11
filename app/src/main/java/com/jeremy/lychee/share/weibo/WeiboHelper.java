/**
 *
 */

package com.jeremy.lychee.share.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jeremy.lychee.channel.Md5Util;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.model.user.User;
import com.jeremy.lychee.preference.AppConstant;
import com.jeremy.lychee.base.ContentApplication;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.ToastHelper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.LogUtil;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * @author xiaozhongzhong
 */
public class WeiboHelper {
    public static WeiboHelper INSTANCE = new WeiboHelper();
    private WeiboHelper() {
        super();
    }

    private SsoHandler mSsoHandler;
    private Observable<ModelBase<User>> mUserObservable;

    public void init(Activity activity) {
        LogUtil.disableLog();
        AuthInfo mWeibo = new AuthInfo(activity, Config.WEIBO_APP_KEY, Config.WEIBO_REDIRECT_URL, Config.SCOPE);
        mSsoHandler = new SsoHandler(activity, mWeibo);
    }

    public boolean loginWeibo(Activity activity) {
        init(activity);
        if (!mSsoHandler.isWeiboAppInstalled()) {
            ToastHelper.getInstance(activity).toast(com.jeremy.lychee.R.string.login_weibo_uninstalled);
            QEventBus.getEventBus().post(new Events.LoginErr());
            return false;
        }
        mSsoHandler.authorizeClientSso(new AuthDialogListener());
        return true;
    }

    public void onAuthorizeResult(int requestCode, int resultCode, Intent data) {
        // sso 授权回调
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    public void removeSsoHandler() {
        mSsoHandler = null;
        if (mUserObservable != null) {
        }
    }

    // 微博sso认证监听类
    class AuthDialogListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                /**
                 * 1. upload sns(weibo, weixin) info to our own server
                 * 2. save our server user info
                 */
                String access_token = accessToken.getToken();
                String code = "";
                String from = AppConstant.UserFrom.FROM_WB;
                String fuid = accessToken.getUid();


                String salt = System.currentTimeMillis() + "";
                String time = salt;
                String src = "lx_android";
                StringBuilder builder = new StringBuilder();
                builder.append("access_token=").append(access_token).append("&")
                        .append("code=").append(code).append("&")
                        .append("from=").append(from).append("&")
                        .append("fuid=").append(fuid).append("&")
                        .append("src=").append(src).append("&")
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
            } else {
                QEventBus.getEventBus().post(new Events.LoginErr());
            }
        }

        @Override
        public void onCancel() {
            ToastHelper.getInstance(ContentApplication.getInstance()).toast(com.jeremy.lychee.R.string.login_cancel);
            QEventBus.getEventBus().post(new Events.LoginErr());
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ToastHelper.getInstance(ContentApplication.getInstance()).toast(e.getMessage());
            QEventBus.getEventBus().post(new Events.LoginErr());
        }

    }
}
