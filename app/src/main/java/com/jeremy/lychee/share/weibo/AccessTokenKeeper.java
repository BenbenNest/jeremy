/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeremy.lychee.share.weibo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Base64;

import com.jeremy.lychee.utils.SDKUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * 该类定义了微博授权时所需要的参数。
 * 
 * @author SINA
 * @since 2013-10-07
 */
public class AccessTokenKeeper {
    private static final String PREFERENCES_NAME = "weibo_android";

    private static final String KEY_UID = "key_uid";
    private static final String KEY_UID_OLD = "uid";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EXPIRES_IN = "expires_in";

    /**
     * 保存 Token 对象到 SharedPreferences。
     * 
     * @param context 应用程序上下文环境
     * @param token Token 对象
     */
    @SuppressLint("InlinedApi")
    public static void writeAccessToken(Context context, Oauth2AccessToken token) {
        if (null == context || null == token || TextUtils.isEmpty(token.getUid())
                || TextUtils.isEmpty(token.getToken())) {
            return;
        }

        SharedPreferences sp = null;
        if (SDKUtils.hasHoneycomb()) {
            sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_MULTI_PROCESS);
        } else {
            sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        }
        Editor editor = sp.edit();
        editor.putString(KEY_UID, Base64.encodeToString(token.getUid().getBytes(), Base64.DEFAULT));
        editor.putString(KEY_ACCESS_TOKEN, Base64.encodeToString(token.getToken().getBytes(), Base64.DEFAULT));
        editor.putLong(KEY_EXPIRES_IN, token.getExpiresTime());
        editor.commit();
    }

    /**
     * 从 SharedPreferences 读取 Token 信息。
     * 
     * 
     * @param context 应用程序上下文环境
     * @return 返回 Token 对象
     */
    @SuppressLint("InlinedApi")
    public static Oauth2AccessToken readAccessToken(Context context) {
        if (null == context) {
            return null;
        }

        Oauth2AccessToken token = new Oauth2AccessToken();
        SharedPreferences sp = null;
        if (SDKUtils.hasHoneycomb()) {
            sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_MULTI_PROCESS);
        } else {
            sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        }
        if (sp.contains(KEY_UID_OLD)) {
            token.setUid(sp.getString(KEY_UID_OLD, ""));
            token.setToken(sp.getString(KEY_ACCESS_TOKEN, ""));
            token.setExpiresTime(sp.getLong(KEY_EXPIRES_IN, 0));
            clear(context);
            writeAccessToken(context, token);
        } else {
            String uid = sp.getString(KEY_UID, "");
            String tokenStr = sp.getString(KEY_ACCESS_TOKEN, "");
            if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(tokenStr)) {
                return null;
            }
            token.setUid(new String(Base64.decode(uid, Base64.DEFAULT)));
            token.setToken(new String(Base64.decode(tokenStr, Base64.DEFAULT)));
            token.setExpiresTime(sp.getLong(KEY_EXPIRES_IN, 0));
        }
        return token;
    }

    /**
     * 清空 SharedPreferences 中 Token信息。
     * 
     * @param context 应用程序上下文环境
     */
    @SuppressLint("InlinedApi")
    public static void clear(Context context) {
        if (null == context) {
            return;
        }

        SharedPreferences sp = null;
        if (SDKUtils.hasHoneycomb()) {
            sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_MULTI_PROCESS);
        } else {
            sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        }
        Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
